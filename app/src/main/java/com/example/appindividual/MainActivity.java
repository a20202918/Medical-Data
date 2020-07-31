package com.example.appindividual;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser =  firebaseAuth.getCurrentUser();

        if(currentUser != null){

            Log.d("infoApp", "Usuario logueado");
            String email = currentUser.getEmail();
            String uid = currentUser.getUid();
            String displayName = currentUser.getDisplayName();
            Log.d("infoApp", "Info:"+email+uid+displayName);

            Intent intent = new Intent(this, MenuActivityAdmin.class);
            int requestCode = 1;
            startActivityForResult(intent,requestCode);


        }else{
            Log.d("infoApp", "Usuario no logueado");
        }

    }

    public void iniciarSesionBtn(View view){

        List<AuthUI.IdpConfig> listaProveedores = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(listaProveedores).build(),1);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser =  firebaseAuth.getCurrentUser();

        if(currentUser != null){

            Intent intent = new Intent(this, MenuActivityAdmin.class);
            int requestCode = 1;
            startActivityForResult(intent,requestCode);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                Log.d("infoApp", "Inicio de sesión exitoso");
            }else{
                Log.d("infoApp", "Inicio de sesión erroneo");
            }
        }

    }

    public void accedeAquiBtn(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        int requestCode = 1;
        startActivityForResult(intent,requestCode);
    }

}