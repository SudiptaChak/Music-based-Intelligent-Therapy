package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUp_Fragment extends Fragment implements View.OnClickListener {
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
  
  String getEmailId;
  
  String getFullName;
  
  String getMobileNumber;
  
  String getPassword;
  
  String url = "http://ogmaprojects.com/music/api/user_signup.php";
  
  private void checkValidation() {
    this.getFullName = fullName.getText().toString();
    this.getEmailId = emailId.getText().toString();
    this.getMobileNumber = mobileNumber.getText().toString();
    this.getPassword = password.getText().toString();
    String str = confirmPassword.getText().toString();
    Matcher matcher = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b").matcher(this.getEmailId);
    if (this.getFullName.equals("") || this.getFullName.length() == 0 || this.getEmailId.equals("") || this.getEmailId.length() == 0 || this.getMobileNumber.equals("") || this.getMobileNumber.length() == 0 || this.getPassword.equals("") || this.getPassword.length() == 0 || str.equals("") || str.length() == 0) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "All fields are required.");
      return;
    } 
    if (!matcher.find()) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Your Email Id is Invalid.");
    } else if (!str.equals(this.getPassword)) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Both password doesn't match.");
    } else if (!terms_conditions.isChecked()) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Please select Terms and Conditions.");
    } else {
      Toast.makeText((Context)getActivity(), "Do SignUp.", 0).show();
      (new OkHttpHandler()).execute(new Object[] { this.url });
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
  
  public class OkHttpHandler extends AsyncTask {
    final SignUp_Fragment this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      OkHttpClient okHttpClient = new OkHttpClient();
      MediaType mediaType = MediaType.parse("application/json");
      JSONObject jSONObject = new JSONObject();
      try {
        Log.e("Mobile", SignUp_Fragment.this.getMobileNumber);
        jSONObject.put("email", SignUp_Fragment.this.getEmailId);
        jSONObject.put("full_name", SignUp_Fragment.this.getFullName);
        jSONObject.put("password", SignUp_Fragment.this.getPassword);
        jSONObject.put("mobile", SignUp_Fragment.this.getMobileNumber);
        jSONObject.put("age", 32);
        jSONObject.put("sex", 32);
        jSONObject.put("problem", 32);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      RequestBody requestBody = RequestBody.create(mediaType, jSONObject.toString());
      Request request = (new Request.Builder()).url(SignUp_Fragment.this.url).post(requestBody).header("Accept", "application/json").header("Content-Type", "application/json").build();
      okHttpClient.newCall(request);
      try {
        return okHttpClient.newCall(request).execute().body().string();
      } catch (Exception exception) {
        exception.printStackTrace();
        return null;
      } 
    }
    
    protected void onPostExecute(Object param1Object) {
      super.onPostExecute(param1Object);
      param1Object = param1Object.toString();
      Log.e("res", (String)param1Object);
      try {
        param1Object = new Intent();
        super((Context)SignUp_Fragment.this.getActivity(), MainActivity.class);
        SignUp_Fragment.this.startActivity((Intent)param1Object);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\SignUp_Fragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */