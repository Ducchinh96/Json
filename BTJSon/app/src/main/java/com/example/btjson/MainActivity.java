package com.example.btjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv_contact ;
    ArrayList<Contact> ds_contact ;
    ArrayAdapter<Contact> adapterContact ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
    }

    private void addControls() {
        lv_contact = findViewById(R.id.lv_contact);
        ds_contact = new ArrayList<>() ;
        adapterContact = new ArrayAdapter<>(
                MainActivity.this, android.R.layout.simple_list_item_1,ds_contact
        );
        lv_contact.setAdapter(adapterContact);
        ContactTask contactTask = new ContactTask();
        contactTask.execute();


    }
    class  ContactTask extends AsyncTask<Void, Void, ArrayList<Contact>>{
        @Override
        protected ArrayList<Contact> doInBackground(Void... voids) {
            ArrayList<Contact> ds =new ArrayList<>();
            //Khai bao duong dan
            try {
                URL url = new URL("https://www.w3schools.com/js/customers_mysql.php") ;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                StringBuilder stringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line != null){
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();

                }
                inputStreamReader.close();
                JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                for(int i=0 ; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Contact contact = new Contact();
                    if(jsonObject.has("Name")){
                        contact.setName(jsonObject.getString("Name"));
                    }
                    if(jsonObject.has("City")){
                        contact.setCity(jsonObject.getString("City"));
                    }
                    if(jsonObject.has("Country")){
                        contact.setCountry(jsonObject.getString("Country"));
                    }

                    ds.add(contact);

                }
            } catch (Exception e) {
                Log.e("err", e.toString());
            }

            return ds;
        }
        protected void  onPreExecute(){
            super.onPreExecute();
            adapterContact.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Contact> contacts) {
            super.onPostExecute(contacts);
            adapterContact.clear();
            adapterContact.addAll(contacts);
        }
    }
}