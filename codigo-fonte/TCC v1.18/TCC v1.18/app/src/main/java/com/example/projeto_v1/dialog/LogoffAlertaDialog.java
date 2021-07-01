package com.example.projeto_v1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.projeto_v1.R;

public class LogoffAlertaDialog extends DialogFragment{
    private LogoffAlertaDialog.AlertaDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Deseja mesmo desconectar?\n (Lembre-se de excluir os alarmes no Relógio de seu celular)")
                .setPositiveButton(R.string.dialog_confirm_action, (dialog, which) -> listener.onLogOffDialogPositiveClick(LogoffAlertaDialog.this))
                .setNegativeButton(R.string.dialog_cancel_action, (dialog, which) -> listener.onLogOffDialogNegativeClick(LogoffAlertaDialog.this));

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
        void onLogOffDialogPositiveClick(DialogFragment dialog);
        void onLogOffDialogNegativeClick(DialogFragment dialog);
    }

    public void setListener(LogoffAlertaDialog.AlertaDialogListener alertaDialogListener){
        this.listener = alertaDialogListener;
    }
}
