package com.deployed.musictherapy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword_Fragment extends Fragment implements View.OnClickListener {
  private static TextView back;
  
  private static EditText emailId;
  
  private static TextView submit;
  
  private static View view;
  
  private void initViews() {
    emailId = (EditText)view.findViewById(2131296456);
    submit = (TextView)view.findViewById(2131296382);
    back = (TextView)view.findViewById(2131296321);
  }
  
  private void setListeners() {
    back.setOnClickListener(this);
    submit.setOnClickListener(this);
  }
  
  private void submitButtonTask() {
    String str = emailId.getText().toString();
    Matcher matcher = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b").matcher(str);
    if (str.equals("") || str.length() == 0) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Please enter your Email Id.");
      return;
    } 
    if (!matcher.find()) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Your Email Id is Invalid.");
    } else {
      Toast.makeText((Context)getActivity(), "Get Forgot Password.", 0).show();
    } 
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i != 2131296321) {
      if (i == 2131296382)
        submitButtonTask(); 
    } else {
      (new MainActivity()).replaceLoginFragment();
    } 
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    view = paramLayoutInflater.inflate(2131492927, paramViewGroup, false);
    initViews();
    setListeners();
    return view;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\ForgotPassword_Fragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */