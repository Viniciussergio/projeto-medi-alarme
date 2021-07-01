package com.example.projeto_v1.receiver;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.Associado;
import com.example.projeto_v1.tela.cadastro.model.AssociadoDAO;
import com.example.projeto_v1.tela.cadastro.model.Historico;
import com.example.projeto_v1.tela.cadastro.model.HistoricoDAO;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.example.projeto_v1.tela.inicial.TelaInicialActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

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
import io.realm.RealmResults;

public class EmailReceiver extends BroadcastReceiver {
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
    private ArrayList<String> allEmails = new ArrayList<>();
    private Realm mRealm;
    private User mUser;
    private Medicine mMedicine;
    private Alarme mAlarme;
    private Historico mHistorico, mNovoHistorico;
    private String mensagem, sEmail, sPassword, dataAtual, modoUso;
    private AssociadoDAO associadoDAO;
    private HistoricoDAO historicoDAO;
    private RealmResults<Associado> associados;
    private int identificador;

    @Override
    public void onReceive(Context context, Intent intent) {
        identificador = intent.getIntExtra("identificador", 0);
        int idUsuario = intent.getIntExtra("userID", 0);
        int idRemedio = intent.getIntExtra("medicineID", 0);
        int idAlarme = intent.getIntExtra("alarmeID", 0);
        int idHistorico = intent.getIntExtra("historicoID", 0);
        int notificationNumber = intent.getIntExtra("notificationNumber", 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mRealm = Realm.getDefaultInstance();
        dataAtual = sdf.format(Calendar.getInstance().getTime());

        mUser = mRealm.where(User.class).equalTo("id", idUsuario).findFirst();
        mMedicine = mRealm.where(Medicine.class).equalTo("idRemedio", idRemedio).findFirst();
        mAlarme = mRealm.where(Alarme.class).equalTo("idAlarme", idAlarme).findFirst();
        mHistorico = mRealm.where(Historico.class).equalTo("idHistorico", idHistorico).findFirst();

        associadoDAO = new AssociadoDAO(null, null, null, mRealm);
        associados = mRealm.where(Associado.class).findAll();
        allEmails = associadoDAO.getAllEmails(associados);

        trocarHistorico();
        configurarEmail();
        notificationManager.cancel(notificationNumber);
    }

    private void trocarHistorico() {
        if(identificador == 1){
            historicoDAO = new HistoricoDAO(mHistorico, mUser, mRealm);
            mNovoHistorico = new Historico();
            mNovoHistorico.setIdAlarmeHistorico(mHistorico.getIdAlarmeHistorico());
            mNovoHistorico.setIdRemedioHistorico(mHistorico.getIdRemedioHistorico());
            mNovoHistorico.setDiaHistorico(mHistorico.getDiaHistorico());
            mNovoHistorico.setIdHistorico(mHistorico.getIdHistorico());
            mNovoHistorico.setStatusHistorico("Não Tomado");
            historicoDAO.updateHistorico(mNovoHistorico);
        }else if(identificador == 2){
            historicoDAO = new HistoricoDAO(mHistorico, mUser, mRealm);
            mNovoHistorico = new Historico();
            mNovoHistorico.setIdAlarmeHistorico(mHistorico.getIdAlarmeHistorico());
            mNovoHistorico.setIdRemedioHistorico(mHistorico.getIdRemedioHistorico());
            mNovoHistorico.setDiaHistorico(mHistorico.getDiaHistorico());
            mNovoHistorico.setIdHistorico(mHistorico.getIdHistorico());
            mNovoHistorico.setStatusHistorico("Tomado");
            historicoDAO.updateHistorico(mNovoHistorico);
        }
    }

    private void configurarEmail(){
        modoUso = mMedicine.getTipoRemedio();
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

        if(associados.size() != 0){
            for(int i = 0; i < associados.size(); i++){
                Associado associado = associados.get(i);
                String nomeAssociado = associado.getAssociadoFirstName();
                String emailAssociado = associado.getAssociadoEmail();
                String emailSubject = "Notificação Remédio MediAlarme";

                if(mMedicine.getTipoRemedio().equals("Gotas")){
                    modoUso = "Tomar "+mAlarme.getGotasDose()+" gotas por dose";
                }else if(mMedicine.getTipoRemedio().equals("Comprimido")){
                    modoUso = "Tomar "+mAlarme.getComprimidosDose()+" comprimidos por dose";
                }else if(mMedicine.getTipoRemedio().equals("Outro")){
                    modoUso = mAlarme.getModoUsoRemedio();
                }

                if(identificador == 2){
                    mensagem = "Olá "+nomeAssociado;
                    mensagem += "\nO usuario "+mUser.getFirstName()+" tomou o remedio "
                            +mMedicine.getNomeRemedio()+" que deveria ser tomado no horário das " +mAlarme.getHorario()+
                            " horas do dia "+ mHistorico.getDiaHistorico();
                    mensagem += "\nFoi utilizado às "+sdfTime.format(Calendar.getInstance().getTime())
                            + " horas do dia "+dataAtual+" com o seguinte modo de uso: ";
                    mensagem += "\n"+modoUso;
                    mensagem += "\nCaso queira saber mais sobre essa dose, sugerimos que mantenha contato com o usuario a partir do telefone cadastrado por ele no nosso Aplicativo";
                    mensagem += "\n"+mUser.getTelefone();
                }
                else if(identificador == 1){
                    mensagem = "Olá "+nomeAssociado;
                    mensagem += "\nO usuario "+mUser.getFirstName()+" nao tomou o remedio "
                            +mMedicine.getNomeRemedio()+" no devido horario(" +mAlarme.getHorario()
                            + ") no dia "+mHistorico.getDiaHistorico()+" com o seguinte modo de uso: ";
                    mensagem += "\n"+modoUso;
                    mensagem += "\nCaso queira saber mais sobre essa dose, sugerimos que mantenha contato com o usuario a partir do telefone cadastrado por ele no nosso Aplicativo";
                    mensagem += "\n"+mUser.getTelefone();
                }

                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(sEmail) );
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(emailAssociado));
                    message.setSubject(emailSubject);
                    message.setText(mensagem);

                    new SendEmail().execute(message);
                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
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
}
