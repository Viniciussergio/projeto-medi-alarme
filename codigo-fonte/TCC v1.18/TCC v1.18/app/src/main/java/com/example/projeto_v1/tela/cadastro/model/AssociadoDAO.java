package com.example.projeto_v1.tela.cadastro.model;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class AssociadoDAO {
    private User usuario;
    private Associado associado;
    private Associado novoAssociado;
    private RealmList<Associado> associados;
    private Realm realm;

    public AssociadoDAO(User usuario, Associado novoAssociado, Associado associado, Realm realm) {
        this.usuario = usuario;
        this.novoAssociado = novoAssociado;
        this.associado = associado;
        this.realm = realm;
    }

    public void createAssociado() {
        realm.beginTransaction();
        usuario.getAssociados().add(associado);
        realm.commitTransaction();
    }

    public void removeAssociado() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                associado.deleteFromRealm();
            }
        });
    }

    public void updateAssociado() {
        realm.beginTransaction();

        associado.setAssociadoFirstName(novoAssociado.getAssociadoFirstName());
        associado.setAssociadoLastName(novoAssociado.getAssociadoLastName());
        associado.setAssociadoUserName(novoAssociado.getAssociadoUserName());
        associado.setAssociadoEmail(novoAssociado.getAssociadoEmail());

        realm.commitTransaction();
    }

    public void selectFromDB(User user) {
        associados = user.getAssociados();
    }

    public ArrayList<Associado> justRefresh() {
        ArrayList<Associado> listItem = new ArrayList<>();
        if(associados.isValid()){
            for (Associado a : associados) {
                listItem.add(a);
            }
        }else {
            return listItem;
        }
        return listItem;
    }

    public ArrayList<String> getAllEmails(RealmResults<Associado> associadosResult){
        ArrayList<String> allEmails = new ArrayList<String>();
        for(Associado a: associadosResult){
            allEmails.add(a.getAssociadoEmail());
        }
        return allEmails;
    }

    //associadoDAO.selectFromDB
    //ArrayList<String> allEmails = associadoDAO.getAllEmails(associadoDAO.justRefresh);
}
