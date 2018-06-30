package com.blanksoft.olympiaddesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PostWriteActivity extends AppCompatActivity {
    EditText etContentadd;
    String contentadd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);
        etContentadd = (EditText)findViewById(R.id.etContent);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_post:
                Toast.makeText(this, "1111",Toast.LENGTH_SHORT).show();
                final String URL =  "http://172.20.10.4:3000/process/contentadd";
                StringRequest request = new StringRequest(Request.Method.POST,
                        URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    if(response.equals("1")) {
                                        Intent i = new Intent(PostWriteActivity.this, ContentActivity.class);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "1111",Toast.LENGTH_SHORT).show();
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        contentadd = etContentadd.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        String uploadTime = sdf.format(cal.getTime());

                        Map<String, String> params = new HashMap<>();
                        params.put("content", contentadd);
                        params.put("name", MainActivity.userName);
                        params.put("date", uploadTime);

                        return params;
                    }
                };

                request.setShouldCache(true);
                Volley.newRequestQueue(this).add(request);


                break;

        }
        return true;
    }

}
