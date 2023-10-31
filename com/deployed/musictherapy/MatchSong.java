package com.deployed.musictherapy;

public class MatchSong {
  public int codeper;
  
  public String id;
  
  public int match;
  
  public String song_title;
  
  public int surveyper;
  
  public MatchSong(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3) {
    this.id = paramString1;
    this.song_title = paramString2;
    this.match = paramInt1;
    this.codeper = paramInt2;
    this.surveyper = paramInt3;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\MatchSong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */