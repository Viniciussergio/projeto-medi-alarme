package com.example.projeto_v1.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.dialog.MedicineAlertaDialog;
import com.example.projeto_v1.tela.cadastro.detail.AssociadoDetailActivity;
import com.example.projeto_v1.tela.cadastro.model.Associado;
import com.example.projeto_v1.tela.cadastro.model.AssociadoCardView;
import com.example.projeto_v1.tela.cadastro.model.AssociadoDAO;

import java.util.ArrayList;

import io.realm.Realm;

public class AssociadoRecyclerAdapter extends RecyclerView.Adapter<AssociadoViewHolder> implements View.OnClickListener, MedicineAlertaDialog.AlertaDialogListener {
    private Context mContext;
    private ArrayList<AssociadoCardView> mAssociadoCardViews;
    private AssociadoCardView mAssociadoCardData;
    private ArrayList<Associado> mAssociados;
    private Associado mAssociadoData;
    private Realm mRealm;

    public AssociadoRecyclerAdapter(Context mContext, ArrayList<AssociadoCardView> mAssociadoCardViews, ArrayList<Associado> mAssociados, Realm mRealm) {
        this.mContext = mContext;
        this.mAssociadoCardViews = mAssociadoCardViews;
        this.mAssociados = mAssociados;
        this.mRealm = mRealm;
    }

    @NonNull
    @Override
    public AssociadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_associado, null);
        return new AssociadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssociadoViewHolder holder, int position) {
        final AssociadoCardView CARD_DATA = mAssociadoCardViews.get(position);
        mAssociadoData = mAssociados.get(position);
        holder.mTextViewTitle.setText(mAssociadoCardViews.get(position).getAssociadoTitle());
        holder.mTextViewDescription.setText(mAssociadoCardViews.get(position).getAssociadoDescription());
        holder.mTextViewDescription1.setText(mAssociadoCardViews.get(position).getAssociadoDescription1());

        holder.buttonUpdateAssociado.setOnClickListener(v -> {
            mAssociadoData = mAssociados.get(position);
            Intent intent = new Intent(mContext, AssociadoDetailActivity.class);
            intent.putExtra("associadoNumPosition", mAssociadoData.getIdAssociado());
            mContext.startActivity(intent);
        });

        holder.buttonDeleteAssociado.setOnClickListener(v -> {
            setCardData(CARD_DATA);
            mAssociadoData = mAssociados.get(position);
            showAlertaDialog();
        });
    }

    @Override
    public int getItemCount() {
        return mAssociadoCardViews.size();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        removerItem(mAssociadoCardData);
        AssociadoDAO associadoDAO = new AssociadoDAO(null, null, mAssociadoData, mRealm);
        associadoDAO.removeAssociado();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Não há código aqui porque o Dialog fechará sozinho
    }

    private void removerItem(AssociadoCardView cardDAta) {
        int posicaoAtual = mAssociadoCardViews.indexOf(cardDAta);
        mAssociadoCardViews.remove(posicaoAtual);
        notifyItemRemoved(posicaoAtual);
    }

    private void showAlertaDialog() {
        MedicineAlertaDialog dialog = new MedicineAlertaDialog();
        dialog.setListener(this);
        dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "AlertaDialog");
    }

    private void setCardData(AssociadoCardView cardData){
        this.mAssociadoCardData = cardData;
    }
}
