package com.deployed.musictherapy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OkHttpExample extends AppCompatActivity {
  OkHttpClient client = new OkHttpClient();
  
  TextView txtString;
  
  public String url = "https://reqres.in/api/users";
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492895);
    (new OkHttpHandler()).execute(new Object[] { this.url });
  }
  
  public class OkHttpHandler extends AsyncTask {
    final OkHttpExample this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      (new Request.Builder()).url(param1ArrayOfObject[0].toString());
      FormBody formBody = (new FormBody.Builder()).add("username", "test").add("password", "test").build();
      Request request = (new Request.Builder()).url(OkHttpExample.this.url).post((RequestBody)formBody).build();
      OkHttpExample.this.client.newCall(request);
      try {
        return OkHttpExample.this.client.newCall(request).execute().body().string();
      } catch (Exception exception) {
        exception.printStackTrace();
        return null;
      } 
    }
    
    protected void onPostExecute(Object param1Object) {
      super.onPostExecute(param1Object);
      String str = param1Object.toString();
      try {
        param1Object = new JSONObject();
        super(str);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
        jSONException = null;
      } 
      try {
        JSONArray jSONArray = jSONException.getJSONArray("users");
        for (byte b = 0; b < jSONArray.length(); b++)
          jSONArray.getJSONObject(b); 
      } catch (JSONException jSONException1) {
        jSONException1.printStackTrace();
      } 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\OkHttpExample.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */