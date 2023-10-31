package androidx.lifecycle;

import androidx.arch.core.util.Function;

public class Transformations {
  public static <X, Y> LiveData<Y> map(LiveData<X> paramLiveData, final Function<X, Y> mapFunction) {
    final MediatorLiveData<Y> result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          final Function val$mapFunction;
          
          final MediatorLiveData val$result;
          
          public void onChanged(X param1X) {
            result.setValue(mapFunction.apply(param1X));
          }
        });
    return mediatorLiveData;
  }
  
  public static <X, Y> LiveData<Y> switchMap(LiveData<X> paramLiveData, final Function<X, LiveData<Y>> switchMapFunction) {
    final MediatorLiveData<Y> result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          LiveData<Y> mSource;
          
          final MediatorLiveData val$result;
          
          final Function val$switchMapFunction;
          
          public void onChanged(X param1X) {
            LiveData<Y> liveData2 = (LiveData)switchMapFunction.apply(param1X);
            LiveData<Y> liveData1 = this.mSource;
            if (liveData1 == liveData2)
              return; 
            if (liveData1 != null)
              result.removeSource(liveData1); 
            this.mSource = liveData2;
            if (liveData2 != null)
              result.addSource(liveData2, new Observer() {
                    final Transformations.null this$0;
                    
                    public void onChanged(Y param2Y) {
                      result.setValue(param2Y);
                    }
                  }); 
          }
        });
    return mediatorLiveData;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\Transformations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */