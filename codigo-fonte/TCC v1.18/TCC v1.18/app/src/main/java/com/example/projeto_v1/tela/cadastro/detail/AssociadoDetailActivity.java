package com.example.projeto_v1.tela.cadastro.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.model.Associado;
import com.example.projeto_v1.tela.cadastro.model.AssociadoDAO;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.inicial.TelaInicialActivity;

import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class AssociadoDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewHolder mViewHolder = new ViewHolder();
    private TelaInicialActivity telaInicialActivity;
    private RealmResults<Associado> mAssociados;
    private Realm mRealm;
    private Associado mAssociado;
    private Associado mNewAssociado;
    private User mUser;
    private AssociadoDAO mAssociadoDAO;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associado_detail);
        getSupportActionBar().setTitle("Senha");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRealm = Realm.getDefaultInstance();
        telaInicialActivity = new TelaInicialActivity();
        idUsuario = telaInicialActivity.getUserId();
        mUser = mRealm.where(User.class).equalTo("id", idUsuario).findFirst();

        Intent getIntent = getIntent();
        int associadoPosition = getIntent.getIntExtra("associadoNumPosition", 0) - 1;

        mAssociados = mRealm.where(Associado.class).findAll();
        mAssociado = mAssociados.get(associadoPosition);

        this.mViewHolder.editUpdateAssociadoFirstName = findViewById(R.id.edit_update_associado_first_name);
        this.mViewHolder.editUpdateAssociadoLastName = findViewById(R.id.edit_update_associado_last_name);
        this.mViewHolder.editUpdateAssociadoUserName = findViewById(R.id.edit_update_associado_username);
        this.mViewHolder.editUpdateAssociadoEmail = findViewById(R.id.edit_update_associado_email);
        this.mViewHolder.buttonAlterarAssociado = findViewById(R.id.button_alterar_associado);

        this.mViewHolder.buttonAlterarAssociado.setOnClickListener(this);

        setTextFields();
    }

    private void setTextFields() {
        this.mViewHolder.editUpdateAssociadoFirstName.setText(mAssociado.getAssociadoFirstName().toString());
        this.mViewHolder.editUpdateAssociadoLastName.setText(mAssociado.getAssociadoLastName().toString());
        this.mViewHolder.editUpdateAssociadoUserName.setText(mAssociado.getAssociadoUserName().toString());
        this.mViewHolder.editUpdateAssociadoEmail.setText(mAssociado.getAssociadoEmail().toString());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_alterar_associado){
            mAssociadoDAO = new AssociadoDAO(null, getNewAssociado(), mAssociado, mRealm);
            mAssociadoDAO.updateAssociado();
            onBackPressed();
        }
    }

    private Associado getNewAssociado() {
        mNewAssociado = new Associado();
        mNewAssociado.setAssociadoFirstName(this.mViewHolder.editUpdateAssociadoFirstName.getText().toString());
        mNewAssociado.setAssociadoLastName(this.mViewHolder.editUpdateAssociadoLastName.getText().toString());
        mNewAssociado.setAssociadoUserName(this.mViewHolder.editUpdateAssociadoUserName.getText().toString());
        mNewAssociado.setAssociadoEmail(this.mViewHolder.editUpdateAssociadoEmail.getText().toString());

        return mNewAssociado;
    }

    private boolean dataValidation() {
        if (mViewHolder.editUpdateAssociadoEmail.getText().toString().isEmpty() ||
                mViewHolder.editUpdateAssociadoFirstName.getText().toString().isEmpty() ||
                mViewHolder.editUpdateAssociadoLastName.getText().toString().isEmpty() ||
                mViewHolder.editUpdateAssociadoUserName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(mViewHolder.editUpdateAssociadoEmail.getText().toString())){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private class ViewHolder{
        EditText editUpdateAssociadoFirstName;
        EditText editUpdateAssociadoLastName;
        EditText editUpdateAssociadoUserName;
        EditText editUpdateAssociadoEmail;
        Button buttonAlterarAssociado;
    }
}