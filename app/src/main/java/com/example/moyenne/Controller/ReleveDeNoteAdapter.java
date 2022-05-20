package com.example.moyenne.Controller;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moyenne.Modéle.MyDatabaseHelpter;
import com.example.moyenne.R;
import com.example.moyenne.Vue.SemestredeuxActivity;
import com.example.moyenne.Vue.SemestreunActivity;
import com.example.moyenne.Vue.AnneeActivity;

import java.util.ArrayList;

public class ReleveDeNoteAdapter extends RecyclerView.Adapter<ReleveDeNoteAdapter.MyViewHolder> {

    private Context context;
    private ArrayList releveDeNote;

    public ReleveDeNoteAdapter(Context context, ArrayList releveDeNote) {
        this.context = context;
        this.releveDeNote = releveDeNote;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.profil_row,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.nomProfil.setText(String.valueOf(releveDeNote.get(position)));

    }

    @Override
    public int getItemCount() {
        return releveDeNote.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomProfil;
        ImageButton btnDeleteReleve;
        Button btnS1;
        Button btnS2;
        Button btnAnnee;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
                nomProfil = itemView.findViewById(R.id.textViewPseudo);
                btnDeleteReleve = itemView.findViewById(R.id.btnDeleteProfil);
                btnS1 = itemView.findViewById(R.id.btnS1);
                btnS2 = itemView.findViewById(R.id.btnS2);
                btnAnnee = itemView.findViewById(R.id.btnAnnee);
                itemView.setClickable(true);

            btnS1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SemestreunActivity.class);
                    intent.putExtra("profil", nomProfil.getText().toString());
                    context.startActivity(intent);
                }
            });

            btnS2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SemestredeuxActivity.class);
                    intent.putExtra("profil", nomProfil.getText().toString());
                    context.startActivity(intent);
                }
            });

            btnAnnee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double result[] = checkMoyennes(nomProfil.getText().toString());
                    if(result[0]!=-1) {
                        double moyS1 = result[0];
                        double moyS2 = result[1];

                        Intent intent = new Intent(context, AnneeActivity.class);
                        intent.putExtra("moyS1",moyS1);
                        intent.putExtra("moyS2",moyS2);
                        context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Calculez d'abord la moyenne du semestres 1 et 2", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            btnDeleteReleve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyDatabaseHelpter myDB = new MyDatabaseHelpter(context);
                    try {
                        myDB.deleteProfil(nomProfil.getText().toString());
                    }
                    catch (SQLException e ){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        releveDeNote.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        notifyItemChanged(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());

                    }
                }
            });
        }
    }

    private double[] checkMoyennes(String nomReleveDeNote) {
        MyDatabaseHelpter myDB = new MyDatabaseHelpter(context);
        SQLiteDatabase db = myDB.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Profil WHERE nom=? and moyS1 IS NOT NULL and moyS2 IS NOT NULL",new String[] {nomReleveDeNote});
        if (c.getCount()==0) {
            return new double[]{-1};
        }
        c.moveToNext();
        return new double[] {c.getDouble(1), c.getDouble(2)};


    }
}
