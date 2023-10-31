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

public class FolkListActivity extends AppCompatActivity {
  ListView listView;
  
  ArrayList<Song> songs;
  
  TextView txtString;
  
  public String url = "http://ogmaprojects.com/music/api/folk_list.php";
  
  public void folkSongs(View paramView) {
    startActivity(new Intent((Context)this, SongsListActivity.class));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492893);
    this.listView = (ListView)findViewById(2131296421);
    (new OkHttpHandler()).execute(new Object[] { this.url });
  }
  
  public class OkHttpHandler extends AsyncTask {
    final FolkListActivity this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      OkHttpClient okHttpClient = new OkHttpClient();
      (new FormBody.Builder()).add("type", "1").build();
      Request request = (new Request.Builder()).url(FolkListActivity.this.url).build();
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
      FolkListActivity.this.songs = new ArrayList<Song>();
      try {
        JSONObject jSONObject = new JSONObject();
        this((String)param1Object);
        JSONArray jSONArray = jSONObject.getJSONArray("songs");
        for (byte b = 0; b < jSONArray.length(); b++) {
          JSONObject jSONObject1 = jSONArray.getJSONObject(b);
          String str1 = jSONObject1.getString("id");
          String str4 = jSONObject1.getString("title");
          String str2 = jSONObject1.getString("f_type");
          param1Object = jSONObject1.getString("description");
          String str3 = jSONObject1.getString("disease");
          String str5 = jSONObject1.getString("path");
          Song song = new Song();
          this(str1, str4, str2, (String)param1Object, str3, str5);
          FolkListActivity.this.songs.add(song);
        } 
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      param1Object = new FolkAdapter(FolkListActivity.this.songs, (Context)FolkListActivity.this);
      FolkListActivity.this.listView.setAdapter((ListAdapter)param1Object);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\FolkListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */