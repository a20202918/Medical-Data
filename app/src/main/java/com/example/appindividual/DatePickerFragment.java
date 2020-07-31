package com.example.appindividual;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    //Clase para ver el frgmento de fechas y elegir
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        AgregarEquipoActivity activity = (AgregarEquipoActivity) getActivity();
        activity.mostrarFecha(year, month, dayOfMonth);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, anio, mes, dia);
    }
}
