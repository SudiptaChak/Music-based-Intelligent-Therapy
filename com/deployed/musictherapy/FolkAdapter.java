package com.deployed.musictherapy;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class FolkAdapter extends BaseAdapter {
  Context context;
  
  ArrayList<Song> data;
  
  LayoutInflater inflater;
  
  private MediaPlayer mediaPlayer;
  
  private int playbackPosition = 0;
  
  public FolkAdapter(ArrayList<Song> paramArrayList, Context paramContext) {
    this.data = paramArrayList;
    this.context = paramContext;
    this.inflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
  }
  
  public int getCount() {
    return this.data.size();
  }
  
  public Object getItem(int paramInt) {
    return null;
  }
  
  public long getItemId(int paramInt) {
    return 0L;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    paramView = this.inflater.inflate(2131492926, null);
    TextView textView2 = (TextView)paramView.findViewById(2131296560);
    TextView textView1 = (TextView)paramView.findViewById(2131296564);
    TextView textView3 = (TextView)paramView.findViewById(2131296555);
    TextView textView4 = (TextView)paramView.findViewById(2131296554);
    ImageView imageView1 = (ImageView)paramView.findViewById(2131296450);
    ImageView imageView2 = (ImageView)paramView.findViewById(2131296517);
    textView2.setText(((Song)this.data.get(paramInt)).name);
    textView1.setText(((Song)this.data.get(paramInt)).f_type);
    textView4.setText(((Song)this.data.get(paramInt)).description);
    imageView1.setTag(Integer.valueOf(paramInt));
    imageView2.setTag(Integer.valueOf(paramInt));
    imageView2.setOnClickListener(new View.OnClickListener() {
          final FolkAdapter this$0;
          
          public void onClick(View param1View) {
            int i = ((Integer)param1View.getTag()).intValue();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://www.atlantisphotographykolkata.com/api/admin/mp3/");
            stringBuilder.append(((Song)FolkAdapter.this.data.get(i)).path);
            String str1 = stringBuilder.toString();
            String str2 = ((Song)FolkAdapter.this.data.get(i)).name;
            String str3 = ((Song)FolkAdapter.this.data.get(i)).id;
            Log.e("id", str3);
            Intent intent = new Intent(FolkAdapter.this.context, SurveyActivity.class);
            intent.putExtra("uri", str1);
            intent.putExtra("name", str2);
            intent.putExtra("id", str3);
            FolkAdapter.this.context.startActivity(intent);
          }
        });
    imageView1.setOnClickListener(new View.OnClickListener() {
          final FolkAdapter this$0;
          
          public void onClick(View param1View) {
            int i = ((Integer)param1View.getTag()).intValue();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://www.atlantisphotographykolkata.com/api/admin/mp3/");
            stringBuilder.append(((Song)FolkAdapter.this.data.get(i)).path);
            String str1 = stringBuilder.toString();
            String str3 = ((Song)FolkAdapter.this.data.get(i)).name;
            String str2 = ((Song)FolkAdapter.this.data.get(i)).id;
            Log.e("songs", str1);
            Intent intent = new Intent(FolkAdapter.this.context, NewMusicPlayerActivity.class);
            intent.putExtra("uri", str1);
            intent.putExtra("name", str3);
            intent.putExtra("id", str2);
            FolkAdapter.this.context.startActivity(intent);
          }
        });
    return paramView;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\FolkAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */