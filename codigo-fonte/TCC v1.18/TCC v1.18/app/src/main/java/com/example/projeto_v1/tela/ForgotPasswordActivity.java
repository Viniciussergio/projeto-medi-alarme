package com.example.projeto_v1.tela;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.cadastro.model.UserDAO;

import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.realm.Realm;
import io.realm.RealmResults;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewHolder mViewHolder = new ViewHolder();
    private Realm mRealm;
    private String novaSenha;
    private UserDAO mUserDAO;
    private String sEmail;
    private String sPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mRealm = Realm.getDefaultInstance();

        mViewHolder.editTextEsqueciSenhaEmail = findViewById(R.id.edit_text_esqueci_senha_email);
        mViewHolder.btnNovaSenha = findViewById(R.id.btn_nova_senha);

        mViewHolder.btnNovaSenha.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_nova_senha){
            if(emailExists()){
                criarNovaSenha();
                onBackPressed();
            }
        }
    }

    private void configurarEmail(String novaSenha){
        sEmail = "medialarme2021@gmail.com";
        sPassword = "projeto2021";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sEmail, sPassword);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sEmail) );
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(this.mViewHolder.editTextEsqueciSenhaEmail.getText().toString()));
            message.setSubject("Medialarme - Nova Senha");
            message.setText("Sua nova senha é: "+novaSenha);

            new SendEmail().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private class SendEmail extends AsyncTask<Message, String, String> {
        @Override
        protected String doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {
                e.printStackTrace();
                return "Error";
            }
        }
    }

    private void criarNovaSenha(){
        User user = mRealm.where(User.class).equalTo("email", mViewHolder.editTextEsqueciSenhaEmail.getText().toString()).findFirst();

        Random aleatorio = new Random();
        novaSenha = ""+aleatorio.nextInt(10);

        while(novaSenha.length() < 6){
            novaSenha += ""+aleatorio.nextInt(10);
        }
        mUserDAO = new UserDAO(null, user, mRealm);
        mUserDAO.updatePassword(BCrypt.withDefaults().hashToString(12, novaSenha.toCharArray()));

        configurarEmail(novaSenha);
    }

    private boolean emailExists(){
        RealmResults<User> loginResult = mRealm.where(User.class)
                .equalTo("email", mViewHolder.editTextEsqueciSenhaEmail.getText().toString())
                .findAll();

        if (loginResult.isEmpty()) {
            Toast.makeText(this, "Esse email não foi cadastrado nesse celular", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class ViewHolder{
        Button btnNovaSenha;
        EditText editTextEsqueciSenhaEmail;
    }
}