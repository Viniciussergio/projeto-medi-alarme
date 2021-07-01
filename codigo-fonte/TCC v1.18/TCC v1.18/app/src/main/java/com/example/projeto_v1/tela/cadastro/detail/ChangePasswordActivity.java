package com.example.projeto_v1.tela.cadastro.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.cadastro.model.UserDAO;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.realm.Realm;
import io.realm.RealmResults;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private UserDAO mUserDAO;
    private User mUser;
    private ViewHolder mViewHolder = new ViewHolder();
    private Realm mRealm;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent getIntent = getIntent();
        mRealm = Realm.getDefaultInstance();

        mPosition = getIntent.getIntExtra("numPosition", 0);
        mUser = mRealm.where(User.class).equalTo("id", mPosition).findFirst();

        this.mViewHolder.btnTrocarSenha = findViewById(R.id.btn_trocar_senha);
        this.mViewHolder.editTextSenhaAtual = findViewById(R.id.edit_text_senha_atual);
        this.mViewHolder.editTextNovaSenha = findViewById(R.id.edit_text_nova_senha);
        this.mViewHolder.editTextConfirmarNovaSenha = findViewById(R.id.edit_text_confirmar_nova_senha);
        this.mViewHolder.textViewUserEmail = findViewById(R.id.text_view_user_email);

        this.mViewHolder.btnTrocarSenha.setOnClickListener(this);

        editTextCustomSelection();
        setText();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void editTextCustomSelection(){
        this.mViewHolder.editTextNovaSenha.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        this.mViewHolder.editTextConfirmarNovaSenha.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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

    private boolean dataValidation() {
        if (mViewHolder.editTextNovaSenha.getText().toString().isEmpty() || mViewHolder.editTextConfirmarNovaSenha.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!mViewHolder.editTextNovaSenha.getText().toString().equals(mViewHolder.editTextConfirmarNovaSenha.getText().toString())) {
            Toast.makeText(this, "As senhas são diferentes", Toast.LENGTH_SHORT).show();
            return false;
        }

        RealmResults<User> loginResult = mRealm.where(User.class)
                .equalTo("email", mUser.getEmail()).findAll();

        User usuario = mRealm.where(User.class).equalTo("email", mUser.getEmail()).findFirst();
        mUserDAO = new UserDAO(null, null, null);
        if(!mUserDAO.verificarSenha(mUser, this.mViewHolder.editTextSenhaAtual.getText().toString())){
            return false;
        }

        if (loginResult.isEmpty()) {
            Toast.makeText(this, "Senha inválida", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setText(){
        mViewHolder.textViewUserEmail.setText(mUser.getEmail().toString());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_trocar_senha){
            if(dataValidation()) {
                User newUser = new User();
                newUser.setFirstName(mUser.getFirstName());
                newUser.setLastName(mUser.getLastName());
                newUser.setUserName(mUser.getUserName());
                newUser.setEmail(mUser.getEmail());
                newUser.setTelefone(mUser.getTelefone());
                newUser.setPassword(BCrypt.withDefaults().hashToString(12,
                        this.mViewHolder.editTextNovaSenha.getText().toString().toCharArray()));
                mUserDAO = new UserDAO(newUser, mUser, mRealm);
                mUserDAO.updatePassword(newUser.getPassword());
                Toast.makeText(this, "Trocou", Toast.LENGTH_SHORT);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        mViewHolder.editTextNovaSenha.setText("");
        mViewHolder.textViewUserEmail.setText("");
        mViewHolder.editTextConfirmarNovaSenha.setText("");
        mViewHolder.editTextSenhaAtual.setText("");
        super.onBackPressed();
    }

    private class ViewHolder{
        TextView textViewUserEmail;
        EditText editTextSenhaAtual;
        EditText editTextNovaSenha;
        EditText editTextConfirmarNovaSenha;
        Button btnTrocarSenha;
    }
}