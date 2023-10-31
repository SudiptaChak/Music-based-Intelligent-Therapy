package butterknife.internal;

final class Parameter {
  static final Parameter[] NONE = new Parameter[0];
  
  private final int listenerPosition;
  
  private final String type;
  
  Parameter(int paramInt, String paramString) {
    this.listenerPosition = paramInt;
    this.type = paramString;
  }
  
  int getListenerPosition() {
    return this.listenerPosition;
  }
  
  String getType() {
    return this.type;
  }
  
  public boolean requiresCast(String paramString) {
    return this.type.equals(paramString) ^ true;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\internal\Parameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */