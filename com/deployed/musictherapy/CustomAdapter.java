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

public class CustomAdapter extends BaseAdapter {
  Context context;
  
  ArrayList<Song> data;
  
  LayoutInflater inflater;
  
  private MediaPlayer mediaPlayer;
  
  private int playbackPosition = 0;
  
  public CustomAdapter(ArrayList<Song> paramArrayList, Context paramContext) {
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
    paramView = this.inflater.inflate(2131492910, null);
    TextView textView1 = (TextView)paramView.findViewById(2131296560);
    TextView textView2 = (TextView)paramView.findViewById(2131296564);
    TextView textView3 = (TextView)paramView.findViewById(2131296555);
    TextView textView4 = (TextView)paramView.findViewById(2131296554);
    ImageView imageView1 = (ImageView)paramView.findViewById(2131296450);
    ImageView imageView2 = (ImageView)paramView.findViewById(2131296423);
    textView1.setText(((Song)this.data.get(paramInt)).name);
    textView2.setText(((Song)this.data.get(paramInt)).type);
    textView4.setText(((Song)this.data.get(paramInt)).description);
    imageView1.setTag(Integer.valueOf(paramInt));
    imageView2.setTag(Integer.valueOf(paramInt));
    imageView1.setOnClickListener(new View.OnClickListener() {
          final CustomAdapter this$0;
          
          public void onClick(View param1View) {
            int i = ((Integer)param1View.getTag()).intValue();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("http://www.atlantisphotographykolkata.com/api/admin/mp3/");
            stringBuilder.append(((Song)CustomAdapter.this.data.get(i)).path);
            String str1 = stringBuilder.toString();
            String str2 = ((Song)CustomAdapter.this.data.get(i)).name;
            String str3 = ((Song)CustomAdapter.this.data.get(i)).id;
            Log.e("songs", str1);
            Intent intent = new Intent(CustomAdapter.this.context, MusicDetailActivity.class);
            intent.putExtra("uri", str1);
            intent.putExtra("name", str2);
            intent.putExtra("id", str3);
            CustomAdapter.this.context.startActivity(intent);
          }
        });
    imageView2.setOnClickListener(new View.OnClickListener() {
          final CustomAdapter this$0;
          
          public void onClick(View param1View) {
            int i = ((Integer)param1View.getTag()).intValue();
            String str1 = ((Song)CustomAdapter.this.data.get(i)).name;
            String str2 = ((Song)CustomAdapter.this.data.get(i)).id;
            Intent intent = new Intent(CustomAdapter.this.context, MatchListActivity.class);
            intent.putExtra("name", str1);
            intent.putExtra("id", str2);
            CustomAdapter.this.context.startActivity(intent);
          }
        });
    return paramView;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\CustomAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */