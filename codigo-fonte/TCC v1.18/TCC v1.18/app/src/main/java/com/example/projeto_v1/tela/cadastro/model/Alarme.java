package com.example.projeto_v1.tela.cadastro.model;

import androidx.annotation.Nullable;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Alarme extends RealmObject {
    @PrimaryKey
    private int idAlarme;
    @Required
    private String horario, tipo;
    @Required
    private RealmList<Integer> alarmeRequestCode, questionRequestCode, alarmeClockRequestCode;
    @Nullable
    private RealmList<String> diasDaSemana;
    @Nullable
    private Date diaEspecifico;
    @Nullable
    private int notificationId, comprimidosDose, gotasDose;
    @Nullable
    private String modoUsoRemedio;
    private Medicine remedio;

    public Medicine getRemedio() {
        return remedio;
    }

    public void setRemedio(Medicine remedio) {
        this.remedio = remedio;
    }

    public int getIdAlarme() {
        return idAlarme;
    }

    public void setIdAlarme(int idAlarme) {
        this.idAlarme = idAlarme;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Nullable
    public RealmList<String> getDiasDaSemana() {
        return diasDaSemana;
    }

    public void setDiasDaSemana(@Nullable RealmList<String> diasDaSemana) {
        this.diasDaSemana = diasDaSemana;
    }

    @Nullable
    public Date getDiaEspecifico() {
        return diaEspecifico;
    }

    public void setDiaEspecifico(@Nullable Date diaEspecifico) {
        this.diaEspecifico = diaEspecifico;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public RealmList<Integer> getAlarmeRequestCode() {
        return alarmeRequestCode;
    }

    public void setAlarmeRequestCode(RealmList<Integer> alarmeRequestCode) {
        this.alarmeRequestCode = alarmeRequestCode;
    }

    public RealmList<Integer> getQuestionRequestCode() {
        return questionRequestCode;
    }

    public void setQuestionRequestCode(RealmList<Integer> questionRequestCode) {
        this.questionRequestCode = questionRequestCode;
    }

    public RealmList<Integer> getAlarmeClockRequestCode() {
        return alarmeClockRequestCode;
    }

    public void setAlarmeClockRequestCode(RealmList<Integer> alarmeClockRequestCode) {
        this.alarmeClockRequestCode = alarmeClockRequestCode;
    }

    public int getComprimidosDose() {
        return comprimidosDose;
    }

    public void setComprimidosDose(int comprimidosDose) {
        this.comprimidosDose = comprimidosDose;
    }

    public int getGotasDose() {
        return gotasDose;
    }

    public void setGotasDose(int gotasDose) {
        this.gotasDose = gotasDose;
    }

    public String getModoUsoRemedio() {
        return modoUsoRemedio;
    }

    public void setModoUsoRemedio(String modoUsoRemedio) {
        this.modoUsoRemedio = modoUsoRemedio;
    }
}
