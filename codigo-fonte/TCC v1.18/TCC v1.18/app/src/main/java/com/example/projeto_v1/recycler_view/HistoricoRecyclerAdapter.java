package com.example.projeto_v1.recycler_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.model.Historico;
import com.example.projeto_v1.tela.cadastro.model.HistoricoCardView;

import java.util.ArrayList;

import io.realm.Realm;

public class HistoricoRecyclerAdapter extends RecyclerView.Adapter<HistoricoViewHolder>{
    private Context mContext;
    private ArrayList<HistoricoCardView> mHistoricoCardViews;
    private HistoricoCardView mCardData;
    private ArrayList<Historico> mHistoricos;
    private Historico mHistoricoData;
    private Realm mRealm;

    public HistoricoRecyclerAdapter(Context mContext, ArrayList<HistoricoCardView> mHistoricoCardViews, ArrayList<Historico> mHistoricos, Realm mRealm) {
        this.mContext = mContext;
        this.mHistoricoCardViews = mHistoricoCardViews;
        this.mHistoricos = mHistoricos;
        this.mRealm = mRealm;
    }

    @NonNull
    @Override
    public HistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_historico, null);
        return new HistoricoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricoViewHolder holder, int position) {
        final HistoricoCardView CARD_DATA = mHistoricoCardViews.get(position);
        mHistoricoData = mHistoricos.get(position);

        holder.mTextViewTitle.setText(mHistoricoCardViews.get(position).getTitle());
        holder.mTextViewDescription.setText(mHistoricoCardViews.get(position).getDescription());
        holder.mTextViewStatusHistorico.setText(mHistoricoCardViews.get(position).getStatusHistorico());
        holder.mTextViewModoUsoRemedio.setText(mHistoricoCardViews.get(position).getModoUsoRemedio());
    }

    @Override
    public int getItemCount() {
        return mHistoricoCardViews.size();
    }

    private void setmCardData(HistoricoCardView mCardData) {
        this.mCardData = mCardData;
    }
}
