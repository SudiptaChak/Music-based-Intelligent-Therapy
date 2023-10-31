package com.deployed.musictherapy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class MatchListActivity extends AppCompatActivity {
  String id;
  
  ListView lvmatch;
  
  ArrayList<MatchSong> songs;
  
  String title;
  
  TextView tvtitle;
  
  public String url = "http://ogmaprojects.com/music/api/match_list.php";
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492896);
    this.lvmatch = (ListView)findViewById(2131296420);
    this.tvtitle = (TextView)findViewById(2131296563);
    paramBundle = getIntent().getExtras();
    this.id = paramBundle.getString("id");
    String str = paramBundle.getString("name");
    this.title = str;
    this.tvtitle.setText(str);
    Log.e("ID", this.id);
    Log.e("title", this.title);
    (new OkHttpHandler()).execute(new Object[] { this.url });
  }
  
  public class OkHttpHandler extends AsyncTask {
    final MatchListActivity this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      OkHttpClient okHttpClient = new OkHttpClient();
      MediaType mediaType = MediaType.parse("application/json");
      JSONObject jSONObject = new JSONObject();
      try {
        jSONObject.put("id", MatchListActivity.this.id);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      RequestBody requestBody = RequestBody.create(mediaType, jSONObject.toString());
      Request request = (new Request.Builder()).url(MatchListActivity.this.url).post(requestBody).header("Accept", "application/json").header("Content-Type", "application/json").build();
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
      MatchListActivity.this.songs = new ArrayList<MatchSong>();
      try {
        param1Object = new JSONObject();
        super(str);
        param1Object = param1Object.getJSONArray("songs");
        for (byte b = 0; b < param1Object.length(); b++) {
          JSONObject jSONObject = param1Object.getJSONObject(b);
          str = jSONObject.getString("song_id");
          String str1 = jSONObject.getString("song_title");
          int j = jSONObject.getInt("match");
          int i = jSONObject.getInt("codeper");
          int k = jSONObject.getInt("surveyper");
          MatchSong matchSong = new MatchSong();
          this(str, str1, j, i, k);
          MatchListActivity.this.songs.add(matchSong);
        } 
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      param1Object = new MatchCustomAdapter(MatchListActivity.this.songs, (Context)MatchListActivity.this);
      MatchListActivity.this.lvmatch.setAdapter((ListAdapter)param1Object);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\MatchListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */