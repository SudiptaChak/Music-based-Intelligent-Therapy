package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class SongDashboradActivity extends AppCompatActivity {
  public void folkmusic(View paramView) {
    startActivity(new Intent((Context)this, FolkListActivity.class));
  }
  
  public void indianclassicalmusic(View paramView) {
    startActivity(new Intent((Context)this, SongsListActivity.class));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492904);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\SongDashboradActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */