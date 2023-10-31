package butterknife;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.util.Property;
import android.view.View;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ButterKnife {
  static final Map<Class<?>, Injector<Object>> INJECTORS = new LinkedHashMap<Class<?>, Injector<Object>>();
  
  static final Injector<Object> NOP_INJECTOR = new Injector() {
      public void inject(ButterKnife.Finder param1Finder, Object param1Object1, Object param1Object2) {}
      
      public void reset(Object param1Object) {}
    };
  
  private static final String TAG = "ButterKnife";
  
  private static boolean debug = false;
  
  private ButterKnife() {
    throw new AssertionError("No instances.");
  }
  
  public static <T extends View, V> void apply(List<T> paramList, Property<? super T, V> paramProperty, V paramV) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++)
      paramProperty.set(paramList.get(b), paramV); 
  }
  
  public static <T extends View> void apply(List<T> paramList, Action<? super T> paramAction) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++)
      paramAction.apply(paramList.get(b), b); 
  }
  
  public static <T extends View, V> void apply(List<T> paramList, Setter<? super T, V> paramSetter, V paramV) {
    int i = paramList.size();
    for (byte b = 0; b < i; b++)
      paramSetter.set(paramList.get(b), paramV, b); 
  }
  
  public static <T extends View> T findById(Activity paramActivity, int paramInt) {
    return (T)paramActivity.findViewById(paramInt);
  }
  
  public static <T extends View> T findById(Dialog paramDialog, int paramInt) {
    return (T)paramDialog.findViewById(paramInt);
  }
  
  public static <T extends View> T findById(View paramView, int paramInt) {
    return (T)paramView.findViewById(paramInt);
  }
  
  private static Injector<Object> findInjectorForClass(Class<?> paramClass) throws IllegalAccessException, InstantiationException {
    Injector<Object> injector1;
    Injector<Object> injector2 = INJECTORS.get(paramClass);
    if (injector2 != null) {
      if (debug)
        Log.d("ButterKnife", "HIT: Cached in injector map."); 
      return injector2;
    } 
    String str = paramClass.getName();
    if (str.startsWith("android.") || str.startsWith("java.")) {
      if (debug)
        Log.d("ButterKnife", "MISS: Reached framework class. Abandoning search."); 
      return NOP_INJECTOR;
    } 
    try {
      StringBuilder stringBuilder = new StringBuilder();
      this();
      stringBuilder.append(str);
      stringBuilder.append("$$ViewInjector");
      Injector injector = (Injector)Class.forName(stringBuilder.toString()).newInstance();
      injector1 = injector;
      if (debug) {
        Log.d("ButterKnife", "HIT: Class loaded injection class.");
        injector1 = injector;
      } 
    } catch (ClassNotFoundException classNotFoundException) {
      if (debug) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Not found. Trying superclass ");
        stringBuilder.append(paramClass.getSuperclass().getName());
        Log.d("ButterKnife", stringBuilder.toString());
      } 
      injector1 = findInjectorForClass(paramClass.getSuperclass());
    } 
    INJECTORS.put(paramClass, injector1);
    return injector1;
  }
  
  public static void inject(Activity paramActivity) {
    inject(paramActivity, paramActivity, Finder.ACTIVITY);
  }
  
  public static void inject(Dialog paramDialog) {
    inject(paramDialog, paramDialog, Finder.DIALOG);
  }
  
  public static void inject(View paramView) {
    inject(paramView, paramView, Finder.VIEW);
  }
  
  public static void inject(Object paramObject, Activity paramActivity) {
    inject(paramObject, paramActivity, Finder.ACTIVITY);
  }
  
  public static void inject(Object paramObject, Dialog paramDialog) {
    inject(paramObject, paramDialog, Finder.DIALOG);
  }
  
  public static void inject(Object paramObject, View paramView) {
    inject(paramObject, paramView, Finder.VIEW);
  }
  
  static void inject(Object paramObject1, Object paramObject2, Finder paramFinder) {
    Class<?> clazz = paramObject1.getClass();
    try {
      if (debug) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Looking up view injector for ");
        stringBuilder.append(clazz.getName());
        Log.d("ButterKnife", stringBuilder.toString());
      } 
      Injector<Object> injector = findInjectorForClass(clazz);
      if (injector != null)
        injector.inject(paramFinder, paramObject1, paramObject2); 
      return;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to inject views for ");
      stringBuilder.append(runtimeException);
      throw new RuntimeException(stringBuilder.toString(), exception);
    } 
  }
  
  public static void reset(Object paramObject) {
    Class<?> clazz = paramObject.getClass();
    try {
      if (debug) {
        StringBuilder stringBuilder = new StringBuilder();
        this();
        stringBuilder.append("Looking up view injector for ");
        stringBuilder.append(clazz.getName());
        Log.d("ButterKnife", stringBuilder.toString());
      } 
      Injector<Object> injector = findInjectorForClass(clazz);
      if (injector != null)
        injector.reset(paramObject); 
      return;
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to reset views for ");
      stringBuilder.append(runtimeException);
      throw new RuntimeException(stringBuilder.toString(), exception);
    } 
  }
  
  public static void setDebug(boolean paramBoolean) {
    debug = paramBoolean;
  }
  
  public static interface Action<T extends View> {
    void apply(T param1T, int param1Int);
  }
  
  public enum Finder {
    ACTIVITY,
    DIALOG,
    VIEW {
      protected View findView(Object param2Object, int param2Int) {
        return ((View)param2Object).findViewById(param2Int);
      }
      
      protected Context getContext(Object param2Object) {
        return ((View)param2Object).getContext();
      }
    };
    
    private static final Finder[] $VALUES;
    
    static {
      null  = new null("DIALOG", 2);
      DIALOG = ;
      $VALUES = new Finder[] { VIEW, ACTIVITY,  };
    }
    
    public static <T> T[] arrayOf(T... param1VarArgs) {
      return param1VarArgs;
    }
    
    public static <T> List<T> listOf(T... param1VarArgs) {
      return new ImmutableList<T>(param1VarArgs);
    }
    
    public <T> T castParam(Object param1Object, String param1String1, int param1Int1, String param1String2, int param1Int2) {
      return (T)param1Object;
    }
    
    public <T> T castView(View param1View, int param1Int, String param1String) {
      return (T)param1View;
    }
    
    public <T> T findOptionalView(Object param1Object, int param1Int, String param1String) {
      return castView(findView(param1Object, param1Int), param1Int, param1String);
    }
    
    public <T> T findRequiredView(Object param1Object, int param1Int, String param1String) {
      T t = (T)findOptionalView(param1Object, param1Int, param1String);
      if (t != null)
        return t; 
      param1Object = getContext(param1Object).getResources().getResourceEntryName(param1Int);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Required view '");
      stringBuilder.append((String)param1Object);
      stringBuilder.append("' with ID ");
      stringBuilder.append(param1Int);
      stringBuilder.append(" for ");
      stringBuilder.append(param1String);
      stringBuilder.append(" was not found. If this view is optional add '@Optional' annotation.");
      throw new IllegalStateException(stringBuilder.toString());
    }
    
    protected abstract View findView(Object param1Object, int param1Int);
    
    protected abstract Context getContext(Object param1Object);
  }
  
  enum null {
    protected View findView(Object param1Object, int param1Int) {
      return ((View)param1Object).findViewById(param1Int);
    }
    
    protected Context getContext(Object param1Object) {
      return ((View)param1Object).getContext();
    }
  }
  
  enum null {
    protected View findView(Object param1Object, int param1Int) {
      return ((Activity)param1Object).findViewById(param1Int);
    }
    
    protected Context getContext(Object param1Object) {
      return (Context)param1Object;
    }
  }
  
  enum null {
    protected View findView(Object param1Object, int param1Int) {
      return ((Dialog)param1Object).findViewById(param1Int);
    }
    
    protected Context getContext(Object param1Object) {
      return ((Dialog)param1Object).getContext();
    }
  }
  
  public static interface Injector<T> {
    void inject(ButterKnife.Finder param1Finder, T param1T, Object param1Object);
    
    void reset(T param1T);
  }
  
  public static interface Setter<T extends View, V> {
    void set(T param1T, V param1V, int param1Int);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\ButterKnife.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */