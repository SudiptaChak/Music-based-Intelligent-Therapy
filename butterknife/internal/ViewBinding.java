package butterknife.internal;

final class ViewBinding implements Binding {
  private final String name;
  
  private final boolean required;
  
  private final String type;
  
  ViewBinding(String paramString1, String paramString2, boolean paramBoolean) {
    this.name = paramString1;
    this.type = paramString2;
    this.required = paramBoolean;
  }
  
  public String getDescription() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("field '");
    stringBuilder.append(this.name);
    stringBuilder.append("'");
    return stringBuilder.toString();
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
  
  public boolean requiresCast() {
    return "android.view.View".equals(this.type) ^ true;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\internal\ViewBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */