package androidx.recyclerview.widget;

import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.core.util.Pools;

class ViewInfoStore {
  private static final boolean DEBUG = false;
  
  final ArrayMap<RecyclerView.ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap();
  
  final LongSparseArray<RecyclerView.ViewHolder> mOldChangedHolders = new LongSparseArray();
  
  private RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(RecyclerView.ViewHolder paramViewHolder, int paramInt) {
    int i = this.mLayoutHolderMap.indexOfKey(paramViewHolder);
    if (i < 0)
      return null; 
    InfoRecord infoRecord = (InfoRecord)this.mLayoutHolderMap.valueAt(i);
    if (infoRecord != null && (infoRecord.flags & paramInt) != 0) {
      RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo;
      infoRecord.flags &= paramInt ^ 0xFFFFFFFF;
      if (paramInt == 4) {
        itemHolderInfo = infoRecord.preInfo;
      } else if (paramInt == 8) {
        itemHolderInfo = infoRecord.postInfo;
      } else {
        throw new IllegalArgumentException("Must provide flag PRE or POST");
      } 
      if ((infoRecord.flags & 0xC) == 0) {
        this.mLayoutHolderMap.removeAt(i);
        InfoRecord.recycle(infoRecord);
      } 
      return itemHolderInfo;
    } 
    return null;
  }
  
  void addToAppearedInPreLayoutHolders(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo) {
    InfoRecord infoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord infoRecord1 = infoRecord2;
    if (infoRecord2 == null) {
      infoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, infoRecord1);
    } 
    infoRecord1.flags |= 0x2;
    infoRecord1.preInfo = paramItemHolderInfo;
  }
  
  void addToDisappearedInLayout(RecyclerView.ViewHolder paramViewHolder) {
    InfoRecord infoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord infoRecord1 = infoRecord2;
    if (infoRecord2 == null) {
      infoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, infoRecord1);
    } 
    infoRecord1.flags |= 0x1;
  }
  
  void addToOldChangeHolders(long paramLong, RecyclerView.ViewHolder paramViewHolder) {
    this.mOldChangedHolders.put(paramLong, paramViewHolder);
  }
  
  void addToPostLayout(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo) {
    InfoRecord infoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord infoRecord1 = infoRecord2;
    if (infoRecord2 == null) {
      infoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, infoRecord1);
    } 
    infoRecord1.postInfo = paramItemHolderInfo;
    infoRecord1.flags |= 0x8;
  }
  
  void addToPreLayout(RecyclerView.ViewHolder paramViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo paramItemHolderInfo) {
    InfoRecord infoRecord2 = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    InfoRecord infoRecord1 = infoRecord2;
    if (infoRecord2 == null) {
      infoRecord1 = InfoRecord.obtain();
      this.mLayoutHolderMap.put(paramViewHolder, infoRecord1);
    } 
    infoRecord1.preInfo = paramItemHolderInfo;
    infoRecord1.flags |= 0x4;
  }
  
  void clear() {
    this.mLayoutHolderMap.clear();
    this.mOldChangedHolders.clear();
  }
  
  RecyclerView.ViewHolder getFromOldChangeHolders(long paramLong) {
    return (RecyclerView.ViewHolder)this.mOldChangedHolders.get(paramLong);
  }
  
  boolean isDisappearing(RecyclerView.ViewHolder paramViewHolder) {
    InfoRecord infoRecord = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    boolean bool = true;
    if (infoRecord == null || (infoRecord.flags & 0x1) == 0)
      bool = false; 
    return bool;
  }
  
  boolean isInPreLayout(RecyclerView.ViewHolder paramViewHolder) {
    boolean bool;
    InfoRecord infoRecord = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    if (infoRecord != null && (infoRecord.flags & 0x4) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
  
  void onDetach() {
    InfoRecord.drainCache();
  }
  
  public void onViewDetached(RecyclerView.ViewHolder paramViewHolder) {
    removeFromDisappearedInLayout(paramViewHolder);
  }
  
  RecyclerView.ItemAnimator.ItemHolderInfo popFromPostLayout(RecyclerView.ViewHolder paramViewHolder) {
    return popFromLayoutStep(paramViewHolder, 8);
  }
  
  RecyclerView.ItemAnimator.ItemHolderInfo popFromPreLayout(RecyclerView.ViewHolder paramViewHolder) {
    return popFromLayoutStep(paramViewHolder, 4);
  }
  
  void process(ProcessCallback paramProcessCallback) {
    for (int i = this.mLayoutHolderMap.size() - 1; i >= 0; i--) {
      RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)this.mLayoutHolderMap.keyAt(i);
      InfoRecord infoRecord = (InfoRecord)this.mLayoutHolderMap.removeAt(i);
      if ((infoRecord.flags & 0x3) == 3) {
        paramProcessCallback.unused(viewHolder);
      } else if ((infoRecord.flags & 0x1) != 0) {
        if (infoRecord.preInfo == null) {
          paramProcessCallback.unused(viewHolder);
        } else {
          paramProcessCallback.processDisappeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
        } 
      } else if ((infoRecord.flags & 0xE) == 14) {
        paramProcessCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
      } else if ((infoRecord.flags & 0xC) == 12) {
        paramProcessCallback.processPersistent(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
      } else if ((infoRecord.flags & 0x4) != 0) {
        paramProcessCallback.processDisappeared(viewHolder, infoRecord.preInfo, null);
      } else if ((infoRecord.flags & 0x8) != 0) {
        paramProcessCallback.processAppeared(viewHolder, infoRecord.preInfo, infoRecord.postInfo);
      } else {
        int j = infoRecord.flags;
      } 
      InfoRecord.recycle(infoRecord);
    } 
  }
  
  void removeFromDisappearedInLayout(RecyclerView.ViewHolder paramViewHolder) {
    InfoRecord infoRecord = (InfoRecord)this.mLayoutHolderMap.get(paramViewHolder);
    if (infoRecord == null)
      return; 
    infoRecord.flags &= 0xFFFFFFFE;
  }
  
  void removeViewHolder(RecyclerView.ViewHolder paramViewHolder) {
    for (int i = this.mOldChangedHolders.size() - 1; i >= 0; i--) {
      if (paramViewHolder == this.mOldChangedHolders.valueAt(i)) {
        this.mOldChangedHolders.removeAt(i);
        break;
      } 
    } 
    InfoRecord infoRecord = (InfoRecord)this.mLayoutHolderMap.remove(paramViewHolder);
    if (infoRecord != null)
      InfoRecord.recycle(infoRecord); 
  }
  
  static class InfoRecord {
    static final int FLAG_APPEAR = 2;
    
    static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
    
    static final int FLAG_APPEAR_PRE_AND_POST = 14;
    
    static final int FLAG_DISAPPEARED = 1;
    
    static final int FLAG_POST = 8;
    
    static final int FLAG_PRE = 4;
    
    static final int FLAG_PRE_AND_POST = 12;
    
    static Pools.Pool<InfoRecord> sPool = (Pools.Pool<InfoRecord>)new Pools.SimplePool(20);
    
    int flags;
    
    RecyclerView.ItemAnimator.ItemHolderInfo postInfo;
    
    RecyclerView.ItemAnimator.ItemHolderInfo preInfo;
    
    static void drainCache() {
      while (sPool.acquire() != null);
    }
    
    static InfoRecord obtain() {
      InfoRecord infoRecord2 = (InfoRecord)sPool.acquire();
      InfoRecord infoRecord1 = infoRecord2;
      if (infoRecord2 == null)
        infoRecord1 = new InfoRecord(); 
      return infoRecord1;
    }
    
    static void recycle(InfoRecord param1InfoRecord) {
      param1InfoRecord.flags = 0;
      param1InfoRecord.preInfo = null;
      param1InfoRecord.postInfo = null;
      sPool.release(param1InfoRecord);
    }
  }
  
  static interface ProcessCallback {
    void processAppeared(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2);
    
    void processDisappeared(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2);
    
    void processPersistent(RecyclerView.ViewHolder param1ViewHolder, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo1, RecyclerView.ItemAnimator.ItemHolderInfo param1ItemHolderInfo2);
    
    void unused(RecyclerView.ViewHolder param1ViewHolder);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\recyclerview\widget\ViewInfoStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */