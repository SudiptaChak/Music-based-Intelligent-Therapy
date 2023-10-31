package androidx.recyclerview.widget;

import java.util.List;

class OpReorderer {
  final Callback mCallback;
  
  OpReorderer(Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  private int getLastMoveOutOfOrder(List<AdapterHelper.UpdateOp> paramList) {
    int i = paramList.size() - 1;
    for (boolean bool = false; i >= 0; bool = bool1) {
      boolean bool1;
      if (((AdapterHelper.UpdateOp)paramList.get(i)).cmd == 8) {
        bool1 = bool;
        if (bool)
          return i; 
      } else {
        bool1 = true;
      } 
      i--;
    } 
    return -1;
  }
  
  private void swapMoveAdd(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    byte b;
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart) {
      b = -1;
    } else {
      b = 0;
    } 
    int i = b;
    if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart)
      i = b + 1; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.positionStart)
      paramUpdateOp1.positionStart += paramUpdateOp2.itemCount; 
    if (paramUpdateOp2.positionStart <= paramUpdateOp1.itemCount)
      paramUpdateOp1.itemCount += paramUpdateOp2.itemCount; 
    paramUpdateOp2.positionStart += i;
    paramList.set(paramInt1, paramUpdateOp2);
    paramList.set(paramInt2, paramUpdateOp1);
  }
  
  private void swapMoveOp(List<AdapterHelper.UpdateOp> paramList, int paramInt1, int paramInt2) {
    AdapterHelper.UpdateOp updateOp2 = paramList.get(paramInt1);
    AdapterHelper.UpdateOp updateOp1 = paramList.get(paramInt2);
    int i = updateOp1.cmd;
    if (i != 1) {
      if (i != 2) {
        if (i == 4)
          swapMoveUpdate(paramList, paramInt1, updateOp2, paramInt2, updateOp1); 
      } else {
        swapMoveRemove(paramList, paramInt1, updateOp2, paramInt2, updateOp1);
      } 
    } else {
      swapMoveAdd(paramList, paramInt1, updateOp2, paramInt2, updateOp1);
    } 
  }
  
  void reorderOps(List<AdapterHelper.UpdateOp> paramList) {
    while (true) {
      int i = getLastMoveOutOfOrder(paramList);
      if (i != -1) {
        swapMoveOp(paramList, i, i + 1);
        continue;
      } 
      break;
    } 
  }
  
  void swapMoveRemove(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    int j = paramUpdateOp1.positionStart;
    int i = paramUpdateOp1.itemCount;
    boolean bool = false;
    if (j < i) {
      if (paramUpdateOp2.positionStart == paramUpdateOp1.positionStart && paramUpdateOp2.itemCount == paramUpdateOp1.itemCount - paramUpdateOp1.positionStart) {
        i = 0;
        bool = true;
      } else {
        i = 0;
      } 
    } else if (paramUpdateOp2.positionStart == paramUpdateOp1.itemCount + 1 && paramUpdateOp2.itemCount == paramUpdateOp1.positionStart - paramUpdateOp1.itemCount) {
      i = 1;
      bool = true;
    } else {
      i = 1;
    } 
    if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart) {
      paramUpdateOp2.positionStart--;
    } else if (paramUpdateOp1.itemCount < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      paramUpdateOp2.itemCount--;
      paramUpdateOp1.cmd = 2;
      paramUpdateOp1.itemCount = 1;
      if (paramUpdateOp2.itemCount == 0) {
        paramList.remove(paramInt2);
        this.mCallback.recycleUpdateOp(paramUpdateOp2);
      } 
      return;
    } 
    j = paramUpdateOp1.positionStart;
    int k = paramUpdateOp2.positionStart;
    AdapterHelper.UpdateOp updateOp = null;
    if (j <= k) {
      paramUpdateOp2.positionStart++;
    } else if (paramUpdateOp1.positionStart < paramUpdateOp2.positionStart + paramUpdateOp2.itemCount) {
      int m = paramUpdateOp2.positionStart;
      j = paramUpdateOp2.itemCount;
      k = paramUpdateOp1.positionStart;
      updateOp = this.mCallback.obtainUpdateOp(2, paramUpdateOp1.positionStart + 1, m + j - k, null);
      paramUpdateOp2.itemCount = paramUpdateOp1.positionStart - paramUpdateOp2.positionStart;
    } 
    if (bool) {
      paramList.set(paramInt1, paramUpdateOp2);
      paramList.remove(paramInt2);
      this.mCallback.recycleUpdateOp(paramUpdateOp1);
      return;
    } 
    if (i != 0) {
      if (updateOp != null) {
        if (paramUpdateOp1.positionStart > updateOp.positionStart)
          paramUpdateOp1.positionStart -= updateOp.itemCount; 
        if (paramUpdateOp1.itemCount > updateOp.positionStart)
          paramUpdateOp1.itemCount -= updateOp.itemCount; 
      } 
      if (paramUpdateOp1.positionStart > paramUpdateOp2.positionStart)
        paramUpdateOp1.positionStart -= paramUpdateOp2.itemCount; 
      if (paramUpdateOp1.itemCount > paramUpdateOp2.positionStart)
        paramUpdateOp1.itemCount -= paramUpdateOp2.itemCount; 
    } else {
      if (updateOp != null) {
        if (paramUpdateOp1.positionStart >= updateOp.positionStart)
          paramUpdateOp1.positionStart -= updateOp.itemCount; 
        if (paramUpdateOp1.itemCount >= updateOp.positionStart)
          paramUpdateOp1.itemCount -= updateOp.itemCount; 
      } 
      if (paramUpdateOp1.positionStart >= paramUpdateOp2.positionStart)
        paramUpdateOp1.positionStart -= paramUpdateOp2.itemCount; 
      if (paramUpdateOp1.itemCount >= paramUpdateOp2.positionStart)
        paramUpdateOp1.itemCount -= paramUpdateOp2.itemCount; 
    } 
    paramList.set(paramInt1, paramUpdateOp2);
    if (paramUpdateOp1.positionStart != paramUpdateOp1.itemCount) {
      paramList.set(paramInt2, paramUpdateOp1);
    } else {
      paramList.remove(paramInt2);
    } 
    if (updateOp != null)
      paramList.add(paramInt1, updateOp); 
  }
  
  void swapMoveUpdate(List<AdapterHelper.UpdateOp> paramList, int paramInt1, AdapterHelper.UpdateOp paramUpdateOp1, int paramInt2, AdapterHelper.UpdateOp paramUpdateOp2) {
    // Byte code:
    //   0: aload_3
    //   1: getfield itemCount : I
    //   4: istore #7
    //   6: aload #5
    //   8: getfield positionStart : I
    //   11: istore #6
    //   13: aconst_null
    //   14: astore #9
    //   16: iload #7
    //   18: iload #6
    //   20: if_icmpge -> 38
    //   23: aload #5
    //   25: aload #5
    //   27: getfield positionStart : I
    //   30: iconst_1
    //   31: isub
    //   32: putfield positionStart : I
    //   35: goto -> 93
    //   38: aload_3
    //   39: getfield itemCount : I
    //   42: aload #5
    //   44: getfield positionStart : I
    //   47: aload #5
    //   49: getfield itemCount : I
    //   52: iadd
    //   53: if_icmpge -> 93
    //   56: aload #5
    //   58: aload #5
    //   60: getfield itemCount : I
    //   63: iconst_1
    //   64: isub
    //   65: putfield itemCount : I
    //   68: aload_0
    //   69: getfield mCallback : Landroidx/recyclerview/widget/OpReorderer$Callback;
    //   72: iconst_4
    //   73: aload_3
    //   74: getfield positionStart : I
    //   77: iconst_1
    //   78: aload #5
    //   80: getfield payload : Ljava/lang/Object;
    //   83: invokeinterface obtainUpdateOp : (IIILjava/lang/Object;)Landroidx/recyclerview/widget/AdapterHelper$UpdateOp;
    //   88: astore #8
    //   90: goto -> 96
    //   93: aconst_null
    //   94: astore #8
    //   96: aload_3
    //   97: getfield positionStart : I
    //   100: aload #5
    //   102: getfield positionStart : I
    //   105: if_icmpgt -> 123
    //   108: aload #5
    //   110: aload #5
    //   112: getfield positionStart : I
    //   115: iconst_1
    //   116: iadd
    //   117: putfield positionStart : I
    //   120: goto -> 197
    //   123: aload_3
    //   124: getfield positionStart : I
    //   127: aload #5
    //   129: getfield positionStart : I
    //   132: aload #5
    //   134: getfield itemCount : I
    //   137: iadd
    //   138: if_icmpge -> 197
    //   141: aload #5
    //   143: getfield positionStart : I
    //   146: aload #5
    //   148: getfield itemCount : I
    //   151: iadd
    //   152: aload_3
    //   153: getfield positionStart : I
    //   156: isub
    //   157: istore #6
    //   159: aload_0
    //   160: getfield mCallback : Landroidx/recyclerview/widget/OpReorderer$Callback;
    //   163: iconst_4
    //   164: aload_3
    //   165: getfield positionStart : I
    //   168: iconst_1
    //   169: iadd
    //   170: iload #6
    //   172: aload #5
    //   174: getfield payload : Ljava/lang/Object;
    //   177: invokeinterface obtainUpdateOp : (IIILjava/lang/Object;)Landroidx/recyclerview/widget/AdapterHelper$UpdateOp;
    //   182: astore #9
    //   184: aload #5
    //   186: aload #5
    //   188: getfield itemCount : I
    //   191: iload #6
    //   193: isub
    //   194: putfield itemCount : I
    //   197: aload_1
    //   198: iload #4
    //   200: aload_3
    //   201: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   206: pop
    //   207: aload #5
    //   209: getfield itemCount : I
    //   212: ifle -> 228
    //   215: aload_1
    //   216: iload_2
    //   217: aload #5
    //   219: invokeinterface set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   224: pop
    //   225: goto -> 247
    //   228: aload_1
    //   229: iload_2
    //   230: invokeinterface remove : (I)Ljava/lang/Object;
    //   235: pop
    //   236: aload_0
    //   237: getfield mCallback : Landroidx/recyclerview/widget/OpReorderer$Callback;
    //   240: aload #5
    //   242: invokeinterface recycleUpdateOp : (Landroidx/recyclerview/widget/AdapterHelper$UpdateOp;)V
    //   247: aload #8
    //   249: ifnull -> 261
    //   252: aload_1
    //   253: iload_2
    //   254: aload #8
    //   256: invokeinterface add : (ILjava/lang/Object;)V
    //   261: aload #9
    //   263: ifnull -> 275
    //   266: aload_1
    //   267: iload_2
    //   268: aload #9
    //   270: invokeinterface add : (ILjava/lang/Object;)V
    //   275: return
  }
  
  static interface Callback {
    AdapterHelper.UpdateOp obtainUpdateOp(int param1Int1, int param1Int2, int param1Int3, Object param1Object);
    
    void recycleUpdateOp(AdapterHelper.UpdateOp param1UpdateOp);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\OpReorderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */