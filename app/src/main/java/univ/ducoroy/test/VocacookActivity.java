package univ.ducoroy.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import univ.ducoroy.test.DO.User;

public class VocacookActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocacook);

        Intent intent = getIntent();
        User user = (User)intent.getParcelableExtra("user");
        Toast.makeText(this, "Bienvenue utilisateur "+ user.getPseudo(), Toast.LENGTH_SHORT).show();
    }
}
