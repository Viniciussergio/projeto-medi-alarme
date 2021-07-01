package com.example.projeto_v1.tela.cadastro.model;

import java.util.Date;

import javax.annotation.Nullable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Medicine extends RealmObject {
    @PrimaryKey
    private int idRemedio;
    @Required
    private String nomeRemedio;
    @Required
    private Date dataValidadeRemedio;
    @Required
    private Date dataInicialRemedio;
    @Required
    private String tipoRemedio;
    private User usuario;

    //Comprimido
    @Nullable
    private int numComprimidosRemedio;

    //Gotas
    @Nullable
    private int numMililitrosRemedio;

    @Nullable
    private RealmList<Alarme> alarmes;

    @Nullable
    public RealmList<Alarme> getAlarmes() {
        return alarmes;
    }

    public int getIdRemedio() {
        return idRemedio;
    }

    public void setIdRemedio(int idRemedio) {
        this.idRemedio = idRemedio;
    }

    public String getNomeRemedio() {
        return nomeRemedio;
    }

    public void setNomeRemedio(String nomeRemedio) {
        this.nomeRemedio = nomeRemedio;
    }

    public Date getDataValidadeRemedio() {
        return dataValidadeRemedio;
    }

    public void setDataValidadeRemedio(Date dataValidadeRemedio) {
        this.dataValidadeRemedio = dataValidadeRemedio;
    }

    public Date getDataInicialRemedio() {
        return dataInicialRemedio;
    }

    public void setDataInicialRemedio(Date dataInicialRemedio) {
        this.dataInicialRemedio = dataInicialRemedio;
    }

    public String getTipoRemedio() {
        return tipoRemedio;
    }

    public void setTipoRemedio(String tipoRemedio) {
        this.tipoRemedio = tipoRemedio;
    }

    public int getNumComprimidosRemedio() {
        return numComprimidosRemedio;
    }

    public void setNumComprimidosRemedio(int numComprimidosRemedio) {
        this.numComprimidosRemedio = numComprimidosRemedio;
    }

    public int getNumMililitrosRemedio() {
        return numMililitrosRemedio;
    }

    public void setNumMililitrosRemedio(int numMililitrosRemedio) {
        this.numMililitrosRemedio = numMililitrosRemedio;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}
