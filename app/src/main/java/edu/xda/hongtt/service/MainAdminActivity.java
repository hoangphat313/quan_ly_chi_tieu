package edu.xda.hongtt.service;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import edu.xda.hongtt.R;
import edu.xda.hongtt.data.MyDatabaseHelper;
import edu.xda.hongtt.fragment.ChatFragment;
import edu.xda.hongtt.fragment.KhoLuuTruFragment;
import edu.xda.hongtt.fragment.ThanhVienFragment;
import edu.xda.hongtt.fragment.ThongKeFragment;


public class MainAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    private NavigationView navigationView;
    private TextView txtTenDangNhap;
    String user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initEvents(toolbar);
        hienThiTen();
    }
    private void initEvents(Toolbar toolbar) {
        drawer = findViewById(R.id.drawer_admin_layout);
        navigationView = findViewById(R.id.nav_admin_view);
        txtTenDangNhap = navigationView.getHeaderView(0).findViewById(R.id.txtTenDangNhap);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_ThongKe);
        loadFragment(new ThongKeFragment());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("tendangnhap");
        }
    }
    private void hienThiTen(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String tenDangNhap = sharedPreferences.getString("username", "null");
        txtTenDangNhap.setVisibility(View.VISIBLE);
        txtTenDangNhap.setText(tenDangNhap);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_admin_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_ThongKe) {
            loadFragment(new ThongKeFragment());
        } else if (id == R.id.nav_Thoat) {
            thongBaoCloseApp();
        }
        else if(id == R.id.nav_DangXuat){
            thongBaoDangXuat();
        }
        else if (id == R.id.nav_chat){
            loadFragment(new ChatFragment());
        }
        else if (id == R.id.nav_DangKy){
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_su_kien){
            Intent intent = new Intent(this,SuKienActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_luu_tru){
            loadFragment(new KhoLuuTruFragment());
        }
        else if(id == R.id.nav_ThanhVien){
            loadFragment(new ThanhVienFragment());
        }
        else if(id==R.id.nav_DSThanhVien){
            Intent intent = new Intent(this,DanhSachCacThanhVienActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_admin_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void thongBaoCloseApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainAdminActivity.this);
        builder.setMessage("Bạn có muốn thoát ứng dụng?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Vâng",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        String tenDangNhap = sharedPreferences.getString("username",null);
                        MyDatabaseHelper db = new MyDatabaseHelper(MainAdminActivity.this);
                        db.GetDate("UPDATE CHAT SET TrangThai = 0 WHERE TenDangNhap='"+tenDangNhap +"'");
                        finish();
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(
                "Không",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void thongBaoDangXuat(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainAdminActivity.this);
        builder.setMessage("Bạn có muốn đăng xuất ứng dụng?");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Vâng",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                        String tenDangNhap = sharedPreferences.getString("username",null);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        MyDatabaseHelper db = new MyDatabaseHelper(MainAdminActivity.this);
                        db.GetDate("UPDATE NguoiDung SET TrangThai = 0 WHERE TenDangNhap='"+tenDangNhap +"'");
                        Intent intent = new Intent(MainAdminActivity.this , LoginActivity.class);
                        startActivity(intent);
                        dialog.cancel();
                    }
                });
        builder.setNegativeButton(
                "Không",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            thongBaoCloseApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}