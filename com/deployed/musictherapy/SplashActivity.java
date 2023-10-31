package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
  Context context;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492906);
    this.context = (Context)this;
    (new Handler()).postDelayed(new Runnable() {
          final SplashActivity this$0;
          
          public void run() {
            Intent intent = new Intent((Context)SplashActivity.this, AcknowledgementActivity.class);
            SplashActivity.this.startActivity(intent);
          }
        }3000L);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\SplashActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */