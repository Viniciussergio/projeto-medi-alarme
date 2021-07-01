package com.example.projeto_v1.tela;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.CadastroUsuarioActivity;
import com.example.projeto_v1.tela.cadastro.model.UserDAO;
import com.example.projeto_v1.tela.inicial.TelaInicialActivity;
import com.example.projeto_v1.tela.cadastro.model.User;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.realm.Realm;
import io.realm.RealmResults;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public int userId;
    private Realm mRealm;
    private User mUser;
    private UserDAO userDAO = new UserDAO(null, null, null);
    private ViewHolder mViewHolder = new ViewHolder();
    private String password = "issue365";
    private String currentPasswordCrypt = BCrypt.withDefaults().hashToString(12, password.toCharArray());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.mViewHolder.buttonLogin = findViewById(R.id.button_login);
        this.mViewHolder.buttonSign = findViewById(R.id.button_sign);
        this.mViewHolder.editTextPassword = findViewById(R.id.edit_text_password);
        this.mViewHolder.editTextUserName = findViewById(R.id.edit_text_username);
        this.mViewHolder.textViewForgotPassword = findViewById(R.id.text_view_forgot_password);

        mRealm = Realm.getDefaultInstance();

        this.mViewHolder.textViewForgotPassword.setOnClickListener(this);
        this.mViewHolder.buttonLogin.setOnClickListener(this);
        this.mViewHolder.buttonSign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_login) {
            if (dataValidation()) {
                mUser = mRealm.where(User.class).equalTo("email", mViewHolder.editTextUserName.getText().toString())
                        .findFirst();

                userId = mUser.getId();

                Intent intent = new Intent(this, TelaInicialActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        }else if (v.getId() == R.id.text_view_forgot_password){
            Intent intent = new Intent(this, ForgotPasswordActivity.class);
            startActivity(intent);
        }else if (v.getId() == R.id.button_sign) {
            Intent intent = new Intent(this, CadastroUsuarioActivity.class);
            startActivity(intent);
        }
    }

    private boolean dataValidation() {
        currentPasswordCrypt = BCrypt.withDefaults().hashToString(12, this.mViewHolder.editTextPassword.getText().toString().toCharArray());

        if (mViewHolder.editTextPassword.getText().toString().isEmpty() || mViewHolder.editTextUserName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        RealmResults<User> loginResult = mRealm.where(User.class)
                .equalTo("email", mViewHolder.editTextUserName.getText().toString())
                .findAll();

        if (loginResult.isEmpty()) {
            Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
            return false;
        }

        User usuario = mRealm.where(User.class).equalTo("email", mViewHolder.editTextUserName.getText().toString()).findFirst();
        if(!userDAO.verificarSenha(usuario, mViewHolder.editTextPassword.getText().toString())){
            Toast.makeText(this, "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private static class ViewHolder {
        Button buttonLogin;
        Button buttonSign;
        EditText editTextPassword;
        EditText editTextUserName;
        TextView textViewForgotPassword;
    }
}