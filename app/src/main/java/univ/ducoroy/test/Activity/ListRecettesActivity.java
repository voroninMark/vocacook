package univ.ducoroy.test.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import univ.ducoroy.test.DO.RecetteSimple;
import univ.ducoroy.test.DO.User;
import univ.ducoroy.test.R;
import univ.ducoroy.test.RecettesAdapter;

public class ListRecettesActivity extends AppCompatActivity {
    private User currentUser;
    private ArrayList<RecetteSimple> recettes;
    private ListView listRecettes;
    private RecettesAdapter rAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_recettes);

        Intent intent = getIntent();
        currentUser = intent.getParcelableExtra("user");

        recettes = new ArrayList<>();
        recettes = intent.getParcelableArrayListExtra("recettes");

        initializeWidgets();
        initializeEvents();
    }


    private void initializeWidgets(){
        listRecettes = (ListView) findViewById(R.id.recettes_list);
        rAdapter = new RecettesAdapter(this, recettes);
        listRecettes.setAdapter(rAdapter);
    }

    private void initializeEvents(){
        listRecettes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentRecette = new Intent(view.getContext(), RecetteActivity.class);
                RecetteSimple recetteSimple = (RecetteSimple) listRecettes.getItemAtPosition(position);
                intentRecette.putExtra("user", currentUser);
                intentRecette.putExtra("recette", recetteSimple);
                startActivity(intentRecette);
            }
        });
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
