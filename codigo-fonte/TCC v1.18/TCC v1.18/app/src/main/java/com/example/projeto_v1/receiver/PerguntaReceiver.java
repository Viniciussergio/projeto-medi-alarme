package com.example.projeto_v1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.projeto_v1.notification.GerenciarCanalNotificacao;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.Historico;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;

public class PerguntaReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        GerenciarCanalNotificacao gerenciarCanalNotificacao = new GerenciarCanalNotificacao(context);
        int notificationNumber = intent.getIntExtra("notificationNumber", 0);
        int idRemedio = intent.getIntExtra("medicineID", 0);
        int idAlarme = intent.getIntExtra("alarmeID", 0);
        Realm mRealm = Realm.getDefaultInstance();
        Alarme alarme = mRealm.where(Alarme.class).equalTo("idAlarme", idAlarme).findFirst();
        Medicine medicine = mRealm.where(Medicine.class).equalTo("idRemedio", idRemedio).findFirst();
        User user = medicine.getUsuario();

        Historico historico = mRealm.where(Historico.class).equalTo("idAlarmeHistorico", idAlarme)
                                        .and().equalTo("idRemedioHistorico", idRemedio)
                                        .and().equalTo("diaHistorico", sdf.format(Calendar.getInstance().getTime())).findFirst();

        String notificationTitle = user.getFirstName()+" "+user.getLastName()+" - "+medicine.getNomeRemedio()+"("+medicine.getTipoRemedio()+")";
        String notificationMessage = "Você tomou ou tomará o medicamento deste horário("+alarme.getHorario()+") hoje?";

        NotificationCompat.Builder nb = gerenciarCanalNotificacao.getQuestionChannelNotification(user.getId(), historico.getIdHistorico(), notificationTitle, notificationMessage, notificationNumber);
        gerenciarCanalNotificacao.getManager().notify(notificationNumber, nb.build());
    }
}
