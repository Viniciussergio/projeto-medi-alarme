package com.example.projeto_v1.tela.inicial.fragmento;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.CadastroRemedioActivity;
import com.example.projeto_v1.tela.inicial.TelaInicialActivity;
import com.example.projeto_v1.tela.cadastro.model.MedicineCardView;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.MedicineDAO;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.recycler_view.MedicineRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private RealmChangeListener mRealmChangeListener;
    private Realm realm;
    private MedicineRecyclerAdapter mMedicineRecyclerAdapter;
    private MedicineDAO medicineDAO;
    private TelaInicialActivity telaInicialActivity;
    private ViewHolder mViewHolder = new ViewHolder();
    private User usuario;
    private int idUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        telaInicialActivity = new TelaInicialActivity();
        idUsuario = telaInicialActivity.getUserId();

        realm = Realm.getDefaultInstance();
        usuario = realm.where(User.class).equalTo("id", idUsuario).findFirst();

        medicineDAO = new MedicineDAO(usuario, null, null, realm);
        medicineDAO.selectFromDB(usuario);

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        this.mViewHolder.recyclerView = v.findViewById(R.id.recycler_view);
        this.mViewHolder.FABCreate = v.findViewById(R.id.fab_create_medicine);
        this.mViewHolder.textView = v.findViewById(R.id.text_view_home);

        mViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mViewHolder.FABCreate.setOnClickListener(this);

        mMedicineRecyclerAdapter = new MedicineRecyclerAdapter(getContext(), getArrayList(), medicineDAO.justRefresh(), realm);
        this.mViewHolder.recyclerView.setAdapter(mMedicineRecyclerAdapter);

        refresh();
        return v;
    }

    public void refresh() {
        mRealmChangeListener = o -> {
            mMedicineRecyclerAdapter = new MedicineRecyclerAdapter(getContext(), getArrayList(), medicineDAO.justRefresh(), realm);
            this.mViewHolder.recyclerView.setAdapter(mMedicineRecyclerAdapter);
        };
        realm.addChangeListener(mRealmChangeListener);
    }

    private ArrayList<MedicineCardView> getArrayList(){
        ArrayList<MedicineCardView> medicineCardViews = new ArrayList<>();
        MedicineCardView medicineCardView;
        ArrayList<Medicine> medicinesTest = medicineDAO.justRefresh();
        if(medicinesTest.size() == 0){
            this.mViewHolder.textView.setVisibility(View.VISIBLE);
        }else{
            for(Medicine medicine: medicinesTest){
                medicineCardView = new MedicineCardView();
                medicineCardView.setDataValidade(sdf.format(medicine.getDataValidadeRemedio()));
                if(medicine.getTipoRemedio().equals("Gotas")){
                    medicineCardView.setTitle(medicine.getNomeRemedio()+" - "+medicine.getNumMililitrosRemedio()+" mL");
                }else if(medicine.getTipoRemedio().equals("Comprimido")){
                    medicineCardView.setTitle(medicine.getNomeRemedio()+" - "+medicine.getNumComprimidosRemedio()+" unidades");
                }else{
                    medicineCardView.setTitle(medicine.getNomeRemedio());
                }
                medicineCardView.setDescription(medicine.getTipoRemedio());
                medicineCardViews.add(medicineCardView);
                this.mViewHolder.textView.setVisibility(View.GONE);
            }
        }

        return medicineCardViews;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_create_medicine) {
            Intent intent = new Intent(getContext(), CadastroRemedioActivity.class);
            intent.putExtra("userId", idUsuario);
            startActivity(intent);
        }
    }

    private static class ViewHolder {
        FloatingActionButton FABCreate;
        RecyclerView recyclerView;
        TextView textView;
    }
}