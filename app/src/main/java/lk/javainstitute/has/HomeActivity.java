package lk.javainstitute.has;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        NavigationBarView navigationView = findViewById(R.id.bottomNavigationView);
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                if(item.getItemId() == R.id.home){
                    transaction.replace(R.id.mainFragmentContainerView, new HomeFragment()).commit();
                } else if (item.getItemId() == R.id.profile) {
                    transaction.replace(R.id.mainFragmentContainerView, new ProfileFragment()).commit();
                } else if (item.getItemId() == R.id.th){
                    transaction.replace(R.id.mainFragmentContainerView, new TransactionHistryFragment()).commit();
                } else if (item.getItemId() == R.id.setting) {
                    transaction.replace(R.id.mainFragmentContainerView, new SettingFragment()).commit();
                } else if (item.getItemId() == R.id.help) {
                    transaction.replace(R.id.mainFragmentContainerView, new HelpFragment()).commit();
                }
                return true;
            }
        });
    }
}