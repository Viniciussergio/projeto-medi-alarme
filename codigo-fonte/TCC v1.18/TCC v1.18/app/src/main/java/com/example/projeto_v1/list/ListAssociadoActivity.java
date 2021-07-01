package com.example.projeto_v1.list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.recycler_view.AssociadoRecyclerAdapter;
import com.example.projeto_v1.tela.cadastro.model.Associado;
import com.example.projeto_v1.tela.cadastro.model.AssociadoCardView;
import com.example.projeto_v1.tela.cadastro.model.AssociadoDAO;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.inicial.TelaInicialActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class ListAssociadoActivity extends AppCompatActivity {
    private AssociadoRecyclerAdapter mAssociadoRecyclerAdapter;
    private TelaInicialActivity mTelaInicialActivity;
    private ViewHolder mViewHolder = new ViewHolder();
    private Realm mRealm;
    private User mUsuario;
    private AssociadoDAO mAssociadoDAO;
    private RealmChangeListener mRealmChangeListener;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_associado);
        getSupportActionBar().setTitle("Associados");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTelaInicialActivity = new TelaInicialActivity();
        idUsuario = mTelaInicialActivity.getUserId();

        mRealm = Realm.getDefaultInstance();
        mUsuario = mRealm.where(User.class).equalTo("id", idUsuario).findFirst();

        mAssociadoDAO = new AssociadoDAO(mUsuario, null, null, mRealm);
        mAssociadoDAO.selectFromDB(mUsuario);

        this.mViewHolder.associadoRecyclerView = findViewById(R.id.associado_recycler_view);

        this.mViewHolder.associadoRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAssociadoRecyclerAdapter = new AssociadoRecyclerAdapter(this, getArrayList(), mAssociadoDAO.justRefresh(), mRealm);
        this.mViewHolder.associadoRecyclerView.setAdapter(mAssociadoRecyclerAdapter);

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

    public void refresh() {
        mRealmChangeListener = o -> {
            mAssociadoRecyclerAdapter = new AssociadoRecyclerAdapter(this, getArrayList(), mAssociadoDAO.justRefresh(), mRealm);
            this.mViewHolder.associadoRecyclerView.setAdapter(mAssociadoRecyclerAdapter);
        };
        mRealm.addChangeListener(mRealmChangeListener);
    }

    private ArrayList<AssociadoCardView> getArrayList() {
        ArrayList<AssociadoCardView> associadoCardViews = new ArrayList<>();
        AssociadoCardView associadoCardView;
        ArrayList<Associado> associadosTest = mAssociadoDAO.justRefresh();

        for (Associado associado : associadosTest) {
            associadoCardView = new AssociadoCardView();
            associadoCardView.setAssociadoTitle(associado.getAssociadoFirstName() + " " + associado.getAssociadoLastName());
            associadoCardView.setAssociadoDescription(associado.getAssociadoUserName());
            associadoCardView.setAssociadoDescription1(associado.getAssociadoEmail());
            associadoCardViews.add(associadoCardView);

        }

        return associadoCardViews;
    }

    private class ViewHolder {
        RecyclerView associadoRecyclerView;
    }
}