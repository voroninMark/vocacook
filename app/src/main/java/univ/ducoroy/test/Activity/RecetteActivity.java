package univ.ducoroy.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import univ.ducoroy.test.DO.Ingredient;
import univ.ducoroy.test.DO.Recette;
import univ.ducoroy.test.DO.RecetteSimple;
import univ.ducoroy.test.DO.User;
import univ.ducoroy.test.HTTP.VolleyResponseListener;
import univ.ducoroy.test.HTTP.VolleyUtils;
import univ.ducoroy.test.R;


public class RecetteActivity extends AppCompatActivity {
    private User currentUser;
    private RecetteSimple currentRecetteSimple;
    private Recette currentRecette;
    private TextView nomRecette, resumeRecette, listIngredients, currentEtape, titleEtape;
    private Button playSoundEtape,  stopSoundEtape;
    private int indexEtape;
    private TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recette);

        indexEtape = 0;
        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("user");
        currentRecetteSimple = intent.getParcelableExtra("recette");

        try {
            reqPost();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeWidgets();
        initializeTextToSpeech();
        initializeEvents();
    }



    private void initializeTextToSpeech(){
        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.FRANCE);
                }
            }
        });
    }
    private void initializeWidgets(){
        nomRecette = findViewById(R.id.nomRecette);
        resumeRecette = findViewById(R.id.resumeRecette);
        listIngredients = findViewById(R.id.ingredients_list);
        currentEtape = findViewById(R.id.current_etape);
        titleEtape = findViewById(R.id.title_etapes);
        playSoundEtape = findViewById(R.id.play_etape_sound);
        stopSoundEtape = findViewById(R.id.stop_etape_sound);
    }

    private void initializeEvents(){
        playSoundEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(currentEtape.getText().subSequence(0, currentEtape.length()), TextToSpeech.QUEUE_FLUSH,null, null);
            }
        });
        stopSoundEtape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.stop();
            }
        });
    }
    private void modifyWidgets(){
        nomRecette.setText(currentRecette.getNom());
        resumeRecette.setText(currentRecette.getResume());
        initializeList();
        initializeEtape(0);
    }

    private void initializeList(){
        String ingredients ="Liste des ingrédients \n";
        for (Ingredient ingredient : currentRecette.getIngredients()) {
            ingredients+= ("- "+ingredient.getNom()+" "+ingredient.getQuantite()+" "+ingredient.getUnite()+"\n");
        }
        ingredients = ingredients.substring(0, ingredients.length()-2);
        listIngredients.setText(ingredients);
    }

    private void initializeEtape(int index){
        titleEtape.setText("Etape "+(index+1)+" /"+currentRecette.split_etapes().length);
        currentEtape.setText(currentRecette.getEtapeByIndex(index));
    }

    public void switchEtape(View view) {
        if(view == findViewById(R.id.previous_etape)){
            if(indexEtape!=0){
                indexEtape --;
                initializeEtape(indexEtape);
            }
        } else {
            if(indexEtape < currentRecette.split_etapes().length-1){
                indexEtape++;
                initializeEtape(indexEtape);
            }

        }
    }

    //---- VOLLEY Implementation (HTTP request) ----//
    public void reqPost() throws JSONException {
        String URL_POST="http://vps507765.ovh.net/api_vocacook/requests/getRecetteByName.php";

        Map<String, String> params = getParams();

        VolleyUtils.POST_METHOD(RecetteActivity.this, URL_POST,params, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                try {
                    if (response.toString().trim().charAt(0) == '{'){
                        JSONObject jsonRecettes = new JSONObject(response.toString());
                        JSONArray arrayRecette = jsonRecettes.getJSONArray("recettes");
                        JSONObject jsonRecette = (JSONObject) arrayRecette.get(0);
                        currentRecette = new Recette(jsonRecette);
                        modifyWidgets();

                    } else {
                        Toast.makeText(RecetteActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
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

    public Map<String,String> getParams() throws JSONException {
        Map<String, String> params = new HashMap<>();
        params.put("api_nick", "laurent");
        params.put("api_passwd", "api_vocacook_acces_0");
        params.put("nomRecette", currentRecetteSimple.getNom());
        params.put("exact", "true");
        return params;
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
