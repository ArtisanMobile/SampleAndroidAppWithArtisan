package %packageNameForProject%;

import com.artisan.services.ArtisanService;
import com.artisan.manager.ArtisanManager;

/**
* This is the ArtisanService, which initializes Artisan and manages its life-cycle.
* This service is automatically started when your application starts, and automatically
* downloads all experiments, configuration, and published changes for your app.
* 
* If you rename this class, you must:
* <ul>
* <li>Rename the class in your AndroidManifest to match the new class name</li>
* <li>Modify the value of 'artisan_service_name' in 'res/values/artisan_ids.xml' to match the new class name</li>
* </ul>
*/
public class %generatedServiceClassName% extends ArtisanService {
	
	@Override
	public void startArtisanManager(ArtisanManager artisanManager) {
		artisanManager.start("%appId%");
	}
	
	@Override
	public void registerPowerhooks() {
		// register your powerhooks here
	}
	
	@Override
	public void registerInCodeExperiments() {
		// register your in-code experiments here
	}
	
}