package com.example.projeto_v1.tela.cadastro.model;

import androidx.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class User extends RealmObject{
    @PrimaryKey
    private int id;
    @Required
    private String firstName;
    @Required
    private String lastName;
    @Required
    private String userName;
    @Required
    private String password;
    @Required
    private String email;
    @Required
    private String telefone;
    @Nullable
    private RealmList<Medicine> remedios;
    @Nullable
    private RealmList<Associado> associados;
    @Nullable
    private RealmList<Historico> historicos;

    @Nullable
    public RealmList<Associado> getAssociados() {
        return associados;
    }

    public void setAssociados(@Nullable RealmList<Associado> associados) {
        this.associados = associados;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public RealmList<Medicine> getRemedios() {
        return remedios;
    }

    public void setRemedios(RealmList<Medicine> remedios) {
        this.remedios = remedios;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Nullable
    public RealmList<Historico> getHistoricos() {
        return historicos;
    }

    public void setHistoricos(@Nullable RealmList<Historico> historicos) {
        this.historicos = historicos;
    }
}
