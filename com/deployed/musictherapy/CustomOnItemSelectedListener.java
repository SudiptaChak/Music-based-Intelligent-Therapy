package com.deployed.musictherapy;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
  public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    Context context = paramAdapterView.getContext();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("OnItemSelectedListener : ");
    stringBuilder.append(paramAdapterView.getItemAtPosition(paramInt).toString());
    Toast.makeText(context, stringBuilder.toString(), 0).show();
  }
  
  public void onNothingSelected(AdapterView<?> paramAdapterView) {}
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\CustomOnItemSelectedListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */