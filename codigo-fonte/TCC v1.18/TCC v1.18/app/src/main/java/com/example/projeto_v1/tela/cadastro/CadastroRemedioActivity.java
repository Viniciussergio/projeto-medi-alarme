package com.example.projeto_v1.tela.cadastro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_v1.R;
import com.example.projeto_v1.notification.GerenciarNotificacao;
import com.example.projeto_v1.tela.cadastro.model.Medicine;
import com.example.projeto_v1.tela.cadastro.model.MedicineDAO;
import com.example.projeto_v1.tela.cadastro.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;

public class CadastroRemedioActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    private final int CODE_EXPIRATION = 1;
    private final int CODE_INITIAL = 2;
    private final int DIA_ATUAL = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    private final int MES_ATUAL = Calendar.getInstance().get(Calendar.MONTH);
    private final int ANO_ATUAL = Calendar.getInstance().get(Calendar.YEAR);
    private final Date DATA_ATUAL = new Date();
    private final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    private final ViewHolder VIEW_HOLDER = new ViewHolder();
    private int mCodigo;
    private Realm mRealm;
    private Medicine mRemedio;
    private User mUser;
    private MedicineDAO mMedicineDAO;
    private String spinnerText;
    private String mDataSelecionada;
    public int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_remedio);
        getSupportActionBar().setTitle("Remédio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.VIEW_HOLDER.buttonSave = findViewById(R.id.button_save_medicine);

        this.VIEW_HOLDER.textViewMedicineCapsules = findViewById(R.id.text_view_medicine_capsules);
        this.VIEW_HOLDER.textViewMedicineMililitros = findViewById(R.id.text_view_medicine_mililitros);

        this.VIEW_HOLDER.editTextMedicineName = findViewById(R.id.edit_text_medicine_name);
        this.VIEW_HOLDER.editTextExpirationDate = findViewById(R.id.edit_text_expiration_date);
        this.VIEW_HOLDER.editTextInitialDate = findViewById(R.id.edit_text_initial_date);
        this.VIEW_HOLDER.spinnerHowToUse = findViewById(R.id.spinner_text_how_to_use);
        this.VIEW_HOLDER.editTextMedicineCapsules = findViewById(R.id.edit_text_medicine_capsules);
        this.VIEW_HOLDER.editTextMedicineMililitros = findViewById(R.id.edit_text_medicine_mililitros);

        VIEW_HOLDER.editTextExpirationDate.setOnClickListener(this);
        VIEW_HOLDER.editTextInitialDate.setOnClickListener(this);
        VIEW_HOLDER.buttonSave.setOnClickListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opcoes_uso, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.VIEW_HOLDER.spinnerHowToUse.setAdapter(adapter);
        this.VIEW_HOLDER.spinnerHowToUse.setOnItemSelectedListener(this);

        mRealm = Realm.getDefaultInstance();

        Intent getIntent = getIntent();
        userId = getIntent.getIntExtra("userId", 0);

        mUser = mRealm.where(User.class).equalTo("id", userId).findFirst();
    }

    public void setAlarmManager(){
        String dataValidade = VIEW_HOLDER.editTextExpirationDate.getText().toString();
        Calendar calendarDataValidade = Calendar.getInstance();
        try {
            calendarDataValidade.setTime(SDF.parse(dataValidade));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendarDataValidade.set(Calendar.HOUR_OF_DAY, 12);
        calendarDataValidade.set(Calendar.MINUTE, 0);
        calendarDataValidade.set(Calendar.SECOND, 0);
        calendarDataValidade.set(Calendar.MILLISECOND, 0);

        GerenciarNotificacao gerenciarNotificacao = new GerenciarNotificacao(this, getRemedio(),
                null, null);

        gerenciarNotificacao.configurarNotificacaoSistema(calendarDataValidade, mRemedio.getIdRemedio());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_save_medicine) {
            if (dataValidation()) {
                mMedicineDAO = new MedicineDAO(mUser, null, getRemedio(), mRealm);
                mMedicineDAO.createMedicine();
                setAlarmManager();
                finish();
            }
        } else if (v.getId() == R.id.edit_text_expiration_date) {
            showDatePickerDialog(CODE_EXPIRATION);
        } else if (v.getId() == R.id.edit_text_initial_date) {
            showDatePickerDialog(CODE_INITIAL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Medicine getRemedio() {
        mRemedio = new Medicine();

        Number maxId = mRealm.where(Medicine.class).max("idRemedio");
        int newKey = (maxId == null) ? 1 : maxId.intValue() + 1;

        mRemedio.setIdRemedio(newKey);
        mRemedio.setNomeRemedio(VIEW_HOLDER.editTextMedicineName.getText().toString());
        mRemedio.setTipoRemedio(spinnerText);
        mRemedio.setUsuario(mUser);

        if (spinnerText.equals("Comprimido")) {
            mRemedio.setNumComprimidosRemedio(Integer.parseInt(VIEW_HOLDER.editTextMedicineCapsules.getText().toString()));
        }else if (spinnerText.equals("Gotas")) {
            mRemedio.setNumMililitrosRemedio(Integer.parseInt(VIEW_HOLDER.editTextMedicineMililitros.getText().toString()));
        }else if (spinnerText.equals("Outro")) {
        }
        try {
            mRemedio.setDataValidadeRemedio(SDF.parse(VIEW_HOLDER.editTextExpirationDate.getText().toString()));
            mRemedio.setDataInicialRemedio(SDF.parse(VIEW_HOLDER.editTextInitialDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return mRemedio;
    }

    private boolean dataValidation() {
        boolean dataSelecionadaValida;
        if (spinnerText.equals("Comprimido")) {
            if (VIEW_HOLDER.editTextMedicineName.getText().toString().isEmpty() ||
                    VIEW_HOLDER.editTextExpirationDate.getText().toString().isEmpty() ||
                    VIEW_HOLDER.editTextInitialDate.getText().toString().isEmpty() ||
                    VIEW_HOLDER.editTextMedicineCapsules.getText().toString().isEmpty()) {
                Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else if (spinnerText.equals("Gotas")) {
            if (VIEW_HOLDER.editTextMedicineName.getText().toString().isEmpty() ||
                    VIEW_HOLDER.editTextExpirationDate.getText().toString().isEmpty() ||
                    VIEW_HOLDER.editTextInitialDate.getText().toString().isEmpty() ||
                    VIEW_HOLDER.editTextMedicineMililitros.getText().toString().isEmpty()) {
                Toast.makeText(this, "Todos os campos devem ser preenchidos", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        dataSelecionadaValida = verificarDataSelecionada();
        return dataSelecionadaValida;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if (text.equals("")) {
            removerEditText();
            spinnerText = text;
        }
        if (text.equals("Comprimido")) {
            this.VIEW_HOLDER.editTextMedicineMililitros.setText("");
            this.VIEW_HOLDER.textViewMedicineMililitros.setVisibility(View.GONE);
            this.VIEW_HOLDER.editTextMedicineMililitros.setVisibility(View.GONE);

            this.VIEW_HOLDER.textViewMedicineCapsules.setVisibility(View.VISIBLE);
            this.VIEW_HOLDER.editTextMedicineCapsules.setVisibility(View.VISIBLE);

            spinnerText = text;
        }else if (text.equals("Gotas")) {
            this.VIEW_HOLDER.editTextMedicineCapsules.setText("");
            this.VIEW_HOLDER.textViewMedicineCapsules.setVisibility(View.GONE);
            this.VIEW_HOLDER.editTextMedicineCapsules.setVisibility(View.GONE);

            this.VIEW_HOLDER.textViewMedicineMililitros.setVisibility(View.VISIBLE);
            this.VIEW_HOLDER.editTextMedicineMililitros.setVisibility(View.VISIBLE);

            spinnerText = text;
        }else if (text.equals("Outro")) {
            this.VIEW_HOLDER.editTextMedicineCapsules.setText("");
            this.VIEW_HOLDER.textViewMedicineCapsules.setVisibility(View.GONE);
            this.VIEW_HOLDER.editTextMedicineCapsules.setVisibility(View.GONE);

            this.VIEW_HOLDER.editTextMedicineMililitros.setText("");
            this.VIEW_HOLDER.textViewMedicineMililitros.setVisibility(View.GONE);
            this.VIEW_HOLDER.editTextMedicineMililitros.setVisibility(View.GONE);

            spinnerText = text;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void removerEditText() {
        this.VIEW_HOLDER.textViewMedicineCapsules.setVisibility(View.GONE);
        this.VIEW_HOLDER.editTextMedicineCapsules.setVisibility(View.GONE);
        this.VIEW_HOLDER.textViewMedicineMililitros.setVisibility(View.GONE);
        this.VIEW_HOLDER.editTextMedicineMililitros.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        VIEW_HOLDER.editTextMedicineName.setText("");
        VIEW_HOLDER.editTextExpirationDate.setText("");
        VIEW_HOLDER.editTextInitialDate.setText("");
        VIEW_HOLDER.editTextMedicineCapsules.setText("");
        VIEW_HOLDER.editTextMedicineMililitros.setText("");

        super.onBackPressed();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        int CORRETOR_MES = 1;

        String diaSelecionado = Integer.toString(dayOfMonth);
        String mesSelecionado = Integer.toString(month + CORRETOR_MES);
        String anoSelecionado = Integer.toString(year);
        mDataSelecionada = diaSelecionado + "/" + mesSelecionado + "/" + anoSelecionado;
        preencherCampo();
    }

    private void showDatePickerDialog(int codigo) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this, ANO_ATUAL, MES_ATUAL, DIA_ATUAL);
        mCodigo = codigo;
        datePickerDialog.show();
    }

    private void preencherCampo() {
        if (mCodigo == CODE_EXPIRATION) {
            VIEW_HOLDER.editTextExpirationDate.setText(mDataSelecionada);
        } else if (mCodigo == CODE_INITIAL) {
            VIEW_HOLDER.editTextInitialDate.setText(mDataSelecionada);
        }
    }

    private boolean verificarDataSelecionada() {
        try {
            String DATA_ATUAL_FORMATADA = SDF.format(DATA_ATUAL);
            if (SDF.parse(VIEW_HOLDER.editTextExpirationDate.getText().toString())
                    .before(SDF.parse(DATA_ATUAL_FORMATADA))) {
                Toast.makeText(this,
                        "O medicamento está vencido",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    private static class ViewHolder {
        Button buttonSave;
        EditText editTextExpirationDate;
        EditText editTextInitialDate;
        EditText editTextMedicineCapsules;
        EditText editTextMedicineMililitros;
        EditText editTextMedicineName;
        Spinner spinnerHowToUse;
        TextView textViewMedicineCapsules;
        TextView textViewMedicineMililitros;
    }
}
