package com.example.currentpositionapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sushant on 14/7/16.
 */
public class VLimit extends AppCompatActivity
{

    TextView tv;
    EditText ed;
    Button b;

    static int value_vlimit=5;

    SharedPreferences pref;

    int v_limit=5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.vechiclelimit);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view = getSupportActionBar().getCustomView();

        tv = (TextView) findViewById(R.id.climit);

        ed = (EditText) findViewById(R.id.newlimit_Text);

           b = (Button) findViewById(R.id.vbutton);

           pref = getSharedPreferences("vList", Activity.MODE_PRIVATE);

           v_limit=pref.getInt("vehiclelimit", 0);

            tv.setText(""+v_limit);



   //     String sql2 = "Select id from vechiclelimit";

        //limitnew.execSQL(sql2);

     /*   final Cursor cursor = limitnew.rawQuery(sql2, null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int nid = cursor.getColumnIndex("id");
            int val = cursor.getInt(nid);
            tv.setText("" + val);

        }
*/

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

               /* String sql="insert into vechiclelimit(id) values("+10+")";

                limitnew.execSQL(sql);

                Toast.makeText(getApplicationContext(),"Inserted value",Toast.LENGTH_LONG).show();


                limitnew.close();
                myDBHelper.close(); */

                //Send value to the next activity


                int vnew = Integer.parseInt(ed.getText().toString());

                value_vlimit=vnew;
                tv.setText("" + vnew);

                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("vehiclelimit", vnew);
                editor.commit();


                Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
                finish();


//                Toast.makeText(getApplicationContext(),"Vehicle Limit Updated",Toast.LENGTH_SHORT).show();

            }


        });


    }
  /*  @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(VLimit.this);
        builder1.setMessage("Are you  sure want go to previous Activity?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        Intent intent=new Intent(VLimit.this,SearchActivity.class);

                        startActivity(intent);

                        finish();

                    }
                });

        builder1.setNegativeButton
                (
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        }
*/
}







