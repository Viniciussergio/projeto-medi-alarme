package com.example.projeto_v1.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.User;

import io.realm.Realm;

public class AlarmeClockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int hora =  intent.getIntExtra("Hora", 0);
        int minuto = intent.getIntExtra("Minuto", 0);

        Realm mRealm = Realm.getDefaultInstance();
        int idRemedio = intent.getIntExtra("medicineID", 0);
        Medicine medicine = mRealm.where(Medicine.class).equalTo("idRemedio", idRemedio).findFirst();Intent alarmeClock = new Intent(AlarmClock.ACTION_SET_ALARM);
        User user = medicine.getUsuario();alarmeClock.putExtra(AlarmClock.EXTRA_HOUR, hora);

        alarmeClock.putExtra(AlarmClock.EXTRA_MINUTES, minuto);
        alarmeClock.putExtra(AlarmClock.EXTRA_MESSAGE, "Medialarme - "+medicine.getNomeRemedio()+"("+user.getFirstName()+" "+user.getLastName()+")");
        alarmeClock.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        context.startActivity(alarmeClock);
    }
}
