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
import android.widget.Spinner;
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

    String id;
    DatabaseReference databaseReference;
    EditText editTextId;
    Query nombreQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_equipo);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        //Obtener el id del equipo seleccionado en la anterior actividad.
        Intent intent = getIntent();
        id = intent.getData().toString();
        Log.d("infoApp", id);

        StorageReference imagenesRef = storageReference.child(id);

        //Cargar la imagen del equipo sin descargarla
        ImageView imageViewFoto = findViewById(R.id.imageViewEquipo);
        Glide.with(this).load(imagenesRef).into(imageViewFoto);

        //Cargar los valores anteriores
        editTextId = findViewById(R.id.editTextId);
        editTextId.setText(id);

        databaseReference.child("pruebas").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    EquipoDto equipoDto = dataSnapshot.getValue(EquipoDto.class);

                    //Cargar los valores anteriores
                    EditText editTextNombre = findViewById(R.id.editTextNombre);
                    String nombre = equipoDto.getNombre();
                    editTextNombre.setText(nombre);

                    EditText editTextMarca = findViewById(R.id.editTextMarca);
                    String marca = equipoDto.getMarca();
                    editTextMarca.setText(marca);

                    EditText editTextFecha = findViewById(R.id.editTextFechaSeleccionada);
                    String fecha = equipoDto.getFechaMantenimiento();
                    editTextFecha.setText(fecha);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void seleccionarFecha(View view){
        //Mostrar el fragment para elegir una fecha
        DatePickerFragment datePickerFragment =  new DatePickerFragment();
        datePickerFragment.show(getSupportFragmentManager(), "fecha");
    }

    public void mostrarFecha(int year, int month, int dayOfMonth){
        //Mostrar fecha elegida en el edit text
        String fecha = String.valueOf(year)+"/"+ String.valueOf(month)+"/"+String.valueOf(dayOfMonth);
        EditText editTextFechaSeleccionada = findViewById(R.id.editTextFechaSeleccionada);
        editTextFechaSeleccionada.setText(fecha);
    }

    public void btnModificar(View view) {

        //Método donde se agrega a la database toda la información del equipo
        EditText editTextId = findViewById(R.id.editTextId);
        String idString = String.valueOf(editTextId.getText());
        int id = Integer.valueOf(idString);
        EditText editTextNombre = findViewById(R.id.editTextNombre);
        String nombre = String.valueOf(editTextNombre.getText());
        EditText editTextMarca = findViewById(R.id.editTextMarca);
        String marca = String.valueOf(editTextMarca.getText());
        Spinner spinnerTipo = findViewById(R.id.spinner);
        String tipo = spinnerTipo.getSelectedItem().toString();
        EditText editTextFechaSeleccionada = findViewById(R.id.editTextFechaSeleccionada);
        String fechaSeleccionada = String.valueOf(editTextFechaSeleccionada.getText());

        //Verificación de valores no nulos
        if (TextUtils.isEmpty(idString) || TextUtils.isEmpty(nombre) ||TextUtils.isEmpty(marca) || TextUtils.isEmpty(fechaSeleccionada)) {
            if(TextUtils.isEmpty(idString)){
                editTextId.setError("Por favor ingrese un id");
            }
            if(TextUtils.isEmpty(nombre)){
                editTextId.setError("Por favor ingrese un nombre");
            }
            if(TextUtils.isEmpty(marca)){
                editTextMarca.setError("Por favor ingrese una marca");
            }
            if(TextUtils.isEmpty(fechaSeleccionada)){
                editTextFechaSeleccionada.setError("Por favor ingrese una fecha");
            }
        } else {

            //Crear el objeto y mandarlo a database
            EquipoDto equipoDto = new EquipoDto();
            equipoDto.setId(id);
            equipoDto.setNombre(nombre);
            equipoDto.setMarca(marca);
            equipoDto.setTipo(tipo);
            equipoDto.setFechaMantenimiento(fechaSeleccionada);

            databaseReference.child("pruebas").child(idString).setValue(equipoDto).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        finish();
    }

    public void btnEliminarEquipo(View view) {
        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        ref.getReference().child("pruebas").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
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