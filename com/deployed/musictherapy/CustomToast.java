package com.deployed.musictherapy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {
  public void Show_Toast(Context paramContext, View paramView, String paramString) {
    paramView = ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2131492911, (ViewGroup)paramView.findViewById(2131296545));
    ((TextView)paramView.findViewById(2131296544)).setText(paramString);
    Toast toast = new Toast(paramContext);
    toast.setGravity(55, 0, 0);
    toast.setDuration(0);
    toast.setView(paramView);
    toast.show();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\CustomToast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */