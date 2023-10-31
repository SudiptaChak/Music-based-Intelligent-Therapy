package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class AcknowledgementActivity extends AppCompatActivity {
  public void goNext(View paramView) {
    SharedPreferences sharedPreferences = getSharedPreferences("user", 0);
    String str = sharedPreferences.getString("username", "");
    sharedPreferences.getString("password", "");
    sharedPreferences.getString("id", "");
    if (str.length() != 0) {
      startActivity(new Intent((Context)this, Page2Activity.class));
    } else {
      startActivity(new Intent((Context)this, Page2Activity.class));
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492892);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\AcknowledgementActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */