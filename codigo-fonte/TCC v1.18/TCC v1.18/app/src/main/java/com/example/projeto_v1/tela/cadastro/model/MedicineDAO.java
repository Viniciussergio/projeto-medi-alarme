package com.example.projeto_v1.tela.cadastro.model;

import android.content.Context;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MedicineDAO {
    private User usuario;
    private Medicine remedio;
    private Medicine novoRemedio;
    private RealmList<Medicine> remedios;
    private Realm realm;

    public MedicineDAO(User usuario, Medicine novoRemedio, Medicine remedio, Realm realm) {
        this.usuario = usuario;
        this.novoRemedio = novoRemedio;
        this.remedio = remedio;
        this.realm = realm;
    }

    public void createMedicine() {
        realm.beginTransaction();
        usuario.getRemedios().add(remedio);
        realm.commitTransaction();
    }

    public void removeMedicine(Context context) {
        removeAlarmes(remedio.getAlarmes(), context);
        removeHistoricos(realm.where(Historico.class).equalTo("idRemedioHistorico", remedio.getIdRemedio()).findAll());

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                remedio.deleteFromRealm();
            }
        });
    }

    public void removeHistoricos(RealmResults<Historico> historicos){

        while(historicos.size() > 0){
            realm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm) {
                    historicos.get(historicos.size()-1).deleteFromRealm();
                }
            });
        }
    }

    public void removeAlarmes(RealmList<Alarme> alarmes, Context context){
        AlarmeDAO alarmeDAO = new AlarmeDAO(null, null, null);

        while(alarmes.size() > 0){
            Alarme alarme = alarmes.get(alarmes.size()-1);
            alarmeDAO.removeNotifications(alarme, context);
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    alarme.deleteFromRealm();
                }
            });
        }
    }

    public void updateMedicine() {
        realm.beginTransaction();

        remedio.setNomeRemedio(novoRemedio.getNomeRemedio());
        remedio.setTipoRemedio(novoRemedio.getTipoRemedio());
        remedio.setNumComprimidosRemedio(novoRemedio.getNumComprimidosRemedio());
        remedio.setNumMililitrosRemedio(novoRemedio.getNumMililitrosRemedio());

        remedio.setDataValidadeRemedio(novoRemedio.getDataValidadeRemedio());
        remedio.setDataInicialRemedio(novoRemedio.getDataInicialRemedio());

        realm.commitTransaction();
    }

    public void selectFromDB(User user) {
        remedios = user.getRemedios();
    }

    public ArrayList<Medicine> justRefresh() {
        ArrayList<Medicine> listItem = new ArrayList<>();
        if(!remedios.isValid()){
            return listItem;
        }else{
            for (Medicine m : remedios) {
                listItem.add(m);
            }
        }
        return listItem;
    }
}
