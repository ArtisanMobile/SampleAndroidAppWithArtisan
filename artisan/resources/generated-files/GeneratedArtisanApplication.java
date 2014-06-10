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
	 * For example:
	 * 
	 * PowerHookManager.registerVariable("WelcomeText", "Welcome Text Sample PowerHook", "Welcome to Artisan!");
	 * 
	 * <code>
	 * 	HashMap<String, String> defaultData = new HashMap<String, String>();
	 * 	defaultData.put("discountCode", "012345ABC");
	 * 	defaultData.put("discountAmount", "25%");
	 * 	defaultData.put("shouldDisplay", "true");
	 * 
	 * 	PowerHookManager.registerBlock("showAlert", "Show Alert Block", defaultData, new ArtisanBlock() {
	 * 		public void execute(Map<String, String> data, Map<String, Object> extraData) {
	 * 			if ("true".equalsIgnoreCase(data.get("shouldDisplay"))) {
	 * 				StringBuilder message = new StringBuilder();
	 * 				message.append("Buy another for a friend! Use discount code ");
	 * 				message.append(data.get("discountCode"));
	 * 				message.append(" to get ");
	 * 				message.append(data.get("discountAmount"));
	 * 				message.append(" off your purchase of 2 or more!");
	 * 				Toast.makeText((Context) extraData.get("context"), message, Toast.LENGTH_LONG).show();
	 * 			}
	 * 		}
	 * });
	 * </code>
	 * 
	 * More examples at http://docs.useartisan.com/dev/quickstart-for-android/#power-hooks
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
	 * More examples at http://docs.useartisan.com/dev/quickstart-for-android/#in-code
	 */
	@Override
	public void registerInCodeExperiments() {
		
	}
	
	/**
	 * Register your Artisan In-code Experiments here
	 * 
	 * For example:
	 * 
	 * ArtisanProfileManager.registerDateTime("lastSeenAt", new Date());
	 * ArtisanProfileManager.registerLocation("lastKnownLocation");
	 * ArtisanProfileManager.registerNumber("totalOrderCount", ArtisanDemoApplication.totalOrderCount);
	 * ArtisanProfileManager.registerString("visitorType", "anonymous");
	 * ArtisanProfileManager.setGender(Gender.Female);
	 * ArtisanProfileManager.setUserAge(29);
	 * ArtisanProfileManager.setSharedUserId("abcdef123456789");
	 * ArtisanProfileManager.setUserAddress("234 Market Street, Philadelphia, PA 19106");
	 * 
	 * More examples at http://docs.useartisan.com/dev/quickstart-for-android/#api
	 */
	@Override
	public void registerUserProfileVariables() {

	}
}