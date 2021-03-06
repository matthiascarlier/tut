package com.example.matthias.tut;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.tut.MESSAGE";
    public final int MY_PERMISSIONS_REQUEST_READ_SMS = 1;

    private Handler handler = new Handler();
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {


                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_SMS},
                        MY_PERMISSIONS_REQUEST_READ_SMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        }
        else {
            //getSMS("6505551212");
        }
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        getSMS(message);
    }

    public void getSMS(final String address){
                Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), new String [] {"address", "date", "date_sent", "body"}, "address='" + address + "'", null, null);
                //Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"address", "date", "date_sent", "body"}, null, null, null);


                String msgData = "";
                TableLayout table = (TableLayout) findViewById(R.id.table);
                table.removeAllViews();
                if (cursor.moveToFirst()) { // must check the result to prevent exception
                    ArrayList<String> data = new ArrayList<String>();
                    data.add("address");
                    data.add("date");
                    //data.add("date_sent");
                    data.add("body");

                    addRowToTable(table, data);

                    msgData = "";
                    System.out.println(cursor.getCount());
                    i = 0;
                    ProgressBar mProgress = (ProgressBar) findViewById(R.id.progressBar);
                    mProgress.setMax(cursor.getCount());
                    do {

                        mProgress.setProgress(i);
                        System.out.println(i++);
                        data.clear();
                        data.add(cursor.getString(cursor.getColumnIndex("address")));
                        data.add(DateFormat.format("dd/mm/yyyy, h:mm:ss", new Date(cursor.getLong(cursor.getColumnIndex("date")))).toString());
                        //data.add(cursor.getString(cursor.getColumnIndex("date_sent")));
                        data.add(cursor.getString(cursor.getColumnIndex("body")));

                        //addRowToTable(table, data);

                        for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                            //msgData += " " + cursor.getColumnName(idx) + "|" + cursor.getString(idx) +"\n";
                            msgData += "" + cursor.getString(cursor.getColumnIndex(""));
                        }
                        // use msgData
                    } while (cursor.moveToNext());
                    System.out.println(msgData);
                } else {
                    // empty box, no SMS
                    msgData = "empty box, no SMS";
                }
                //System.out.println(msgData);
                //TextView txt = (TextView) findViewById(R.id.textView2);
                //txt.setText(msgData);
        System.out.println("test!!!!!");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getSMS("6505551212");

                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public TableLayout addRowToTable(TableLayout table, ArrayList<String> data){
        TableRow row = new TableRow(this);
        for(int i = 0; i < data.size(); i++) {
            TextView txt = new TextView(this);
            txt.setText(data.get(i));
            row.addView(txt);
        }
        table.addView(row);
        return table;
    }
}
