package androidx.lifecycle;

class FullLifecycleObserverAdapter implements LifecycleEventObserver {
  private final FullLifecycleObserver mFullLifecycleObserver;
  
  private final LifecycleEventObserver mLifecycleEventObserver;
  
  FullLifecycleObserverAdapter(FullLifecycleObserver paramFullLifecycleObserver, LifecycleEventObserver paramLifecycleEventObserver) {
    this.mFullLifecycleObserver = paramFullLifecycleObserver;
    this.mLifecycleEventObserver = paramLifecycleEventObserver;
  }
  
  public void onStateChanged(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent) {
    switch (paramEvent) {
      case null:
        throw new IllegalArgumentException("ON_ANY must not been send by anybody");
      case null:
        this.mFullLifecycleObserver.onDestroy(paramLifecycleOwner);
        break;
      case null:
        this.mFullLifecycleObserver.onStop(paramLifecycleOwner);
        break;
      case null:
        this.mFullLifecycleObserver.onPause(paramLifecycleOwner);
        break;
      case null:
        this.mFullLifecycleObserver.onResume(paramLifecycleOwner);
        break;
      case null:
        this.mFullLifecycleObserver.onStart(paramLifecycleOwner);
        break;
      case null:
        this.mFullLifecycleObserver.onCreate(paramLifecycleOwner);
        break;
    } 
    LifecycleEventObserver lifecycleEventObserver = this.mLifecycleEventObserver;
    if (lifecycleEventObserver != null)
      lifecycleEventObserver.onStateChanged(paramLifecycleOwner, paramEvent); 
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\FullLifecycleObserverAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */