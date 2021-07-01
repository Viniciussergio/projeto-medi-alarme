package com.example.projeto_v1.tela.cadastro;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.cadastro.model.UserDAO;
import com.example.projeto_v1.watcher.MaskWatcher;
import com.example.projeto_v1.watcher.NumberTextWatcher;

import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class CadastroUsuarioActivity extends AppCompatActivity implements View.OnClickListener {

    private static Realm sRealm;
    private UserDAO mUserDAO;
    private User mUsuario;
    private ViewHolder mViewHolder = new ViewHolder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        getSupportActionBar().setTitle("Usuário");

        sRealm = Realm.getDefaultInstance();

        this.mViewHolder.buttonSave = findViewById(R.id.button_save_user);
        this.mViewHolder.editTextEmail = findViewById(R.id.edit_text_email);
        this.mViewHolder.editTextEmail.addTextChangedListener(new NumberTextWatcher(this.mViewHolder.editTextEmail));
        this.mViewHolder.editTextConfirmEmail = findViewById(R.id.edit_text_confirm_email);
        this.mViewHolder.editTextFirstName = findViewById(R.id.edit_text_first_name);
        this.mViewHolder.editTextLastName = findViewById(R.id.edit_text_last_name);
        this.mViewHolder.editTextPassword = findViewById(R.id.edit_text_password);
        this.mViewHolder.editTextConfirmPassword = findViewById(R.id.edit_text_confirm_password);
        this.mViewHolder.editTextUsername = findViewById(R.id.edit_text_username);
        this.mViewHolder.editTextPhone = findViewById(R.id.edit_text_user_telefone);

        mViewHolder.editTextPhone.addTextChangedListener(new MaskWatcher("+## (##) #####-####"));

        this.mViewHolder.buttonSave.setOnClickListener(this);
        editTextCustomSelection();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_save_user) {
            if (dataValidation()) {
                if(emailExists()){
                    Intent intent = new Intent(this, VerificarEmailActivity.class);
                    intent.putExtra("userFirstName", mViewHolder.editTextFirstName.getText().toString());
                    intent.putExtra("userLastName", mViewHolder.editTextLastName.getText().toString());
                    intent.putExtra("userNickname", mViewHolder.editTextUsername.getText().toString());
                    intent.putExtra("userTelefone", mViewHolder.editTextPhone.getText().toString());
                    intent.putExtra("userPassword", mViewHolder.editTextPassword.getText().toString());
                    intent.putExtra("userEmail", mViewHolder.editTextEmail.getText().toString());
                    startActivity(intent);
                }
            }
        }
    }

    private void editTextCustomSelection(){
        this.mViewHolder.editTextEmail.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        this.mViewHolder.editTextConfirmEmail.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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
        this.mViewHolder.editTextPassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) { }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        this.mViewHolder.editTextConfirmPassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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

    private User setUser() {
        User user = new User();
        user.setFirstName(mViewHolder.editTextFirstName.getText().toString());
        user.setLastName(mViewHolder.editTextLastName.getText().toString());
        user.setUserName(mViewHolder.editTextUsername.getText().toString());
        user.setPassword(mViewHolder.editTextPassword.getText().toString());
        user.setEmail(mViewHolder.editTextEmail.getText().toString());
        user.setTelefone(mViewHolder.editTextPhone.getText().toString());
        return user;
    }

    private boolean dataValidation() {
        if (mViewHolder.editTextEmail.getText().toString().isEmpty() ||
                mViewHolder.editTextConfirmEmail.getText().toString().isEmpty() ||
                mViewHolder.editTextFirstName.getText().toString().isEmpty() ||
                mViewHolder.editTextLastName.getText().toString().isEmpty() ||
                mViewHolder.editTextPassword.getText().toString().isEmpty() ||
                mViewHolder.editTextConfirmPassword.getText().toString().isEmpty() ||
                mViewHolder.editTextUsername.getText().toString().isEmpty() ||
                mViewHolder.editTextPhone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!mViewHolder.editTextEmail.getText().toString()
                .equals(mViewHolder.editTextConfirmEmail.getText().toString())) {
            Toast.makeText(this, "Os emails são diferentes", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!mViewHolder.editTextPassword.getText().toString()
                .equals(mViewHolder.editTextConfirmPassword.getText().toString())) {
            Toast.makeText(this, "As senhas são diferentes", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(mViewHolder.editTextEmail.getText().toString())){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mViewHolder.editTextPhone.getText().toString().length() != 19){
            Log.v("V", ""+mViewHolder.editTextPhone.getText().toString().length());
            Toast.makeText(this, "Número de Telefone Inválido", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mViewHolder.editTextPhone.getText().toString().charAt(9) != '9'){
            Log.v("V", ""+mViewHolder.editTextPhone.getText().toString().charAt(10));
            Toast.makeText(this, "Número de Telefone Inválido", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean emailExists() {
        RealmResults<User> loginResult = sRealm.where(User.class).findAll();
        for(User u : loginResult){
            if(u.getEmail().equals(this.mViewHolder.editTextEmail.getText().toString())){
                Toast.makeText(this, "E-mail já cadastrado", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onBackPressed() {
        mViewHolder.editTextEmail.setText("");
        mViewHolder.editTextConfirmEmail.setText("");
        mViewHolder.editTextFirstName.setText("");
        mViewHolder.editTextLastName.setText("");
        mViewHolder.editTextUsername.setText("");
        mViewHolder.editTextPassword.setText("");
        mViewHolder.editTextConfirmPassword.setText("");
        mViewHolder.editTextPhone.setText("");
        super.onBackPressed();
    }

    private static class ViewHolder {
        Button buttonSave;
        EditText editTextEmail;
        EditText editTextConfirmEmail;
        EditText editTextFirstName;
        EditText editTextLastName;
        EditText editTextPassword;
        EditText editTextConfirmPassword;
        EditText editTextUsername;
        EditText editTextPhone;
    }
}