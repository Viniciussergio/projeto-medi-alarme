package com.example.projeto_v1.tela.inicial.fragmento;

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
import com.example.projeto_v1.recycler_view.HistoricoRecyclerAdapter;
import com.example.projeto_v1.recycler_view.MedicineRecyclerAdapter;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.Historico;
import com.example.projeto_v1.tela.cadastro.model.HistoricoCardView;
import com.example.projeto_v1.tela.cadastro.model.HistoricoDAO;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.MedicineCardView;
import com.example.projeto_v1.tela.cadastro.model.MedicineDAO;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.inicial.TelaInicialActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class HistoryFragment extends Fragment {
    private TelaInicialActivity telaInicialActivity;
    private ViewHolder mViewHolder = new ViewHolder();
    private HistoricoRecyclerAdapter mHistoricoRecyclerAdapter;
    private HistoricoDAO historicoDAO;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private Realm realm;
    private Medicine medicine;
    private User usuario;
    private Alarme alarme;
    private int idUsuario;

    private RealmChangeListener mRealmChangeListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        telaInicialActivity = new TelaInicialActivity();
        idUsuario = telaInicialActivity.getUserId();

        realm = Realm.getDefaultInstance();
        usuario = realm.where(User.class).equalTo("id", idUsuario).findFirst();

        historicoDAO = new HistoricoDAO(null, usuario, realm);
        historicoDAO.selectFromDB(usuario);

        View v = inflater.inflate(R.layout.fragment_history, container, false);
        this.mViewHolder.recyclerView = v.findViewById(R.id.recycler_view_historico);
        this.mViewHolder.textView = v.findViewById(R.id.text_view_historico);

        mViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mHistoricoRecyclerAdapter = new HistoricoRecyclerAdapter(getContext(), getArrayList(), historicoDAO.justRefresh(), realm);
        this.mViewHolder.recyclerView.setAdapter(mHistoricoRecyclerAdapter);

        refresh();
        return v;
    }

    public void refresh() {
        mRealmChangeListener = o -> {
            mHistoricoRecyclerAdapter = new HistoricoRecyclerAdapter(getContext(), getArrayList(), historicoDAO.justRefresh(), realm);
            this.mViewHolder.recyclerView.setAdapter(mHistoricoRecyclerAdapter);
        };
        realm.addChangeListener(mRealmChangeListener);
    }

    private ArrayList<HistoricoCardView> getArrayList(){
        ArrayList<HistoricoCardView> historicoCardViews = new ArrayList<>();
        HistoricoCardView historicoCardView;
        ArrayList<Historico> historicoTest = historicoDAO.justRefresh();
        if(historicoTest.size() == 0){
            this.mViewHolder.textView.setVisibility(View.VISIBLE);
        }else{
            for(Historico historico: historicoTest){
                historicoCardView = new HistoricoCardView();
                medicine = realm.where(Medicine.class).equalTo("idRemedio", historico.getIdRemedioHistorico()).findFirst();
                alarme = realm.where(Alarme.class).equalTo("idAlarme", historico.getIdAlarmeHistorico()).findFirst();

                if(medicine.getTipoRemedio().equals("Gotas")){
                    historicoCardView.setTitle(medicine.getNomeRemedio()+" - "+medicine.getTipoRemedio());
                    historicoCardView.setModoUsoRemedio("Modo de Uso: "+alarme.getGotasDose()+" gotas por dose");
                }else if(medicine.getTipoRemedio().equals("Comprimido")){
                    historicoCardView.setTitle(medicine.getNomeRemedio()+" - "+medicine.getTipoRemedio());
                    historicoCardView.setModoUsoRemedio("Modo de Uso: "+alarme.getComprimidosDose()+" comprimidos por dose");
                }else{
                    historicoCardView.setTitle(medicine.getNomeRemedio());
                    historicoCardView.setModoUsoRemedio("Modo de Uso: "+alarme.getModoUsoRemedio());
                }
                historicoCardView.setDescription(historico.getDiaHistorico()+" - "+alarme.getHorario());
                historicoCardView.setStatusHistorico(historico.getStatusHistorico());
                historicoCardViews.add(historicoCardView);
                this.mViewHolder.textView.setVisibility(View.GONE);
            }
        }
        return historicoCardViews;
    }

    private static class ViewHolder {
        RecyclerView recyclerView;
        TextView textView;
    }
}