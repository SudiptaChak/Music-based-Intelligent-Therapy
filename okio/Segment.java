package okio;

final class Segment {
  static final int SIZE = 2048;
  
  final byte[] data = new byte[2048];
  
  int limit;
  
  Segment next;
  
  boolean owner;
  
  int pos;
  
  Segment prev;
  
  boolean shared;
  
  Segment() {
    this.owner = true;
    this.shared = false;
  }
  
  Segment(Segment paramSegment) {
    this(paramSegment.data, paramSegment.pos, paramSegment.limit);
    paramSegment.shared = true;
  }
  
  Segment(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    this.pos = paramInt1;
    this.limit = paramInt2;
    this.owner = false;
    this.shared = true;
  }
  
  public void compact() {
    Segment segment = this.prev;
    if (segment != this) {
      int i;
      if (!segment.owner)
        return; 
      int k = this.limit - this.pos;
      int j = segment.limit;
      if (segment.shared) {
        i = 0;
      } else {
        i = segment.pos;
      } 
      if (k > 2048 - j + i)
        return; 
      writeTo(this.prev, k);
      pop();
      SegmentPool.recycle(this);
      return;
    } 
    throw new IllegalStateException();
  }
  
  public Segment pop() {
    Segment segment1 = this.next;
    if (segment1 == this)
      segment1 = null; 
    Segment segment2 = this.prev;
    segment2.next = this.next;
    this.next.prev = segment2;
    this.next = null;
    this.prev = null;
    return segment1;
  }
  
  public Segment push(Segment paramSegment) {
    paramSegment.prev = this;
    paramSegment.next = this.next;
    this.next.prev = paramSegment;
    this.next = paramSegment;
    return paramSegment;
  }
  
  public Segment split(int paramInt) {
    if (paramInt > 0 && paramInt <= this.limit - this.pos) {
      Segment segment = new Segment(this);
      segment.limit = segment.pos + paramInt;
      this.pos += paramInt;
      this.prev.push(segment);
      return segment;
    } 
    throw new IllegalArgumentException();
  }
  
  public void writeTo(Segment paramSegment, int paramInt) {
    if (paramSegment.owner) {
      int i = paramSegment.limit;
      if (i + paramInt > 2048)
        if (!paramSegment.shared) {
          int j = paramSegment.pos;
          if (i + paramInt - j <= 2048) {
            byte[] arrayOfByte = paramSegment.data;
            System.arraycopy(arrayOfByte, j, arrayOfByte, 0, i - j);
            paramSegment.limit -= paramSegment.pos;
            paramSegment.pos = 0;
          } else {
            throw new IllegalArgumentException();
          } 
        } else {
          throw new IllegalArgumentException();
        }  
      System.arraycopy(this.data, this.pos, paramSegment.data, paramSegment.limit, paramInt);
      paramSegment.limit += paramInt;
      this.pos += paramInt;
      return;
    } 
    throw new IllegalArgumentException();
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\okio\Segment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */