package com.example.projeto_v1.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.projeto_v1.receiver.AlarmeClockReceiver;
import com.example.projeto_v1.receiver.AlarmeReceiver;
import com.example.projeto_v1.receiver.PerguntaReceiver;
import com.example.projeto_v1.receiver.ValidadeReceiver;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.AlarmeConfiguradoUsuario;
import com.example.projeto_v1.tela.cadastro.model.Medicine;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.RealmList;

public class GerenciarNotificacao extends ContextWrapper {

    private final RealmList<Integer> ALARME_REQUEST_CODE = new RealmList<>();
    private final RealmList<Integer> QUESTION_REQUEST_CODE = new RealmList<>();
    private final RealmList<Integer> ALARME_CLOCK_REQUEST_CODE = new RealmList<>();
    private final int VALOR_FLAG = 0;
    private final SharedPreferences NOTIFICATION_REQUEST_CODE_NUMEROS;
    private final Medicine MEDICAMENTO;
    private Calendar CALENDARIO;
    private final AlarmeConfiguradoUsuario ALARME_CONFIGURADO_USUARIO;
    private final AlarmManager ALARME_MANAGER;
    private int mNotificationNumber;
    private int mAlarmeRequestCode;
    private int mAlarmeClockRequestCode;
    private int mPerguntaRequestCode;
    private int mValidadeRequestCode;
    private PendingIntent mAlarmeReceiverPendingIntent;
    private PendingIntent mPerguntaReceiverPendingIntent;
    private Alarme alarme;

    public GerenciarNotificacao(Context base, Medicine medicine, AlarmeConfiguradoUsuario alarmeConfiguradoUsuario, Alarme alarme) {
        super(base);
        this.MEDICAMENTO = medicine;
        this.ALARME_CONFIGURADO_USUARIO = alarmeConfiguradoUsuario;
        this.alarme = alarme;
        if (ALARME_CONFIGURADO_USUARIO != null) {
            CALENDARIO = alarmeConfiguradoUsuario.getCalendar();
        }
        ALARME_MANAGER = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        NOTIFICATION_REQUEST_CODE_NUMEROS = getSharedPreferences("notificationRequestCodeNumbers", Context.MODE_PRIVATE);
        mNotificationNumber = NOTIFICATION_REQUEST_CODE_NUMEROS.getInt("notificationNumber", 0);
        mAlarmeRequestCode = NOTIFICATION_REQUEST_CODE_NUMEROS.getInt("alarmeRequestCode", 1);
        mAlarmeClockRequestCode = NOTIFICATION_REQUEST_CODE_NUMEROS.getInt("alarmeClockRequestCode", 50);
        mPerguntaRequestCode = NOTIFICATION_REQUEST_CODE_NUMEROS.getInt("perguntaRequestCode", 100);
        mValidadeRequestCode = NOTIFICATION_REQUEST_CODE_NUMEROS.getInt("validadeRequestCode", 150);
    }

    public void configurarNotificacaoUsuario(Alarme alarme, String mSpinnerText) {

        int corretorDiaDiario = 1;

        ALARME_REQUEST_CODE.add(mAlarmeRequestCode);
        QUESTION_REQUEST_CODE.add(mPerguntaRequestCode);

        criarRecriarIntentsUsuario();

        if (mSpinnerText.equals("Diário")) {
            if (CALENDARIO.before(Calendar.getInstance())) {
                CALENDARIO.add(Calendar.DATE, corretorDiaDiario);
            }
            criarNotificacaoRepetente(CALENDARIO.getTimeInMillis(), AlarmManager.INTERVAL_DAY);
        } else if (mSpinnerText.equals("Personalizado")) {
            criarNotificacaoPersonalizada();
        } else {
            criarNotificacaoEspecifica();
        }

        alarme.setAlarmeRequestCode(ALARME_REQUEST_CODE);
        alarme.setQuestionRequestCode(QUESTION_REQUEST_CODE);
        alarme.setAlarmeClockRequestCode(ALARME_CLOCK_REQUEST_CODE);

        salvarModificacoesSharedPreferences(true);

        Toast.makeText(this, "Tudo certo", Toast.LENGTH_SHORT).show();
    }

    public void configurarNotificacaoSistema(Calendar dataValidade, int remedioId) {
        Intent validadeReceiverIntent = new Intent(this, ValidadeReceiver.class);
        validadeReceiverIntent.putExtra("notificationNumber", mNotificationNumber);
        validadeReceiverIntent.putExtra("medicineID", remedioId);
        Log.v("ID", ""+remedioId);
        mNotificationNumber++;

        PendingIntent validadeReceiverPendingIntent = PendingIntent.getBroadcast(this, mValidadeRequestCode,
                validadeReceiverIntent, VALOR_FLAG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ALARME_MANAGER.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    dataValidade.getTimeInMillis(), validadeReceiverPendingIntent);
        } else {
            ALARME_MANAGER.setExact(AlarmManager.RTC_WAKEUP,
                    dataValidade.getTimeInMillis(), validadeReceiverPendingIntent);
        }

        salvarModificacoesSharedPreferences(false);
    }

    private void criarNotificacaoRepetente(long gatilho, long intervalo) {
        long tempoAjustadoSegundaNotificacao = 35 * 60000 + gatilho;
        ALARME_MANAGER.setRepeating(AlarmManager.RTC_WAKEUP, gatilho,
                intervalo, mAlarmeReceiverPendingIntent);
        ALARME_MANAGER.setRepeating(AlarmManager.RTC_WAKEUP, tempoAjustadoSegundaNotificacao,
                intervalo, mPerguntaReceiverPendingIntent);
    }

    private void criarNotificacaoPersonalizada() {
        int corretorDiaPersonalizado = 7;
        int transformaIntervaloSemana = 7;
        long intervaloSemana = AlarmManager.INTERVAL_DAY * transformaIntervaloSemana;
        ArrayList<Integer> alarmDays = ALARME_CONFIGURADO_USUARIO.getAlarmDays();

        for (int posicao = 0; posicao < alarmDays.size(); posicao++) {
            mAlarmeRequestCode++;
            mPerguntaRequestCode++;

            ALARME_REQUEST_CODE.add(mAlarmeRequestCode);
            QUESTION_REQUEST_CODE.add(mPerguntaRequestCode);

            CALENDARIO.set(Calendar.DAY_OF_WEEK, alarmDays.get(posicao));

            if (CALENDARIO.before(Calendar.getInstance())) {
                CALENDARIO.add(Calendar.DATE, corretorDiaPersonalizado);
                criarNotificacaoRepetente(CALENDARIO.getTimeInMillis(), intervaloSemana);
                CALENDARIO.add(Calendar.DATE, -corretorDiaPersonalizado);
            } else {
                criarNotificacaoRepetente(CALENDARIO.getTimeInMillis(), intervaloSemana);
            }

            criarRecriarIntentsUsuario();
        }
    }

    private void criarNotificacaoEspecifica() {
        long corretorTempo = 5 * 60000;
        long corretorMinuto = 35 * 60000;
        String mesSelecionado = ALARME_CONFIGURADO_USUARIO.getMesSelecionado();
        String diaSelecionado = ALARME_CONFIGURADO_USUARIO.getDiaSelecionado();
        String anoSelecionado = ALARME_CONFIGURADO_USUARIO.getAnoSelecionado();
        String horaSelecionada = ALARME_CONFIGURADO_USUARIO.getHoraSeleionada();
        String minutoSelecionado = ALARME_CONFIGURADO_USUARIO.getMinutoSelecionado();

        ALARME_CLOCK_REQUEST_CODE.add(mAlarmeClockRequestCode);
        CALENDARIO.set(Calendar.DAY_OF_MONTH, Integer.parseInt(diaSelecionado));
        CALENDARIO.set(Calendar.MONTH, Integer.parseInt(mesSelecionado));
        CALENDARIO.set(Calendar.YEAR, Integer.parseInt(anoSelecionado));

        Intent alarmeClockIntent = new Intent(this, AlarmeClockReceiver.class);
        alarmeClockIntent.putExtra("Hora", Integer.parseInt(horaSelecionada));
        alarmeClockIntent.putExtra("Minuto", Integer.parseInt(minutoSelecionado));
        alarmeClockIntent.putExtra("medicineID", MEDICAMENTO.getIdRemedio());
        PendingIntent alarmeClockReceiverPendingIntent = PendingIntent.getBroadcast(this,
                mAlarmeClockRequestCode, alarmeClockIntent, VALOR_FLAG);
        mAlarmeClockRequestCode++;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ALARME_MANAGER.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    CALENDARIO.getTimeInMillis() - corretorTempo, alarmeClockReceiverPendingIntent);
            ALARME_MANAGER.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    CALENDARIO.getTimeInMillis(), mAlarmeReceiverPendingIntent);
            ALARME_MANAGER.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    CALENDARIO.getTimeInMillis() + corretorMinuto, mPerguntaReceiverPendingIntent);
        } else {
            ALARME_MANAGER.setExact(AlarmManager.RTC_WAKEUP,
                    CALENDARIO.getTimeInMillis() - corretorTempo, alarmeClockReceiverPendingIntent);
            ALARME_MANAGER.setExact(AlarmManager.RTC_WAKEUP,
                    CALENDARIO.getTimeInMillis(), mAlarmeReceiverPendingIntent);
            ALARME_MANAGER.setExact(AlarmManager.RTC_WAKEUP,
                    CALENDARIO.getTimeInMillis() + corretorMinuto, mPerguntaReceiverPendingIntent);
        }
    }

    private void criarRecriarIntentsUsuario() {
        Intent alarmeReceiverIntent = new Intent(this, AlarmeReceiver.class);
        alarmeReceiverIntent.putExtra("notificationNumber", mNotificationNumber);
        alarmeReceiverIntent.putExtra("medicineID", MEDICAMENTO.getIdRemedio());
        alarmeReceiverIntent.putExtra("alarmeID", alarme.getIdAlarme());
        mNotificationNumber++;
        mAlarmeReceiverPendingIntent = PendingIntent.getBroadcast(this, mAlarmeRequestCode,
                alarmeReceiverIntent, VALOR_FLAG);

        Intent perguntaReceiverIntent = new Intent(this, PerguntaReceiver.class);
        perguntaReceiverIntent.putExtra("notificationNumber", mNotificationNumber);
        perguntaReceiverIntent.putExtra("medicineID", MEDICAMENTO.getIdRemedio());
        perguntaReceiverIntent.putExtra("alarmeID", alarme.getIdAlarme());
        mNotificationNumber++;
        mPerguntaReceiverPendingIntent = PendingIntent.getBroadcast(this, mPerguntaRequestCode,
                perguntaReceiverIntent, VALOR_FLAG);
    }

    private void salvarModificacoesSharedPreferences(boolean notificacaoUsuario) {
        SharedPreferences.Editor editor = NOTIFICATION_REQUEST_CODE_NUMEROS.edit();
        mNotificationNumber++;
        if (notificacaoUsuario) {
            mAlarmeRequestCode++;
            mPerguntaRequestCode++;
        } else
            mValidadeRequestCode++;
        editor.putInt("notificationNumber", mNotificationNumber);
        editor.putInt("alarmeRequestCode", mAlarmeRequestCode);
        editor.putInt("alarmeClockRequestCode", mAlarmeClockRequestCode);
        editor.putInt("perguntaRequestCode", mPerguntaRequestCode);
        editor.putInt("validadeRequestCode", mValidadeRequestCode);
        editor.apply();
    }
}
