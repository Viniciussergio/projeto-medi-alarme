package com.example.projeto_v1.recycler_view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;

public class HistoricoViewHolder extends RecyclerView.ViewHolder{
    public TextView mTextViewTitle;
    public TextView mTextViewDescription;
    public TextView mTextViewModoUsoRemedio;
    public TextView mTextViewStatusHistorico;

    public HistoricoViewHolder(@NonNull View itemView) {
        super(itemView);
        this.mTextViewTitle = itemView.findViewById(R.id.text_view_historico_nome_remedio);
        this.mTextViewDescription = itemView.findViewById(R.id.text_view_historico_horario);
        this.mTextViewStatusHistorico = itemView.findViewById(R.id.text_view_historico_status);
        this.mTextViewModoUsoRemedio = itemView.findViewById(R.id.text_view_historico_modo_uso_remedio);
    }
}
