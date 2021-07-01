package com.example.projeto_v1.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.projeto_v1.R;
import com.example.projeto_v1.receiver.EmailReceiver;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.Historico;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.User;

import io.realm.Realm;

public class GerenciarCanalNotificacao extends ContextWrapper {
    public static final String CHANNEL_ALARMS_ID = "channelAlarmID";
    public static final String CHANNEL_QUESTION_ID = "channelQuestionID";
    public static final String CHANNEL_EXPIRATION_ID = "channelExpirationID";
    public static final String CHANNEL_ALARMS = "Alarmes";
    public static final String CHANNEL_QUESTION  = "Medicamentos";
    public static final String CHANNEL_EXPIRATION = "Data de validade";
    public int mEmailRequestCode;
    public SharedPreferences EMAIL_REQUEST_CODE_NUMEROS;
    private NotificationManager mManager;

    public GerenciarCanalNotificacao(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel alarmsChannelNC = new NotificationChannel(CHANNEL_ALARMS_ID, CHANNEL_ALARMS, NotificationManager.IMPORTANCE_DEFAULT);
        alarmsChannelNC.enableLights(true);
        alarmsChannelNC.enableVibration(true);
        alarmsChannelNC.setLightColor(R.color.colorPrimary);
        alarmsChannelNC.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(alarmsChannelNC);

        NotificationChannel questionChannelNC = new NotificationChannel(CHANNEL_QUESTION_ID, CHANNEL_QUESTION, NotificationManager.IMPORTANCE_DEFAULT);
        questionChannelNC.enableLights(true);
        questionChannelNC.enableVibration(true);
        questionChannelNC.setLightColor(R.color.colorPrimary);
        questionChannelNC.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        notificationManager.createNotificationChannel(questionChannelNC);

        NotificationChannel expirationChannelNC = new NotificationChannel(CHANNEL_EXPIRATION_ID, CHANNEL_EXPIRATION, NotificationManager.IMPORTANCE_DEFAULT);
        questionChannelNC.enableLights(true);
        questionChannelNC.enableVibration(true);
        questionChannelNC.setLightColor(R.color.colorPrimary);
        questionChannelNC.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        notificationManager.createNotificationChannel(expirationChannelNC);
    }

    public NotificationManager getManager() {
        if(mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getAlarmeChannelNotification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ALARMS_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_alarm_on)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getQuestionChannelNotification(int idUsuario, int idHistorico, String title, String message, int notificationNumber){
        Realm mRealm = Realm.getDefaultInstance();
        Historico historico = mRealm.where(Historico.class).equalTo("idHistorico", idHistorico).findFirst();
        int idRemedio = historico.getIdRemedioHistorico();
        int idAlarme = historico.getIdAlarmeHistorico();
        EMAIL_REQUEST_CODE_NUMEROS = getSharedPreferences("emailRequestCodeNumbers", Context.MODE_PRIVATE);
        mEmailRequestCode = EMAIL_REQUEST_CODE_NUMEROS.getInt("emailRequestCode", 200);

        Intent emailIntent = new Intent(this, EmailReceiver.class);
        emailIntent.putExtra("identificador", 1);
        emailIntent.putExtra("medicineID", idRemedio);
        emailIntent.putExtra("alarmeID", idAlarme);
        emailIntent.putExtra("userID", idUsuario);
        emailIntent.putExtra("historicoID", idHistorico);
        emailIntent.putExtra("notificationNumber", notificationNumber);
        PendingIntent emailPendingIntent = PendingIntent.getBroadcast(this, mEmailRequestCode, emailIntent, 0);
        mEmailRequestCode++;

        Intent historicoIntent = new Intent(this, EmailReceiver.class);
        historicoIntent.putExtra("identificador", 2);
        historicoIntent.putExtra("medicineID", idRemedio);
        historicoIntent.putExtra("alarmeID", idAlarme);
        historicoIntent.putExtra("userID", idUsuario);
        historicoIntent.putExtra("historicoID", idHistorico);
        historicoIntent.putExtra("notificationNumber", notificationNumber);
        PendingIntent historicoPendingIntent = PendingIntent.getBroadcast(this, mEmailRequestCode, historicoIntent, 0);
        mEmailRequestCode++;

        SharedPreferences.Editor editor = EMAIL_REQUEST_CODE_NUMEROS.edit();
        editor.putInt("emailRequestCode", mEmailRequestCode);
        editor.apply();

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_QUESTION_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_alarm_on)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(false)
                .setOngoing(true)
                .addAction(R.mipmap.ic_launcher, "Sim", historicoPendingIntent)
                .addAction(R.mipmap.ic_launcher, "Não", emailPendingIntent);
    }

    public NotificationCompat.Builder getExpirationChannelNotification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_EXPIRATION_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_warning)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
    }
}
