package com.example.projeto_v1.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.dialog.MedicineAlertaDialog;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.AlarmeCardView;
import com.example.projeto_v1.tela.cadastro.model.AlarmeDAO;

import java.util.ArrayList;

import io.realm.Realm;

public class AlarmeRecyclerAdapter extends RecyclerView.Adapter<AlarmeViewHolder> implements View.OnClickListener, MedicineAlertaDialog.AlertaDialogListener {
    private Context mContext;
    private ArrayList<AlarmeCardView> mAlarmeCardViews;
    private AlarmeCardView mAlarmeCardData;
    private ArrayList<Alarme> mAlarmes;
    private Alarme mAlarmeData;
    private Realm mRealm;

    public AlarmeRecyclerAdapter(Context mContext, ArrayList<AlarmeCardView> mAlarmeCardViews, ArrayList<Alarme> mAlarmes, Realm mRealm) {
        this.mContext = mContext;
        this.mAlarmeCardViews = mAlarmeCardViews;
        this.mAlarmes = mAlarmes;
        this.mRealm = mRealm;
    }

    @NonNull
    @Override
    public AlarmeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_alarme, null);
        return new AlarmeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmeViewHolder holder, int position) {
        holder.mTextViewAlarmeTitle.setText(mAlarmeCardViews.get(position).getAlarmeTitle());
        holder.mTextViewAlarmeDescription.setText(mAlarmeCardViews.get(position).getAlarmeDescription());
        holder.mTextViewAlarmeDescription1.setText(mAlarmeCardViews.get(position).getAlarmeDescription1());

        final AlarmeCardView CARD_DATA = mAlarmeCardViews.get(position);
        mAlarmeData = mAlarmes.get(position);

        holder.buttonDeleteAlarme.setOnClickListener(v -> {
            setCardData(CARD_DATA);
            mAlarmeData = mAlarmes.get(position);
            showAlertaDialog();
        });
    }

    @Override
    public int getItemCount() {
        return mAlarmeCardViews.size();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        removerItem(mAlarmeCardData);
        AlarmeDAO alarmeDAO = new AlarmeDAO(null, mAlarmeData, mRealm);
        alarmeDAO.removeNotifications(mAlarmeData, mContext);
        alarmeDAO.removeAlarm();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Não há código aqui porque o Dialog fechará sozinho
    }

    private void removerItem(AlarmeCardView cardDAta) {
        int posicaoAtual = mAlarmeCardViews.indexOf(cardDAta);
        mAlarmeCardViews.remove(posicaoAtual);
        notifyItemRemoved(posicaoAtual);
    }

    private void showAlertaDialog() {
        MedicineAlertaDialog dialog = new MedicineAlertaDialog();
        dialog.setListener(this);
        dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "AlertaDialog");
    }

    private void setCardData(AlarmeCardView cardData) {
        this.mAlarmeCardData = cardData;
    }
}
