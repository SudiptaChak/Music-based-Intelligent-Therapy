package butterknife.internal;

final class CollectionBinding implements Binding {
  private final Kind kind;
  
  private final String name;
  
  private final boolean required;
  
  private final String type;
  
  CollectionBinding(String paramString1, String paramString2, Kind paramKind, boolean paramBoolean) {
    this.name = paramString1;
    this.type = paramString2;
    this.kind = paramKind;
    this.required = paramBoolean;
  }
  
  public String getDescription() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("field '");
    stringBuilder.append(this.name);
    stringBuilder.append("'");
    return stringBuilder.toString();
  }
  
  public Kind getKind() {
    return this.kind;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getType() {
    return this.type;
  }
  
  public boolean isRequired() {
    return this.required;
  }
  
  enum Kind {
    ARRAY, LIST;
    
    private static final Kind[] $VALUES;
    
    static {
      Kind kind = new Kind("LIST", 1);
      LIST = kind;
      $VALUES = new Kind[] { ARRAY, kind };
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\internal\CollectionBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */