package com.example.projeto_v1.tela.cadastro;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.LoginActivity;
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

import io.realm.Realm;

public class VerificarEmailActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewHolder mViewHolder = new ViewHolder();
    private Realm mRealm;
    private User user = new User();
    private String sEmail;
    private String sPassword;
    private String codigoVerificacao;
    private UserDAO mUserDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_email);

        Intent getIntent = getIntent();
        user.setFirstName(getIntent.getStringExtra("userFirstName"));
        user.setUserName(getIntent.getStringExtra("userNickname"));
        user.setLastName(getIntent.getStringExtra("userLastName"));
        user.setTelefone(getIntent.getStringExtra("userTelefone"));
        user.setPassword(getIntent.getStringExtra("userPassword"));
        user.setEmail(getIntent.getStringExtra("userEmail"));

        mRealm = Realm.getDefaultInstance();

        this.mViewHolder.editTextCodigoVerificacao = findViewById(R.id.edit_text_codigo_verificacao);
        this.mViewHolder.textViewEmailVerificacao = findViewById(R.id.text_view_verificar_email);
        this.mViewHolder.textViewReenviarCodigo = findViewById(R.id.text_view_reenviar_codigo);
        this.mViewHolder.buttonVerificarEmail = findViewById(R.id.btn_verificar_email);

        this.mViewHolder.buttonVerificarEmail.setOnClickListener(this);
        this.mViewHolder.textViewReenviarCodigo.setOnClickListener(this);

        enviarCodigo();
        setEmailText();
    }

    private void setEmailText(){
        this.mViewHolder.textViewEmailVerificacao.setText(user.getEmail());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_verificar_email){
            if(verificarCodigo()){
                mUserDAO = new UserDAO(null, user, mRealm);
                mUserDAO.createUser();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "O código de verificação está errado", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.text_view_reenviar_codigo){
            enviarCodigo();
        }
    }

    private boolean verificarCodigo() {
        if (this.mViewHolder.editTextCodigoVerificacao.getText().toString().equals(codigoVerificacao)) {
            return true;
        } else {
            return false;
        }
    }

    private void enviarCodigo(){
        Random aleatorio = new Random();
        codigoVerificacao = ""+aleatorio.nextInt(10);

        while(codigoVerificacao.length() < 6){
            codigoVerificacao += ""+aleatorio.nextInt(10);
        }

        configurarEmail(codigoVerificacao);
    }

    private void configurarEmail(String codigo){
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
                    InternetAddress.parse(user.getEmail()));
            message.setSubject("Medialarme - Código de Verificação");
            message.setText("Seu código de verificação é: "+codigo);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class ViewHolder{
        EditText editTextCodigoVerificacao;
        TextView textViewEmailVerificacao;
        TextView textViewReenviarCodigo;
        Button buttonVerificarEmail;
    }
}