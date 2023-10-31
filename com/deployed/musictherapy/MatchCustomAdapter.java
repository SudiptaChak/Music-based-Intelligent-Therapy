package com.deployed.musictherapy;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MatchCustomAdapter extends BaseAdapter {
  Context context;
  
  ArrayList<MatchSong> data;
  
  LayoutInflater inflater;
  
  private MediaPlayer mediaPlayer;
  
  private int playbackPosition = 0;
  
  public MatchCustomAdapter(ArrayList<MatchSong> paramArrayList, Context paramContext) {
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
    View view = this.inflater.inflate(2131492929, null);
    TextView textView2 = (TextView)view.findViewById(2131296560);
    TextView textView1 = (TextView)view.findViewById(2131296448);
    TextView textView4 = (TextView)view.findViewById(2131296346);
    TextView textView3 = (TextView)view.findViewById(2131296516);
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(((MatchSong)this.data.get(paramInt)).match);
    stringBuilder1.append("% Avg. Match");
    String str1 = stringBuilder1.toString();
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(((MatchSong)this.data.get(paramInt)).codeper);
    stringBuilder2.append("% Match by Frequency");
    String str2 = stringBuilder2.toString();
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(((MatchSong)this.data.get(paramInt)).surveyper);
    stringBuilder3.append("% Match by Survey");
    String str3 = stringBuilder3.toString();
    textView2.setText(((MatchSong)this.data.get(paramInt)).song_title);
    textView1.setText(str1);
    textView4.setText(str2);
    textView3.setText(str3);
    return view;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\MatchCustomAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */