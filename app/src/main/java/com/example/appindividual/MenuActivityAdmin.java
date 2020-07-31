package com.example.appindividual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appindividual.entidades.EquipoDto;
import com.example.appindividual.entidades.EquipoRV;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MenuActivityAdmin extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_agregar,menu);
        return true;
    }

    DatabaseReference databaseReference;
    RecyclerView recycler;
    ArrayList<EquipoRV> listEquipos;
    String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Hacer que se muestre en recycler View
        listEquipos = new ArrayList<EquipoRV>();

        databaseReference.child("pruebas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null){

                    EquipoDto equipoDto = dataSnapshot.getValue(EquipoDto.class);
                    Log.d("infoApp", equipoDto.getNombre());

                    recycler = findViewById(R.id.recyclerView);
                    recycler.setLayoutManager(new LinearLayoutManager(MenuActivityAdmin.this));

                    listEquipos.add(new EquipoRV(equipoDto.getNombre()));

                    AdapterDatos adapter = new AdapterDatos(listEquipos);

                    //Click en algún elemento de la lista
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"Seleccion: "+listEquipos.get
                            //        (recycler.getChildAdapterPosition(v)).getNombre(),Toast.LENGTH_SHORT).show();

                            nombre = listEquipos.get(recycler.getChildAdapterPosition(v)).getNombre();

                            Intent intent2 = new Intent(MenuActivityAdmin.this, ModificarEquipoActivity.class);
                            intent2.setData(Uri.parse(nombre));
                            int requestCode = 2;
                            startActivityForResult(intent2,requestCode);

                        }
                    });

                    recycler.setAdapter(adapter);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void modificarDatos(String nombre) {

        EditText editTextNombre = findViewById(R.id.editTextNombre);
        editTextNombre.setText(nombre);
        String nombreModificado = String.valueOf(editTextNombre.getText());
        EditText editTextColor = findViewById(R.id.editTextMarca);
        String color = String.valueOf(editTextColor.getText());

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(color)) {
            if (TextUtils.isEmpty(nombre)) {
                editTextNombre.setError("Por favor ingrese un nombre");
            }
            if (TextUtils.isEmpty(color)) {
                editTextColor.setError("Por favor ingrese un color");
            }
        } else {

            EquipoDto equipoDto = new EquipoDto();
            equipoDto.setNombre(nombre);
            //equipoDto.setColor(color);

            databaseReference.child("pruebas").child(nombre).setValue(equipoDto).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Equipo agregado exitosamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void btnAgregar(MenuItem item){
        Intent intent = new Intent(this, AgregarEquipoActivity.class);
        int requestCode = 1;
        startActivityForResult(intent,requestCode);
    }

    public void btnCerrarSesion(MenuItem item){

        AuthUI.getInstance().signOut(MenuActivityAdmin.this);
        finish();

    }

}