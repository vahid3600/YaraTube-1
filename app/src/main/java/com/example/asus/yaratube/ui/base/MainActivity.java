package com.example.asus.yaratube.ui.base;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.asus.yaratube.R;
import com.example.asus.yaratube.data.UserRepository;
import com.example.asus.yaratube.data.local.AppDatabase;
import com.example.asus.yaratube.data.model.Category;
import com.example.asus.yaratube.data.model.Product;
import com.example.asus.yaratube.ui.developerinfo.AboutFragment;
import com.example.asus.yaratube.ui.developerinfo.ContactFragment;
import com.example.asus.yaratube.ui.home.BottomHolderFragment;
import com.example.asus.yaratube.ui.productdetail.ProductDetailFragment;
import com.example.asus.yaratube.ui.productlist.ProductListFragment;
import com.example.asus.yaratube.ui.profile.ProfileFragment;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements TransferBetweenFragments, DrawerLocker {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private UserRepository userRepository;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // change app direction to RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setDrawer();
        setBottomNavigationFragment();

        AppDatabase database = AppDatabase.getAppDatabase(this);
        userRepository = new UserRepository(this);
        userRepository.setDatabase(database);
    }

    public void setDrawer() {

        // drawer settings
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // click listener
        NavigationView navigationView = findViewById(R.id.drawer_nv);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()) {
                    case R.id.profile_nd:
                        if(userRepository.getUser()!= null)
                            Log.e("user in db", "onNavigationItemSelected: "+userRepository.getUser().getToken());
                        if(userRepository.isLogin()) // fixme do it by presenter
                            addFragment(ProfileFragment.newInstance());
                        else {
                            userRepository.login(getSupportFragmentManager());
                        }
                        return true;

                    case R.id.about_nd:

                        addFragment(AboutFragment.newInstance());
                        return true;

                    case R.id.contact_nd:
                        addFragment(ContactFragment.newInstance());
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    public void setBottomNavigationFragment() {

        BottomHolderFragment bottomHolderFragment = BottomHolderFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, bottomHolderFragment).commit();
    }

    // Open the drawer when the filled_button is tapped
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (actionBarDrawerToggle.onOptionsItemSelected(item)) || super.onOptionsItemSelected(item);
    }

    // close the drawer if opened when back filled_button tapped
    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void addFragment (Fragment fragment){

        String backStateName = fragment.getClass().getName();

        boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate (backStateName, 0);

        // if there is other option of drawer menu open, close it
        if(topFragment() instanceof  AboutFragment ||
                topFragment() instanceof ContactFragment)
            getSupportFragmentManager().popBackStack();

        //fragment not in back stack, create it
        if (!fragmentPopped){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.main_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    // this method is because of that removing a fragment, does not make the list size decrease (the fragment entry just turn to null)
    public Fragment topFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Fragment top = null;
        for (int i = fragments.size() -1; i>=0 ; i--) {
            top = fragments.get(i);
            if (top != null) {
                return top;
            }
        }
        return top;
    }

    @Override
    public void goFromCategoryToProductList(Category category) {

        ProductListFragment productListFragment = ProductListFragment.newInstance(category);
        getSupportFragmentManager().beginTransaction().addToBackStack(productListFragment.getClass().getName())
                .add(R.id.main_container, productListFragment).commit();
    }

    @Override
    public void goToProductDetail(Product product, String categoryName) {

        ProductDetailFragment productDetailFragment = ProductDetailFragment.newInstance(product, categoryName);
        getSupportFragmentManager().beginTransaction().addToBackStack(productDetailFragment.getClass().getName())
                .add(R.id.main_container, productDetailFragment).commit();
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {

        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawerLayout.setDrawerLockMode(lockMode);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(enabled);
    }
}
