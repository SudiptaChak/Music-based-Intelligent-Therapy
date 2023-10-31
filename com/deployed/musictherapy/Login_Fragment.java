package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONException;
import org.json.JSONObject;

public class Login_Fragment extends Fragment implements View.OnClickListener {
  private static EditText emailid;
  
  private static TextView forgotPassword;
  
  private static FragmentManager fragmentManager;
  
  private static Button loginButton;
  
  private static LinearLayout loginLayout;
  
  private static EditText password;
  
  private static Animation shakeAnimation;
  
  private static CheckBox show_hide_password;
  
  private static TextView signUp;
  
  private static View view;
  
  String txtpassword;
  
  String url = "http://ogmaprojects.com/music/api/user_login.php";
  
  String username;
  
  private void checkValidation() {
    this.username = emailid.getText().toString();
    this.txtpassword = password.getText().toString();
    Matcher matcher = Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b").matcher(this.username);
    if (this.username.equals("") || this.username.length() == 0 || this.txtpassword.equals("") || this.txtpassword.length() == 0) {
      loginLayout.startAnimation(shakeAnimation);
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Enter both credentials.");
      return;
    } 
    if (!matcher.find()) {
      (new CustomToast()).Show_Toast((Context)getActivity(), view, "Your Email Id is Invalid.");
    } else {
      Toast.makeText((Context)getActivity(), "Do Login.", 0).show();
      (new OkHttpHandler()).execute(new Object[] { this.url });
    } 
  }
  
  private void initViews() {
    fragmentManager = getActivity().getSupportFragmentManager();
    emailid = (EditText)view.findViewById(2131296416);
    password = (EditText)view.findViewById(2131296418);
    loginButton = (Button)view.findViewById(2131296415);
    forgotPassword = (TextView)view.findViewById(2131296383);
    signUp = (TextView)view.findViewById(2131296353);
    show_hide_password = (CheckBox)view.findViewById(2131296488);
    loginLayout = (LinearLayout)view.findViewById(2131296417);
    shakeAnimation = AnimationUtils.loadAnimation((Context)getActivity(), 2130772001);
  }
  
  private void setListeners() {
    loginButton.setOnClickListener(this);
    forgotPassword.setOnClickListener(this);
    signUp.setOnClickListener(this);
    show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          final Login_Fragment this$0;
          
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            if (param1Boolean) {
              Login_Fragment.show_hide_password.setText(2131623982);
              Login_Fragment.password.setInputType(1);
              Login_Fragment.password.setTransformationMethod((TransformationMethod)HideReturnsTransformationMethod.getInstance());
            } else {
              Login_Fragment.show_hide_password.setText(2131623998);
              Login_Fragment.password.setInputType(129);
              Login_Fragment.password.setTransformationMethod((TransformationMethod)PasswordTransformationMethod.getInstance());
            } 
          }
        });
  }
  
  public void onClick(View paramView) {
    int i = paramView.getId();
    if (i != 2131296353) {
      if (i != 2131296383) {
        if (i == 2131296415)
          checkValidation(); 
      } else {
        fragmentManager.beginTransaction().setCustomAnimations(2130771999, 2130771998).replace(2131296384, new ForgotPassword_Fragment(), "ForgotPassword_Fragment").commit();
      } 
    } else {
      fragmentManager.beginTransaction().setCustomAnimations(2130771999, 2130771998).replace(2131296384, new SignUp_Fragment(), "SignUp_Fragment").commit();
    } 
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    view = paramLayoutInflater.inflate(2131492928, paramViewGroup, false);
    initViews();
    setListeners();
    return view;
  }
  
  public class OkHttpHandler extends AsyncTask {
    final Login_Fragment this$0;
    
    protected String doInBackground(Object[] param1ArrayOfObject) {
      OkHttpClient okHttpClient = new OkHttpClient();
      MediaType mediaType = MediaType.parse("application/json");
      JSONObject jSONObject = new JSONObject();
      try {
        jSONObject.put("username", Login_Fragment.this.username);
        jSONObject.put("password", Login_Fragment.this.txtpassword);
      } catch (JSONException jSONException) {
        jSONException.printStackTrace();
      } 
      RequestBody requestBody = RequestBody.create(mediaType, jSONObject.toString());
      Request request = (new Request.Builder()).url(Login_Fragment.this.url).post(requestBody).header("Accept", "application/json").header("Content-Type", "application/json").build();
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
      String str = param1Object.toString();
      Log.e("res", str);
      try {
        param1Object = new JSONObject();
        super(str);
        JSONObject jSONObject = param1Object.getJSONObject("user");
        param1Object = jSONObject.getString("id");
        String str2 = jSONObject.getString("email");
        String str4 = jSONObject.getString("password");
        str = jSONObject.getString("age_group");
        String str3 = jSONObject.getString("sex");
        String str1 = jSONObject.getString("problem");
        String str5 = jSONObject.getString("status");
        FragmentActivity fragmentActivity = Login_Fragment.this.getActivity();
        try {
          SharedPreferences.Editor editor = fragmentActivity.getSharedPreferences("User", 0).edit();
          editor.putString("id", (String)param1Object);
          editor.putString("email", str2);
          editor.putString("password", str4);
          editor.putString("age_group", str);
          editor.putString("sex", str3);
          editor.putString("problem", str1);
          editor.putString("status", str5);
          editor.commit();
          param1Object = new Intent();
          super((Context)Login_Fragment.this.getActivity(), SongsListActivity.class);
          Login_Fragment.this.startActivity((Intent)param1Object);
        } catch (JSONException null) {}
      } catch (JSONException jSONException) {}
      jSONException.printStackTrace();
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\Login_Fragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */