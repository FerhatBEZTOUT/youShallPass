package com.example.moyenne.Modéle;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Module {
    private int id;
    private String intitule;
    private int coef;
    private int semestre;
    private int type;
    private String nom;
    private double noteExam;
    private double noteTD;
    private double noteTP;
    private double moyModule;
    private boolean notesEnregistrees;

    public Module() {

    }

    public Module(int id, String intitule, int coef, int semestre, int type, String nom, double noteExam, double noteTD, double noteTP, boolean notesEnregistrees, double moyModule) {
        this.id = id;
        this.intitule = intitule;
        this.coef = coef;
        this.semestre = semestre;
        this.type = type;
        this.nom = nom;
        this.noteExam = noteExam;
        this.noteTD = noteTD;
        this.noteTP = noteTP;
        this.moyModule = moyModule;
        this.notesEnregistrees = notesEnregistrees;
    }

    public static boolean isModuleExist(String intitule, String nomProfil, int semestre, Context context) {
        MyDatabaseHelpter myDB = new MyDatabaseHelpter(context);
        SQLiteDatabase db = myDB.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM module WHERE intitule=? and nom=? and semestre=?",new String[] {intitule,nomProfil,String.valueOf(semestre)});
        if (c.getCount()==0) {
            return false;
        }
        return true;

    }

    public double getNoteExam() {
        return noteExam;
    }

    public double getNoteTD() {
        return noteTD;
    }

    public double getNoteTP() {
        return noteTP;
    }

    public boolean isNotesEnregistrees() {
        return notesEnregistrees;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getIntitule() {
        return intitule;
    }

    public int getCoef() {
        return coef;
    }

    public int getSemestre() {
        return semestre;
    }

    public String getNom() {
        return nom;
    }

    public double getMoyModule() {
        return moyModule;
    }

    public static double calcMoyModule (double exam) {
        return exam;
    }

    public static double calcMoyModule(double exam, double td_ou_tp) {
        return ((exam*0.6) + (td_ou_tp*0.4));
    }

    public static double calcMoyModule(double exam,double td, double tp) {
        return ((exam*0.6) + (((td+tp)/2)*0.4));
    }


}
