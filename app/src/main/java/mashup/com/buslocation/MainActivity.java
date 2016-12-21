package mashup.com.buslocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private Usuario usuario;

    private Button map_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!verificarSesion()) {
            goLoginScreen();
        }

        map_button = (Button) findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMapScreen();
            }
        });
    }

    public boolean verificarSesion() {
        if(Twitter.getInstance().core.getSessionManager().getActiveSession() != null) {

            TwitterSession session = Twitter.getSessionManager().getActiveSession();
            Call<User> userResult = Twitter.getApiClient(session).getAccountService().verifyCredentials(true, false);
            userResult.enqueue(new Callback<User>() {

                @Override
                public void failure(TwitterException e) {
                }

                @Override
                public void success(Result<User> userResult) {
                    User user = userResult.data;
                    usuario = new Usuario(Long.toString(user.getId()), user.name);
                }

            });

            return true;
        }
        if(AccessToken.getCurrentAccessToken() != null) {

            Bundle params = new Bundle();
            params.putString("fields", "id, name, email, picture");
            new GraphRequest(
                    AccessToken.getCurrentAccessToken(), "/me", params, HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {
                                JSONObject data = response.getJSONObject();
                                usuario = new Usuario(data.getString("id"), data.getString("name"));
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();

            return true;
        }
        return false;
    }

    public void cerrarSesion() {
        if(Twitter.getInstance().core.getSessionManager().getActiveSession() != null) {
            Twitter.getInstance().core.getSessionManager().clearActiveSession();
        }
        if(AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        goLoginScreen();
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        cerrarSesion();
    }

    public void goMapScreen() {
        Intent intent = new Intent(getApplication(), MapsActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }
}