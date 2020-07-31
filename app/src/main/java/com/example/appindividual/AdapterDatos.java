package com.example.appindividual;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appindividual.entidades.EquipoRV;

import java.util.ArrayList;

//Clase para adaptar los datos y mostrarlos en el Recycler View

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos>
        implements View.OnClickListener {

    //Lista a llenar
    ArrayList<EquipoRV> listDatos;
    private View.OnClickListener listener;

    public AdapterDatos(ArrayList<EquipoRV> listDatos) {
        this.listDatos = listDatos;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv,null,false);
        view.setOnClickListener(this);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.textViewNombre.setText(listDatos.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    //Constructor de la clase
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView textViewNombre;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
        }

    }
}
