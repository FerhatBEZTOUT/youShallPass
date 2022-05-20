package com.example.moyenne.Controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.moyenne.Modéle.Module;

public class MyDatabaseHelpter extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "moyenneD.db";
    private static final int DATABASE_VERSION = 1;
    private Module module;


    public MyDatabaseHelpter(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        module = new Module();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL("CREATE TABLE profil (nom TEXT PRIMARY KEY, moyS1 REAL, moyS2 REAL)");
        db.execSQL("CREATE TABLE module (id INTEGER PRIMARY KEY AUTOINCREMENT,intitule TEXT,coef INTEGER, semestre INTEGER,type INTEGER,nom TEXT,FOREIGN KEY(nom) REFERENCES profil(nom) ON DELETE CASCADE ON UPDATE NO ACTION)");
        db.execSQL("CREATE TABLE note (id INTEGER,nom TEXT,td REAL,tp REAL,exam REAL,moy REAL,PRIMARY KEY(id,nom),FOREIGN KEY(id) REFERENCES module(id) ON DELETE CASCADE ON UPDATE NO ACTION,FOREIGN KEY(nom) REFERENCES profil(nom) ON DELETE CASCADE ON UPDATE NO ACTION)");
    }

    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL("DROP TABLE IF EXISTS note");
        db.execSQL("DROP TABLE IF EXISTS module");
        db.execSQL("DROP TABLE IF EXISTS profil");
        onCreate(db);
    }





    // Requetes sur les relevés de notes  =============================================================

    public void addProfil(String nom, Activity a) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nom",nom);

        try {
            long result = db.insert("profil",null,cv);

            if (result == -1) {
                Toast.makeText(context, "Relevé de note existe déjà",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Relevé de note créé",Toast.LENGTH_SHORT).show();
                a.finish();
            }
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }

    }

    public Cursor readProfils() {
        String query = "SELECT * FROM profil";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if (db != null) {
            cursor = db.rawQuery(query,null);

        }
        return cursor;
    }

    public void deleteProfil(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete("profil","nom=?",new String[]{id});

        if (result == -1) {
            Toast.makeText(context, "Echec de supression",Toast.LENGTH_SHORT).show();
        }

    }





    // Requetes sur les modules =============================================================

    public void addModule (String intitule, int coef, int semestre, int type, String nom,Context c){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues cv = new ContentValues();
        cv.put("intitule",intitule);
        cv.put("coef",coef);
        cv.put("semestre",semestre);
        cv.put("type",type);
        cv.put("nom",nom);

        try {
            long result = db.insert("module",null,cv);
            if (result == -1) {
                Toast.makeText(context, "Erreur d'ajout module",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Module ajouté",Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(),Toast.LENGTH_LONG).show();
        } finally {
            db.close();
        }
    }

    public Cursor readModules(String nom,int semestre) {
        Cursor cursor = null;
        try {
            String query = "SELECT * FROM module WHERE nom=? AND semestre=?";
            SQLiteDatabase db = this.getReadableDatabase();

            if (db != null) {
                cursor = db.rawQuery(query,new String[] {nom,String.valueOf(semestre)});
            }

        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {
            return cursor;
        }

    }

    public void deleteModule(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("module","id=?",new String[]{String.valueOf(id)});

        if (result == -1) {
            Toast.makeText(context, "Echec de supression",Toast.LENGTH_SHORT).show();
        }
    }




    // Requêtes sur les notes =============================================================

    public Cursor lookForNotes(String nom, int idModule) {
        Cursor cursor = null;

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM note WHERE nom=? AND id=?";


            if (db != null) {
                cursor = db.rawQuery(query,new String[] {nom,String.valueOf(idModule)});
            }

        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {

            return cursor;
        }
    }



    public void insertNote(int id, String nom, double exam, double moy) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("nom",nom);
        cv.put("exam",exam);
        cv.put("tp",0);
        cv.put("td",0);
        cv.put("moy",moy);


        Cursor cursor = lookForNotes(nom,id);
        if (cursor.getCount()==0) {

            try {
                long result = db.insert("note",null,cv);
                if (result == -1) {
                    Toast.makeText(context, "Erreur d'ajout note",Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        } else {
            try {
                long result = db.update("note",cv,"id=? AND nom=?",new String[]{String.valueOf(id),nom});
                if (result == -1) {
                    Toast.makeText(context, "Erreur update note",Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        }


    }

    public void insertNote(int id, String nom, double exam, double tp_ou_td, double moy) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("nom",nom);
        cv.put("exam",exam);
        cv.put("td",tp_ou_td);
        cv.put("tp",tp_ou_td);
        cv.put("moy",moy);

        Cursor cursor = lookForNotes(nom,id);
        if (cursor.getCount()==0) {


            try {
                long result = db.insert("note",null,cv);
                if (result == -1) {
                    Toast.makeText(context, "Erreur d'ajout note",Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        } else {
            try {
                long result = db.update("note",cv,"id=? AND nom=?",new String[]{String.valueOf(id),nom});
                if (result == -1) {
                    Toast.makeText(context, "Erreur update note",Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        }

    }

    public void insertNote(int id, String nom, double exam, double td, double tp, double moy) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues cv = new ContentValues();
        cv.put("id",id);
        cv.put("nom",nom);
        cv.put("exam",exam);
        cv.put("td",td);
        cv.put("tp",tp);
        cv.put("moy",moy);

        Cursor cursor = lookForNotes(nom,id);
        if (cursor.getCount()==0) {

            try {
                long result = db.insert("note",null,cv);
                if (result == -1) {
                    Toast.makeText(context, "Erreur d'ajout note",Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        } else {
            try {
                long result = db.update("note",cv,"id=? AND nom=?",new String[]{String.valueOf(id),nom});
                if (result == -1) {
                    Toast.makeText(context, "Erreur update note",Toast.LENGTH_SHORT).show();
                }
            } catch (SQLException e) {
                Toast.makeText(context, e.getMessage(),Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        }

    }




    // Requêtes sur les semestres =============================================================

    public Cursor moySemestre(int semestre,String nom) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query;

                query = "SELECT moyS"+String.valueOf(semestre)+" FROM profil WHERE nom=? AND moyS"+String.valueOf(semestre)+" IS NOT NULL";

            if (db != null) {
                cursor = db.rawQuery(query,new String[] {nom});

            }

        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {

            return cursor;
        }
    }

    public void updateMoyenneSemestre(String nom, double moySemestre, int semestre) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON");
        ContentValues cv = new ContentValues();
        cv.put("moyS"+String.valueOf(semestre),moySemestre);
        Log.d("updateSemestre","moyS"+String.valueOf(semestre));
        Log.d("moySemestre",String.valueOf(moySemestre));
        try {
            long result = db.update("profil",cv,"nom=?",new String[]{nom});
            if (result == -1) {
                Log.d("updateMoyenneSemestre","update profil");
            }
        } catch (SQLException e) {
            Log.d("updateMoyenneSemestre",e.getMessage());
        } finally {
            db.close();
        }

    }

    /*public Cursor lookForMoySemestre(String nom, int semestre) {
        Cursor cursor = null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            String query = "SELECT * FROM profil WHERE nom=? AND moyS"+String.valueOf(semestre)+"=?";


            if (db != null) {
                cursor = db.rawQuery(query,new String[] {nom,String.valueOf(semestre)});
            }

        } catch (SQLException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        finally {

            return cursor;
        }
    }*/
}
