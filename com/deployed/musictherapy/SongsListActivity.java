package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SongsListActivity extends AppCompatActivity {
  ListView listView;
  
  ArrayList<Song> songs;
  
  TextView txtString;
  
  public String url = "http://ogmaprojects.com/music/api/songs_list.php";
  
  public void folkSongs(View paramView) {
    startActivity(new Intent((Context)this, FolkListActivity.class));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492905);
    this.listView = (ListView)findViewById(2131296421);
    (new OkHttpHandler()).execute(new Object[] { this.url });
  }
  
  public class OkHttpHandler extends AsyncTask {
    final SongsListActivity this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      OkHttpClient okHttpClient = new OkHttpClient();
      (new FormBody.Builder()).add("type", "1").build();
      Request request = (new Request.Builder()).url(SongsListActivity.this.url).build();
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
      SongsListActivity.this.songs = new ArrayList<Song>();
      try {
        JSONObject jSONObject = new JSONObject();
        this((String)param1Object);
        JSONArray jSONArray = jSONObject.getJSONArray("songs");
        for (byte b = 0; b < jSONArray.length(); b++) {
          JSONObject jSONObject1 = jSONArray.getJSONObject(b);
          String str2 = jSONObject1.getString("id");
          String str1 = jSONObject1.getString("title");
          if (jSONObject1.getInt("type") == 1) {
            param1Object = "Indian Classical Music";
          } else {
            param1Object = "Indian Folk Music";
          } 
          String str3 = jSONObject1.getString("description");
          String str4 = jSONObject1.getString("disease");
          String str5 = jSONObject1.getString("path");
          Song song = new Song();
          this(str2, str1, (String)param1Object, str3, str4, str5);
          SongsListActivity.this.songs.add(song);
        } 
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      param1Object = new CustomAdapter(SongsListActivity.this.songs, (Context)SongsListActivity.this);
      SongsListActivity.this.listView.setAdapter((ListAdapter)param1Object);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\SongsListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */