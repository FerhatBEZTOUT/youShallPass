package com.example.moyenne.Vue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.moyenne.Controller.ModuleAdapter;
import com.example.moyenne.Controller.MyDatabaseHelpter;
import com.example.moyenne.Modéle.Module;
import com.example.moyenne.R;

import java.util.ArrayList;

public class SemestredeuxActivity extends AppCompatActivity {
    private MyDatabaseHelpter myDB;
    private ArrayList<Module> moduleList;
    private String nom;
    private final int SEMESTRE = 2;
    ModuleAdapter moduleAdapter;
    RecyclerView recyclerView;
    TextView titre;
    Button btnAddModule2, btnCalcMoy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semestredeux);
        recyclerView = findViewById(R.id.recyclerViewS2);
        btnAddModule2 = findViewById(R.id.btnAddModule2);
        btnCalcMoy = findViewById(R.id.btnCalcMoyS2);

        Intent i = getIntent();
        this.nom = i.getStringExtra("profil");
        titre = findViewById(R.id.titreS2);

        myDB = new MyDatabaseHelpter(SemestredeuxActivity.this);
        moduleList = new ArrayList<>();
        storedModules(nom,moduleList);
        remplirTitre(moduleList);
        moduleAdapter = new ModuleAdapter(SemestredeuxActivity.this, moduleList, nom);
        recyclerView.setAdapter(moduleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SemestredeuxActivity.this));
        btnAddModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SemestredeuxActivity.this, AddmoduleActivity.class);
                intent.putExtra("profil",nom);
                intent.putExtra("semestre",SEMESTRE);
                startActivity(intent);
            }
        });

        btnCalcMoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcMoySemestre();
            }
        });
    }


    @Override
    protected  void onRestart() {
        super.onRestart();
        setContentView(R.layout.activity_semestredeux);
        recyclerView = findViewById(R.id.recyclerViewS2);
        btnAddModule2 = findViewById(R.id.btnAddModule2);
        btnCalcMoy = findViewById(R.id.btnCalcMoyS2);

        Intent i = getIntent();
        this.nom = i.getStringExtra("profil");
        titre = findViewById(R.id.titreS2);
        myDB = new MyDatabaseHelpter(SemestredeuxActivity.this);
        moduleList = new ArrayList<>();
        storedModules(nom,moduleList);
        remplirTitre(moduleList);
        moduleAdapter = new ModuleAdapter(SemestredeuxActivity.this, moduleList, nom);
        recyclerView.setAdapter(moduleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SemestredeuxActivity.this));

        btnCalcMoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcMoySemestre();
            }
        });
        btnAddModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SemestredeuxActivity.this,AddmoduleActivity.class);
                intent.putExtra("profil",nom);
                intent.putExtra("semestre",SEMESTRE);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        recyclerView = findViewById(R.id.recyclerViewS2);
        btnAddModule2 = findViewById(R.id.btnAddModule2);
        btnCalcMoy = findViewById(R.id.btnCalcMoyS2);

        Intent i = getIntent();
        this.nom = i.getStringExtra("profil");
        titre = findViewById(R.id.titreS2);

        myDB = new MyDatabaseHelpter(SemestredeuxActivity.this);
        moduleList = new ArrayList<>();
        storedModules(nom,moduleList);
        remplirTitre(moduleList);

        moduleAdapter = new ModuleAdapter(SemestredeuxActivity.this, moduleList, nom);
        recyclerView.setAdapter(moduleAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(SemestredeuxActivity.this));
        btnAddModule2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SemestredeuxActivity.this,AddmoduleActivity.class);
                intent.putExtra("profil",nom);
                intent.putExtra("semestre",SEMESTRE);
                startActivity(intent);
            }
        });

        btnCalcMoy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcMoySemestre();
            }
        });
    }


    private void calcMoySemestre() {
        int i = 0;
        double sumMoyCoef=0;
        int sumCoef=0;

        ArrayList<Module> modList = new ArrayList<>();
        storedModules(nom,modList);

        int tailleList = modList.size();
        boolean AllnoteSaved = true;

        while(i<tailleList) {
            recyclerView.scrollToPosition(i);
            int j = i;
            int rvChildNumber = recyclerView.getChildCount();
            while (j<rvChildNumber) {
                Log.d("(i,j):",String.valueOf(i)+","+String.valueOf(j));
                RecyclerView.ViewHolder h1 = recyclerView.getChildViewHolder(recyclerView.getChildAt(j));
                Button btnCalcMoyMod = h1.itemView.findViewById(R.id.btnCalcModule);
                btnCalcMoyMod.performClick();
                j++;
                //TextView moyModuleText = h1.itemView.findViewById(R.id.textViewMoyModule);
                //TextView coefModuleText = h1.itemView.findViewById(R.id.coefContainer);
                //double moyModule = Double.valueOf(moyModuleText.getText().toString());
                //int coefModule = Integer.valueOf(coefModuleText.getText().toString());
            }
            i += rvChildNumber;
        }
        ArrayList<Module> modList2 = new ArrayList<>();
        storedModules(nom,modList2);
        i=0;
        while (i<tailleList) {
            if(!modList2.get(i).isNotesEnregistrees()) {
                AllnoteSaved = false;
                break;
            } else {
                sumCoef += modList2.get(i).getCoef();
                sumMoyCoef += modList2.get(i).getCoef()*modList2.get(i).getMoyModule();
            }
            i++;
        }

        if (AllnoteSaved) {
            double moySemestre = sumMoyCoef / sumCoef;
            myDB.updateMoyenneSemestre(nom,moySemestre,SEMESTRE);
            String s = String.format("%.2f",moySemestre);

            titre.setText("Moyenne : "+String.valueOf(s));


            if (moySemestre>=10) {
                titre.setTextColor(0xFF00cc00);
            } else {
                titre.setTextColor(0xFFff0000);
            }
        } else {
            titre.setTextColor(0xFF03A9F4);
            titre.setText("Remplissez tous les modules");
        }


        if(0==modList.size()) {
            titre.setTextColor(0xFF03A9F4);
            titre.setText("Ajoutez des modules");
        }





    }

    void storedModules(String nom, ArrayList<Module> moduleList) {
        Cursor cursor = myDB.readModules(nom,SEMESTRE);
        if (cursor.getCount()==0) {

        } else {
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String intitule = cursor.getString(1);
                int coef = cursor.getInt(2);
                int s = cursor.getInt(3);
                int type = cursor.getInt(4);
                String n = cursor.getString(5);
                Cursor c = myDB.lookForNotes(nom,id);
                if (c.getCount()==0) {
                    moduleList.add(new Module(id,intitule,coef,s,type,n,0,0,0, false,0));
                } else {
                    c.moveToNext();
                    double noteTD = c.getDouble(2);
                    double noteTP = c.getDouble(3);
                    double noteExam = c.getDouble(4);
                    double moyModule = c.getDouble(5);
                    moduleList.add(new Module(id,intitule,coef,s,type,n,noteExam,noteTD,noteTP, true,moyModule));
                }

            }
        }

    }

    private void remplirTitre(ArrayList<Module> moduleList) {
        if (moduleList.size()==0) {
            titre.setTextColor(0xFF03A9F4);
            titre.setText("Ajoutez des modules");
        } else {
            Cursor c = myDB.moySemestre(SEMESTRE,nom);

            if (c.getCount()==0) {
                titre.setTextColor(0xFF03A9F4);
                titre.setText("Remplissez tous les modules");
            } else {
                c.moveToNext();
                double moy=c.getDouble(0);
                if (moy>=10) {
                    titre.setTextColor(0xFF00cc00);
                } else {
                    titre.setTextColor(0xFFff0000);
                }
                titre.setText("Moyenne : "+String.format("%.2f",moy));
            }
        }
    }
}