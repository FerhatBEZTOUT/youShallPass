package com.example.moyenne.Vue;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moyenne.Controller.MyDatabaseHelpter;
import com.example.moyenne.Modéle.Module;
import com.example.moyenne.R;

public class AddmoduleActivity extends AppCompatActivity {
    EditText nomModule,coef;
    int semestre;
    String nomProfil;
    CheckBox checkTD,checkTP;
    Button addModule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmodule);
        Intent i = getIntent();
        nomProfil = i.getStringExtra("profil");
        semestre = i.getIntExtra("semestre",1);
        nomModule = findViewById(R.id.editTextNomModule);
        coef = findViewById(R.id.editTextCoef);
        checkTD = findViewById(R.id.cBoxTD);
        checkTP = findViewById(R.id.cBoxTP);
        addModule = findViewById(R.id.addModule);

        addModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String intitule = nomModule.getText().toString().trim();
                if(isEmpty(nomModule.getText()) || isEmpty(coef.getText())) {
                    Toast.makeText(AddmoduleActivity.this,"Tous les champs doivent être remplis",Toast.LENGTH_SHORT).show();
                }else if(nomModule.length()>18){
                    Toast.makeText(AddmoduleActivity.this,"Nom trop long",Toast.LENGTH_SHORT).show();
                } else if(Module.isModuleExist(intitule,nomProfil,semestre,AddmoduleActivity.this)) {
                    Toast.makeText(AddmoduleActivity.this,"Module existe déjà",Toast.LENGTH_SHORT).show();
                }
                else {
                    int type;
                    if (checkTD.isChecked() && checkTP.isChecked()) {
                        type = 1; // type 1 : module avec EXAM, TP et TD
                    } else if (checkTD.isChecked()) {
                        type = 2; // type 2 : module avec EXAM, TD uniquement
                    } else if (checkTP.isChecked()) {
                        type = 3; // type 3 : module avec EXAM, TP uniquement
                    } else {
                        type = 4; // type 4 : module avec EXAM uniquement
                    }

                    MyDatabaseHelpter myDB = new MyDatabaseHelpter(AddmoduleActivity.this);

                    int coefficient = Integer.valueOf(coef.getText().toString().trim());

                    try {
                        myDB.addModule(intitule,coefficient,semestre,type,nomProfil,AddmoduleActivity.this);
                        finish();
                    } catch (SQLException e) {
                        Log.d("AddModuleActivity",e.getMessage());
                    }

                }
            }
        });
    }
}