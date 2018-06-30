package com.blanksoft.olympiaddesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.sql.DriverManager.println;

public class ContentActivity extends AppCompatActivity {

    List<Content> jsoncontent;
    ImageView image;


    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    CheckBox chklike;
    String userid;
    JSONArray Array;
    JSONObject mArray;
    char[] uid;

    JSONObject JOBJ;
    String SETLIKE="1";

    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                jsoncontent.clear();
                content();
                adapter.notifyDataSetChanged();

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        chklike = (CheckBox) findViewById(R.id.checkBoxLike);

        jsoncontent = new ArrayList<>();
        content();



    }





        //ivImage.setImageBitmap(bm);

    private void content(){
        final String URL =  "http://172.20.10.4:3000/content";




            com.android.volley.toolbox.StringRequest request = new com.android.volley.toolbox.StringRequest(Request.Method.POST,
                URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            Log.d("qweqwerqwt" ,response.toString());

                            JSONArray jsonarray = new JSONArray(response);
                            for(int i =0; i<jsonarray.length(); i++){

                                JSONObject jresponse = jsonarray.getJSONObject(i);
                                byte[] bytePlainOrg = Base64.decode(jresponse.optString("image", "iVBORw0KGgoAAAANSUhEUgAAATkAAAE5CAYAAADr4VfxAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJ\n" +
                                        "bWFnZVJlYWR5ccllPAAABqFJREFUeNrs3T1vW1UcwOHjJKgDoRjlA8QlLEiohImBAVtMWWgKO20n"), 0);

                                ByteArrayInputStream inStream = new ByteArrayInputStream(bytePlainOrg);
                                Bitmap bm = BitmapFactory.decodeStream(inStream) ;

                                //Log.d("ggw",jresponse.getString("content"));

                                String Test = "[{\"TV\":\"3d tv\"},{\"computer\":\"Computer\notebook\",\"LG\":\"1\",\"count\":\"20\"}]";


                                //JOBJ = jresponse.optJSONObject("Ulike");
                                Array = jresponse.optJSONArray("likeUsers");
                               // String SETCHECK;
//                                JSONArray subArray = new JSONArray(JOBJ.get("userid").toString());
                                SETLIKE = "1";
                                if(Array.length() >0){
                                for(int j = 0; j < Array.length(); j++) {
//                                     String val = (String)Array.get(j);
                                     SETLIKE = "1";

                                         if (Array.get(j).equals(MainActivity.userId)) {
                                             SETLIKE = "0";
                                             Log.d("yeeag", "jj");
                                             break;
                                         }

                                }
                                }

                                //Log.d("qwtqt", subArray.toString());

                                Log.d("like",SETLIKE);
                                Content content2 =new Content(jresponse.optString("content", "text on no value"), jresponse.optString("name", "text on no value"), jresponse.optString("date", "text on no value"), bm, jresponse.optString("like".toString()), jresponse.optString("contentid"), SETLIKE);

                                //Collections.reverse(jsoncontent);






                                jsoncontent.add(content2);



                                Log.d("qwt", content2.toString());

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);
                            if(adapter == null) {
                                adapter = new Adapter(getApplicationContext(), jsoncontent);
                                recyclerView.setAdapter(adapter);

                            }
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);


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
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };

        request.setShouldCache(true);
        request.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                200000 ,

                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_add_post:
                Toast.makeText(this, "1111",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ContentActivity.this, PostWriteActivity.class);
                startActivity(i);
                break;

        }
        return true;
    }

}
