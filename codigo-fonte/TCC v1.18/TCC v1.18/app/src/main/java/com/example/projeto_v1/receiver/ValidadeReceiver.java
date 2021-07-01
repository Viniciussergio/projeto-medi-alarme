package com.example.projeto_v1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.projeto_v1.notification.GerenciarCanalNotificacao;
import com.example.projeto_v1.tela.cadastro.model.Medicine;

import io.realm.Realm;

public class ValidadeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        GerenciarCanalNotificacao gerenciarCanalNotificacao = new GerenciarCanalNotificacao(context);
        Realm realm = Realm.getDefaultInstance();
        int notificationNumber = intent.getIntExtra("notificationNumber", 0);
        int idRemedio = intent.getIntExtra("medicineID", 0)-1;
        Medicine remedio = realm.where(Medicine.class).equalTo("idRemedio", idRemedio).findFirst();

        String notificationTitle = "Medicamento vencido";
        String notificationMessage = "O medicamento: "+remedio.getNomeRemedio()+" ("+remedio.getTipoRemedio()+") vence hoje";

        NotificationCompat.Builder nb = gerenciarCanalNotificacao.getExpirationChannelNotification(notificationTitle, notificationMessage);
        gerenciarCanalNotificacao.getManager().notify(notificationNumber, nb.build());
    }
}
