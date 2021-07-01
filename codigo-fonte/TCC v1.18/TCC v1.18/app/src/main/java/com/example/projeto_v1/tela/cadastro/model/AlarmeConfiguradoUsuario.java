package com.example.projeto_v1.tela.cadastro.model;

import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

public class AlarmeConfiguradoUsuario {

    private Calendar calendar;
    private String mesSelecionado;
    private String diaSelecionado;
    private String anoSelecionado;
    private String horaSeleionada;
    private String minutoSelecionado;
    private ArrayList<Integer> alarmDays;

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getMesSelecionado() {
        return mesSelecionado;
    }

    public void setMesSelecionado(String mesSelecionado) {
        this.mesSelecionado = mesSelecionado;
    }

    public String getDiaSelecionado() {
        return diaSelecionado;
    }

    public void setDiaSelecionado(String diaSelecionado) {
        this.diaSelecionado = diaSelecionado;
    }

    public String getAnoSelecionado() {
        return anoSelecionado;
    }

    public void setAnoSelecionado(String anoSelecionado) {
        this.anoSelecionado = anoSelecionado;
    }

    public String getHoraSeleionada() {
        return horaSeleionada;
    }

    public void setHoraSeleionada(String horaSeleionada) {
        this.horaSeleionada = horaSeleionada;
    }

    public String getMinutoSelecionado() {
        return minutoSelecionado;
    }

    public void setMinutoSelecionado(String minutoSelecionado) {
        this.minutoSelecionado = minutoSelecionado;
    }

    public ArrayList<Integer> getAlarmDays() {
        return alarmDays;
    }

    public void setAlarmDays(ArrayList<Integer> alarmDays) {
        this.alarmDays = alarmDays;
    }
}
