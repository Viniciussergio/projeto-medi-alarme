package com.example.projeto_v1.tela.cadastro;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projeto_v1.notification.GerenciarNotificacao;
import com.example.projeto_v1.R;
import com.example.projeto_v1.tela.cadastro.model.Alarme;
import com.example.projeto_v1.tela.cadastro.model.AlarmeDAO;
import com.example.projeto_v1.tela.cadastro.model.AlarmeConfiguradoUsuario;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;

public class CadastroAlarmeActivity extends AppCompatActivity implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    private final int DIA_ATUAL = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private final int MES_ATUAL = Calendar.getInstance().get(Calendar.MONTH);
    private final int ANO_ATUAL = Calendar.getInstance().get(Calendar.YEAR);
    private final int HORA_ATUAL = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private final int MINUTO_ATUAL = Calendar.getInstance().get(Calendar.MINUTE);
    private final ViewHolder VIEW_HOLDER = new ViewHolder();
    private final ArrayList<Integer> ALARM_DAYS = new ArrayList<Integer>();
    private final RealmList<String> diasDaSemana = new RealmList<String>();
    private final RealmList<String> todosDiasDaSemana = new RealmList<String>();
    private Alarme alarme;
    private String mDiaSelecionado;
    private String mMesSelecionado;
    private String mAnoSelecionado;
    private String mHoraSelecionada;
    private String mMinutoSelecionado;
    private String mSpinnerText;
    private Calendar mCalendar;
    private Realm mRealm;
    private AlarmeDAO alarmeDAO;
    private int medicineId;
    private Medicine medicine;
    private User user;
    private String horarioAlarme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_alarme);
        getSupportActionBar().setTitle("Alarme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRealm = Realm.getDefaultInstance();
        alarme = new Alarme();

        Intent getIntent = getIntent();
        medicineId = getIntent.getIntExtra("medicineId", 0);
        medicine = mRealm.where(Medicine.class).equalTo("idRemedio", medicineId).findFirst();
        user = medicine.getUsuario();

        VIEW_HOLDER.editTextComprimidosDose = findViewById(R.id.edit_text_comprimidos_dose);
        VIEW_HOLDER.editTextGotasDose = findViewById(R.id.edit_text_gotas_dose);
        VIEW_HOLDER.editTextModoUsoRemedio = findViewById(R.id.edit_text_modo_uso);
        VIEW_HOLDER.textViewComprimidosDose = findViewById(R.id.text_view_comprimidos_dose);
        VIEW_HOLDER.textViewGotasDose = findViewById(R.id.text_view_gotas_dose);
        VIEW_HOLDER.textViewModoUsoRemedio = findViewById(R.id.text_view_modo_uso);

        VIEW_HOLDER.editTextAlarmTime = findViewById(R.id.edit_text_alarm_time);
        VIEW_HOLDER.spinnerAlarm = findViewById(R.id.alarms_spinner);
        VIEW_HOLDER.saveAlarm = findViewById(R.id.button_save_alarm);
        VIEW_HOLDER.dayPicker = findViewById(R.id.day_picker);
        VIEW_HOLDER.editTextSelectedDate = findViewById(R.id.edit_text_selected_date);
        VIEW_HOLDER.toggleButtonSunday = findViewById(R.id.toggle_button_sunday);
        VIEW_HOLDER.toggleButtonMonday = findViewById(R.id.toggle_button_monday);
        VIEW_HOLDER.toggleButtonTuesday = findViewById(R.id.toggle_button_tuesday);
        VIEW_HOLDER.toggleButtonWednesday = findViewById(R.id.toggle_button_wednesday);
        VIEW_HOLDER.toggleButtonThursday = findViewById(R.id.toggle_button_thursday);
        VIEW_HOLDER.toggleButtonFriday = findViewById(R.id.toggle_button_friday);
        VIEW_HOLDER.toggleButtonSaturday = findViewById(R.id.toggle_button_saturday);
        VIEW_HOLDER.editTextAlarmTime.setOnClickListener(this);
        VIEW_HOLDER.editTextSelectedDate.setOnClickListener(this);
        VIEW_HOLDER.spinnerAlarm.setOnItemSelectedListener(this);
        VIEW_HOLDER.saveAlarm.setOnClickListener(this);
        tipoRemedio();
        configurarAdapter();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendarHoraEscolhida = Calendar.getInstance();
        int minutoAntecedencia = 5;

        if (DateFormat.is24HourFormat(this)) {
            VIEW_HOLDER.editTextAlarmTime.setText(String.format("%02d:%02d", hourOfDay, minute));
        } else {
            verificarAMPM(hourOfDay, minute);
        }

        calendarHoraEscolhida.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendarHoraEscolhida.set(Calendar.MINUTE, minute - minutoAntecedencia);
        calendarHoraEscolhida.set(Calendar.SECOND, 0);
        calendarHoraEscolhida.set(Calendar.MILLISECOND, 0);
        setCalendar(calendarHoraEscolhida);
        mHoraSelecionada = String.valueOf(hourOfDay);
        mMinutoSelecionado = String.valueOf(minute);
    }

    private void tipoRemedio() {
        if (medicine.getTipoRemedio().equals("Comprimido")) {
            VIEW_HOLDER.textViewComprimidosDose.setVisibility(View.VISIBLE);
            VIEW_HOLDER.editTextComprimidosDose.setVisibility(View.VISIBLE);
        } else if (medicine.getTipoRemedio().equals("Gotas")) {
            VIEW_HOLDER.textViewGotasDose.setVisibility(View.VISIBLE);
            VIEW_HOLDER.editTextGotasDose.setVisibility(View.VISIBLE);
        } else if (medicine.getTipoRemedio().equals("Outro")) {
            VIEW_HOLDER.textViewModoUsoRemedio.setVisibility(View.VISIBLE);
            VIEW_HOLDER.editTextModoUsoRemedio.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.edit_text_alarm_time) {
            showTimePicker();
        } else if (v.getId() == R.id.edit_text_selected_date) {
            showDatePickerDialog();
        } else if (v.getId() == R.id.button_save_alarm) {
            if (dataValidation()) {
                horarioAlarme = this.VIEW_HOLDER.editTextAlarmTime.getText().toString();
                String[] result = VIEW_HOLDER.editTextAlarmTime.getText().toString().split(":");
                String horaSelecionada = result[0];
                String minutoSelecionado = result[1];
                if (minutoSelecionado.contains("AM"))
                    minutoSelecionado = minutoSelecionado.replace("AM", "").trim();
                else if (minutoSelecionado.contains("PM"))
                    minutoSelecionado = minutoSelecionado.replace("PM", "").trim();

                configurarDiasAlarme();

                if (mSpinnerText.equals("Diário") || mSpinnerText.equals("Personalizado")) {
                    configurarAlarme(horaSelecionada, minutoSelecionado, medicine.getNomeRemedio());
                }

                AlarmeConfiguradoUsuario alarmeConfiguradoUsuario = new AlarmeConfiguradoUsuario();

                alarmeConfiguradoUsuario.setCalendar(mCalendar);
                alarmeConfiguradoUsuario.setMesSelecionado(mMesSelecionado);
                alarmeConfiguradoUsuario.setDiaSelecionado(mDiaSelecionado);
                alarmeConfiguradoUsuario.setAnoSelecionado(mAnoSelecionado);
                alarmeConfiguradoUsuario.setAlarmDays(ALARM_DAYS);
                alarmeConfiguradoUsuario.setHoraSeleionada(mHoraSelecionada);
                alarmeConfiguradoUsuario.setMinutoSelecionado(mMinutoSelecionado);

                setAlarmeModel();
                alarmeDAO = new AlarmeDAO(medicine, alarme, mRealm);
                alarmeDAO.createAlarm();

                GerenciarNotificacao gerenciarNotificacao = new GerenciarNotificacao(this, medicine, alarmeConfiguradoUsuario, alarme);
                gerenciarNotificacao.configurarNotificacaoUsuario(alarme, mSpinnerText);

                VIEW_HOLDER.editTextSelectedDate.setText("");
                VIEW_HOLDER.editTextAlarmTime.setText("");

                onBackPressed();
            }
        }
    }

    private void setAlarmeModel() {
        Number maxId = mRealm.where(Alarme.class).max("idAlarme");
        int newKey = (maxId == null) ? 1 : maxId.intValue() + 1;
        alarme.setIdAlarme(newKey);
        alarme.setHorario(horarioAlarme);
        alarme.setTipo(mSpinnerText);
        alarme.setRemedio(medicine);
        if (mSpinnerText.equals("Personalizado")) {
            alarme.setDiasDaSemana(diasDaSemana);
        } else if (mSpinnerText.equals("Diário")) {
            getAllDays();
            alarme.setDiasDaSemana(todosDiasDaSemana);
        }
        if (medicine.getTipoRemedio().equals("Comprimido")) {
            alarme.setComprimidosDose(Integer.parseInt(VIEW_HOLDER.editTextComprimidosDose.getText().toString()));
        } else if (medicine.getTipoRemedio().equals("Gotas")) {
            alarme.setGotasDose(Integer.parseInt(VIEW_HOLDER.editTextGotasDose.getText().toString()));
        } else if (medicine.getTipoRemedio().equals("Outro")) {
            alarme.setModoUsoRemedio(VIEW_HOLDER.editTextModoUsoRemedio.getText().toString());
        }
    }

    private void getAllDays() {
        todosDiasDaSemana.add("Domingo");
        todosDiasDaSemana.add("Segunda");
        todosDiasDaSemana.add("Terça");
        todosDiasDaSemana.add("Quarta");
        todosDiasDaSemana.add("Quinta");
        todosDiasDaSemana.add("Sexta");
        todosDiasDaSemana.add("Sábado");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

        setSpinnerText(text);

        if (text.equals("Diário")) {
            VIEW_HOLDER.dayPicker.setVisibility(View.GONE);
            VIEW_HOLDER.editTextSelectedDate.setText("");
            VIEW_HOLDER.editTextSelectedDate.setVisibility(View.GONE);
        } else if (text.equals("Personalizado")) {
            VIEW_HOLDER.dayPicker.setVisibility(View.VISIBLE);
            VIEW_HOLDER.editTextSelectedDate.setText("");
            VIEW_HOLDER.editTextSelectedDate.setVisibility(View.GONE);

        } else {
            VIEW_HOLDER.dayPicker.setVisibility(View.GONE);
            VIEW_HOLDER.editTextSelectedDate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void verificarAMPM(int hourOfDay, int minute) {
        String ampm = "";

        if (hourOfDay == 12) {
            ampm = "PM";
        } else if (hourOfDay == 0) {
            ampm = "AM";
            hourOfDay = 12;
        } else if (hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
            ampm = "PM";
        } else if (hourOfDay < 12 && hourOfDay >= 1) {
            ampm = "AM";
        }

        VIEW_HOLDER.editTextAlarmTime.setText(String.format("%02d:%02d", hourOfDay, minute) + " " + ampm);
    }

    private void configurarAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.alarms_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VIEW_HOLDER.spinnerAlarm.setAdapter(adapter);
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, this,
                HORA_ATUAL, MINUTO_ATUAL, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this, ANO_ATUAL, MES_ATUAL, DIA_ATUAL);
        datePickerDialog.show();
    }

    private boolean dataValidation() {
        if (VIEW_HOLDER.editTextAlarmTime.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
            return false;
        } else if (mSpinnerText.equals("Dia específico")){
            return verificarDataSelecionada();
        }
        return true;
    }

    private boolean verificarDataSelecionada(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataAtual = sdf.format(new Date());

        try {
            if(sdf.parse(VIEW_HOLDER.editTextSelectedDate.getText().toString()).before(sdf.parse(dataAtual))) {
                Toast.makeText(this, "O dia inserido já passou", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void configurarAlarme(String hora, String minuto, String nomeRemedio) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR, Integer.parseInt(hora));
        intent.putExtra(AlarmClock.EXTRA_MINUTES, Integer.parseInt(minuto));
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Medialarme - " + nomeRemedio + "(" + user.getFirstName() + " " + user.getLastName() + ")");
        intent.putExtra(AlarmClock.EXTRA_DAYS, ALARM_DAYS);
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        startActivity(intent);
    }

    private void configurarDiasAlarme() {
        if (!ALARM_DAYS.isEmpty())
            ALARM_DAYS.clear();

        if (mSpinnerText.equals("Personalizado")) {
            if (VIEW_HOLDER.toggleButtonSunday.isChecked()) {
                ALARM_DAYS.add(Calendar.SUNDAY);
                diasDaSemana.add("Domingo");
            }
            if (VIEW_HOLDER.toggleButtonMonday.isChecked()) {
                ALARM_DAYS.add(Calendar.MONDAY);
                diasDaSemana.add("Segunda");
            }
            if (VIEW_HOLDER.toggleButtonTuesday.isChecked()) {
                ALARM_DAYS.add(Calendar.TUESDAY);
                diasDaSemana.add("Terça");
            }
            if (VIEW_HOLDER.toggleButtonWednesday.isChecked()) {
                ALARM_DAYS.add(Calendar.WEDNESDAY);
                diasDaSemana.add("Quarta");
            }
            if (VIEW_HOLDER.toggleButtonThursday.isChecked()) {
                ALARM_DAYS.add(Calendar.THURSDAY);
                diasDaSemana.add("Quinta");
            }
            if (VIEW_HOLDER.toggleButtonFriday.isChecked()) {
                ALARM_DAYS.add(Calendar.FRIDAY);
                diasDaSemana.add("Sexta");
            }
            if (VIEW_HOLDER.toggleButtonSaturday.isChecked()) {
                ALARM_DAYS.add(Calendar.SATURDAY);
                diasDaSemana.add("Sábado");
            }
        } else if (mSpinnerText.equals("Diário")) {
            ALARM_DAYS.add(Calendar.SUNDAY);
            ALARM_DAYS.add(Calendar.MONDAY);
            ALARM_DAYS.add(Calendar.TUESDAY);
            ALARM_DAYS.add(Calendar.WEDNESDAY);
            ALARM_DAYS.add(Calendar.THURSDAY);
            ALARM_DAYS.add(Calendar.FRIDAY);
            ALARM_DAYS.add(Calendar.SATURDAY);
        }
    }

    private void setSpinnerText(String text) {
        this.mSpinnerText = text;
    }

    private void setCalendar(Calendar calendar) {
        this.mCalendar = calendar;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int CORRETOR_MES = 1;

        mDiaSelecionado = Integer.toString(dayOfMonth);
        mMesSelecionado = Integer.toString(month + CORRETOR_MES);
        mAnoSelecionado = Integer.toString(year);

        String dataSelecionada = mDiaSelecionado + "/" + mMesSelecionado + "/" + mAnoSelecionado;

        VIEW_HOLDER.editTextSelectedDate.setText(dataSelecionada);

        mMesSelecionado = Integer.toString(month);
    }

    private static class ViewHolder {
        EditText editTextAlarmTime;
        EditText editTextSelectedDate;
        Spinner spinnerAlarm;
        Button saveAlarm;
        ToggleButton toggleButtonSunday;
        ToggleButton toggleButtonMonday;
        ToggleButton toggleButtonTuesday;
        ToggleButton toggleButtonWednesday;
        ToggleButton toggleButtonThursday;
        ToggleButton toggleButtonFriday;
        ToggleButton toggleButtonSaturday;
        EditText editTextComprimidosDose;
        EditText editTextGotasDose;
        EditText editTextModoUsoRemedio;
        TextView textViewComprimidosDose;
        TextView textViewGotasDose;
        TextView textViewModoUsoRemedio;

        View dayPicker;
    }
}