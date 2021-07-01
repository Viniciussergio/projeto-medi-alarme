package com.example.projeto_v1.recycler_view;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;

public class MedicineViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewTitle;
    public TextView mTextViewDescription;
    public TextView mTextViewDataValidade;
    public Button buttonAddAlarm;
    public Button buttonDeleteMedicine;

    public MedicineViewHolder(@NonNull View itemView) {
        super(itemView);

        this.mTextViewDataValidade = itemView.findViewById(R.id.text_view_data_validade);
        this.mTextViewTitle = itemView.findViewById(R.id.text_view_title);
        this.mTextViewDescription = itemView.findViewById(R.id.text_view_description);
        this.buttonAddAlarm = itemView.findViewById(R.id.button_add_alarm);
        this.buttonDeleteMedicine = itemView.findViewById(R.id.button_delete_medicine);

    }
}
