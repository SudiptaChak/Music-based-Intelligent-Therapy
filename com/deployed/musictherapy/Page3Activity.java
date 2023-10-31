package com.deployed.musictherapy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Page3Activity extends Activity {
  private Button btnSubmit;
  
  private Spinner spinner1;
  
  private Spinner spinner2;
  
  public void addListenerOnButton() {
    this.spinner1 = (Spinner)findViewById(2131296502);
    Button button = (Button)findViewById(2131296327);
    this.btnSubmit = button;
    button.setOnClickListener(new View.OnClickListener() {
          final Page3Activity this$0;
          
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)Page3Activity.this, SongDashboradActivity.class);
            Page3Activity.this.startActivity(intent);
          }
        });
  }
  
  public void addListenerOnSpinnerItemSelection() {
    this.spinner1 = (Spinner)findViewById(2131296503);
    this.spinner2 = (Spinner)findViewById(2131296496);
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492901);
    addListenerOnButton();
    addListenerOnSpinnerItemSelection();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\Page3Activity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */