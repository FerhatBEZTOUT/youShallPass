package com.example.moyenne.Controller;

import static android.text.TextUtils.isEmpty;

import android.content.Context;
import android.database.SQLException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moyenne.Modéle.Module;
import com.example.moyenne.R;

import java.util.ArrayList;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.MyViewHolder>{

    Context context;
    ArrayList<Module> modulesList;
    MyDatabaseHelpter myDB;
    String nomProfil;
    TextView moyModule;
    /*EditText td;
    EditText tp;
    EditText exam;*/
    boolean noteEnregistrees;
    int id;
    int type;

    public ModuleAdapter(Context context, ArrayList modulesList, String nomProfil) {
        this.context = context;
        this.modulesList = modulesList;
        this.nomProfil = nomProfil;
        myDB = new MyDatabaseHelpter(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.module_row,parent,false);
        return new ModuleAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.coefContainer.setText(String.valueOf(modulesList.get(position).getCoef()));
        holder.typeContainer.setText(String.valueOf(modulesList.get(position).getType()));
        holder.nomModule.setText(String.valueOf(modulesList.get(position).getIntitule()));
        holder.idContainer.setText(String.valueOf(modulesList.get(position).getId()));
        id = Integer.parseInt(holder.idContainer.getText().toString());
        type = modulesList.get(position).getType();
        noteEnregistrees = modulesList.get(position).isNotesEnregistrees();
        if (!noteEnregistrees) {
            holder.exam1.setText("");
            holder.td1.setText("");
            holder.tp1.setText("");
            holder.myMoyModule.setText("");
        } else {
            holder.exam1.setText(String.format("%.2f",modulesList.get(position).getNoteExam()).replace(",","."));
            holder.td1.setText(String.format("%.2f",modulesList.get(position).getNoteTD()).replace(",","."));
            holder.tp1.setText(String.format("%.2f",modulesList.get(position).getNoteTP()).replace(",","."));
            double a = modulesList.get(position).getMoyModule();

            if (a==10) {
                holder.myMoyModule.setTextColor(0xFFFF9800);
            } else if (a<10) {
                holder.myMoyModule.setTextColor(0xFFEA1708);
            } else {
                holder.myMoyModule.setTextColor(0xFF0DDA15);
            }
            holder.myMoyModule.setText(String.format("%.2f",modulesList.get(position).getMoyModule()).replace(",","."));
        }


        if (modulesList.get(position).getType()==4) {
            holder.td1.setVisibility(View.GONE);
            holder.tp1.setVisibility(View.GONE);

        } else if (modulesList.get(position).getType()==3) {
            holder.td1.setVisibility(View.GONE);

        } else if (modulesList.get(position).getType()==2) {
            holder.tp1.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return modulesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView nomModule,typeContainer,coefContainer,idContainer,myMoyModule;
            EditText td1;
            EditText tp1;
            EditText exam1;
            ImageButton btnDeleteModule;
            Button btnCalcMoyModule;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moyModule = itemView.findViewById(R.id.textViewMoyModule);
            nomModule = itemView.findViewById(R.id.textViewNomModule);
            myMoyModule = itemView.findViewById(R.id.textViewMoyModule);
            td1 = itemView.findViewById(R.id.editTextTD);
            tp1 = itemView.findViewById(R.id.editTextTP);
            exam1 = itemView.findViewById(R.id.editTextExam);
            btnDeleteModule = itemView.findViewById(R.id.btnDeleteModule);
            btnCalcMoyModule = itemView.findViewById(R.id.btnCalcModule);
            typeContainer = itemView.findViewById(R.id.typeContainer);
            coefContainer = itemView.findViewById(R.id.coefContainer);
            idContainer = itemView.findViewById(R.id.idContainer);


            itemView.setClickable(true);

            btnCalcMoyModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    boolean error=false;
                    String msg="";
                    double moy,myexam,mytd,mytp;
                    /*moy=0;*/
                    int myId = Integer.valueOf(idContainer.getText().toString());
                    int myType = Integer.valueOf(typeContainer.getText().toString());
                    if (myType==4) {
                        if (isEmpty(exam1.getText())) {
                            error = true;
                            msg = "Champ exam vide";
                        } else {
                            myexam =Double.valueOf(exam1.getText().toString());

                            if (myexam>=0 && myexam<=20) {
                                moy = Module.calcMoyModule(myexam);
                                if (moy==10) {
                                    myMoyModule.setTextColor(0xFFFF9800);
                                } else if (moy<10) {
                                    myMoyModule.setTextColor(0xFFEA1708);
                                 } else {
                                    myMoyModule.setTextColor(0xFF0DDA15);
                                }
                                myMoyModule.setText(String.format("%.2f",moy).replace(",","."));

                                myDB.insertNote(myId,nomProfil,myexam,moy);

                            } else {
                                error = true;
                                msg = "Les notes doivent être entre 0 et 20";
                            }
                        }
                    } else if (myType==3) {
                        if (isEmpty(exam1.getText()) || isEmpty(tp1.getText())) {
                            error = true;
                            msg = "Champ exam ou tp vide";
                        } else {
                            myexam = Double.valueOf(exam1.getText().toString());
                            mytp = Double.valueOf(tp1.getText().toString());
                            if (myexam>=0 && myexam<=20 && mytp>=0 && mytp<=20) {
                                moy = Module.calcMoyModule(myexam,mytp);
                                if (moy==10) {
                                    myMoyModule.setTextColor(0xFFFF9800);
                                } else if (moy<10) {
                                    myMoyModule.setTextColor(0xFFEA1708);
                                } else {
                                    myMoyModule.setTextColor(0xFF0DDA15);
                                }
                                myMoyModule.setText(String.format("%.2f",moy).replace(",","."));

                                myDB.insertNote(myId,nomProfil,myexam,mytp,moy);
                            } else {
                                error = true;
                                msg = "Les notes doivent être entre 0 et 20";
                            }
                        }
                    } else if (myType==2) {
                        if (isEmpty(exam1.getText()) || isEmpty(td1.getText())) {
                            error = true;
                            msg = "Champ exam ou td vide";
                        } else {
                            myexam = Double.valueOf(exam1.getText().toString());
                            mytd = Double.valueOf(td1.getText().toString());
                            if (myexam>=0 && myexam<=20 && mytd>=0 && mytd<=20) {
                                moy = Module.calcMoyModule(myexam,mytd);
                                if (moy==10) {
                                    myMoyModule.setTextColor(0xFFFF9800);
                                } else if (moy<10) {
                                    myMoyModule.setTextColor(0xFFEA1708);
                                } else {
                                    myMoyModule.setTextColor(0xFF0DDA15);
                                }
                                myMoyModule.setText(String.format("%.2f",moy).replace(",","."));

                                myDB.insertNote(myId,nomProfil,myexam,mytd,moy);
                            } else {
                                error = true;
                                msg = "Les notes doivent être entre 0 et 20";
                            }
                        }
                    } else if (myType==1) {
                        if (isEmpty(exam1.getText()) || isEmpty(td1.getText()) || isEmpty(tp1.getText())) {
                            error = true;
                            msg = "Champ exam ou td ou tp vide";
                        } else {
                            myexam =Double.valueOf(exam1.getText().toString());
                            mytd = Double.valueOf(td1.getText().toString());
                            mytp = Double.valueOf(tp1.getText().toString());
                            if (myexam>=0 && myexam<=20 && mytd>=0 && mytd<=20 && mytp>=0 && mytp<=20) {
                                moy = Module.calcMoyModule(myexam,mytd,mytp);
                                if (moy==10) {
                                    myMoyModule.setTextColor(0xFFFF9800);
                                } else if (moy<10) {
                                    myMoyModule.setTextColor(0xFFEA1708);
                                } else {
                                    myMoyModule.setTextColor(0xFF0DDA15);
                                }
                                myMoyModule.setText(String.format("%.2f",moy).replace(",","."));
                                myDB.insertNote(myId,nomProfil,myexam,mytd,mytp,moy);
                            } else {
                                error = true;
                                msg = "Les notes doivent être entre 0 et 20";
                            }
                        }
                    }

                    if (error) {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnDeleteModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyDatabaseHelpter myDB = new MyDatabaseHelpter(context);
                    try {
                        myDB.deleteModule(Integer.valueOf(idContainer.getText().toString()));
                    }
                    catch (SQLException e ){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    finally {
                        modulesList.remove(getAdapterPosition());
                        notifyDataSetChanged();
                        notifyItemChanged(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    }
                }
            });
        }
    }

}
