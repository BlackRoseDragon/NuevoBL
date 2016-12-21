package mashup.com.buslocation;

import android.app.Application;

import com.estimote.sdk.EstimoteSDK;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class BusLocationApp extends Application {

    private String TWITTER_KEY = "nkPiCTOziku9dZH5fmsdBvINC";
    private String TWITTER_SECRET = "4gyCi2XkK4F30NPH8bSFYu39atL53lgFDkysFlX9MPRmaGm2nc";

    private String appId = "buslocation-6st";
    private String appToken = "7b95845516d1f6897fc16a04200a3f60";

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        EstimoteSDK.initialize(getApplicationContext(), appId, appToken);
        EstimoteSDK.enableDebugLogging(true);
    }
}