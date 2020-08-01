package com.example.appindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appindividual.entidades.EquipoDto;
import com.example.appindividual.entidades.FallaDto;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Text;

import javax.mail.MessagingException;

public class ReportarFallosActivity extends AppCompatActivity {

    String id;
    String nombre;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar_fallos);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        Log.d("infoApp", id);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("pruebas").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    EquipoDto equipoDto = dataSnapshot.getValue(EquipoDto.class);
                    nombre = equipoDto.getNombre();
                    TextView textViewEquipo = findViewById(R.id.textViewEquipo);
                    textViewEquipo.setText("Equipo seleccionado: "+nombre);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void enviarFallo(View view) throws MessagingException {

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //Escribir y guardar comentario sobre la falla
        EditText editTextComentarioFallo = findViewById(R.id.editTextComentarioFallo);
        String comentario = editTextComentarioFallo.getText().toString();

        //Enviar correo al administrador cuando hay falla
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"a20202918@pucp.edu.pe"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Reporte de fallas MEDICAL-CARE");
        i.putExtra(Intent.EXTRA_TEXT   , "Fallas en el equipo: "+id+"\nComentario: "+comentario);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ReportarFallosActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

        //Cambiar el estado de la falla
        FallaDto fallaDto = new FallaDto();
        fallaDto.setFallo(true);
        fallaDto.setComentario(comentario);

        databaseReference.child(id).setValue(fallaDto).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast toast = Toast.makeText(getApplicationContext(),"Redireccionando...",Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast toast = Toast.makeText(getApplicationContext(),"No enviado",Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        finish();

    }

}