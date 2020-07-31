package com.example.appindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appindividual.entidades.EquipoDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ModificarEquipoActivity extends AppCompatActivity {

    String nombre;
    DatabaseReference databaseReference;
    EditText editTextNombre;
    Query nombreQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_equipo);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        Intent intent = getIntent();
        nombre = intent.getData().toString();
        Log.d("infoApp", nombre);

        StorageReference imagenesRef = storageReference.child(nombre);

        ImageView imageViewFoto = findViewById(R.id.imageViewEquipo);
        Glide.with(this).load(imagenesRef).into(imageViewFoto);

        editTextNombre = findViewById(R.id.editTextNombre);
        editTextNombre.setText(nombre);

        databaseReference.child("pruebas").child(nombre).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    EquipoDto equipoDto = dataSnapshot.getValue(EquipoDto.class);

                    EditText editTextColor = findViewById(R.id.editTextMarca);
                    //String color = equipoDto.getColor();
                    //editTextColor.setText(color);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void btnModificar(View view) {

        EditText editTextNombre = findViewById(R.id.editTextNombre);
        String nombre = String.valueOf(editTextNombre.getText());
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
                    Toast.makeText(getApplicationContext(), "Equipo modificado exitosamente", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
                }
            });
        }

        finish();
    }

    public void btnEliminarEquipo(View view) {
        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        ref.getReference().child("pruebas").child(nombre).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(), "Equipo eliminado exitosamente", Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error al eliminar equipo", Toast.LENGTH_SHORT);
            }
        });

        finish();
    }


}