package com.google.android.material.stateful;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.collection.SimpleArrayMap;
import androidx.customview.view.AbsSavedState;

public class ExtendableSavedState extends AbsSavedState {
  public static final Parcelable.Creator<ExtendableSavedState> CREATOR = (Parcelable.Creator<ExtendableSavedState>)new Parcelable.ClassLoaderCreator<ExtendableSavedState>() {
      public ExtendableSavedState createFromParcel(Parcel param1Parcel) {
        return new ExtendableSavedState(param1Parcel, null);
      }
      
      public ExtendableSavedState createFromParcel(Parcel param1Parcel, ClassLoader param1ClassLoader) {
        return new ExtendableSavedState(param1Parcel, param1ClassLoader);
      }
      
      public ExtendableSavedState[] newArray(int param1Int) {
        return new ExtendableSavedState[param1Int];
      }
    };
  
  public final SimpleArrayMap<String, Bundle> extendableStates;
  
  private ExtendableSavedState(Parcel paramParcel, ClassLoader paramClassLoader) {
    super(paramParcel, paramClassLoader);
    int i = paramParcel.readInt();
    String[] arrayOfString = new String[i];
    paramParcel.readStringArray(arrayOfString);
    Bundle[] arrayOfBundle = new Bundle[i];
    paramParcel.readTypedArray((Object[])arrayOfBundle, Bundle.CREATOR);
    this.extendableStates = new SimpleArrayMap(i);
    for (byte b = 0; b < i; b++)
      this.extendableStates.put(arrayOfString[b], arrayOfBundle[b]); 
  }
  
  public ExtendableSavedState(Parcelable paramParcelable) {
    super(paramParcelable);
    this.extendableStates = new SimpleArrayMap();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ExtendableSavedState{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" states=");
    stringBuilder.append(this.extendableStates);
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    super.writeToParcel(paramParcel, paramInt);
    int i = this.extendableStates.size();
    paramParcel.writeInt(i);
    String[] arrayOfString = new String[i];
    Bundle[] arrayOfBundle = new Bundle[i];
    for (paramInt = 0; paramInt < i; paramInt++) {
      arrayOfString[paramInt] = (String)this.extendableStates.keyAt(paramInt);
      arrayOfBundle[paramInt] = (Bundle)this.extendableStates.valueAt(paramInt);
    } 
    paramParcel.writeStringArray(arrayOfString);
    paramParcel.writeTypedArray((Parcelable[])arrayOfBundle, 0);
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\com\google\android\material\stateful\ExtendableSavedState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */