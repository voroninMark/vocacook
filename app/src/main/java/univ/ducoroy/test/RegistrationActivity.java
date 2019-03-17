package univ.ducoroy.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import univ.ducoroy.test.DO.User;
import univ.ducoroy.test.HTTP.VolleyResponseListener;
import univ.ducoroy.test.HTTP.VolleyUtils;

public class RegistrationActivity extends AppCompatActivity {
    private Button mCancel, mValidate;
    private EditText mPseudo, mMail, mPsswd, mConfirmPsswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Initialisation des widgets
        this.initializeWidgets();
        //Initialisation des events
        this.eventsWidgets();
    }

    private void initializeWidgets(){
        mCancel = findViewById(R.id.bttCancelRegistration);
        mValidate = findViewById(R.id.bttValidateRegistration);
        mPseudo = findViewById(R.id.edtPseudoRegistration);
        mMail = findViewById(R.id.edtMailRegistration);
        mPsswd = findViewById(R.id.edtPsswdRegistration);
        mConfirmPsswd = findViewById(R.id.edtConfirmPsswdRegistration);
    }

    private void eventsWidgets(){
        //Gestion clic bouton Valider
        mValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validate();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //Gestion clic bouton Annuler
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //Méthode appelée via le click sur le bouton Valider
    private void validate() throws JSONException {
        //Vérification de la saisie des champs
        String pseudo = mPseudo.getText().toString();
        String mail = mMail.getText().toString();
        String password = mPsswd.getText().toString();
        String confirm_password = mConfirmPsswd.getText().toString();
        if (!pseudo.trim().isEmpty() && !mail.trim().isEmpty() && !password.trim().isEmpty()){
            //Vérification de la saisie correcte du mail
            String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(mail);
            if (matcher.matches()){
                //Si les mots de passes saisies sont équivalents
                if(confirm_password.equals(password)){
                    reqPostUser(new User(pseudo, password, mail));
                } else {
                    Toast.makeText(this, "Mots de passe pas identiques !", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Saisie de mail incorrecte", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Certains champs ne sont pas remplis", Toast.LENGTH_SHORT).show();
        }
    }

    //---- VOLLEY Implementation (HTTP request) ----//
    public void reqPostUser(User user){
        String URL_POST="http://vps507765.ovh.net/api_vocacook/requests/insertUser.php";
        String userName = user.getPseudo();
        String password = user.getMdp();
        String mail = user.getMail();
        Map<String, String> params = getParams(userName,password, mail);

        VolleyUtils.POST_METHOD(RegistrationActivity.this, URL_POST,params, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                finishRegistration(response.toString());
            }
            @Override
            public void onError(String message) {
                System.out.println("Error" + message);
            }
        });
    }

    public Map<String,String> getParams(String nickname, String password, String mail)
    {
        Map<String, String> params = new HashMap<>();
        params.put("api_nick", "laurent");
        params.put("api_passwd", "api_vocacook_access_0");
        params.put("nick", nickname);
        params.put("passwd", password);
        params.put("email", mail);
        return params;
    }
    //---- END VOLLEY Implementation (HTTP request) ----//

    //Méthode qui détermine si l'inscription a bien été effectuée
    private void finishRegistration(String response){
        if(response.trim().equals("true")){
            Toast.makeText(this, "Inscription terminée", Toast.LENGTH_SHORT).show();
            //Fermeture de l'activité
            finish();
        } else {
            Toast.makeText(this, "Utilisateur déjà existant", Toast.LENGTH_SHORT).show();
        }
    }
    //Méthode appelée via le click sur le bouton Valider
    private void cancel(){
        finish();
    }

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
