package univ.ducoroy.test.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import univ.ducoroy.test.DO.User;
import univ.ducoroy.test.HTTP.VolleyResponseListener;
import univ.ducoroy.test.HTTP.VolleyUtils;
import univ.ducoroy.test.R;

public class MainActivity extends AppCompatActivity {
    private Button mConnection;
    private TextView mNoAccount;
    private EditText mUserName, mPassword;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialisation des widgets
        this.initializeWidgets();
        //Initialisation des events
        this.eventsWidgets();

        doPerms();
    }

    void doPerms()
    {
        int MY_PERMISSIONS_RECORD_AUDIO = 1;
        MainActivity thisActivity = this;

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(thisActivity,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE},
                    MY_PERMISSIONS_RECORD_AUDIO);
        }
    }

    private void initializeWidgets(){
        mConnection = findViewById(R.id.bttConnexion);
        mNoAccount = findViewById(R.id.tvNoAccount);
        mUserName = findViewById(R.id.edtUsername);
        mPassword = findViewById(R.id.edtPassword);
    }

    private void eventsWidgets(){
        //Gestion clic bouton Connnexion
        mConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connexion();
            }
        });
        //Gestion click sur le TextView "No account"
        mNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noAccount();
            }
        });
    }

    //Méthode appelée via le click sur le textview NoAccount
    private void noAccount(){
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    //Méthode appelée via le click sur le bouton Connexion
    private void connexion() {

        //Vérification de la saisie des champs
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();

        if (!userName.trim().isEmpty() && !password.trim().isEmpty()){
            reqPost();
        } else {
            Toast.makeText(this, "Certains champs ne sont pas remplis", Toast.LENGTH_SHORT).show();
        }
    }

    //Lancement de l'activité une fois la connexion réussie
    public void startConnexion(String response) throws JSONException {
        //Création du nouvel user
        if (response.charAt(1) == '{'){
            User newUser = new User(new JSONObject(response.trim()));
            //Création de l'intent
            Intent userIntent = new Intent(this, VocacookActivity.class);
            userIntent.putExtra("user",newUser);
            //Lancement de l'activité
            startActivity(userIntent);
        } else {
            Toast.makeText(this, "Identifiants incorrects", Toast.LENGTH_SHORT).show();
        }
    }

    //---- VOLLEY Implementation (HTTP request) ----//
    public void reqPost(){
        String URL_POST="http://vps507765.ovh.net/api_vocacook/requests/getUserByNick.php";
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        Map<String, String> params = getParams(userName,password);

        VolleyUtils.POST_METHOD(MainActivity.this, URL_POST,params, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                try {
                    startConnexion(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                System.out.println("Error" + message);
            }
        });
    }

    public Map<String,String> getParams(String nickname, String password)
    {
        Map<String, String> params = new HashMap<>();
        params.put("api_nick", "laurent");
        params.put("api_passwd", "api_vocacook_acces_0");
        params.put("nick", nickname);
        params.put("passwd", password);
        return params;
    }
    //---- END VOLLEY Implementation (HTTP request) ----//

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /** Called when another activity is taking focus. */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /** Called when the activity is no longer visible. */
    @Override
    protected void onStop() {
        super.onStop();
    }

    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
