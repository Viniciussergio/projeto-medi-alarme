package com.example.projeto_v1.tela.cadastro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.ActionMode;
import android.view.Menu;
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

public class CadastroAssociadoActivity extends AppCompatActivity implements View.OnClickListener{

    private final ViewHolder mViewHolder = new ViewHolder();
    private Realm mRealm;
    private int idUsuario;
    private TelaInicialActivity telaInicialActivity;
    private AssociadoDAO mAssociadoDAO;
    private Associado mAssociado;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_associado);
        getSupportActionBar().setTitle("Associado");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRealm = Realm.getDefaultInstance();
        telaInicialActivity = new TelaInicialActivity();
        idUsuario = telaInicialActivity.getUserId();
        mUser = mRealm.where(User.class).equalTo("id", idUsuario).findFirst();

        this.mViewHolder.editTextAssociadoFirstName = findViewById(R.id.edit_text_associado_first_name);
        this.mViewHolder.editTextAssociadoLastName = findViewById(R.id.edit_text_associado_last_name);
        this.mViewHolder.editTextAssociadoUserName = findViewById(R.id.edit_text_associado_username);
        this.mViewHolder.editTextAssociadoEmail = findViewById(R.id.edit_text_associado_email);
        this.mViewHolder.editTextAssociadoConfirmEmail = findViewById(R.id.edit_text_associado_confirm_email);

        this.mViewHolder.buttonSaveAssociado = findViewById(R.id.button_save_associado);

        this.mViewHolder.buttonSaveAssociado.setOnClickListener(this);
        editTextCustomSelection();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_save_associado) {
            if (dataValidation()) {
                mAssociadoDAO = new AssociadoDAO(mUser, null, getAssociado(), mRealm);
                mAssociadoDAO.createAssociado();
                finish();
            }
        }
    }

    private void editTextCustomSelection(){
        this.mViewHolder.editTextAssociadoEmail.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        this.mViewHolder.editTextAssociadoConfirmEmail.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean dataValidation() {
        if (mViewHolder.editTextAssociadoEmail.getText().toString().isEmpty() ||
                mViewHolder.editTextAssociadoFirstName.getText().toString().isEmpty() ||
                mViewHolder.editTextAssociadoLastName.getText().toString().isEmpty() ||
                mViewHolder.editTextAssociadoUserName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!mViewHolder.editTextAssociadoEmail.getText().toString()
                .equals(mViewHolder.editTextAssociadoConfirmEmail.getText().toString())) {
            Toast.makeText(this, "Os emails são diferentes", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(mViewHolder.editTextAssociadoEmail.getText().toString())){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private Associado getAssociado() {
        mAssociado = new Associado();

        Number maxId = mRealm.where(Associado.class).max("idAssociado");
        int newKey = (maxId == null) ? 1 : maxId.intValue() + 1;

        mAssociado.setIdAssociado(newKey);

        mAssociado.setAssociadoFirstName(this.mViewHolder.editTextAssociadoFirstName.getText().toString());
        mAssociado.setAssociadoLastName(this.mViewHolder.editTextAssociadoLastName.getText().toString());
        mAssociado.setAssociadoUserName(this.mViewHolder.editTextAssociadoUserName.getText().toString());
        mAssociado.setAssociadoEmail(this.mViewHolder.editTextAssociadoEmail.getText().toString());

        return mAssociado;
    }

    private class ViewHolder{
        Button buttonSaveAssociado;
        EditText editTextAssociadoFirstName;
        EditText editTextAssociadoLastName;
        EditText editTextAssociadoUserName;
        EditText editTextAssociadoEmail;
        EditText editTextAssociadoConfirmEmail;
    }
}