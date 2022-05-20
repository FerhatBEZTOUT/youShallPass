package com.example.moyenne.Vue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moyenne.R;

public class AnneeActivity extends AppCompatActivity {
    ImageView imgEmoji;
    TextView txtMoyAnnee,txtAnneeMoyS1,txtAnneeMoyS2,txtRemarque;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annee);
        Intent i = new Intent();
        i = getIntent();

        double moyS1 = i.getDoubleExtra("moyS1",-1);
        double moyS2 = i.getDoubleExtra("moyS2",-1);
        double moyGenerale = (moyS1+moyS2)/2;

        imgEmoji = findViewById(R.id.imgEmoji);
        txtMoyAnnee = findViewById(R.id.txtMoyAnnee);
        txtAnneeMoyS1 = findViewById(R.id.txtAnneeMoyS1);
        txtAnneeMoyS2 = findViewById(R.id.txtAnneeMoyS2);
        txtRemarque = findViewById(R.id.txtRemarque);

        if (moyS1>=10) {
            txtAnneeMoyS1.setTextColor(Color.GREEN);
        } else {
            txtAnneeMoyS1.setTextColor(Color.RED);
        }
        txtAnneeMoyS1.setText(String.format("%.2f",moyS1));

        if (moyS2>=10) {
            txtAnneeMoyS2.setTextColor(Color.GREEN);
        } else {
            txtAnneeMoyS2.setTextColor(Color.RED);
        }
        txtAnneeMoyS2.setText(String.format("%.2f",moyS2));

        if (moyGenerale>=10) {
            txtMoyAnnee.setTextColor(Color.GREEN);
            txtRemarque.setTextColor(Color.GREEN);
            txtRemarque.setText("Admis");
            imgEmoji.setImageResource(R.drawable.happyemoji);
        } else {
            txtMoyAnnee.setTextColor(Color.RED);
            txtRemarque.setTextColor(Color.RED);
            txtRemarque.setText("Ajourné");
            imgEmoji.setImageResource(R.drawable.sademoji);
        }
        txtMoyAnnee.setText(String.format("%.2f",moyGenerale));




    }
}