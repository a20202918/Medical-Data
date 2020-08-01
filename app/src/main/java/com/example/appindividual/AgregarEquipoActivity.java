package com.example.appindividual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appindividual.entidades.EquipoDto;
import com.example.appindividual.entidades.FallaDto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AgregarEquipoActivity extends AppCompatActivity {

    //Variables globales
    DatabaseReference databaseReference;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_equipo);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
    }

    public void subirImagenStream(View view) {

        //Método para subir imagen Stream, se validan los permisos y se crea un intent para seleccionar la imagen deseada (en la carpeta imágenes)

        if (validarPermisos(1)) {

            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            int requestCode = 1;
            intent.setType("image/");
            startActivityForResult(intent, requestCode);

        }
    }

    public boolean validarPermisos(int requestCode) {

        //Método para validar permisos de lectura de los archivos
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Si los resultados se cumplen, se corre el método subirImagenStream
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                subirImagenStream(null);
            }
        }
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

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

                firebaseAuth = FirebaseAuth.getInstance();
                currentUser = firebaseAuth.getCurrentUser();
                Button bsubir = findViewById(R.id.buttonSubirFotoEquipo);
                bsubir.setEnabled(false);
                if (resultCode == RESULT_OK) {

                    //Proceso de subir la imagen con el nombre propuesto
                    Uri path = data.getData();
                    //imagen.setImageURI(path);
                    EditText editTextId = findViewById(R.id.editTextId);
                    String id = String.valueOf(editTextId.getText());
                    StorageReference imagenesRef = storageReference.child(id);

                    StorageMetadata metadata = new StorageMetadata.Builder().build();

                    imagenesRef.putFile(path, metadata)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d("infoAapp", "subida exitosa");
                                    Toast.makeText(AgregarEquipoActivity.this, "subida exitosa", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("infoAapp", "error al subir");
                            Toast.makeText(AgregarEquipoActivity.this, "error al subir", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
        }

        public void btnAgregarEquipo (View view){

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
            }else{

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

                databaseReference = FirebaseDatabase.getInstance().getReference();

                //Se crea el objeto de fallos
                FallaDto fallaDto = new FallaDto();
                fallaDto.setFallo(false);

                databaseReference.child(idString).setValue(fallaDto);

            }
            finish();
        }

    }