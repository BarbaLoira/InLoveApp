package inlove.barbaloira.inlove;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;


import inlove.barbaloira.inlove.fragments.LoginFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbarBottomVisible(true);
        getSupportFragmentManager().beginTransaction().add(R.id.rl_content_main, new LoginFragment()).addToBackStack(null).commit();

      /*  BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bnv_navigation);
       if(bottomNavigationView != null) {
           bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
               @Override
               public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                   switch (item.getItemId()) {

                   }
                   return false;
               }
           });
       }*/

    }

    public void setToolbarBottomVisible(boolean visible) {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bnv_navigation);
        if (visible) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        } else {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }
}
