package com.google.android.material.bottomsheet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;

public class BottomSheetDialog extends AppCompatDialog {
  private BottomSheetBehavior<FrameLayout> behavior;
  
  private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
      final BottomSheetDialog this$0;
      
      public void onSlide(View param1View, float param1Float) {}
      
      public void onStateChanged(View param1View, int param1Int) {
        if (param1Int == 5)
          BottomSheetDialog.this.cancel(); 
      }
    };
  
  boolean cancelable = true;
  
  private boolean canceledOnTouchOutside = true;
  
  private boolean canceledOnTouchOutsideSet;
  
  public BottomSheetDialog(Context paramContext) {
    this(paramContext, 0);
  }
  
  public BottomSheetDialog(Context paramContext, int paramInt) {
    super(paramContext, getThemeResId(paramContext, paramInt));
    supportRequestWindowFeature(1);
  }
  
  protected BottomSheetDialog(Context paramContext, boolean paramBoolean, DialogInterface.OnCancelListener paramOnCancelListener) {
    super(paramContext, paramBoolean, paramOnCancelListener);
    supportRequestWindowFeature(1);
    this.cancelable = paramBoolean;
  }
  
  private static int getThemeResId(Context paramContext, int paramInt) {
    int i = paramInt;
    if (paramInt == 0) {
      TypedValue typedValue = new TypedValue();
      if (paramContext.getTheme().resolveAttribute(R.attr.bottomSheetDialogTheme, typedValue, true)) {
        i = typedValue.resourceId;
      } else {
        i = R.style.Theme_Design_Light_BottomSheetDialog;
      } 
    } 
    return i;
  }
  
  private View wrapInBottomSheet(int paramInt, View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    FrameLayout frameLayout2 = (FrameLayout)View.inflate(getContext(), R.layout.design_bottom_sheet_dialog, null);
    CoordinatorLayout coordinatorLayout = (CoordinatorLayout)frameLayout2.findViewById(R.id.coordinator);
    View view = paramView;
    if (paramInt != 0) {
      view = paramView;
      if (paramView == null)
        view = getLayoutInflater().inflate(paramInt, (ViewGroup)coordinatorLayout, false); 
    } 
    FrameLayout frameLayout1 = (FrameLayout)coordinatorLayout.findViewById(R.id.design_bottom_sheet);
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout1);
    this.behavior = bottomSheetBehavior;
    bottomSheetBehavior.setBottomSheetCallback(this.bottomSheetCallback);
    this.behavior.setHideable(this.cancelable);
    if (paramLayoutParams == null) {
      frameLayout1.addView(view);
    } else {
      frameLayout1.addView(view, paramLayoutParams);
    } 
    coordinatorLayout.findViewById(R.id.touch_outside).setOnClickListener(new View.OnClickListener() {
          final BottomSheetDialog this$0;
          
          public void onClick(View param1View) {
            if (BottomSheetDialog.this.cancelable && BottomSheetDialog.this.isShowing() && BottomSheetDialog.this.shouldWindowCloseOnTouchOutside())
              BottomSheetDialog.this.cancel(); 
          }
        });
    ViewCompat.setAccessibilityDelegate((View)frameLayout1, new AccessibilityDelegateCompat() {
          final BottomSheetDialog this$0;
          
          public void onInitializeAccessibilityNodeInfo(View param1View, AccessibilityNodeInfoCompat param1AccessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(param1View, param1AccessibilityNodeInfoCompat);
            if (BottomSheetDialog.this.cancelable) {
              param1AccessibilityNodeInfoCompat.addAction(1048576);
              param1AccessibilityNodeInfoCompat.setDismissable(true);
            } else {
              param1AccessibilityNodeInfoCompat.setDismissable(false);
            } 
          }
          
          public boolean performAccessibilityAction(View param1View, int param1Int, Bundle param1Bundle) {
            if (param1Int == 1048576 && BottomSheetDialog.this.cancelable) {
              BottomSheetDialog.this.cancel();
              return true;
            } 
            return super.performAccessibilityAction(param1View, param1Int, param1Bundle);
          }
        });
    frameLayout1.setOnTouchListener(new View.OnTouchListener() {
          final BottomSheetDialog this$0;
          
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            return true;
          }
        });
    return (View)frameLayout2;
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    Window window = getWindow();
    if (window != null) {
      if (Build.VERSION.SDK_INT >= 21) {
        window.clearFlags(67108864);
        window.addFlags(-2147483648);
      } 
      window.setLayout(-1, -1);
    } 
  }
  
  protected void onStart() {
    super.onStart();
    BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.behavior;
    if (bottomSheetBehavior != null && bottomSheetBehavior.getState() == 5)
      this.behavior.setState(4); 
  }
  
  public void setCancelable(boolean paramBoolean) {
    super.setCancelable(paramBoolean);
    if (this.cancelable != paramBoolean) {
      this.cancelable = paramBoolean;
      BottomSheetBehavior<FrameLayout> bottomSheetBehavior = this.behavior;
      if (bottomSheetBehavior != null)
        bottomSheetBehavior.setHideable(paramBoolean); 
    } 
  }
  
  public void setCanceledOnTouchOutside(boolean paramBoolean) {
    super.setCanceledOnTouchOutside(paramBoolean);
    if (paramBoolean && !this.cancelable)
      this.cancelable = true; 
    this.canceledOnTouchOutside = paramBoolean;
    this.canceledOnTouchOutsideSet = true;
  }
  
  public void setContentView(int paramInt) {
    super.setContentView(wrapInBottomSheet(paramInt, (View)null, (ViewGroup.LayoutParams)null));
  }
  
  public void setContentView(View paramView) {
    super.setContentView(wrapInBottomSheet(0, paramView, (ViewGroup.LayoutParams)null));
  }
  
  public void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams) {
    super.setContentView(wrapInBottomSheet(0, paramView, paramLayoutParams));
  }
  
  boolean shouldWindowCloseOnTouchOutside() {
    if (!this.canceledOnTouchOutsideSet) {
      TypedArray typedArray = getContext().obtainStyledAttributes(new int[] { 16843611 });
      this.canceledOnTouchOutside = typedArray.getBoolean(0, true);
      typedArray.recycle();
      this.canceledOnTouchOutsideSet = true;
    } 
    return this.canceledOnTouchOutside;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\bottomsheet\BottomSheetDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */