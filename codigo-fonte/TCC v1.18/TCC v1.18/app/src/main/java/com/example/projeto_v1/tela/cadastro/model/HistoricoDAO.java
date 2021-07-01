package com.example.projeto_v1.tela.cadastro.model;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;

public class HistoricoDAO {
    private User usuario;
    private Historico historico;
    private RealmList<Historico> historicos;
    private Realm realm;

    public HistoricoDAO(Historico historico, User usuario, Realm realm) {
        this.historico = historico;
        this.usuario = usuario;
        this.realm = realm;
    }

    public void createHistorico() {
        realm.beginTransaction();
        usuario.getHistoricos().add(historico);
        realm.commitTransaction();
    }

    public void removeHistorico() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                historico.deleteFromRealm();
            }
        });
    }

    public void updateHistorico(Historico novoHistorico) {
        realm.beginTransaction();

        historico.setStatusHistorico(novoHistorico.getStatusHistorico());
        historico.setDiaHistorico(novoHistorico.getDiaHistorico());
        historico.setIdAlarmeHistorico(novoHistorico.getIdAlarmeHistorico());
        historico.setIdRemedioHistorico(novoHistorico.getIdRemedioHistorico());

        realm.commitTransaction();
    }

    public void selectFromDB(User user) {
        historicos = user.getHistoricos();
    }

    public ArrayList<Historico> justRefresh() {
        ArrayList<Historico> listItem = new ArrayList<>();
        for (Historico h : historicos) {
            listItem.add(h);
        }
        return listItem;
    }
}
