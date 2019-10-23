package com.example.codingtr.lolquery.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.codingtr.lolquery.Adapter.Adapter;

import com.example.codingtr.lolquery.Management.News;
import com.example.codingtr.lolquery.Management.SessionManagement;
import com.example.codingtr.lolquery.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home_Fragment extends Fragment  {

    private SessionManagement sessionManagement;
    private RecyclerView recyclerView;
    private Adapter adapter;

    private String link;
    private List<News> newsList;

    private final static String URL_READ = "http://lolquery.codingtr.com/webservice/results.json";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sessionManagement = new SessionManagement(getContext());
        sessionManagement.checkLogin();

        View rootView = inflater.inflate(R.layout.fragment_home, container,false);

        newsList = new ArrayList<>();

        recyclerView = rootView.findViewById(R.id.fragmentHomeRecylerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        readNews();


        return rootView;
    }

    private void readNews() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject  = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("posts");

                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            int id = jsonObject1.getInt("haber_id");
                            String author = jsonObject1.getString("yazan");
                            String title = jsonObject1.getString("baslik");
                            String category = jsonObject1.getString("kategori");
                            String fotograf = jsonObject1.getString("foto");
                            link = jsonObject1.getString("link");

                            newsList.add(new News(id, title, category, author, fotograf, link));


                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new Adapter(getContext(), newsList);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> param = new HashMap<>();
                param.put("user_email", "denizf.b@outlook.com.tr");
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

}
