package com.example.projeto_v1.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.projeto_v1.R;
import com.example.projeto_v1.recycler_view.AlarmeRecyclerAdapter;
import com.example.projeto_v1.tela.cadastro.CadastroAlarmeActivity;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.AlarmeCardView;
import com.example.projeto_v1.tela.cadastro.model.AlarmeDAO;
import com.example.projeto_v1.tela.cadastro.model.Medicine;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class ListAlarmesActivity extends AppCompatActivity implements View.OnClickListener {
    private RealmChangeListener mRealmChangeListener;
    private Realm mRealm;
    private Medicine mRemedio;
    private ViewHolder mViewHolder = new ViewHolder();
    private AlarmeDAO mAlarmeDAO;
    private AlarmeRecyclerAdapter mAlarmeRecyclerAdapter;
    private int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_alarmes);
        getSupportActionBar().setTitle("Alarmes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent getIntent = getIntent();
        medicineId = getIntent.getIntExtra("medicineId", 0);
        mRealm = Realm.getDefaultInstance();
        mRemedio = mRealm.where(Medicine.class).equalTo("idRemedio", medicineId).findFirst();

        mAlarmeDAO = new AlarmeDAO(mRemedio, null, mRealm);
        mAlarmeDAO.selectFromDB(mRemedio);

        this.mViewHolder.buttonAddAlarme = findViewById(R.id.button_add_alarm);
        this.mViewHolder.recyclerViewAlarmes = findViewById(R.id.recycler_view_alarmes);

        this.mViewHolder.buttonAddAlarme.setOnClickListener(this);
        this.mViewHolder.recyclerViewAlarmes.setLayoutManager(new LinearLayoutManager(this));

        mAlarmeRecyclerAdapter = new AlarmeRecyclerAdapter(this, getArrayList(), mAlarmeDAO.justRefresh(), mRealm);
        this.mViewHolder.recyclerViewAlarmes.setAdapter(mAlarmeRecyclerAdapter);

        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refresh() {
        mRealmChangeListener = o -> {
            mAlarmeRecyclerAdapter = new AlarmeRecyclerAdapter(this, getArrayList(), mAlarmeDAO.justRefresh(), mRealm);
            this.mViewHolder.recyclerViewAlarmes.setAdapter(mAlarmeRecyclerAdapter);
        };
        mRealm.addChangeListener(mRealmChangeListener);
    }

    private ArrayList<AlarmeCardView> getArrayList() {
        ArrayList<AlarmeCardView> alarmeCardViews = new ArrayList<>();
        AlarmeCardView alarmeCardView;

        mAlarmeDAO.selectFromDB(mRemedio);
        ArrayList<Alarme> alarmesTest = mAlarmeDAO.justRefresh();

        for (Alarme alarme : alarmesTest) {
            alarmeCardView = new AlarmeCardView();
            alarmeCardView.setAlarmeTitle(alarme.getHorario());
            alarmeCardView.setAlarmeDescription("Alarme " + alarme.getTipo());
            if (alarme.getTipo().equals("Personalizado")) {
                String stringDiasSemana = "";
                RealmList<String> alarmeDiasSemana = new RealmList<>();
                alarmeDiasSemana = alarme.getDiasDaSemana();

                for (int i = 0; i < alarmeDiasSemana.size(); i++) {
                    if (i != 0) {
                        stringDiasSemana = stringDiasSemana + ", " + alarmeDiasSemana.get(i);
                    } else {
                        stringDiasSemana = stringDiasSemana + alarmeDiasSemana.get(i);
                    }
                }
                alarmeCardView.setAlarmeDescription1(stringDiasSemana);
            }
            if (alarme.getTipo().equals("Diário")) {
                alarmeCardView.setAlarmeDescription1("Diário");
            }
            alarmeCardViews.add(alarmeCardView);
        }

        return alarmeCardViews;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_add_alarm) {
            Intent intent = new Intent(this, CadastroAlarmeActivity.class);
            intent.putExtra("medicineId", medicineId);
            startActivity(intent);
        }
    }

    private class ViewHolder {
        Button buttonAddAlarme;
        RecyclerView recyclerViewAlarmes;
    }
}