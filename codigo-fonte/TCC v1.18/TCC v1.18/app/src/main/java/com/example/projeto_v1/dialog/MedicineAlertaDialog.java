package com.example.projeto_v1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.projeto_v1.R;

public class MedicineAlertaDialog extends DialogFragment {
    private AlertaDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_message)
        .setPositiveButton(R.string.dialog_confirm_action, (dialog, which) -> listener.onDialogPositiveClick(MedicineAlertaDialog.this))
        .setNegativeButton(R.string.dialog_cancel_action, (dialog, which) -> listener.onDialogNegativeClick(MedicineAlertaDialog.this));

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
           setListener(listener);
        } catch (ClassCastException e) {
            throw new ClassCastException("AlertaDialogListener não foi implementado");
        }
    }

    public interface AlertaDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public void setListener(AlertaDialogListener alertaDialogListener){
        this.listener = alertaDialogListener;
    }
}

