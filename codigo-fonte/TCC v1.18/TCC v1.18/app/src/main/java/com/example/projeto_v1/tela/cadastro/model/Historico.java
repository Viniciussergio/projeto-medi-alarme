package com.example.projeto_v1.tela.cadastro.model;

import androidx.annotation.Nullable;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Historico extends RealmObject {
    @PrimaryKey
    private int idHistorico;
    private int idAlarmeHistorico;
    private int idRemedioHistorico;
    private String statusHistorico;
    private String diaHistorico;

    public int getIdHistorico() {
        return idHistorico;
    }

    public void setIdHistorico(int idHistorico) {
        this.idHistorico = idHistorico;
    }

    @Nullable
    public String getStatusHistorico() {
        return statusHistorico;
    }

    public void setStatusHistorico(@Nullable String statusHistorico) {
        this.statusHistorico = statusHistorico;
    }

    public String getDiaHistorico() {
        return diaHistorico;
    }

    public void setDiaHistorico(String diaHistorico) {
        this.diaHistorico = diaHistorico;
    }

    public int getIdAlarmeHistorico() {
        return idAlarmeHistorico;
    }

    public void setIdAlarmeHistorico(int idAlarmeHistorico) {
        this.idAlarmeHistorico = idAlarmeHistorico;
    }

    public int getIdRemedioHistorico() {
        return idRemedioHistorico;
    }

    public void setIdRemedioHistorico(int idRemedioHistorico) {
        this.idRemedioHistorico = idRemedioHistorico;
    }
}
