package com.deployed.musictherapy;

public class Song {
  public String description;
  
  public String disease;
  
  public String energy_level;
  
  public String f_type;
  
  public String harmony;
  
  public String id;
  
  public String melody;
  
  public String mood;
  
  public String name;
  
  public String path;
  
  public String rhythmic_pattern;
  
  public String tempo;
  
  public String type;
  
  public Song(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) {
    this.id = paramString1;
    this.name = paramString2;
    this.f_type = paramString3;
    this.description = paramString4;
    this.disease = paramString5;
    this.path = paramString6;
  }
  
  public Song(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12) {
    this.id = paramString1;
    this.name = paramString2;
    this.type = paramString3;
    this.description = paramString4;
    this.disease = paramString5;
    this.path = paramString6;
    this.tempo = paramString7;
    this.energy_level = paramString8;
    this.rhythmic_pattern = paramString9;
    this.melody = paramString10;
    this.mood = paramString12;
    this.harmony = paramString11;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\deployed\musictherapy\Song.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */