package androidx.core.os;

import android.content.res.Configuration;
import android.os.Build;
import java.util.Locale;

public final class ConfigurationCompat {
  public static LocaleListCompat getLocales(Configuration paramConfiguration) {
    return (Build.VERSION.SDK_INT >= 24) ? LocaleListCompat.wrap(paramConfiguration.getLocales()) : LocaleListCompat.create(new Locale[] { paramConfiguration.locale });
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\core\os\ConfigurationCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */