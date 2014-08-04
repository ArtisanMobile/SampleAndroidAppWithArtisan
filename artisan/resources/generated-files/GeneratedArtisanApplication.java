package %packageNameForProject%;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.widget.Toast;

import com.artisan.application.ArtisanApplication;
import com.artisan.manager.ArtisanManager;
import com.artisan.application.ArtisanRegisteredApplication;
import com.artisan.incodeapi.ArtisanExperimentManager;
import com.artisan.incodeapi.ArtisanLocationValue;
import com.artisan.incodeapi.ArtisanProfileManager;
import com.artisan.incodeapi.ArtisanProfileManager.Gender;
import com.artisan.powerhooks.ArtisanBlock;
import com.artisan.powerhooks.PowerHookManager;

public class %generatedApplicationClassName% extends ArtisanApplication {
	
	@Override
	public void onCreate() {
		super.onCreate();
		%pushConfigCall%
		ArtisanManager.startArtisan(this, "%appId%");
	}
	
	/**
	 * Register your Artisan Power Hook variables and Power Hook blocks here
	 * 
	 * Examples at http://docs.useartisan.com/dev/android/power-hooks/
	 * 
	 */
	@Override
	public void registerPowerhooks() {
		
	}
	
	/**
	 * Register your Artisan In-code Experiments here
	 * 
	 * For example:
	 * 
	 * ArtisanExperimentManager.registerExperiment("my first experiment");
	 * ArtisanExperimentManager.addVariantForExperiment("blue variation", "my first experiment");
	 * ArtisanExperimentManager.addVariantForExperiment("green variation", "my first experiment");
	 * 
	 * Examples at http://docs.useartisan.com/dev/android/incode-experiments/
	 */
	@Override
	public void registerInCodeExperiments() {
		
	}
	
	/**
	 * Register your Artisan User Profile Variables here
	 * 
	 * Examples at http://docs.useartisan.com/dev/android/user-profiles/
	 */
	@Override
	public void registerUserProfileVariables() {

	}
}