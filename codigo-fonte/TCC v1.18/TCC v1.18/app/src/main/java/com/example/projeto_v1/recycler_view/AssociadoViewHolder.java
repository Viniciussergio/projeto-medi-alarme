package com.example.projeto_v1.recycler_view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;

public class AssociadoViewHolder extends RecyclerView.ViewHolder{
    public TextView mTextViewTitle;
    public TextView mTextViewDescription;
    public TextView mTextViewDescription1;
    public Button buttonUpdateAssociado;
    public Button buttonDeleteAssociado;

    public AssociadoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mTextViewTitle = itemView.findViewById(R.id.text_view_associado_title);
        this.mTextViewDescription = itemView.findViewById(R.id.text_view_associado_description);
        this.mTextViewDescription1 = itemView.findViewById(R.id.text_view_associado_description1);
        this.buttonUpdateAssociado = itemView.findViewById(R.id.button_update_associado);
        this.buttonDeleteAssociado = itemView.findViewById(R.id.button_delete_associado);
    }
}
