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
    String id;

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
                    Log.d("infoApyp", equipoDto.getNombre());

                    recycler = findViewById(R.id.recyclerView);
                    recycler.setLayoutManager(new LinearLayoutManager(MenuActivityAdmin.this));

                    //Se agrega un nuevo equipo a la lista
                    EquipoRV equipoRV = new EquipoRV(equipoDto.getId(),equipoDto.getNombre());
                    listEquipos.add(equipoRV);

                    AdapterDatos adapter = new AdapterDatos(listEquipos);

                    //Click en algún elemento de la lista
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(getApplicationContext(),"Seleccion: "+listEquipos.get
                            //        (recycler.getChildAdapterPosition(v)).getNombre(),Toast.LENGTH_SHORT).show();

                            id = String.valueOf(listEquipos.get(recycler.getChildAdapterPosition(v)).getId());

                            //Si se le da Click, pasa a modificar el equipo seleccionado
                            Intent intent2 = new Intent(MenuActivityAdmin.this, ModificarEquipoActivity.class);
                            intent2.setData(Uri.parse(id));
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

    public void btnAgregar(MenuItem item){
        //Botón para agregar un equipo (en el menú superior)
        Intent intent = new Intent(this, AgregarEquipoActivity.class);
        int requestCode = 1;
        startActivityForResult(intent,requestCode);
    }

    public void btnCerrarSesion(MenuItem item){
        //Botón para cerrar la sesión iniciada
        AuthUI.getInstance().signOut(MenuActivityAdmin.this);
        finish();

    }

}