package androidx.lifecycle;

import android.app.Application;

public class AndroidViewModel extends ViewModel {
  private Application mApplication;
  
  public AndroidViewModel(Application paramApplication) {
    this.mApplication = paramApplication;
  }
  
  public <T extends Application> T getApplication() {
    return (T)this.mApplication;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\lifecycle\AndroidViewModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */