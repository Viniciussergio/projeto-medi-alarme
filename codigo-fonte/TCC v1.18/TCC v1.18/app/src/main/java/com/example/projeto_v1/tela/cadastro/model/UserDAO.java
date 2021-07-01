package com.example.projeto_v1.tela.cadastro.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.realm.Realm;
import io.realm.RealmResults;

public class UserDAO {
    private Context context;
    private User user;
    private User newUser;
    private User usuario;
    private RealmResults<User> usuarios;
    private Realm realm;

    public UserDAO(User newUser, User user, Realm realm) {
        this.newUser = newUser;
        this.user = user;
        this.realm = realm;
    }

    public void createUser() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Number maxId = bgRealm.where(User.class).max("id");
                int newKey = (maxId == null) ? 1 : maxId.intValue() + 1;

                usuario = bgRealm.createObject(User.class, newKey);

                usuario.setEmail(user.getEmail());
                usuario.setFirstName(user.getFirstName());
                usuario.setLastName(user.getLastName());
                usuario.setUserName(user.getUserName());
                usuario.setTelefone(user.getTelefone());

                String cryptPassword = BCrypt.withDefaults().hashToString(12, user.getPassword().toCharArray());
                usuario.setPassword(cryptPassword);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
            }
        });
    }

    public void removeUser() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                user.deleteFromRealm();
            }
        });
    }

    public void updateUser() {
        realm.beginTransaction();
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setUserName(newUser.getUserName());
        user.setTelefone(newUser.getTelefone());
        user.setEmail(newUser.getEmail());
        realm.commitTransaction();
    }

    public void updatePassword(String newPassword) {
        realm.beginTransaction();
        user.setPassword(newPassword);
        realm.commitTransaction();
    }

    public void selectFromDB() {
        usuarios = realm.where(User.class).findAll();
    }

    public ArrayList<User> justRefresh() {
        ArrayList<User> listItem = new ArrayList<>();
        for (User p : usuarios) {
            listItem.add(p);
        }
        return listItem;
    }

    public boolean verificarSenha(User user, String password){
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if(result.verified){
            Log.v("Password", "Senhas iguais");
            return true;
        }else{
            Log.v("Password", "Senhas diferentes");
            return false;
        }
    }
}
