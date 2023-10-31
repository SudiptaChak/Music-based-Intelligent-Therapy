package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class MusicDetailActivity extends AppCompatActivity {
  String id;
  
  String path;
  
  Song song;
  
  String title;
  
  TextView tvdesc;
  
  TextView tvdisease;
  
  TextView tvenergy;
  
  TextView tvharmony;
  
  TextView tvmelody;
  
  TextView tvmood;
  
  TextView tvrhythmic;
  
  TextView tvtempo;
  
  TextView tvtitle;
  
  public String url = "http://ogmaprojects.com/music/api/get_song_by_id.php";
  
  public void goSurvey(View paramView) {
    startActivity(new Intent((Context)this, SurveyActivity.class));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492897);
    if (getApplicationContext().getSharedPreferences("User", 0).getString("id", null) != null) {
      String str = getIntent().getExtras().getString("id");
      this.id = str;
      Log.e("id", str);
      this.tvtitle = (TextView)findViewById(2131296563);
      this.tvdesc = (TextView)findViewById(2131296554);
      this.tvtempo = (TextView)findViewById(2131296562);
      this.tvenergy = (TextView)findViewById(2131296556);
      this.tvdisease = (TextView)findViewById(2131296555);
      this.tvmood = (TextView)findViewById(2131296559);
      this.tvharmony = (TextView)findViewById(2131296557);
      this.tvrhythmic = (TextView)findViewById(2131296561);
      this.tvmelody = (TextView)findViewById(2131296558);
      (new OkHttpHandler()).execute(new Object[] { this.url });
    } else {
      startActivity(new Intent((Context)this, MainActivity.class));
    } 
  }
  
  public void playSong(View paramView) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("http://ogmaprojects.com/music/api/admin/mp3/");
    stringBuilder.append(this.path);
    String str = stringBuilder.toString();
    Intent intent = new Intent((Context)this, NewMusicPlayerActivity.class);
    intent.putExtra("uri", str);
    intent.putExtra("name", this.title);
    intent.putExtra("id", this.id);
    startActivity(intent);
  }
  
  public class OkHttpHandler extends AsyncTask {
    final MusicDetailActivity this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      OkHttpClient okHttpClient = new OkHttpClient();
      MediaType mediaType = MediaType.parse("application/json");
      JSONObject jSONObject = new JSONObject();
      try {
        jSONObject.put("id", MusicDetailActivity.this.id);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      RequestBody requestBody = RequestBody.create(mediaType, jSONObject.toString());
      Request request = (new Request.Builder()).url(MusicDetailActivity.this.url).post(requestBody).header("Accept", "application/json").header("Content-Type", "application/json").build();
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
      String str = param1Object.toString();
      Log.e("res", str);
      try {
        param1Object = new JSONObject();
        super(str);
        JSONObject jSONObject = param1Object.getJSONObject("song");
        MusicDetailActivity.this.title = jSONObject.getString("title");
        String str3 = jSONObject.getString("description");
        String str4 = jSONObject.getString("tempo");
        String str7 = jSONObject.getString("energy_level");
        String str5 = jSONObject.getString("rhythmic_pattern");
        String str1 = jSONObject.getString("melody");
        String str2 = jSONObject.getString("harmony");
        String str6 = jSONObject.getString("mood");
        param1Object = jSONObject.getString("disease");
        MusicDetailActivity.this.path = jSONObject.getString("path");
        Log.e("Disease", (String)param1Object);
        MusicDetailActivity.this.tvtitle.setText(MusicDetailActivity.this.title);
        MusicDetailActivity.this.tvdesc.setText(str3);
        MusicDetailActivity.this.tvdisease.setText((CharSequence)param1Object);
        MusicDetailActivity.this.tvenergy.setText(str7);
        MusicDetailActivity.this.tvharmony.setText(str2);
        MusicDetailActivity.this.tvmood.setText(str6);
        MusicDetailActivity.this.tvrhythmic.setText(str5);
        MusicDetailActivity.this.tvtempo.setText(str4);
        MusicDetailActivity.this.tvmelody.setText(str1);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\MusicDetailActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */