package com.example.moyenne.Vue;

import static android.text.TextUtils.isEmpty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.moyenne.Controller.MyDatabaseHelpter;
import com.example.moyenne.R;

public class AddReleveDeNoteActivity extends AppCompatActivity {
    EditText pseudo;
    Button addProfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        pseudo = findViewById(R.id.editTextPseudo);
        addProfil = findViewById(R.id.btnAddProfil);

        addProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(pseudo.getText())) {
                    Toast.makeText(AddReleveDeNoteActivity.this,"Champ vide",Toast.LENGTH_SHORT).show();
                } else if (pseudo.getText().length()>7) {
                    Toast.makeText(AddReleveDeNoteActivity.this,"Trop long (max : 7 char)",Toast.LENGTH_SHORT).show();
                }
                else {
                    MyDatabaseHelpter myDB = new MyDatabaseHelpter(AddReleveDeNoteActivity.this);
                    myDB.addProfil(pseudo.getText().toString().trim(), AddReleveDeNoteActivity.this);

                }
            }
        });
    }
}