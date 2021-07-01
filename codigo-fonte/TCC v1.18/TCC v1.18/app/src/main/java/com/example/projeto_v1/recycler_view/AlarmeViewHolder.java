package com.example.projeto_v1.recycler_view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;

public class AlarmeViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewAlarmeTitle;
    public TextView mTextViewAlarmeDescription;
    public TextView mTextViewAlarmeDescription1;
    public Button buttonDeleteAlarme;

    public AlarmeViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mTextViewAlarmeTitle = itemView.findViewById(R.id.text_view_alarme_title);
        this.mTextViewAlarmeDescription = itemView.findViewById(R.id.text_view_alarme_description);
        this.mTextViewAlarmeDescription1 = itemView.findViewById(R.id.text_view_alarme_description1);
        this.buttonDeleteAlarme = itemView.findViewById(R.id.button_delete_alarme);
    }
}