package com.example.projeto_v1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.projeto_v1.notification.GerenciarCanalNotificacao;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.Historico;
import com.example.projeto_v1.tela.cadastro.model.HistoricoDAO;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;

public class AlarmeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Realm mRealm = Realm.getDefaultInstance();
        int notificationNumber = intent.getIntExtra("notificationNumber", 0);
        int idRemedio = intent.getIntExtra("medicineID", 0);
        int idAlarme = intent.getIntExtra("alarmeID", 0);

        Alarme alarme = mRealm.where(Alarme.class).equalTo("idAlarme", idAlarme).findFirst();
        Medicine medicine = mRealm.where(Medicine.class).equalTo("idRemedio", idRemedio).findFirst();

        User user = medicine.getUsuario();
        String modoUso = "";

        if(medicine.getTipoRemedio().equals("Gotas")){
            modoUso = "Tomar "+alarme.getGotasDose()+" gotas";
        }else if(medicine.getTipoRemedio().equals("Comprimido")){
            modoUso = "Tomar "+alarme.getComprimidosDose()+" comprimidos";
        }else if(medicine.getTipoRemedio().equals("Outro")){
            modoUso = alarme.getModoUsoRemedio();
        }

        GerenciarCanalNotificacao gerenciarCanalNotificacao = new GerenciarCanalNotificacao(context);
        String notificationTitle = user.getFirstName()+" "+user.getLastName()+" - "+medicine.getNomeRemedio();
        String notificationMessage = "Em breve("+alarme.getHorario()+") você deve: "+modoUso;

        NotificationCompat.Builder nb = gerenciarCanalNotificacao.getAlarmeChannelNotification(notificationTitle, notificationMessage);
        gerenciarCanalNotificacao.getManager().notify(notificationNumber, nb.build());

        Historico historico = new Historico();

        Number maxId = mRealm.where(Alarme.class).max("idAlarme");
        int newKey = (maxId == null) ? 1 : maxId.intValue() + 1;

        historico.setIdHistorico(newKey);
        historico.setIdAlarmeHistorico(alarme.getIdAlarme());
        historico.setIdRemedioHistorico(medicine.getIdRemedio());
        historico.setStatusHistorico("Pendente");
        historico.setDiaHistorico(sdf.format(Calendar.getInstance().getTime()));

        HistoricoDAO historicoDAO = new HistoricoDAO(historico, user, mRealm);
        historicoDAO.createHistorico();
    }
}
