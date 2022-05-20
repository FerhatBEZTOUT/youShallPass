package com.example.moyenne.Modéle;

public class ReleveDeNote {
    private String nom;
    private double moyS1;
    private double moyS2;

    public ReleveDeNote(String nom, double moyS1, double moyS2) {
        this.nom = nom;
        this.moyS1 = moyS1;
        this.moyS2 = moyS2;
    }

    public String getNom() {
        return nom;
    }

    public double getMoyS1() {
        return moyS1;
    }

    public double getMoyS2() {
        return moyS2;
    }

    public double calcMoyAnnuelle() {
        return (moyS1+moyS2)/2;
    }
}
