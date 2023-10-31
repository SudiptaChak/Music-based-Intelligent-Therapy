package com.deployed.musictherapy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class NewMusicPlayerActivity extends AppCompatActivity {
  public static int oneTimeOnly;
  
  private Runnable UpdateSongTime = new Runnable() {
      final NewMusicPlayerActivity this$0;
      
      public void run() {
        NewMusicPlayerActivity newMusicPlayerActivity = NewMusicPlayerActivity.this;
        NewMusicPlayerActivity.access$202(newMusicPlayerActivity, newMusicPlayerActivity.mediaPlayer.getCurrentPosition());
        NewMusicPlayerActivity.this.tx1.setText(String.format("%d min, %d sec", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long)NewMusicPlayerActivity.access$200(this.this$0))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long)NewMusicPlayerActivity.access$200(this.this$0)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)NewMusicPlayerActivity.access$200(this.this$0)))) }));
        NewMusicPlayerActivity.this.seekbar.setProgress((int)NewMusicPlayerActivity.this.startTime);
        NewMusicPlayerActivity.this.myHandler.postDelayed(this, 100L);
      }
    };
  
  private Button b1;
  
  private Button b2;
  
  private Button b3;
  
  private Button b4;
  
  private int backwardTime = 5000;
  
  private double finalTime = 0.0D;
  
  private int forwardTime = 5000;
  
  private ImageView iv;
  
  private Activity mActivity;
  
  private Context mContext;
  
  private MediaPlayer mediaPlayer;
  
  private Handler myHandler = new Handler();
  
  private SeekBar seekbar;
  
  private double startTime = 0.0D;
  
  private TextView tx1;
  
  private TextView tx2;
  
  private TextView tx3;
  
  public void backSong(View paramView) {
    this.mediaPlayer.release();
    startActivity(new Intent((Context)this, SongsListActivity.class));
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492898);
    paramBundle = getIntent().getExtras();
    String str2 = paramBundle.getString("name");
    this.b1 = (Button)findViewById(2131296331);
    this.b2 = (Button)findViewById(2131296332);
    this.b3 = (Button)findViewById(2131296333);
    this.b4 = (Button)findViewById(2131296334);
    this.iv = (ImageView)findViewById(2131296397);
    this.tx2 = (TextView)findViewById(2131296534);
    this.mContext = getApplicationContext();
    this.mActivity = (Activity)this;
    String str1 = paramBundle.getString("uri");
    Log.e("Song URL", str1);
    this.tx2.setText(str2);
    MediaPlayer mediaPlayer = new MediaPlayer();
    this.mediaPlayer = mediaPlayer;
    mediaPlayer.setAudioStreamType(3);
    try {
      this.mediaPlayer.setDataSource(str1);
      this.mediaPlayer.prepare();
      this.mediaPlayer.start();
      Toast.makeText(this.mContext, "Playing", 0).show();
    } catch (IOException iOException) {
      iOException.printStackTrace();
    } catch (IllegalArgumentException illegalArgumentException) {
      illegalArgumentException.printStackTrace();
    } catch (SecurityException securityException) {
      securityException.printStackTrace();
    } catch (IllegalStateException illegalStateException) {
      illegalStateException.printStackTrace();
    } 
    SeekBar seekBar = (SeekBar)findViewById(2131296481);
    this.seekbar = seekBar;
    seekBar.setClickable(false);
    this.b2.setEnabled(false);
    this.b3.setOnClickListener(new View.OnClickListener() {
          final NewMusicPlayerActivity this$0;
          
          public void onClick(View param1View) {
            Toast.makeText(NewMusicPlayerActivity.this.getApplicationContext(), "Playing sound", 0).show();
            NewMusicPlayerActivity.this.mediaPlayer.start();
            NewMusicPlayerActivity newMusicPlayerActivity = NewMusicPlayerActivity.this;
            NewMusicPlayerActivity.access$102(newMusicPlayerActivity, newMusicPlayerActivity.mediaPlayer.getDuration());
            newMusicPlayerActivity = NewMusicPlayerActivity.this;
            NewMusicPlayerActivity.access$202(newMusicPlayerActivity, newMusicPlayerActivity.mediaPlayer.getCurrentPosition());
            if (NewMusicPlayerActivity.oneTimeOnly == 0) {
              NewMusicPlayerActivity.this.seekbar.setMax((int)NewMusicPlayerActivity.this.finalTime);
              NewMusicPlayerActivity.oneTimeOnly = 1;
            } 
            NewMusicPlayerActivity.this.tx2.setText(String.format("%d min, %d sec", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long)NewMusicPlayerActivity.access$100(this.this$0))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long)NewMusicPlayerActivity.access$100(this.this$0)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)NewMusicPlayerActivity.access$100(this.this$0)))) }));
            NewMusicPlayerActivity.this.tx1.setText(String.format("%d min, %d sec", new Object[] { Long.valueOf(TimeUnit.MILLISECONDS.toMinutes((long)NewMusicPlayerActivity.access$200(this.this$0))), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds((long)NewMusicPlayerActivity.access$200(this.this$0)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long)NewMusicPlayerActivity.access$200(this.this$0)))) }));
            NewMusicPlayerActivity.this.seekbar.setProgress((int)NewMusicPlayerActivity.this.startTime);
            NewMusicPlayerActivity.this.myHandler.postDelayed(NewMusicPlayerActivity.this.UpdateSongTime, 100L);
            NewMusicPlayerActivity.this.b2.setEnabled(true);
            NewMusicPlayerActivity.this.b3.setEnabled(false);
          }
        });
    this.b2.setOnClickListener(new View.OnClickListener() {
          final NewMusicPlayerActivity this$0;
          
          public void onClick(View param1View) {
            Toast.makeText(NewMusicPlayerActivity.this.getApplicationContext(), "Pausing sound", 0).show();
            NewMusicPlayerActivity.this.mediaPlayer.pause();
            NewMusicPlayerActivity.this.b2.setEnabled(false);
            NewMusicPlayerActivity.this.b3.setEnabled(true);
          }
        });
    this.b1.setOnClickListener(new View.OnClickListener() {
          final NewMusicPlayerActivity this$0;
          
          public void onClick(View param1View) {
            if (((int)NewMusicPlayerActivity.this.startTime + NewMusicPlayerActivity.this.forwardTime) <= NewMusicPlayerActivity.this.finalTime) {
              NewMusicPlayerActivity newMusicPlayerActivity = NewMusicPlayerActivity.this;
              NewMusicPlayerActivity.access$202(newMusicPlayerActivity, newMusicPlayerActivity.startTime + NewMusicPlayerActivity.this.forwardTime);
              NewMusicPlayerActivity.this.mediaPlayer.seekTo((int)NewMusicPlayerActivity.this.startTime);
              Toast.makeText(NewMusicPlayerActivity.this.getApplicationContext(), "You have Jumped forward 5 seconds", 0).show();
            } else {
              Toast.makeText(NewMusicPlayerActivity.this.getApplicationContext(), "Cannot jump forward 5 seconds", 0).show();
            } 
          }
        });
    this.b4.setOnClickListener(new View.OnClickListener() {
          final NewMusicPlayerActivity this$0;
          
          public void onClick(View param1View) {
            if ((int)NewMusicPlayerActivity.this.startTime - NewMusicPlayerActivity.this.backwardTime > 0) {
              NewMusicPlayerActivity newMusicPlayerActivity = NewMusicPlayerActivity.this;
              NewMusicPlayerActivity.access$202(newMusicPlayerActivity, newMusicPlayerActivity.startTime - NewMusicPlayerActivity.this.backwardTime);
              NewMusicPlayerActivity.this.mediaPlayer.seekTo((int)NewMusicPlayerActivity.this.startTime);
              Toast.makeText(NewMusicPlayerActivity.this.getApplicationContext(), "You have Jumped backward 5 seconds", 0).show();
            } else {
              Toast.makeText(NewMusicPlayerActivity.this.getApplicationContext(), "Cannot jump backward 5 seconds", 0).show();
            } 
          }
        });
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\NewMusicPlayerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */