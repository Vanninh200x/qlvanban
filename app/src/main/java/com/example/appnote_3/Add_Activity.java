package com.example.appnote_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import Alarm.AlarmBrodcast;

public class Add_Activity extends AppCompatActivity {
    final String DATABASE_NAME = "appNote.db";
    private ImageView imgV_back, imgV_done, imgV_img_add, img_icon_check;
    private EditText editText_title, editText_content, editText_time;
    private Button button_date, button_time;
    private final int REQUEST_CHOOSE_PHOTO = 123;
    private DatePickerDialog datePickerDialog;
    private int hour, minute;
    private Uri uri = null;

    private boolean flag = false;
    private boolean flag2 = false;

    private Calendar calendar = Calendar.getInstance();
    private final int year = calendar.get(Calendar.YEAR);
    private final int month = calendar.get(Calendar.MONTH);
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private SQLiteDatabase database;

    private String timeTonotify;
    private String DayAlert = "";
    private String TimeAlert;

    private byte[] img;
    private char aChar;
    private Bitmap bitmap_1;
    private String dateandtime_1 = "";

    //
//
    private EditText textViewsoHieuVB, textViewloaiVB, textViewtrangthaiVB, textViewcoquanNhanGui, textViewnguoiKy;
    private TextView textViewdinhkemFile;
    private ImageView imageViewDinhkem;
    private int PICKFILE_RESULT_CODE = 1;

    private int MODE = 0;
    private String URIString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = Database.initDatabase(this, "appNote.db");
        setContentView(R.layout.activity_add);

        init();

        initDataPicker();

        imgV_img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

//DONE
        imgV_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
//                processInsert(title, content,day, time);
            }
        });

//        BTN_CHECK TO show Button
        img_icon_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag) {
                    img_icon_check.setImageResource(R.drawable.ic_checkbox_checked);
                    button_date.setVisibility(View.VISIBLE);
                    button_time.setVisibility(View.VISIBLE);
//                    button_date.setText(getTodayDate());
//                    button_time.setText(getCurrentTime1());
                    flag = true;
                } else {
                    img_icon_check.setImageResource(R.drawable.ic_checkbox_uncheck);
                    button_date.setVisibility(View.GONE);
                    button_time.setVisibility(View.GONE);
                    flag = false;
                }
            }
        });

        imageViewDinhkem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!flag2) {
                    imageViewDinhkem.setImageResource(R.drawable.ic_checkbox_checked);
                    Intent intent = new Intent();
                    intent.setType("file/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra("return-data", true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICKFILE_RESULT_CODE);
                    MODE = 2;
                    flag2 = true;
                } else {
                    imageViewDinhkem.setImageResource(R.drawable.ic_checkbox_uncheck);
                    flag2 = false;
                }
            }
        });


        imgV_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });

        button_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                openDatePicker(view);
                selectDate();
            }
        });


    }


    //        Insert
    private void insert() {
        if (isEmpty()) {
            Toast.makeText(this, getString(R.string.addFail), Toast.LENGTH_SHORT).show();
        } else {
            String title = editText_title.getText().toString();
            String content = editText_content.getText().toString();
            String time = button_time.getText().toString();
            String day = button_date.getText().toString();
            String updatetime = getTodayDate() + ". " + getCurrentTime();

//
            String soHieuVB = textViewsoHieuVB.getText().toString();
            String loaiVB = textViewloaiVB.getText().toString();
            String trangthaiVB = textViewtrangthaiVB.getText().toString();
            String coQuan = textViewcoquanNhanGui.getText().toString();
            String nguoiKy = textViewnguoiKy.getText().toString();
            String duongdan = URIString;


            ContentValues contentValues = new ContentValues();
            //PUT dữ liệu
//            contentValues.put("id",id);
            contentValues.put("title", title);
            contentValues.put("content", content);
            contentValues.put("day", day);
            contentValues.put("time", time);
            contentValues.put("updatetime", updatetime);
            contentValues.put("sohieuvb", soHieuVB);
            contentValues.put("loaivanban", loaiVB);
            contentValues.put("trangthaivb", trangthaiVB);
            contentValues.put("coquanguinhan", coQuan);
            contentValues.put("nguoiki", nguoiKy);
            contentValues.put("duongdan",duongdan);

            if (uri != null) {
                img = getByteArrayFromImageView(imgV_img_add);
                contentValues.put("img", img);
                database.insert("ghichu", null, contentValues);
                setAlarm(title, content, DayAlert, TimeAlert, img);
            } else {
                aChar = title.charAt(0);
                TextView textView_aChar = findViewById(R.id.id_textView_wImg_Null);
                textView_aChar.setText(String.valueOf(aChar));
//                KO duoc giao tiep voi giao dien
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout layout = (RelativeLayout) findViewById(R.id.id_img_null_add);
                        layout.setDrawingCacheEnabled(true);
                        bitmap_1 = Bitmap.createBitmap(layout.getDrawingCache());
                        layout.setDrawingCacheEnabled(false);
                        //CONVERT BITMAP TO BYTE
//                        Log.e("CHECK_achar", aChar +"");

//                        THAO tac voi giao dien
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ByteArrayOutputStream BoutputStream = new ByteArrayOutputStream();
                                bitmap_1.compress(Bitmap.CompressFormat.JPEG, 50, BoutputStream);
                                byte[] byteArray = BoutputStream.toByteArray();
                                img = byteArray;
                                contentValues.put("img", new byte[]{});
                                database.insert("ghichu", null, contentValues);
                                setAlarm(title, content, DayAlert, TimeAlert, img);
                            }
                        });
                    }
                }).start();
            }


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    //    SET ALARM
    private void setAlarm(String title, String content, String day, String time, byte[] img) {

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigining alaram manager object to set alaram
//
//        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
//        intent.putExtra("title", title);                                                       //sending data to alarm class to create channel and notification
//        intent.putExtra("content", content);
//        intent.putExtra("day", day);
//        intent.putExtra("time", time);
//        intent.putExtra("img", img);
//

        //        LINE 2
        Bundle extras = new Bundle();
        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        extras.putString("title", title);
        extras.putString("content", content);
        extras.putString("day", day);
        extras.putString("time", time);
        extras.putByteArray("img", img);
        intent.putExtras(extras);

        Log.e("CHECK_TI", title);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String dateandtime = day + " " + timeTonotify;
        dateandtime_1 = dateandtime;
        Log.e("CHECK_Date2", dateandtime_1);
//        Log.e("CHECK_IMG", img + "");
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
//            Log.d("KIEMTRA_1", date1.toString());
//            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            am.setExact(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), getString(R.string.AddSuccess), Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Intent intentBack = new Intent(getApplicationContext(), MainActivity.class);
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);
    }


    //      GetByte
    private byte[] getByteArrayFromImageView(ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream BoutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, BoutputStream);
        byte[] byteArray = BoutputStream.toByteArray();
        return byteArray;
    }


    private boolean isEmpty() {

        String soHieuVB = textViewsoHieuVB.getText().toString();
        String loaiVB = textViewloaiVB.getText().toString();
        String trangthaiVB = textViewtrangthaiVB.getText().toString();
        String coQuan = textViewcoquanNhanGui.getText().toString();
        String nguoiKy = textViewnguoiKy.getText().toString();
        String duongdan = URIString;

        if (editText_title.getText().toString().isEmpty() || editText_content.getText().toString().isEmpty() || flag == false
                || button_date.getText().toString().equals("Chọn ngày") || button_time.getText().toString().equals("Chọn giờ")
                || textViewsoHieuVB.getText().toString().isEmpty() || textViewloaiVB.getText().toString().isEmpty() || textViewtrangthaiVB.getText().toString().isEmpty()
                || textViewcoquanNhanGui.getText().toString().isEmpty() || textViewnguoiKy.getText().toString().isEmpty()
        ) {
            return true;
        }
        return false;
    }

    private void init() {
        imgV_back = findViewById(R.id.id_ivBack);
        imgV_done = findViewById(R.id.id_ivDone);
        imgV_img_add = findViewById(R.id.id_imgV_anh);
        img_icon_check = findViewById(R.id.id_imgV_uncheck_1);
        editText_title = findViewById(R.id.id_edt_title);
        editText_content = findViewById(R.id.id_edt_content);
        editText_time = findViewById(R.id.id_edit_time);
        button_date = findViewById(R.id.id_btn_chonngay);
        button_time = findViewById(R.id.id_btn_chongio);
        textViewsoHieuVB = findViewById(R.id.id_sohieuvb);
        textViewloaiVB = findViewById(R.id.id_loai_vanban);
        textViewtrangthaiVB = findViewById(R.id.id_trang_thai_vb);
        textViewcoquanNhanGui = findViewById(R.id.id_coquan);
        textViewnguoiKy = findViewById(R.id.id_nguoiKy);
        imageViewDinhkem = findViewById(R.id.id_imgV_dinhkem);
        textViewdinhkemFile = findViewById(R.id.id_dinhkem);

//      SET GONE BUTTON
        button_date.setVisibility(View.GONE);
        button_time.setVisibility(View.GONE);

//      SET COLOR
//        imgV_img_add.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
        editText_time.setEnabled(false);
        textViewdinhkemFile.setEnabled(false);
        imageViewDinhkem.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_back.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        imgV_done.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);
        img_icon_check.setColorFilter(Color.parseColor("#686EFE"), PorterDuff.Mode.SRC_IN);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (MODE){
            case 1:
                if (resultCode == RESULT_OK) {
                    if (requestCode == REQUEST_CHOOSE_PHOTO) {
                        try {
//                    imgV_img_add.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);
                            uri = data.getData();
                            Uri imgURI = data.getData();
                            InputStream is = getContentResolver().openInputStream(imgURI);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            imgV_img_add.setImageBitmap(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case 2:
                if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
                    Uri URI = data.getData();
                    URIString = URI.toString();
                    Log.e("CHECK_1", URI + "");
                    textViewdinhkemFile.setText(URI.getLastPathSegment());
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    private String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        DayAlert = day + "-" + (month) + "-" + year;
        return makeDateString(day, month, year);
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
        MODE =  1;
    }


    private void selectTime() {                                                                     //this method performs the time picker task
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;                                                        //temp variable to store the time to set alarm
                button_time.setText(String.format(Locale.getDefault(), "%02d:%02d", i, i1));
                TimeAlert = i + ":" + i1;
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }


    public String FormatTime(int hour, int minute) {                                                //this method converts the time into 12hr farmat and assigns am or pm

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }

    private void initDataPicker() {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(year, month, day);
                button_date.setText(date);
//                DayAlert = day + "-" + (month + 1) + "-" + year;
            }
        };
        datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
    }


    private String getCurrentTime() {
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String timeNow = currentTime;
        return timeNow;
    }

    private String getCurrentTime1() {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String timeNow = currentTime;
        return timeNow;
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "Tháng 1";
        }
        if (month == 2) {
            return "Tháng 2";
        }
        if (month == 3) {
            return "Tháng 3";
        }
        if (month == 4) {
            return "Tháng 4";
        }
        if (month == 5) {
            return "Tháng 5";
        }
        if (month == 6) {
            return "Tháng 6";
        }
        if (month == 7) {
            return "Tháng 7";
        }
        if (month == 8) {
            return "Tháng 8";
        }
        if (month == 9) {
            return "Tháng 9";
        }
        if (month == 10) {
            return "Tháng 10";
        }
        if (month == 11) {
            return "Tháng 11";
        }
        if (month == 12) {
            return "Tháng 12";
        }
//        Trường hợp này không bao giờ xảy ra.!

        return "Tháng 1";
    }


    private void selectDate() {                                                                     //this method performs the date picker task
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                button_date.setText(day + " Tháng " + (month + 1) + " " + year);//sets the selected date as test for button
                DayAlert = day + "-" + (month + 1) + "-" + year;
//                Log.d("KIEMTRA", DayAlert);
            }

        }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }


}