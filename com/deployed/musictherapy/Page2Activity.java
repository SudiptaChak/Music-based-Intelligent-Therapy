package com.deployed.musictherapy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Page2Activity extends Activity {
  private Button btnSubmit;
  
  private Spinner spinner1;
  
  private Spinner spinner2;
  
  public void addListenerOnButton() {
    this.spinner1 = (Spinner)findViewById(2131296502);
    Button button = (Button)findViewById(2131296327);
    this.btnSubmit = button;
    button.setOnClickListener(new View.OnClickListener() {
          final Page2Activity this$0;
          
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)Page2Activity.this, Page3Activity.class);
            Page2Activity.this.startActivity(intent);
            Toast.makeText((Context)Page2Activity.this, "OnClickListener : ", 0).show();
          }
        });
  }
  
  public void addListenerOnSpinnerItemSelection() {
    this.spinner1 = (Spinner)findViewById(2131296502);
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492900);
    addListenerOnButton();
    addListenerOnSpinnerItemSelection();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\Page2Activity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */