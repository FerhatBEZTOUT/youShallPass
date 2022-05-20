package com.example.moyenne.Vue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.moyenne.Modéle.MyDatabaseHelpter;
import com.example.moyenne.Controller.ReleveDeNoteAdapter;
import com.example.moyenne.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton addProfil;
    TextView titre;
    MyDatabaseHelpter myDB;
    ArrayList<String> profil;
    ReleveDeNoteAdapter releveDeNoteAdapter;

    @Override
    protected void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_main);

        titre = findViewById(R.id.titreMainActivity);
        recyclerView = findViewById(R.id.recyclerViewProfil);
        addProfil = findViewById(R.id.addProfil);
        addProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReleveDeNoteActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelpter(MainActivity.this);
        profil = new ArrayList<>();

        StoredProfils();
        releveDeNoteAdapter = new ReleveDeNoteAdapter(MainActivity.this, profil);
        recyclerView.setAdapter(releveDeNoteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if (profil.size()==0) {
            titre.setText("Ajoutez des relevés de notes");
        } else {
            titre.setText("Relevés de notes");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titre = findViewById(R.id.titreMainActivity);
        recyclerView = findViewById(R.id.recyclerViewProfil);
        addProfil = findViewById(R.id.addProfil);
        addProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddReleveDeNoteActivity.class);
                startActivity(intent);
            }
        });

        myDB = new MyDatabaseHelpter(MainActivity.this);
        profil = new ArrayList<>();

        StoredProfils();
        releveDeNoteAdapter = new ReleveDeNoteAdapter(MainActivity.this, profil);
        recyclerView.setAdapter(releveDeNoteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        if (profil.size()==0) {
            titre.setText("Ajoutez des relevés de notes");
        } else {
            titre.setText("Relevés de notes");
        }

    }

    void StoredProfils() {
        Cursor cursor = myDB.readProfils();
        if (cursor.getCount()==0) {

        } else {
            while(cursor.moveToNext()){
                profil.add(cursor.getString(0));
            }
        }
    }

}