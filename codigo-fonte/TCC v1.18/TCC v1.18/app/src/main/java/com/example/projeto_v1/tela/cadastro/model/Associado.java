package com.example.projeto_v1.tela.cadastro.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Associado extends RealmObject {
    @PrimaryKey
    private int idAssociado;
    @Required
    private String associadoFirstName;
    @Required
    private String associadoLastName;
    @Required
    private String associadoUserName;
    @Required
    private String associadoEmail;

    public int getIdAssociado() {
        return idAssociado;
    }

    public void setIdAssociado(int idAssociado) {
        this.idAssociado = idAssociado;
    }

    public String getAssociadoFirstName() {
        return associadoFirstName;
    }

    public void setAssociadoFirstName(String associadoFirstName) {
        this.associadoFirstName = associadoFirstName;
    }

    public String getAssociadoLastName() {
        return associadoLastName;
    }

    public void setAssociadoLastName(String associadoLastName) {
        this.associadoLastName = associadoLastName;
    }

    public String getAssociadoUserName() {
        return associadoUserName;
    }

    public void setAssociadoUserName(String associadoUserName) {
        this.associadoUserName = associadoUserName;
    }

    public String getAssociadoEmail() {
        return associadoEmail;
    }

    public void setAssociadoEmail(String associadoEmail) {
        this.associadoEmail = associadoEmail;
    }
}
