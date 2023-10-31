package androidx.core.content.pm;

import android.content.pm.PackageInfo;
import android.os.Build;

public final class PackageInfoCompat {
  public static long getLongVersionCode(PackageInfo paramPackageInfo) {
    return (Build.VERSION.SDK_INT >= 28) ? paramPackageInfo.getLongVersionCode() : paramPackageInfo.versionCode;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\content\pm\PackageInfoCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */