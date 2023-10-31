package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class SurveyActivity extends AppCompatActivity {
  String age;
  
  EditText edname;
  
  String energy;
  
  String harmony;
  
  String id;
  
  String melody;
  
  String mood;
  
  String name;
  
  String person;
  
  String problem;
  
  RadioGroup rgsex;
  
  RadioGroup rgsong;
  
  String rhythmic;
  
  String sex;
  
  String song;
  
  Spinner spenergy;
  
  Spinner spharmony;
  
  Spinner spmelody;
  
  Spinner spmood;
  
  Spinner sprhythmic;
  
  Spinner sptempo;
  
  String tempo;
  
  String title;
  
  TextView tvtitle;
  
  String url = "http://ogmaprojects.com/music/api/survey.php";
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    paramBundle = getIntent().getExtras();
    this.id = paramBundle.getString("id");
    this.title = paramBundle.getString("title");
    setContentView(2131492907);
    this.tvtitle = (TextView)findViewById(2131296369);
    this.sptempo = (Spinner)findViewById(2131296507);
    this.spenergy = (Spinner)findViewById(2131296497);
    this.sprhythmic = (Spinner)findViewById(2131296506);
    this.spmelody = (Spinner)findViewById(2131296500);
    this.spharmony = (Spinner)findViewById(2131296498);
    this.spmood = (Spinner)findViewById(2131296501);
    this.tvtitle.setText(this.title);
  }
  
  public void saveSurvey(View paramView) {
    this.tempo = this.sptempo.getSelectedItem().toString();
    this.energy = this.spenergy.getSelectedItem().toString();
    this.rhythmic = this.sprhythmic.getSelectedItem().toString();
    this.melody = this.spmelody.getSelectedItem().toString();
    this.harmony = this.spharmony.getSelectedItem().toString();
    this.mood = this.spmood.getSelectedItem().toString();
    (new OkHttpHandler()).execute(new Object[] { this.url });
  }
  
  public class OkHttpHandler extends AsyncTask {
    final SurveyActivity this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      OkHttpClient okHttpClient = new OkHttpClient();
      MediaType mediaType = MediaType.parse("application/json");
      JSONObject jSONObject = new JSONObject();
      try {
        SurveyActivity.this.tempo = SurveyActivity.this.sptempo.getSelectedItem().toString();
        SurveyActivity.this.energy = SurveyActivity.this.spenergy.getSelectedItem().toString();
        SurveyActivity.this.rhythmic = SurveyActivity.this.sprhythmic.getSelectedItem().toString();
        SurveyActivity.this.melody = SurveyActivity.this.spmelody.getSelectedItem().toString();
        SurveyActivity.this.harmony = SurveyActivity.this.spharmony.getSelectedItem().toString();
        SurveyActivity.this.mood = SurveyActivity.this.spmood.getSelectedItem().toString();
        Log.e("tempo", SurveyActivity.this.tempo);
        jSONObject.put("song_id", SurveyActivity.this.id);
        jSONObject.put("tempo", SurveyActivity.this.tempo);
        jSONObject.put("energy", SurveyActivity.this.energy);
        jSONObject.put("rhythmic", SurveyActivity.this.rhythmic);
        jSONObject.put("melody", SurveyActivity.this.melody);
        jSONObject.put("harmony", SurveyActivity.this.harmony);
        jSONObject.put("mood", SurveyActivity.this.mood);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      RequestBody requestBody = RequestBody.create(mediaType, jSONObject.toString());
      Request request = (new Request.Builder()).url(SurveyActivity.this.url).post(requestBody).header("Accept", "application/json").header("Content-Type", "application/json").build();
      okHttpClient.newCall(request);
      try {
        return okHttpClient.newCall(request).execute().body().string();
      } catch (Exception exception) {
        exception.printStackTrace();
        return null;
      } 
    }
    
    protected void onPostExecute(Object param1Object) {
      super.onPostExecute(param1Object);
      param1Object = param1Object.toString();
      Log.e("res", (String)param1Object);
      try {
        Toast.makeText((Context)SurveyActivity.this, "Thank you for helping us with this information!!", 1).show();
        param1Object = new Intent();
        super((Context)SurveyActivity.this, SongDashboradActivity.class);
        SurveyActivity.this.startActivity((Intent)param1Object);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\SurveyActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */