package com.artisan.android.demo.service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.artisan.android.demo.model.collection.NewsFeed;
import com.artisan.android.demo.model.collection.ShoppingCart;

public class LocalStorageManager {

	private static final String TAG = LocalStorageManager.class.getSimpleName();

	private final ReentrantLock serviceLock = new ReentrantLock();
	private final Condition conditionServiceBound = serviceLock.newCondition();
	private ExecutorService executor;

	private volatile boolean isStopped;
	private LocalStorageService boundService;

	private static final long SERVICE_WAIT_MILLISECONDS = 3000;

	private final LocalStorageServiceConnection serviceConnection = new LocalStorageServiceConnection();

	public class LocalStorageException extends Exception {

		private static final long serialVersionUID = 1L;

		public LocalStorageException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
		}

		public LocalStorageException(String detailMessage) {
			super(detailMessage);
		}

		public LocalStorageException(Throwable throwable) {
			super(throwable);
		}
	}

	public void start(Context context) {
		executor = Executors.newSingleThreadExecutor();
		isStopped = false;
		Intent serviceIntent = new Intent(context, LocalStorageService.class);
		context.bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	public void stop(Context context) {
		executor.shutdownNow();
		isStopped = true;
		context.unbindService(serviceConnection);
	}

	public void loadShoppingCart(final LocalStorageListener<ShoppingCart> listener) {
		executor.submit(new Runnable() {
			public void run() {
				loadModelContainer(new ShoppingCartAccessor(), listener);
			}
		});
	}

	public void loadNewsFeed(final LocalStorageListener<NewsFeed> listener) {
		executor.submit(new Runnable() {
			public void run() {
				loadModelContainer(new NewsFeedAccessor(), listener);
			}
		});
	}

	private class ShoppingCartAccessor implements Callable<ShoppingCart> {
		public ShoppingCart call() throws Exception {
			return boundService.getShoppingCart();
		}
	}

	private class NewsFeedAccessor implements Callable<NewsFeed> {
		public NewsFeed call() throws Exception {
			return boundService.getNewsFeed();
		}
	}

	private <ModelType> void loadModelContainer(Callable<ModelType> serviceAccessor, final LocalStorageListener<ModelType> listener) {
		CallbackRunnable<ModelType> callbackRunnable;
		try {
			boolean isBound = waitForServiceToBeBound(SERVICE_WAIT_MILLISECONDS);
			if (isBound) {
				callbackRunnable = new CallbackRunnable<ModelType>(listener, serviceAccessor.call());
			} else {
				callbackRunnable = new CallbackRunnable<ModelType>(listener, "Could not load data: operation timed out before service could be bound.", null);
			}

		} catch (Exception e) {
			callbackRunnable = new CallbackRunnable<ModelType>(listener, "Could not load data: operation was interrupted before service could be bound", e);
		}
		final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
		mainThreadHandler.post(callbackRunnable);
	}

	private class CallbackRunnable<ResultType> implements Runnable {

		private Exception exceptionCause;
		private String exceptionMessage;
		private ResultType result;
		private LocalStorageListener<ResultType> listener;

		public CallbackRunnable(LocalStorageListener<ResultType> listener, String message, Exception cause) {
			this.listener = listener;
			this.exceptionMessage = message;
			this.exceptionCause = cause;
		}

		public CallbackRunnable (LocalStorageListener<ResultType> listener, ResultType result) {
			this.listener = listener;
			this.result = result;
		};

		public void run() {
			if (exceptionMessage != null || exceptionCause != null) {
				listener.onError(new LocalStorageException(exceptionMessage, exceptionCause));
			} else {
				listener.onLoadComplete(result);
			}
		}
	};

	private void setService(LocalStorageService service) {
		boundService = service;
		if (service != null) {
			serviceLock.lock();
			conditionServiceBound.signalAll();
			serviceLock.unlock();
		}
	}

	public class LocalStorageServiceConnection implements ServiceConnection {

		public void onServiceConnected(ComponentName name, IBinder binder) {
			LocalStorageService.ServiceBinder localBinder = (LocalStorageService.ServiceBinder) binder;
			setService(localBinder.getService());
		}

		public void onServiceDisconnected(ComponentName name) {
			setService(null);
		}
	}

	//TODO: this should throw an exception on timeout, possibly
	protected boolean waitForServiceToBeBound(long milliseconds) throws InterruptedException {

		boolean serviceDidBind = true;

		serviceLock.lock();
		try {
			while (boundService == null && !isStopped) {
				Log.d(TAG, "Stopping execution -- waiting for service to be bound.");
				serviceDidBind = conditionServiceBound.await(milliseconds, TimeUnit.MILLISECONDS);
				if (serviceDidBind) {
					Log.d(TAG, "Continuing execution -- service is bound.");
				} else {
					Log.w(TAG, "Continuing execution -- service binding timed out.");
				}
			}
		} catch (InterruptedException e) {
			Log.w(TAG, "Continuing execution -- service connection lock was interrupted.");
			throw e;
		} finally {
			serviceLock.unlock();
		}

		return serviceDidBind;
	}

}
