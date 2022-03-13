package com.example.mcproject;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CovidCasesActivity extends AppCompatActivity {
    RecyclerView rcv;
    private String TAG="myapp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_cases);
        rcv = (RecyclerView) findViewById(R.id.recyclerview);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        Download_json();

    }
    public void Download_json(){
        RequestQueue queue = Volley.newRequestQueue(this);
        ArrayList<Covid_Model>data = new ArrayList<Covid_Model>();
        String url= "https://covid19-mohfw.herokuapp.com/";
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray jsonArray=response.getJSONArray("states");
                    //Toast.makeText(CovidCasesActivity.this, ""+jsonArray, Toast.LENGTH_SHORT).show();
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Log.d("myapp", "onResponse: "+jsonObject);
                        String state_name=jsonObject.getString("state");
                        String active_case=jsonObject.getString("cases");
                        String total_case=jsonObject.getString("total");
                        String total_death=jsonObject.getString("deaths");
                        String total_recovery=jsonObject.getString("recoveries");
                        Log.d("myapp", "onResponse:"+state_name);
                        Covid_Model cm=new Covid_Model(state_name,active_case,total_case,total_death,total_recovery);
                        data.add(cm);
                        rcv.setAdapter(new adaptor(CovidCasesActivity.this,data));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        queue.add(jsonObjectRequest);
    }
}