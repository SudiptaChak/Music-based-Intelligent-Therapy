package androidx.vectordrawable.graphics.drawable;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimationUtilsCompat {
  private static Interpolator createInterpolatorFromXml(Context paramContext, Resources paramResources, Resources.Theme paramTheme, XmlPullParser paramXmlPullParser) throws XmlPullParserException, IOException {
    PathInterpolatorCompat pathInterpolatorCompat;
    int i = paramXmlPullParser.getDepth();
    paramResources = null;
    while (true) {
      int j = paramXmlPullParser.next();
      if ((j != 3 || paramXmlPullParser.getDepth() > i) && j != 1) {
        LinearInterpolator linearInterpolator;
        AccelerateInterpolator accelerateInterpolator;
        DecelerateInterpolator decelerateInterpolator;
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator;
        CycleInterpolator cycleInterpolator;
        AnticipateInterpolator anticipateInterpolator;
        OvershootInterpolator overshootInterpolator;
        AnticipateOvershootInterpolator anticipateOvershootInterpolator;
        BounceInterpolator bounceInterpolator;
        if (j != 2)
          continue; 
        AttributeSet attributeSet = Xml.asAttributeSet(paramXmlPullParser);
        String str = paramXmlPullParser.getName();
        if (str.equals("linearInterpolator")) {
          linearInterpolator = new LinearInterpolator();
          continue;
        } 
        if (linearInterpolator.equals("accelerateInterpolator")) {
          accelerateInterpolator = new AccelerateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (accelerateInterpolator.equals("decelerateInterpolator")) {
          decelerateInterpolator = new DecelerateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (decelerateInterpolator.equals("accelerateDecelerateInterpolator")) {
          accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
          continue;
        } 
        if (accelerateDecelerateInterpolator.equals("cycleInterpolator")) {
          cycleInterpolator = new CycleInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (cycleInterpolator.equals("anticipateInterpolator")) {
          anticipateInterpolator = new AnticipateInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (anticipateInterpolator.equals("overshootInterpolator")) {
          overshootInterpolator = new OvershootInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (overshootInterpolator.equals("anticipateOvershootInterpolator")) {
          anticipateOvershootInterpolator = new AnticipateOvershootInterpolator(paramContext, attributeSet);
          continue;
        } 
        if (anticipateOvershootInterpolator.equals("bounceInterpolator")) {
          bounceInterpolator = new BounceInterpolator();
          continue;
        } 
        if (bounceInterpolator.equals("pathInterpolator")) {
          pathInterpolatorCompat = new PathInterpolatorCompat(paramContext, attributeSet, paramXmlPullParser);
          continue;
        } 
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown interpolator name: ");
        stringBuilder.append(paramXmlPullParser.getName());
        throw new RuntimeException(stringBuilder.toString());
      } 
      break;
    } 
    return pathInterpolatorCompat;
  }
  
  public static Interpolator loadInterpolator(Context paramContext, int paramInt) throws Resources.NotFoundException {
    if (Build.VERSION.SDK_INT >= 21)
      return AnimationUtils.loadInterpolator(paramContext, paramInt); 
    XmlResourceParser xmlResourceParser2 = null;
    XmlResourceParser xmlResourceParser3 = null;
    XmlResourceParser xmlResourceParser1 = null;
    if (paramInt == 17563663) {
      try {
        return (Interpolator)new FastOutLinearInInterpolator();
      } catch (XmlPullParserException xmlPullParserException) {
      
      } catch (IOException iOException) {
      
      } finally {}
    } else {
      if (paramInt == 17563661)
        return (Interpolator)new FastOutSlowInInterpolator(); 
      if (paramInt == 17563662)
        return (Interpolator)new LinearOutSlowInInterpolator(); 
      XmlResourceParser xmlResourceParser = paramContext.getResources().getAnimation(paramInt);
      xmlResourceParser1 = xmlResourceParser;
      xmlResourceParser2 = xmlResourceParser;
      xmlResourceParser3 = xmlResourceParser;
      Interpolator interpolator = createInterpolatorFromXml(paramContext, paramContext.getResources(), paramContext.getTheme(), (XmlPullParser)xmlResourceParser);
      if (xmlResourceParser != null)
        xmlResourceParser.close(); 
      return interpolator;
    } 
    xmlResourceParser1 = xmlResourceParser2;
    Resources.NotFoundException notFoundException = new Resources.NotFoundException();
    xmlResourceParser1 = xmlResourceParser2;
    StringBuilder stringBuilder = new StringBuilder();
    xmlResourceParser1 = xmlResourceParser2;
    this();
    xmlResourceParser1 = xmlResourceParser2;
    stringBuilder.append("Can't load animation resource ID #0x");
    xmlResourceParser1 = xmlResourceParser2;
    stringBuilder.append(Integer.toHexString(paramInt));
    xmlResourceParser1 = xmlResourceParser2;
    this(stringBuilder.toString());
    xmlResourceParser1 = xmlResourceParser2;
    notFoundException.initCause((Throwable)xmlResourceParser3);
    xmlResourceParser1 = xmlResourceParser2;
    throw notFoundException;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\androidx\vectordrawable\graphics\drawable\AnimationUtilsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */