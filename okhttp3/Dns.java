package okhttp3;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public interface Dns {
  public static final Dns SYSTEM = new Dns() {
      public List<InetAddress> lookup(String param1String) throws UnknownHostException {
        if (param1String != null)
          return Arrays.asList(InetAddress.getAllByName(param1String)); 
        throw new UnknownHostException("hostname == null");
      }
    };
  
  List<InetAddress> lookup(String paramString) throws UnknownHostException;
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\Dns.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */