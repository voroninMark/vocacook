package univ.ducoroy.test.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import univ.ducoroy.test.DO.RecetteSimple;
import univ.ducoroy.test.DO.User;
import univ.ducoroy.test.HTTP.VolleyResponseListener;
import univ.ducoroy.test.HTTP.VolleyUtils;
import univ.ducoroy.test.R;
import univ.ducoroy.test.Tools;


public class VocacookActivity extends AppCompatActivity {
    private User currentUser;
    private Button mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocacook);

        Intent intent = getIntent();
        currentUser = (User)intent.getParcelableExtra("user");

        initializeWidgets();
        eventsWidgets();

    }

    private void initializeWidgets(){
        mIngredients = findViewById(R.id.btt_ingredients);
    }

    private void eventsWidgets(){
        //Gestion clic bouton Ingrédients
        mIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listenning();
            }
        });
    }

    private void listenning(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().getDisplayLanguage());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Vous pouvez parler ...");
        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Désolé, votre appareil ne supporte pas d'entrée vocale...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> buffer = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String result = buffer.get(0);
                    try {
                        reqPost(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    //---- VOLLEY Implementation (HTTP request) ----//
    public void reqPost(final String result) throws JSONException {
        String URL_POST="http://vps507765.ovh.net/api_vocacook/requests/getRecetteByIngredients.php";

        Map<String, String> params = getParams(result);

        VolleyUtils.POST_METHOD(VocacookActivity.this, URL_POST,params, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                try {
                    if(response.toString().trim().charAt(0) == '{'){
                        JSONArray jsonArray =  new JSONObject(response.toString()).getJSONArray("recettes");
                        ArrayList<RecetteSimple> recettes = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonRecette = jsonArray.getJSONObject(i);
                            recettes.add(new RecetteSimple(jsonRecette));
                        }
                        startListRecettes(recettes);
                    } else {
                        Toast.makeText(VocacookActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String message) {
                System.out.println("Error : " + message);
            }
        });
    }

    public Map<String,String> getParams(String result) throws JSONException {
        String[] results = Tools.parseInJSON(result);
        Map<String,String> finalParams = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("api_nick", "laurent");
        params.put("api_passwd", "api_vocacook_acces_0");
        params.put("ingredients", results);

        JSONObject jsonParam = new JSONObject(params);

        finalParams.put("jsonRequest", jsonParam.toString());
        return finalParams;
    }

    public void startListRecettes(ArrayList<RecetteSimple> recettes){
        //Création de l'intent
        Intent listRecettes = new Intent(this, ListRecettesActivity.class);
        listRecettes.putExtra("user", currentUser);
        listRecettes.putExtra("recettes", recettes);
        //Lancement de l'activité
        startActivity(listRecettes);
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
