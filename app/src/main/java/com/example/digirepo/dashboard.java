package com.example.digirepo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class dashboard extends AppCompatActivity implements View.OnClickListener {
    Dialog myDialog;
    int id;
    CardView card1,card2,card3,card4,card5,card6;
    ListView lv;

    FirebaseAuth fAuth;
    String rname , createdat;

    private String JSON_URL;

    ArrayList<HashMap<String,String>> reportsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Inside on create");
        super.onCreate(savedInstanceState);

        lv = findViewById(R.id.reportsListView);

        fAuth = FirebaseAuth.getInstance();
        System.out.println(fAuth.getCurrentUser().getPhoneNumber());

        setContentView(R.layout.activity_dashboard);
        card1 = (CardView)findViewById(R.id.l1);
        card2 = (CardView)findViewById(R.id.l2);
        card3 = (CardView)findViewById(R.id.l3);
        card4 = (CardView)findViewById(R.id.l4);
        card5 = (CardView)findViewById(R.id.l5);
        card6 = (CardView)findViewById(R.id.l6);

//      test1 = (TextView)findViewById(R.id.testprint) ;

        card1.setOnClickListener((View.OnClickListener) this);
        card2.setOnClickListener((View.OnClickListener) this);
        card3.setOnClickListener((View.OnClickListener) this);
        card4.setOnClickListener((View.OnClickListener) this);
        card5.setOnClickListener((View.OnClickListener) this);
        card6.setOnClickListener((View.OnClickListener) this);

        myDialog = new Dialog(this);
    }
    @Override
    public void onClick(View v){
//        int id;
       if(v.getId()==R.id.l1) {
        id=1;
       }
       else if (v.getId()==R.id.l2){
           id=2;
       }else if (v.getId()==R.id.l3){
           id=3;
       }else if (v.getId()==R.id.l4){
           id=4;
       }else if (v.getId()==R.id.l5){
           id=5;
       }else if (v.getId()==R.id.l6){
           id=6;
       }
       String phonen = fAuth.getCurrentUser().getPhoneNumber();
        phonen = phonen.substring(3);
       System.out.println(phonen);
       System.out.println("after if");
       System.out.println(id);
        JSON_URL = "https://378e-14-139-238-92.ngrok.io/api/user/report/metadata/"+phonen+"/"+id;
        System.out.println(JSON_URL);
//           id=1;
//       else
//           id=0;
           ShowPopup(v);

    }

    public void ShowPopup(View v) {
        TextView txtclose;

        myDialog.setContentView(R.layout.popup_screen);
        reportsList = new ArrayList<>();

        GetData getData = new GetData();
        getData.execute();

        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
//        test1.setText(id);
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public class GetData extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(in);

                    int data = isr.read();
                    while (data != -1) {

                        current += (char) data;
                        data = isr.read();
                    }

                    return current;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

                return current;
            }

        @Override
        protected void onPostExecute(String s) {

            try{
                JSONObject jsonObject = new JSONObject(s);
                System.out.println(jsonObject);
                JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                System.out.println(jsonObject2);
                JSONArray jsonArray = jsonObject2.getJSONArray("reports");
                System.out.println(jsonArray);

                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    rname= jsonObject1.getString("report_name");
                    System.out.println(rname);
                    createdat = jsonObject1.getString("issue_date");

//                    HashMap
                    HashMap<String, String> reportsdata = new HashMap<>();

                    reportsdata.put("Report_name",rname);
                    reportsdata.put("issue_date",createdat);

                    reportsList.add(reportsdata);
                }
                System.out.println(reportsList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListAdapter adapter= new SimpleAdapter(
                    dashboard.this,
                    reportsList,
                    R.layout.row_layout,
                    new String[]{"Report_name","issue_date"},
                    new int[]{R.id.rnameText, R.id.dateText}
            );

            lv.setAdapter(adapter);
        }
    }
}
