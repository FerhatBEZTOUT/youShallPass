package com.example.moyenne.Modéle;

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
