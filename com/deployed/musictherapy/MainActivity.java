package com.deployed.musictherapy;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
  private static FragmentManager fragmentManager;
  
  public void onBackPressed() {
    Fragment fragment1 = fragmentManager.findFragmentByTag("SignUp_Fragment");
    Fragment fragment2 = fragmentManager.findFragmentByTag("ForgotPassword_Fragment");
    if (fragment1 != null) {
      replaceLoginFragment();
    } else if (fragment2 != null) {
      replaceLoginFragment();
    } else {
      super.onBackPressed();
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492895);
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager = fragmentManager;
    if (paramBundle == null)
      fragmentManager.beginTransaction().replace(2131296384, new Login_Fragment(), "Login_Fragment").commit(); 
    findViewById(2131296345).setOnClickListener(new View.OnClickListener() {
          final MainActivity this$0;
          
          public void onClick(View param1View) {
            MainActivity.this.finish();
          }
        });
  }
  
  protected void replaceLoginFragment() {
    fragmentManager.beginTransaction().setCustomAnimations(2130771997, 2130772000).replace(2131296384, new Login_Fragment(), "Login_Fragment").commit();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\MainActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */