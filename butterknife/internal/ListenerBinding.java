package butterknife.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class ListenerBinding implements Binding {
  private final String name;
  
  private final List<Parameter> parameters;
  
  private final boolean required;
  
  ListenerBinding(String paramString, List<Parameter> paramList, boolean paramBoolean) {
    this.name = paramString;
    this.parameters = Collections.unmodifiableList(new ArrayList<Parameter>(paramList));
    this.required = paramBoolean;
  }
  
  public String getDescription() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("method '");
    stringBuilder.append(this.name);
    stringBuilder.append("'");
    return stringBuilder.toString();
  }
  
  public String getName() {
    return this.name;
  }
  
  public List<Parameter> getParameters() {
    return this.parameters;
  }
  
  public boolean isRequired() {
    return this.required;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\internal\ListenerBinding.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */