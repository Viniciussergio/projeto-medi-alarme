package com.example.projeto_v1.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.projeto_v1.R;

public class UserAlertaDialog extends DialogFragment {
    private UserAlertaDialog.AlertaDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder.setMessage("Você deseja mesmo excluir sua conta?")
                .setPositiveButton(R.string.dialog_confirm_action, (dialog, which) -> listener.onUserDialogPositiveClick(UserAlertaDialog.this))
                .setNegativeButton(R.string.dialog_cancel_action, (dialog, which) -> listener.onUserDialogNegativeClick(UserAlertaDialog.this));

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
        void onUserDialogPositiveClick(DialogFragment dialog);
        void onUserDialogNegativeClick(DialogFragment dialog);
    }

    public void setListener(UserAlertaDialog.AlertaDialogListener alertaDialogListener){
        this.listener = alertaDialogListener;
    }
}
