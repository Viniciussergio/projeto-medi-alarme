package com.example.projeto_v1.tela.cadastro.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.projeto_v1.receiver.AlarmeClockReceiver;
import com.example.projeto_v1.receiver.AlarmeReceiver;
import com.example.projeto_v1.receiver.PerguntaReceiver;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AlarmeDAO {
    private Medicine medicine;
    private Alarme alarme;
    private Alarme novoAlarme;
    private RealmList<Alarme> alarmes;
    private Realm realm;
    private RealmList<Integer> alarmeRequestCode;
    private RealmList<Integer> alarmeClockRequestCode;
    private RealmList<Integer> perguntaRequestCode;

    public AlarmeDAO(Medicine medicine, Alarme alarme, Realm realm) {
        this.medicine = medicine;
        this.alarme = alarme;
        this.realm = realm;
    }

    public void createAlarm() {
        realm.beginTransaction();
        medicine.getAlarmes().add(alarme);
        realm.commitTransaction();
    }

    public void removeAlarm() {
        removeHistoricos(realm.where(Historico.class).equalTo("idAlarmeHistorico", alarme.getIdAlarme()).findAll());
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                alarme.deleteFromRealm();
            }
        });
    }

    public void removeHistoricos(RealmResults<Historico> historicos){
        while(historicos.size() > 0){
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm) {
                    historicos.get(historicos.size()-1).deleteFromRealm();
                }
            });
        }
    }

    public void removeNotifications(Alarme alarmeData, Context context){
        alarmeRequestCode = alarmeData.getAlarmeRequestCode();
        if (alarmeData.getAlarmeClockRequestCode() != null) {
            alarmeClockRequestCode = alarmeData.getAlarmeClockRequestCode();
        }
        perguntaRequestCode = alarmeData.getQuestionRequestCode();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        for(int position = 0; position < alarmeRequestCode.size(); position++){
            Intent alarmeReceiverIntent = new Intent(context, AlarmeReceiver.class);
            PendingIntent alarmeReceiverPendingIntent = PendingIntent.getBroadcast(context, alarmeRequestCode.get(position),
                    alarmeReceiverIntent, 0);

            alarmManager.cancel(alarmeReceiverPendingIntent);
        }

        for(int position = 0; position < perguntaRequestCode.size(); position++){
            Intent perguntaReceiverIntent = new Intent(context, PerguntaReceiver.class);
            PendingIntent perguntaReceiverPendingIntent = PendingIntent.getBroadcast(context, perguntaRequestCode.get(position),
                    perguntaReceiverIntent, 0);

            alarmManager.cancel(perguntaReceiverPendingIntent);
        }

        if(alarmeClockRequestCode != null){
            for(int position = 0; position < alarmeClockRequestCode.size(); position++){
                Intent alarmeClockReceiverIntent = new Intent(context, AlarmeClockReceiver.class);
                PendingIntent alarmeClockReceiverPendingIntent = PendingIntent.getBroadcast(context, alarmeClockRequestCode.get(position),
                        alarmeClockReceiverIntent, 0);

                alarmManager.cancel(alarmeClockReceiverPendingIntent);
            }
        }
    }

    public void selectFromDB(Medicine remedio) {
        alarmes = remedio.getAlarmes();
    }

    public ArrayList<Alarme> justRefresh() {
        ArrayList<Alarme> listItem = new ArrayList<>();
        if(alarmes.isValid()){
            for (Alarme a : alarmes) {
                listItem.add(a);
            }
        }else{
            return listItem;
        }
        return listItem;
    }
}
