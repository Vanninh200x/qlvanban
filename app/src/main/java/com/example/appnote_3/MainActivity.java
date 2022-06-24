package com.example.appnote_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private final String DATABASE_NAME = "appNote.db";
    private SQLiteDatabase database;
    private ImageView imgV_menu, imgV_Plus, imgV_search, imageView_clock, imgV_padLock, imgV_notifi, imgV_trash, img_rightArrow1, img_rightArrow2, img_rightArrow3;
    private ImageView imgV_file, img_rightArrrow4;
    private EditText editText_Search;

    private ListView listView;
    private ArrayList<oneNote_class> list;
    private AdapterNote adapter;

    //    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private int id = -1;
    private static final String SHARE_PRE_NAME = "mypref";
    private static final String KEY_PASS = "passwd";
    private SharedPreferences sharedPreferences;
    private String passwd_App = "";


    private EditText editText_passswd_loginAC;
    private TextInputLayout textInputLayout;
    private Button button_login_loginAC;
    private static int CHECK_OPEN_MAINAC_ONETIME = 0;

    private RelativeLayout rlvMenuLeft, rlv_navigation_1, rlv_navigation_2, rlv_navigation_3, rlv_navigation_4;
    private TextView textViewTrangChu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = Database.initDatabase(this, DATABASE_NAME);
        sharedPreferences = getSharedPreferences(SHARE_PRE_NAME, MODE_PRIVATE);
        passwd_App = sharedPreferences.getString(KEY_PASS, "");
        if (passwd_App.length() > 0 && CHECK_OPEN_MAINAC_ONETIME == 0) {
            setContentView(R.layout.activity_login);
            initLogin();
            button_login_loginAC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String realPass = sharedPreferences.getString(KEY_PASS, "");
                    String passInput = editText_passswd_loginAC.getText().toString().trim();
                    if (passInput.equals(realPass)) {
                        CHECK_OPEN_MAINAC_ONETIME++;
                        Log.d("CHECK_KQ_1", CHECK_OPEN_MAINAC_ONETIME + "");
                        setContentView(R.layout.activity_main);
                        init();
                        read();
                        initClick();
                        Log.d("CHECK_PO", CHECK_OPEN_MAINAC_ONETIME + "");
                    } else {
                        textInputLayout.setErrorEnabled(false);
                        textInputLayout.setError("Sai mật khẩu");
                    }
                }
            });
        } else {
            setContentView(R.layout.activity_main);
            init();
            read();
            initClick();
        }
    }


    private void initClick() {
        imgV_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Add_Activity.class);
                startActivity(intent);
            }
        });

        imgV_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, Detail_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", list.get(i).getId() + "");
                bundle.putString("title", list.get(i).getTextV_title());
                bundle.putString("content", list.get(i).getTextV_content());
                bundle.putString("time", list.get(i).getTextV_time());
                bundle.putString("day", list.get(i).getTextV_day());
                bundle.putString("updatetime", list.get(i).getTextV_updateTime());
//
//
                bundle.putString("sohieuvb", list.get(i).getSoHieuVB());
                bundle.putString("loaivanban", list.get(i).getLoaivanban());
                bundle.putString("trangthaivb", list.get(i).getTrangthai());
                bundle.putString("coquanguinhan", list.get(i).getCoquan());
                bundle.putString("nguoiki", list.get(i).getNguoiki());
                bundle.putString("duongdan", list.get(i).getStringDuongDan());


//   sohieu       Log.d("UPDATE", list.get(i).getTextV_updateTime());

// Lỗi do không bundle được ảnh null. Đã fix
                if (list.get(i).getImg_font().length > 0) {
                    Bitmap b = BitmapFactory.decodeByteArray(list.get(i).getImg_font(), 0, list.get(i).getImg_font().length);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    b.compress(Bitmap.CompressFormat.JPEG, 50, bs);
                    bundle.putByteArray("img", bs.toByteArray());
                } else {
                    bundle.putByteArray("img", new byte[]{
                    });
                }

                intent.putExtra("dulieu", bundle);
                startActivity(intent);
            }
        });


        editText_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });


        rlv_navigation_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Editpasswd_Activity.class);
                startActivity(intent);
            }
        });

        rlv_navigation_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, rlv_navigation_2);
                popupMenu.getMenuInflater().inflate(R.menu.menu_thongke, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.id_thongke_theo_truong:
                                thongKe("KMA");
                                break;
                            case R.id.id_thongketheobo:
                                thongKe("BGD");
                                break;
                            case R.id.id_thongkevanbandi:
                                thongKeLoai("di", "đi");
                                break;
                            case R.id.id_thongkevanbanden:
                                thongKeLoai("den", "đên", "đến", "đen");
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        rlv_navigation_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.delete("ghichu", null, null);
                finish();
                startActivity(getIntent());
                Toast.makeText(MainActivity.this, "Xóa tất cả", Toast.LENGTH_SHORT).show();

            }
        });

        rlv_navigation_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.filterList(list);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    //    FILTER : SEARCH
    private void filter(String text) {
        ArrayList<oneNote_class> filterList = new ArrayList<>();
        for (oneNote_class oneNote : list) {
            if (oneNote.getTextV_title().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(oneNote);
            }
        }
        adapter.filterList(filterList);
    }

    private void thongKe(String text) {
        ArrayList<oneNote_class> thongkeKMA = new ArrayList<>();
        for (oneNote_class oneNote : list) {
            if (oneNote.getSoHieuVB().toLowerCase().contains(text.toLowerCase())) {
                thongkeKMA.add(oneNote);
            }
        }
        adapter.filterList(thongkeKMA);
    }


    private void thongKeLoai(String text, String text1) {
        ArrayList<oneNote_class> thongkeKMA = new ArrayList<>();
        for (oneNote_class oneNote : list) {
            if (oneNote.getTrangthai().toLowerCase().contains(text.toLowerCase())
                    || oneNote.getTrangthai().toLowerCase().contains(text1.toLowerCase())) {
                thongkeKMA.add(oneNote);
            }
        }
        adapter.filterList(thongkeKMA);
    }

    private void thongKeLoai(String text, String text1, String text3, String text2) {
        ArrayList<oneNote_class> thongkeKMA = new ArrayList<>();
        for (oneNote_class oneNote : list) {
            if (oneNote.getTrangthai().toLowerCase().contains(text.toLowerCase())
                    || oneNote.getTrangthai().toLowerCase().contains(text1.toLowerCase())
                    || oneNote.getTrangthai().toLowerCase().contains(text3.toLowerCase())
                    || oneNote.getTrangthai().toLowerCase().contains(text2.toLowerCase())) {
                thongkeKMA.add(oneNote);
            }
        }
        adapter.filterList(thongkeKMA);
    }

    private void initLogin() {
        editText_passswd_loginAC = findViewById(R.id.id_editT_passwd);
        button_login_loginAC = findViewById(R.id.id_button_login);
        textInputLayout = findViewById(R.id.id_textIPLayout_loginAC);
    }

    private void init() {
        imgV_menu = findViewById(R.id.id_ivMenu);
        imgV_Plus = findViewById(R.id.id_ivPlus);
        imgV_search = findViewById(R.id.id_imgV_main_search);
        imgV_padLock = findViewById(R.id.id_img_padLock);
        imgV_notifi = findViewById(R.id.id_img_notification);
        imgV_trash = findViewById(R.id.id_img_trash);
        imgV_file = findViewById(R.id.id_img_qly);
        img_rightArrrow4 = findViewById(R.id.id_imgV_right_arrow4);
        editText_Search = findViewById(R.id.id_edt_search);

        rlv_navigation_1 = findViewById(R.id.id_rela_layout_navigation_1);
        rlv_navigation_2 = findViewById(R.id.id_rela_layout_navigation_2);
        rlv_navigation_3 = findViewById(R.id.id_rela_layout_navigation_3);
        rlv_navigation_4 = findViewById(R.id.id_rela_layout_navigation_4);
        img_rightArrow1 = findViewById(R.id.id_imgV_right_arrow1);
        img_rightArrow2 = findViewById(R.id.id_imgV_right_arrow2);
        img_rightArrow3 = findViewById(R.id.id_imgV_right_arrow3);


        rlvMenuLeft = findViewById(R.id.layoutLeftMenu);
        textViewTrangChu = findViewById(R.id.id_textV_header_navigation);


        imgV_menu.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_Plus.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_search.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_IN);
        imgV_padLock.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_notifi.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_trash.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_file.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);

        img_rightArrow1.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_IN);
        img_rightArrow2.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_IN);
        img_rightArrow3.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_IN);
        img_rightArrrow4.setColorFilter(Color.parseColor("#999999"), PorterDuff.Mode.SRC_IN);

//
//

//
//        Init layoutDrawer
//        navigationView = findViewById(R.id.id_navigation);
        drawerLayout = findViewById(R.id.id_drawerlayout);
//        navigationView.setNavigationItemSelectedListener(this);
//        Change color icon list
//        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.parseColor("#686EFE")));


        listView = findViewById(R.id.id_list_View);
        list = new ArrayList<>();
        adapter = new AdapterNote(this, list);
        listView.setAdapter(adapter);


    }

    private void read() {
        Cursor cursor = database.rawQuery("SELECT*FROM ghichu ORDER BY id DESC", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            id = cursor.getInt(0);
            String title = cursor.getString(1);
            String content = cursor.getString(2);
            String day = cursor.getString(3);
            String time = cursor.getString(4);
            byte[] img = cursor.getBlob(5);
            String updateTime = cursor.getString(6);
            String sohieu = cursor.getString(7);
            String loaivb = cursor.getString(8);
            String trangthai = cursor.getString(9);
            String coquan = cursor.getString(10);
            String nguoiki = cursor.getString(11);
            String duongdan = cursor.getString(12);
            list.add(new oneNote_class(id, title, content, day, time, img, updateTime, sohieu, loaivb, trangthai, coquan, nguoiki, duongdan));
        }
        adapter.notifyDataSetChanged();
    }


}