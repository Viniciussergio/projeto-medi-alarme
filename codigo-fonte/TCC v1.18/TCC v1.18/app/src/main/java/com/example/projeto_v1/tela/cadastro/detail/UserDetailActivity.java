package com.example.projeto_v1.tela.cadastro.detail;

import android.content.Intent;
import android.os.Bundle;

import com.example.projeto_v1.watcher.MaskWatcher;
import com.example.projeto_v1.watcher.NumberTextWatcher;
import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.cadastro.model.UserDAO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import io.realm.Realm;
import io.realm.RealmResults;

public class UserDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Realm mRealm;
    private User mUser;
    private UserDAO mUserDAO;
    private ViewHolder mViewHolder = new ViewHolder();
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setTitle("Usuário");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent getIntent = getIntent();
        mRealm = Realm.getDefaultInstance();

        mPosition = getIntent.getIntExtra("numPosition", 0);
        mUser = mRealm.where(User.class).equalTo("id", mPosition).findFirst();

        this.mViewHolder.btnUpdate = findViewById(R.id.button_update_user);
        this.mViewHolder.btnPassword = findViewById(R.id.button_change_password);
        this.mViewHolder.editUpdateFirstName = findViewById(R.id.edit_update_first_name);
        this.mViewHolder.editUpdateLastName = findViewById(R.id.edit_update_last_name);
        this.mViewHolder.editUpdateUsername = findViewById(R.id.edit_update_username);
        this.mViewHolder.editUpdateEmail = findViewById(R.id.edit_update_email);
        this.mViewHolder.editUpdateTelefone = findViewById(R.id.edit_update_telefone);

        this.mViewHolder.btnUpdate.setOnClickListener(this);
        this.mViewHolder.btnPassword.setOnClickListener(this);

        mViewHolder.editUpdateTelefone.addTextChangedListener(new MaskWatcher("+## (##) #####-####"));
        this.mViewHolder.editUpdateEmail.addTextChangedListener(new NumberTextWatcher(this.mViewHolder.editUpdateEmail));

        setTextFields();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_update_user) {
            if(dataValidation()){
                mUserDAO = new UserDAO(getNewUser(), mUser, mRealm);
                mUserDAO.updateUser();
                onBackPressed();
            }
        }
        if (v.getId() == R.id.button_change_password) {
            Intent intent = new Intent(this, ChangePasswordActivity.class);
            intent.putExtra("numPosition", mUser.getId());
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setTextFields() {
        this.mViewHolder.editUpdateFirstName.setText(mUser.getFirstName());
        this.mViewHolder.editUpdateLastName.setText(mUser.getLastName());
        this.mViewHolder.editUpdateUsername.setText(mUser.getUserName());
        this.mViewHolder.editUpdateEmail.setText(mUser.getEmail());
        this.mViewHolder.editUpdateTelefone.setHint(mUser.getTelefone());
    }

    private User getNewUser() {
        User newUser = new User();
        newUser.setFirstName(mViewHolder.editUpdateFirstName.getText().toString());
        newUser.setLastName(mViewHolder.editUpdateLastName.getText().toString());
        newUser.setUserName(mViewHolder.editUpdateUsername.getText().toString());
        newUser.setEmail(mViewHolder.editUpdateEmail.getText().toString());
        newUser.setTelefone(mViewHolder.editUpdateTelefone.getText().toString());
        return newUser;
    }

    private boolean dataValidation() {
        if (mViewHolder.editUpdateEmail.getText().toString().isEmpty() ||
                mViewHolder.editUpdateFirstName.getText().toString().isEmpty() ||
                mViewHolder.editUpdateLastName.getText().toString().isEmpty() ||
                mViewHolder.editUpdateUsername.getText().toString().isEmpty() ||
                mViewHolder.editUpdateTelefone.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isValidEmail(mViewHolder.editUpdateEmail.getText().toString())){
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mViewHolder.editUpdateTelefone.getText().toString().length() != 19){
            Log.v("V", ""+mViewHolder.editUpdateTelefone.getText().toString().length());
            Toast.makeText(this, "Número de Telefone Inválido", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mViewHolder.editUpdateTelefone.getText().toString().charAt(10) != '9'){
            Log.v("V", ""+mViewHolder.editUpdateTelefone.getText().toString().charAt(10));
            Toast.makeText(this, "Número de Telefone Inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        RealmResults<User> loginResult = mRealm.where(User.class).findAll();
        for(User u : loginResult){
            if(u.getEmail().equals(this.mViewHolder.editUpdateEmail.getText().toString())
                    && !this.mViewHolder.editUpdateEmail.getText().toString().equals(u.getEmail())){
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
        super.onBackPressed();
    }

    private static class ViewHolder {
        private TextView editUpdateFirstName;
        private EditText editUpdateLastName;
        private EditText editUpdateUsername;
        private EditText editUpdateEmail;
        private EditText editUpdateTelefone;
        private Button btnUpdate;
        private Button btnPassword;
    }
}