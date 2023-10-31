package okhttp3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;

public final class FormBody extends RequestBody {
  private static final MediaType CONTENT_TYPE = MediaType.parse("application/x-www-form-urlencoded");
  
  private final List<String> encodedNames;
  
  private final List<String> encodedValues;
  
  private FormBody(List<String> paramList1, List<String> paramList2) {
    this.encodedNames = Util.immutableList(paramList1);
    this.encodedValues = Util.immutableList(paramList2);
  }
  
  private long writeOrCountBytes(BufferedSink paramBufferedSink, boolean paramBoolean) {
    Buffer buffer;
    long l;
    if (paramBoolean) {
      buffer = new Buffer();
    } else {
      buffer = buffer.buffer();
    } 
    byte b = 0;
    int i = this.encodedNames.size();
    while (b < i) {
      if (b > 0)
        buffer.writeByte(38); 
      buffer.writeUtf8(this.encodedNames.get(b));
      buffer.writeByte(61);
      buffer.writeUtf8(this.encodedValues.get(b));
      b++;
    } 
    if (paramBoolean) {
      l = buffer.size();
      buffer.clear();
    } else {
      l = 0L;
    } 
    return l;
  }
  
  public long contentLength() {
    return writeOrCountBytes(null, true);
  }
  
  public MediaType contentType() {
    return CONTENT_TYPE;
  }
  
  public String encodedName(int paramInt) {
    return this.encodedNames.get(paramInt);
  }
  
  public String encodedValue(int paramInt) {
    return this.encodedValues.get(paramInt);
  }
  
  public String name(int paramInt) {
    return HttpUrl.percentDecode(encodedName(paramInt), true);
  }
  
  public int size() {
    return this.encodedNames.size();
  }
  
  public String value(int paramInt) {
    return HttpUrl.percentDecode(encodedValue(paramInt), true);
  }
  
  public void writeTo(BufferedSink paramBufferedSink) throws IOException {
    writeOrCountBytes(paramBufferedSink, false);
  }
  
  public static final class Builder {
    private final List<String> names = new ArrayList<String>();
    
    private final List<String> values = new ArrayList<String>();
    
    public Builder add(String param1String1, String param1String2) {
      this.names.add(HttpUrl.canonicalize(param1String1, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true));
      this.values.add(HttpUrl.canonicalize(param1String2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", false, false, true, true));
      return this;
    }
    
    public Builder addEncoded(String param1String1, String param1String2) {
      this.names.add(HttpUrl.canonicalize(param1String1, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true));
      this.values.add(HttpUrl.canonicalize(param1String2, " \"':;<=>@[]^`{}|/\\?#&!$(),~", true, false, true, true));
      return this;
    }
    
    public FormBody build() {
      return new FormBody(this.names, this.values);
    }
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\FormBody.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */