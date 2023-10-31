package okhttp3;

import java.io.IOException;

public interface Authenticator {
  public static final Authenticator NONE = new Authenticator() {
      public Request authenticate(Route param1Route, Response param1Response) {
        return null;
      }
    };
  
  Request authenticate(Route paramRoute, Response paramResponse) throws IOException;
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Authenticator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */