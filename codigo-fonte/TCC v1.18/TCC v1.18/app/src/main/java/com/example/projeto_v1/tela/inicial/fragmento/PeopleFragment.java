package com.example.projeto_v1.tela.inicial.fragmento;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_v1.R;
import com.example.projeto_v1.dialog.LogoffAlertaDialog;
import com.example.projeto_v1.dialog.MedicineAlertaDialog;
import com.example.projeto_v1.list.ListAssociadoActivity;
import com.example.projeto_v1.tela.LoginActivity;
import com.example.projeto_v1.tela.cadastro.CadastroAssociadoActivity;
import com.example.projeto_v1.tela.cadastro.CadastroRemedioActivity;
import com.example.projeto_v1.tela.cadastro.detail.UserDetailActivity;
import com.example.projeto_v1.tela.cadastro.model.Associado;
import com.example.projeto_v1.tela.cadastro.model.AssociadoDAO;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.inicial.TelaInicialActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class PeopleFragment extends Fragment implements View.OnClickListener{
    private ViewHolder mViewHolder = new ViewHolder();
    private View v;
    private int idUsuario;
    private TelaInicialActivity telaInicialActivity;
    private Realm mRealm;
    private User mUser;
    private AssociadoDAO associadoDAO;
    private ArrayList<Associado> associados = new ArrayList<Associado>();
    private RealmChangeListener mRealmChangeListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        telaInicialActivity = new TelaInicialActivity();
        idUsuario = telaInicialActivity.getUserId();

        v = inflater.inflate(R.layout.fragment_people, container, false);
        this.mViewHolder.textViewDetailFirstName = v.findViewById(R.id.text_view_detail_first_name);
        this.mViewHolder.textViewDetailLastName = v.findViewById(R.id.text_view_detail_last_name);
        this.mViewHolder.textViewDetailUsername = v.findViewById(R.id.text_view_detail_username);
        this.mViewHolder.textViewDetailEmail = v.findViewById(R.id.text_view_detail_email);
        this.mViewHolder.textViewDetailTelefone = v.findViewById(R.id.text_view_detail_telefone);


        this.mViewHolder.buttonAddAssociado = v.findViewById(R.id.button_insert_associado);
        this.mViewHolder.buttonShowAssociadosList = v.findViewById(R.id.button_show_associados_list);

        this.mViewHolder.buttonAddAssociado.setOnClickListener(this);
        this.mViewHolder.buttonShowAssociadosList.setOnClickListener(this);

        mRealm = Realm.getDefaultInstance();
        mUser = mRealm.where(User.class).equalTo("id", idUsuario).findFirst();

        associadoDAO = new AssociadoDAO(mUser, null, null, mRealm);
        associadoDAO.selectFromDB(mUser);
        associados = associadoDAO.justRefresh();

        setButtonText();
        setTextFields();
        return v;
    }

    @Override
    public void onResume() {
        setTextFields();
        setButtonText();
        super.onResume();
    }

    private void setButtonText() {
        associadoDAO = new AssociadoDAO(mUser, null, null, mRealm);
        associadoDAO.selectFromDB(mUser);
        associados = associadoDAO.justRefresh();
        this.mViewHolder.buttonShowAssociadosList.setText("Seus associados("+associados.size()+")");
    }

    private void setTextFields() {
        mUser = mRealm.where(User.class).equalTo("id", idUsuario).findFirst();

        this.mViewHolder.textViewDetailFirstName.setText(mUser.getFirstName());
        this.mViewHolder.textViewDetailLastName.setText(mUser.getLastName());
        this.mViewHolder.textViewDetailUsername.setText(mUser.getUserName());
        this.mViewHolder.textViewDetailEmail.setText(mUser.getEmail());
        this.mViewHolder.textViewDetailTelefone.setText(mUser.getTelefone());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_insert_associado) {
            Intent intent = new Intent(getContext(), CadastroAssociadoActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.button_show_associados_list) {
            if(mUser.getAssociados().isEmpty()){
                Toast.makeText(getActivity().getApplicationContext(), "Você não têm Associados cadastrados", Toast.LENGTH_SHORT).show();
            }else{
                Intent intent = new Intent(getContext(), ListAssociadoActivity.class);
                startActivity(intent);
            }
        }
    }

    private static class ViewHolder {
        Button buttonShowAssociadosList;
        Button buttonAddAssociado;
        TextView textViewDetailEmail;
        TextView textViewDetailFirstName;
        TextView textViewDetailLastName;
        TextView textViewDetailUsername;
        TextView textViewDetailTelefone;
    }
}