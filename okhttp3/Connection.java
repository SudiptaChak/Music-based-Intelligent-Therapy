package okhttp3;

import java.net.Socket;

public interface Connection {
  Handshake handshake();
  
  Protocol protocol();
  
  Route route();
  
  Socket socket();
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */