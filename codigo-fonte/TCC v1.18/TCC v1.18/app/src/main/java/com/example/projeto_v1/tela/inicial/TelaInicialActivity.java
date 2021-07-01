package com.example.projeto_v1.tela.inicial;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.projeto_v1.R;
import com.example.projeto_v1.dialog.LogoffAlertaDialog;
import com.example.projeto_v1.dialog.MedicineAlertaDialog;
import com.example.projeto_v1.dialog.UserAlertaDialog;
import com.example.projeto_v1.tela.LoginActivity;
import com.example.projeto_v1.tela.cadastro.CadastroRemedioActivity;
import com.example.projeto_v1.tela.cadastro.detail.UserDetailActivity;
import com.example.projeto_v1.tela.cadastro.model.UserDAO;
import com.example.projeto_v1.tela.inicial.fragmento.HistoryFragment;
import com.example.projeto_v1.tela.inicial.fragmento.HomeFragment;
import com.example.projeto_v1.tela.inicial.fragmento.PeopleFragment;
import com.example.projeto_v1.tela.cadastro.model.MedicineDAO;
import com.example.projeto_v1.tela.cadastro.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import io.realm.Realm;

public class TelaInicialActivity extends AppCompatActivity implements LogoffAlertaDialog.AlertaDialogListener, UserAlertaDialog.AlertaDialogListener {

    public static int sUserId;
    final private BottomNavigationView.OnNavigationItemSelectedListener mNavListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectedFragment = new HomeFragment();
                        break;
                    case R.id.nav_history:
                        selectedFragment = new HistoryFragment();
                        break;
                    case R.id.nav_people:
                        selectedFragment = new PeopleFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();

                return true;
            };
    private User mUser;
    private UserDAO mUserDAO;
    private ViewHolder mViewHolder = new ViewHolder();
    private Realm mRealm;
    private MedicineDAO mMedicineDAO;

    public int getUserId() {
        return sUserId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(mNavListener);

        mRealm = Realm.getDefaultInstance();
        Intent getIntent = getIntent();

        sUserId = getIntent.getIntExtra("userId", 0);
        mUser = mRealm.where(User.class).equalTo("id", sUserId).findFirst();

        mMedicineDAO = new MedicineDAO(mUser, null, null, mRealm);
        mMedicineDAO.selectFromDB(mUser);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_perfil, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_update_user){
            Intent intent = new Intent(this, UserDetailActivity.class);
            intent.putExtra("numPosition", mUser.getId());
            startActivity(intent);
        }else if(item.getItemId() == R.id.menu_logout_user){
            showLogOffAlertaDialog();
        }else if(item.getItemId() == R.id.menu_delete_user){
            showUserAlertaDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLogOffDialogPositiveClick(DialogFragment dialog) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLogOffDialogNegativeClick(DialogFragment dialog) {
    }

    @Override
    public void onUserDialogPositiveClick(DialogFragment dialog) {
        mUserDAO = new UserDAO(null, mUser, mRealm);
        mUserDAO.removeUser();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserDialogNegativeClick(DialogFragment dialog) {
    }

    private void showLogOffAlertaDialog() {
        LogoffAlertaDialog logOffDialog = new LogoffAlertaDialog();
        logOffDialog.setListener(this);
        logOffDialog.show(this.getSupportFragmentManager(), "AlertaDialog");
    }

    private void showUserAlertaDialog() {
        UserAlertaDialog userDialog = new UserAlertaDialog();
        userDialog.setListener(this);
        userDialog.show(this.getSupportFragmentManager(), "AlertaDialog");
    }

    private static class ViewHolder {
        ListView medicineListView;
    }
}
