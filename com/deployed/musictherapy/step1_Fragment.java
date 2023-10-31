package com.deployed.musictherapy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class step1_Fragment extends Fragment implements View.OnClickListener {
  private static EditText confirmPassword;
  
  private static EditText emailId;
  
  private static EditText fullName;
  
  private static EditText location;
  
  private static TextView login;
  
  private static EditText mobileNumber;
  
  private static EditText password;
  
  private static Button signUpButton;
  
  private static CheckBox terms_conditions;
  
  private static View view;
  
  private void checkValidation() {
    String str5 = fullName.getText().toString();
    String str3 = emailId.getText().toString();
    String str6 = mobileNumber.getText().toString();
    String str1 = location.getText().toString();
    String str4 = password.getText().toString();
    String str2 = confirmPassword.getText().toString();
    Matcher matcher = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b").matcher(str3);
    if (str5.equals("") || str5.length() == 0 || str3.equals("") || str3.length() == 0 || str6.equals("") || str6.length() == 0 || str1.equals("") || str1.length() == 0 || str4.equals("") || str4.length() == 0 || str2.equals("") || str2.length() == 0) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "All fields are required.");
      return;
    } 
    if (!matcher.find()) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Your Email Id is Invalid.");
    } else if (!str2.equals(str4)) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Both password doesn't match.");
    } else if (!terms_conditions.isChecked()) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Please select Terms and Conditions.");
    } else {
      Toast.makeText((Context)getActivity(), "Do SignUp.", 0).show();
    } 
  }
  
  private void initViews() {
    fullName = (EditText)view.findViewById(2131296385);
    emailId = (EditText)view.findViewById(2131296570);
    mobileNumber = (EditText)view.findViewById(2131296427);
    password = (EditText)view.findViewById(2131296446);
    confirmPassword = (EditText)view.findViewById(2131296348);
    signUpButton = (Button)view.findViewById(2131296489);
    login = (TextView)view.findViewById(2131296317);
    terms_conditions = (CheckBox)view.findViewById(2131296527);
  }
  
  private void setListeners() {
    signUpButton.setOnClickListener(this);
    login.setOnClickListener(this);
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i != 2131296317) {
      if (i == 2131296489)
        checkValidation(); 
    } else {
      (new MainActivity()).replaceLoginFragment();
    } 
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    view = paramLayoutInflater.inflate(2131492941, paramViewGroup, false);
    initViews();
    setListeners();
    return view;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\step1_Fragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */