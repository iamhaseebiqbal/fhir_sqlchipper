package com.demo.haseeb.fhir_sqlchipper_demo.Helper;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class FetchJSON extends AsyncTask<Void,Void,Void> {

    String data = "";
    Activity mainContext;
    TextView textView;

    public FetchJSON(Activity context, TextView tv) {
        super();

        mainContext = context;
        textView = tv;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Log.i("IKMB", "Downloading Patients Data: ", null);

        try {
            URL url = new URL("http://demo.oridashi.com.au:8298/Patient?family=andrew&_format=json");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            JSONObject json_object = new JSONObject(data);
            JSONArray entry = (JSONArray) json_object.get("entry");

            for(int i =0 ;i <entry.length(); i++){
                String lastName = (String) entry
                        .getJSONObject(i)
                        .getJSONObject("resource")
                        .getJSONArray("name")
                        .getJSONObject(0)
                        .getJSONArray("given")
                        .get(0);

                DBHelper.getInstance(mainContext).insertPatient(lastName);

                Log.i("IKMB", "Writing in DB: " + lastName, null);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        displayPatientsNames();
    }

    public void displayPatientsNames() {
        List<String> lastNames = DBHelper.getInstance(mainContext).getAllPatient();

        Log.i("IKMB", "Total Saved Names : " + lastNames.size(), null);

        String namesString = "";


        if(lastNames.size() > 0) {
            for (String lname : lastNames) {
                namesString += lname + "\n";
            }
        }

        textView.setText(namesString);
    }
}
