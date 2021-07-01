package com.example.projeto_v1.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.dialog.MedicineAlertaDialog;
import com.example.projeto_v1.list.ListAlarmesActivity;
import com.example.projeto_v1.tela.cadastro.model.AlarmeDAO;
import com.example.projeto_v1.tela.cadastro.model.MedicineCardView;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.MedicineDAO;

import java.util.ArrayList;

import io.realm.Realm;

public class MedicineRecyclerAdapter extends RecyclerView.Adapter<MedicineViewHolder> implements MedicineAlertaDialog.AlertaDialogListener {
    private Context mContext;
    private ArrayList<MedicineCardView> mMedicineCardViews;
    private MedicineCardView cardData;
    private ArrayList<Medicine> mMedicines;
    private Medicine mMedicineData;
    private Realm mRealm;

    public MedicineRecyclerAdapter(Context mContext, ArrayList<MedicineCardView> mMedicineCardViews, ArrayList<Medicine> mMedicines, Realm mRealm) {
        this.mContext = mContext;
        this.mMedicineCardViews = mMedicineCardViews;
        this.mMedicines = mMedicines;
        this.mRealm = mRealm;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_medicine, null);
        return new MedicineViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        mMedicineData = mMedicines.get(position);
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mMedicineCardViews.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        final MedicineCardView CARD_DATA = mMedicineCardViews.get(position);
        mMedicineData = mMedicines.get(position);
        AlarmeDAO alarmeDAO = new AlarmeDAO(mMedicineData, null, mRealm);
        alarmeDAO.selectFromDB(mMedicineData);
        holder.mTextViewTitle.setText(mMedicineCardViews.get(position).getTitle());
        holder.mTextViewDescription.setText(mMedicineCardViews.get(position).getDescription());
        holder.mTextViewDataValidade.setText(mMedicineCardViews.get(position).getDataValidade());
        holder.buttonAddAlarm.setText("Alarmes(" + alarmeDAO.justRefresh().size() + ")");
        int medicineId = mMedicineData.getIdRemedio();

        holder.buttonDeleteMedicine.setOnClickListener(v -> {
            setCardData(CARD_DATA);
            mMedicineData = mMedicines.get(position);
            showAlertaDialog();
        });
        holder.buttonAddAlarm.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ListAlarmesActivity.class);
            intent.putExtra("medicineId", medicineId);
            mContext.startActivity(intent);
        });
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        removerItem(cardData);
        MedicineDAO medicineDAO = new MedicineDAO(null, null, mMedicineData, mRealm);
        medicineDAO.removeMedicine(mContext);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        //Não há código aqui porque o Dialog fechará sozinho
    }

    private void showAlertaDialog() {
        MedicineAlertaDialog dialog = new MedicineAlertaDialog();
        dialog.setListener(this);
        dialog.show(((AppCompatActivity) mContext).getSupportFragmentManager(), "AlertaDialog");
    }

    private void removerItem(MedicineCardView cardDAta) {
        int posicaoAtual = mMedicineCardViews.indexOf(cardDAta);
        mMedicineCardViews.remove(posicaoAtual);
        notifyItemRemoved(posicaoAtual);
    }


    private void setCardData(MedicineCardView cardData) {
        this.cardData = cardData;
    }
}
