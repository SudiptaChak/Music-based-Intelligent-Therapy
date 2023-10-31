package okhttp3.internal.framed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSource;
import okio.InflaterSource;
import okio.Okio;
import okio.Source;

class NameValueBlockReader {
  private int compressedLimit;
  
  private final InflaterSource inflaterSource;
  
  private final BufferedSource source;
  
  public NameValueBlockReader(BufferedSource paramBufferedSource) {
    InflaterSource inflaterSource = new InflaterSource((Source)new ForwardingSource((Source)paramBufferedSource) {
          final NameValueBlockReader this$0;
          
          public long read(Buffer param1Buffer, long param1Long) throws IOException {
            if (NameValueBlockReader.this.compressedLimit == 0)
              return -1L; 
            param1Long = super.read(param1Buffer, Math.min(param1Long, NameValueBlockReader.this.compressedLimit));
            if (param1Long == -1L)
              return -1L; 
            NameValueBlockReader nameValueBlockReader = NameValueBlockReader.this;
            NameValueBlockReader.access$002(nameValueBlockReader, (int)(nameValueBlockReader.compressedLimit - param1Long));
            return param1Long;
          }
        }new Inflater() {
          final NameValueBlockReader this$0;
          
          public int inflate(byte[] param1ArrayOfbyte, int param1Int1, int param1Int2) throws DataFormatException {
            int j = super.inflate(param1ArrayOfbyte, param1Int1, param1Int2);
            int i = j;
            if (j == 0) {
              i = j;
              if (needsDictionary()) {
                setDictionary(Spdy3.DICTIONARY);
                i = super.inflate(param1ArrayOfbyte, param1Int1, param1Int2);
              } 
            } 
            return i;
          }
        });
    this.inflaterSource = inflaterSource;
    this.source = Okio.buffer((Source)inflaterSource);
  }
  
  private void doneReading() throws IOException {
    if (this.compressedLimit > 0) {
      this.inflaterSource.refill();
      if (this.compressedLimit != 0) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("compressedLimit > 0: ");
        stringBuilder.append(this.compressedLimit);
        throw new IOException(stringBuilder.toString());
      } 
    } 
  }
  
  private ByteString readByteString() throws IOException {
    int i = this.source.readInt();
    return this.source.readByteString(i);
  }
  
  public void close() throws IOException {
    this.source.close();
  }
  
  public List<Header> readNameValueBlock(int paramInt) throws IOException {
    this.compressedLimit += paramInt;
    int i = this.source.readInt();
    if (i >= 0) {
      if (i <= 1024) {
        ArrayList<Header> arrayList = new ArrayList(i);
        paramInt = 0;
        while (paramInt < i) {
          ByteString byteString2 = readByteString().toAsciiLowercase();
          ByteString byteString1 = readByteString();
          if (byteString2.size() != 0) {
            arrayList.add(new Header(byteString2, byteString1));
            paramInt++;
            continue;
          } 
          throw new IOException("name.size == 0");
        } 
        doneReading();
        return arrayList;
      } 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("numberOfPairs > 1024: ");
      stringBuilder1.append(i);
      throw new IOException(stringBuilder1.toString());
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("numberOfPairs < 0: ");
    stringBuilder.append(i);
    throw new IOException(stringBuilder.toString());
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okhttp3\internal\framed\NameValueBlockReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */