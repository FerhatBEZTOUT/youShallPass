package com.example.moyenne.Modéle;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.moyenne.Controller.MyDatabaseHelpter;

public class Note {
    private int idModule;
    private String nom;
    private double noteTD;
    private double noteTP;
    private double noteExam;
    private double moyModule;

    public Note(int idModule, String nom, double noteTD, double noteTP, double noteExam, double moyModule) {
        this.idModule = idModule;
        this.nom = nom;
        this.noteTD = noteTD;
        this.noteTP = noteTP;
        this.noteExam = noteExam;
        this.moyModule = moyModule;
    }

    public double getMoyModule() {
        return moyModule;
    }

    public int getIdModule() {
        return idModule;
    }

    public String getNom() {
        return nom;
    }

    public double getNoteTD() {
        return noteTD;
    }

    public double getNoteTP() {
        return noteTP;
    }

    public double getNoteExam() {
        return noteExam;
    }

}
