package com.example.appindividual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appindividual.entidades.EquipoDto;
import com.example.appindividual.entidades.EquipoRV;
import com.example.appindividual.entidades.FallaDto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    //Variables globales
    DatabaseReference databaseReference;
    RecyclerView recycler;
    ArrayList<EquipoRV> listEquipos;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recycler = findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //Hacer que se muestre en recycler View
        listEquipos = new ArrayList<EquipoRV>();

        //Listener de los equipos
        databaseReference.child("pruebas").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getValue() != null){

                    //Obtener el nombre de cada equipo
                    EquipoDto equipoDto = dataSnapshot.getValue(EquipoDto.class);
                    Log.d("infoApyp", equipoDto.getNombre());

                    recycler = findViewById(R.id.recyclerView);
                    recycler.setLayoutManager(new LinearLayoutManager(MenuActivity.this));

                    //Agregar los nombres a la lista
                    listEquipos.add(new EquipoRV(equipoDto.getId(),equipoDto.getNombre()));

                    AdapterDatos adapter = new AdapterDatos(listEquipos);

                    //Click en algún elemento de la lista
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Se lee el ID de cada equipo
                            id = listEquipos.get(recycler.getChildAdapterPosition(v)).getId();

                            //Se realiza una petición, se eligió hacerla con Voller en lugar de addValueEventListener para que se
                            // busque si la falla esta reportada o no una sola vez.
                            RequestQueue queue = Volley.newRequestQueue(MenuActivity.this);
                            String url = "https://medical-data-57270.firebaseio.com/"+ String.valueOf(id) +".json";

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            //Se pasa del archivo JSON al objeto falla
                                            Gson gson = new Gson();
                                            FallaDto falla = gson.fromJson(response,FallaDto.class);
                                            final String eleccion = String.valueOf(falla.isFallo());
                                            Log.d("infoApp", eleccion);

                                            //Dependiendo si hay o no un fallo reportado se muestra:
                                            switch (eleccion){

                                                case "true":
                                                    Log.d("infoApp", "hay fallo reportado");

                                                    //Un alert para dar conformidad al servicio
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                                                    builder.setMessage("¿El equipo ahora está funcionando correctamente?");
                                                    builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            //poner el fallo en false
                                                            modificarFallo(id);
                                                        }
                                                    });
                                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                                    builder.show();
                                                    break;

                                                case "false":

                                                    //O se redirecciona a la página para reportar fallas.
                                                    Log.d("infoApp", "sin fallo");
                                                    Intent intent = new Intent(MenuActivity.this, ReportarFallosActivity.class);
                                                    intent.putExtra("id", String.valueOf(id));
                                                    int requestCode = 1;
                                                    startActivityForResult(intent, requestCode);
                                                    break;

                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                            queue.add(stringRequest);

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

    public void modificarFallo(int id){

        //Método para eliminar el fallo cuando se etá conforme con el servicio
        databaseReference = FirebaseDatabase.getInstance().getReference();

        FallaDto fallaDto = new FallaDto();
        fallaDto.setFallo(false);

        databaseReference.child(String.valueOf(id)).setValue(fallaDto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast toast = Toast.makeText(getApplicationContext(),"Enviado exitosamente",Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast toast = Toast.makeText(getApplicationContext(),"No enviado",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


}