package com.deployed.musictherapy;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;

public class PlayMusicActivity extends AppCompatActivity {
  private Activity mActivity;
  
  private Button mButtonPlay;
  
  private Context mContext;
  
  MediaPlayer mPlayer;
  
  private LinearLayout mRootLayout;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131492902);
    this.mContext = getApplicationContext();
    this.mActivity = (Activity)this;
    this.mRootLayout = (LinearLayout)findViewById(2131296461);
    Button button = (Button)findViewById(2131296329);
    this.mButtonPlay = button;
    button.setOnClickListener(new View.OnClickListener() {
          final PlayMusicActivity this$0;
          
          public void onClick(View param1View) {
            PlayMusicActivity.this.mButtonPlay.setEnabled(false);
            PlayMusicActivity.this.mPlayer = new MediaPlayer();
            PlayMusicActivity.this.mPlayer.setAudioStreamType(3);
            try {
              PlayMusicActivity.this.mPlayer.setDataSource("http://www.atlantisphotographykolkata.com/api/admin/mp3/1593835170Soft_Blossom.mp3");
              PlayMusicActivity.this.mPlayer.prepare();
              PlayMusicActivity.this.mPlayer.start();
              Toast.makeText(PlayMusicActivity.this.mContext, "Playing", 0).show();
            } catch (IOException iOException) {
              iOException.printStackTrace();
            } catch (IllegalArgumentException illegalArgumentException) {
              illegalArgumentException.printStackTrace();
            } catch (SecurityException securityException) {
              securityException.printStackTrace();
            } catch (IllegalStateException illegalStateException) {
              illegalStateException.printStackTrace();
            } 
            PlayMusicActivity.this.mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                  final PlayMusicActivity.null this$1;
                  
                  public void onCompletion(MediaPlayer param2MediaPlayer) {
                    Toast.makeText(PlayMusicActivity.this.mContext, "End", 0).show();
                    PlayMusicActivity.this.mButtonPlay.setEnabled(true);
                  }
                });
          }
        });
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\PlayMusicActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */