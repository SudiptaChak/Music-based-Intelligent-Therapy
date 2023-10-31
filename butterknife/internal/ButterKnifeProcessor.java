package butterknife.internal;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import butterknife.OnLongClick;
import butterknife.OnPageChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import butterknife.Optional;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public final class ButterKnifeProcessor extends AbstractProcessor {
  static final boolean $assertionsDisabled = false;
  
  public static final String ANDROID_PREFIX = "android.";
  
  public static final String JAVA_PREFIX = "java.";
  
  private static final List<Class<? extends Annotation>> LISTENERS;
  
  private static final String LIST_TYPE = List.class.getCanonicalName();
  
  public static final String SUFFIX = "$$ViewInjector";
  
  static final String VIEW_TYPE = "android.view.View";
  
  private Elements elementUtils;
  
  private Filer filer;
  
  private Types typeUtils;
  
  static {
    LISTENERS = Arrays.asList((Class<? extends Annotation>[])new Class[] { 
          OnCheckedChanged.class, OnClick.class, OnEditorAction.class, OnFocusChange.class, OnItemClick.class, OnItemLongClick.class, OnItemSelected.class, OnLongClick.class, OnPageChange.class, OnTextChanged.class, 
          OnTouch.class });
  }
  
  private String doubleErasure(TypeMirror paramTypeMirror) {
    String str2 = this.typeUtils.erasure(paramTypeMirror).toString();
    int i = str2.indexOf('<');
    String str1 = str2;
    if (i != -1)
      str1 = str2.substring(0, i); 
    return str1;
  }
  
  private void error(Element paramElement, String paramString, Object... paramVarArgs) {
    String str = paramString;
    if (paramVarArgs.length > 0)
      str = String.format(paramString, paramVarArgs); 
    this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, str, paramElement);
  }
  
  private void findAndParseListener(RoundEnvironment paramRoundEnvironment, Class<? extends Annotation> paramClass, Map<TypeElement, ViewInjector> paramMap, Set<String> paramSet) {
    for (Element element : paramRoundEnvironment.getElementsAnnotatedWith(paramClass)) {
      try {
        parseListenerAnnotation(paramClass, element, paramMap, paramSet);
      } catch (Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        error(element, "Unable to generate view injector for @%s.\n\n%s", new Object[] { paramClass.getSimpleName(), stringWriter.toString() });
      } 
    } 
  }
  
  private Map<TypeElement, ViewInjector> findAndParseTargets(RoundEnvironment paramRoundEnvironment) {
    LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<Object, Object>();
    LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
    for (Element element : paramRoundEnvironment.getElementsAnnotatedWith((Class)InjectView.class)) {
      try {
        parseInjectView(element, (Map)linkedHashMap, linkedHashSet);
      } catch (Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        error(element, "Unable to generate view injector for @InjectView.\n\n%s", new Object[] { stringWriter });
      } 
    } 
    for (Element element : paramRoundEnvironment.getElementsAnnotatedWith((Class)InjectViews.class)) {
      try {
        parseInjectViews(element, (Map)linkedHashMap, linkedHashSet);
      } catch (Exception exception) {
        StringWriter stringWriter = new StringWriter();
        exception.printStackTrace(new PrintWriter(stringWriter));
        error(element, "Unable to generate view injector for @InjectViews.\n\n%s", new Object[] { stringWriter });
      } 
    } 
    null = LISTENERS.iterator();
    while (null.hasNext())
      findAndParseListener(paramRoundEnvironment, null.next(), (Map)linkedHashMap, linkedHashSet); 
    for (Map.Entry<Object, Object> entry : linkedHashMap.entrySet()) {
      String str = findParentFqcn((TypeElement)entry.getKey(), linkedHashSet);
      if (str != null) {
        ViewInjector viewInjector = (ViewInjector)entry.getValue();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append("$$ViewInjector");
        viewInjector.setParentInjector(stringBuilder.toString());
      } 
    } 
    return (Map)linkedHashMap;
  }
  
  private static Integer findDuplicate(int[] paramArrayOfint) {
    LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet();
    int i = paramArrayOfint.length;
    for (byte b = 0; b < i; b++) {
      int j = paramArrayOfint[b];
      if (!linkedHashSet.add(Integer.valueOf(j)))
        return Integer.valueOf(j); 
    } 
    return null;
  }
  
  private String findParentFqcn(TypeElement paramTypeElement, Set<String> paramSet) {
    while (true) {
      TypeMirror typeMirror = paramTypeElement.getSuperclass();
      if (typeMirror.getKind() == TypeKind.NONE)
        return null; 
      TypeElement typeElement2 = (TypeElement)((DeclaredType)typeMirror).asElement();
      TypeElement typeElement1 = typeElement2;
      if (paramSet.contains(typeElement2.toString())) {
        String str = getPackageName(typeElement2);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(".");
        stringBuilder.append(getClassName(typeElement2, str));
        return stringBuilder.toString();
      } 
    } 
  }
  
  private static String getClassName(TypeElement paramTypeElement, String paramString) {
    int i = paramString.length();
    return paramTypeElement.getQualifiedName().toString().substring(i + 1).replace('.', '$');
  }
  
  private ViewInjector getOrCreateTargetClass(Map<TypeElement, ViewInjector> paramMap, TypeElement paramTypeElement) {
    ViewInjector viewInjector2 = paramMap.get(paramTypeElement);
    ViewInjector viewInjector1 = viewInjector2;
    if (viewInjector2 == null) {
      String str2 = paramTypeElement.getQualifiedName().toString();
      String str1 = getPackageName(paramTypeElement);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getClassName(paramTypeElement, str1));
      stringBuilder.append("$$ViewInjector");
      viewInjector1 = new ViewInjector(str1, stringBuilder.toString(), str2);
      paramMap.put(paramTypeElement, viewInjector1);
    } 
    return viewInjector1;
  }
  
  private String getPackageName(TypeElement paramTypeElement) {
    return this.elementUtils.getPackageOf(paramTypeElement).getQualifiedName().toString();
  }
  
  private boolean isBindingInWrongPackage(Class<? extends Annotation> paramClass, Element paramElement) {
    String str = ((TypeElement)paramElement.getEnclosingElement()).getQualifiedName().toString();
    if (str.startsWith("android.")) {
      error(paramElement, "@%s-annotated class incorrectly in Android framework package. (%s)", new Object[] { paramClass.getSimpleName(), str });
      return true;
    } 
    if (str.startsWith("java.")) {
      error(paramElement, "@%s-annotated class incorrectly in Java framework package. (%s)", new Object[] { paramClass.getSimpleName(), str });
      return true;
    } 
    return false;
  }
  
  private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> paramClass, String paramString, Element paramElement) {
    TypeElement typeElement = (TypeElement)paramElement.getEnclosingElement();
    Set<Modifier> set = paramElement.getModifiers();
    boolean bool = set.contains(Modifier.PRIVATE);
    boolean bool1 = true;
    if (bool || set.contains(Modifier.STATIC)) {
      error(paramElement, "@%s %s must not be private or static. (%s.%s)", new Object[] { paramClass.getSimpleName(), paramString, typeElement.getQualifiedName(), paramElement.getSimpleName() });
      bool = true;
    } else {
      bool = false;
    } 
    if (typeElement.getKind() != ElementKind.CLASS) {
      error(typeElement, "@%s %s may only be contained in classes. (%s.%s)", new Object[] { paramClass.getSimpleName(), paramString, typeElement.getQualifiedName(), paramElement.getSimpleName() });
      bool = true;
    } 
    if (typeElement.getModifiers().contains(Modifier.PRIVATE)) {
      error(typeElement, "@%s %s may not be contained in private classes. (%s.%s)", new Object[] { paramClass.getSimpleName(), paramString, typeElement.getQualifiedName(), paramElement.getSimpleName() });
      bool = bool1;
    } 
    return bool;
  }
  
  private boolean isInterface(TypeMirror paramTypeMirror) {
    boolean bool1 = paramTypeMirror instanceof DeclaredType;
    boolean bool = false;
    if (!bool1)
      return false; 
    if (((DeclaredType)paramTypeMirror).asElement().getKind() == ElementKind.INTERFACE)
      bool = true; 
    return bool;
  }
  
  private boolean isSubtypeOfType(TypeMirror paramTypeMirror, String paramString) {
    if (paramString.equals(paramTypeMirror.toString()))
      return true; 
    if (!(paramTypeMirror instanceof DeclaredType))
      return false; 
    paramTypeMirror = paramTypeMirror;
    List<? extends TypeMirror> list = paramTypeMirror.getTypeArguments();
    if (list.size() > 0) {
      StringBuilder stringBuilder = new StringBuilder(paramTypeMirror.asElement().toString());
      stringBuilder.append('<');
      for (byte b = 0; b < list.size(); b++) {
        if (b > 0)
          stringBuilder.append(','); 
        stringBuilder.append('?');
      } 
      stringBuilder.append('>');
      if (stringBuilder.toString().equals(paramString))
        return true; 
    } 
    Element element = paramTypeMirror.asElement();
    if (!(element instanceof TypeElement))
      return false; 
    element = element;
    if (isSubtypeOfType(element.getSuperclass(), paramString))
      return true; 
    Iterator<? extends TypeMirror> iterator = element.getInterfaces().iterator();
    while (iterator.hasNext()) {
      if (isSubtypeOfType(iterator.next(), paramString))
        return true; 
    } 
    return false;
  }
  
  private void parseInjectView(Element paramElement, Map<TypeElement, ViewInjector> paramMap, Set<String> paramSet) {
    boolean bool1;
    TypeElement typeElement = (TypeElement)paramElement.getEnclosingElement();
    TypeMirror typeMirror2 = paramElement.asType();
    TypeMirror typeMirror1 = typeMirror2;
    if (typeMirror2 instanceof TypeVariable)
      typeMirror1 = ((TypeVariable)typeMirror2).getUpperBound(); 
    boolean bool2 = isSubtypeOfType(typeMirror1, "android.view.View");
    boolean bool = false;
    if (!bool2 && !isInterface(typeMirror1)) {
      error(paramElement, "@InjectView fields must extend from View or be an interface. (%s.%s)", new Object[] { typeElement.getQualifiedName(), paramElement.getSimpleName() });
      bool1 = true;
    } else {
      bool1 = false;
    } 
    int i = bool1 | isInaccessibleViaGeneratedCode((Class)InjectView.class, "fields", paramElement) | isBindingInWrongPackage((Class)InjectView.class, paramElement);
    if (paramElement.getAnnotation(InjectViews.class) != null) {
      error(paramElement, "Only one of @InjectView and @InjectViews is allowed. (%s.%s)", new Object[] { typeElement.getQualifiedName(), paramElement.getSimpleName() });
      i = 1;
    } 
    if (i != 0)
      return; 
    i = ((InjectView)paramElement.<InjectView>getAnnotation(InjectView.class)).value();
    ViewInjector viewInjector = paramMap.get(typeElement);
    if (viewInjector != null) {
      ViewInjection viewInjection = viewInjector.getViewInjection(i);
      if (viewInjection != null) {
        Iterator<ViewBinding> iterator = viewInjection.getViewBindings().iterator();
        if (iterator.hasNext()) {
          error(paramElement, "Attempt to use @InjectView for an already injected ID %d on '%s'. (%s.%s)", new Object[] { Integer.valueOf(i), ((ViewBinding)iterator.next()).getName(), typeElement.getQualifiedName(), paramElement.getSimpleName() });
          return;
        } 
      } 
    } 
    String str2 = paramElement.getSimpleName().toString();
    String str1 = typeMirror1.toString();
    if (paramElement.getAnnotation(Optional.class) == null)
      bool = true; 
    getOrCreateTargetClass(paramMap, typeElement).addView(i, new ViewBinding(str2, str1, bool));
    paramSet.add(typeElement.toString());
  }
  
  private void parseInjectViews(Element paramElement, Map<TypeElement, ViewInjector> paramMap, Set<String> paramSet) {
    boolean bool1;
    TypeElement typeElement = (TypeElement)paramElement.getEnclosingElement();
    TypeMirror typeMirror3 = paramElement.asType();
    String str2 = doubleErasure(typeMirror3);
    TypeKind typeKind1 = typeMirror3.getKind();
    TypeKind typeKind2 = TypeKind.ARRAY;
    TypeMirror typeMirror1 = null;
    CollectionBinding.Kind kind = null;
    boolean bool = false;
    if (typeKind1 == typeKind2) {
      typeMirror1 = ((ArrayType)typeMirror3).getComponentType();
      kind = CollectionBinding.Kind.ARRAY;
      bool1 = false;
    } else if (LIST_TYPE.equals(str2)) {
      CollectionBinding.Kind kind1;
      List<? extends TypeMirror> list = ((DeclaredType)typeMirror3).getTypeArguments();
      if (list.size() != 1) {
        error(paramElement, "@InjectViews List must have a generic component. (%s.%s)", new Object[] { typeElement.getQualifiedName(), paramElement.getSimpleName() });
        bool1 = true;
        kind1 = kind;
      } else {
        typeMirror1 = kind1.get(0);
        bool1 = false;
      } 
      kind = CollectionBinding.Kind.LIST;
    } else {
      error(paramElement, "@InjectViews must be a List or array. (%s.%s)", new Object[] { typeElement.getQualifiedName(), paramElement.getSimpleName() });
      kind = null;
      bool1 = true;
    } 
    TypeMirror typeMirror2 = typeMirror1;
    if (typeMirror1 instanceof TypeVariable)
      typeMirror2 = ((TypeVariable)typeMirror1).getUpperBound(); 
    boolean bool2 = bool1;
    if (typeMirror2 != null) {
      bool2 = bool1;
      if (!isSubtypeOfType(typeMirror2, "android.view.View")) {
        bool2 = bool1;
        if (!isInterface(typeMirror2)) {
          error(paramElement, "@InjectViews type must extend from View or be an interface. (%s.%s)", new Object[] { typeElement.getQualifiedName(), paramElement.getSimpleName() });
          bool2 = true;
        } 
      } 
    } 
    if ((bool2 | isInaccessibleViaGeneratedCode((Class)InjectViews.class, "fields", paramElement) | isBindingInWrongPackage((Class)InjectViews.class, paramElement)) != 0)
      return; 
    String str3 = paramElement.getSimpleName().toString();
    int[] arrayOfInt = ((InjectViews)paramElement.<InjectViews>getAnnotation(InjectViews.class)).value();
    if (arrayOfInt.length == 0) {
      error(paramElement, "@InjectViews must specify at least one ID. (%s.%s)", new Object[] { typeElement.getQualifiedName(), paramElement.getSimpleName() });
      return;
    } 
    Integer integer = findDuplicate(arrayOfInt);
    if (integer != null)
      error(paramElement, "@InjectViews annotation contains duplicate ID %d. (%s.%s)", new Object[] { integer, typeElement.getQualifiedName(), paramElement.getSimpleName() }); 
    String str1 = typeMirror2.toString();
    if (paramElement.getAnnotation(Optional.class) == null)
      bool = true; 
    getOrCreateTargetClass(paramMap, typeElement).addCollection(arrayOfInt, new CollectionBinding(str3, str1, kind, bool));
    paramSet.add(typeElement.toString());
  }
  
  private void parseListenerAnnotation(Class<? extends Annotation> paramClass, Element paramElement, Map<TypeElement, ViewInjector> paramMap, Set<String> paramSet) throws Exception {
    // Byte code:
    //   0: aload_2
    //   1: instanceof javax/lang/model/element/ExecutableElement
    //   4: ifeq -> 1549
    //   7: aload_2
    //   8: invokeinterface getKind : ()Ljavax/lang/model/element/ElementKind;
    //   13: getstatic javax/lang/model/element/ElementKind.METHOD : Ljavax/lang/model/element/ElementKind;
    //   16: if_acmpne -> 1549
    //   19: aload_2
    //   20: checkcast javax/lang/model/element/ExecutableElement
    //   23: astore #19
    //   25: aload_2
    //   26: invokeinterface getEnclosingElement : ()Ljavax/lang/model/element/Element;
    //   31: checkcast javax/lang/model/element/TypeElement
    //   34: astore #16
    //   36: aload_2
    //   37: aload_1
    //   38: invokeinterface getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   43: astore #12
    //   45: aload_1
    //   46: ldc_w 'value'
    //   49: iconst_0
    //   50: anewarray java/lang/Class
    //   53: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   56: astore #11
    //   58: aload #11
    //   60: invokevirtual getReturnType : ()Ljava/lang/Class;
    //   63: ldc_w [I
    //   66: if_acmpne -> 1527
    //   69: aload #11
    //   71: aload #12
    //   73: iconst_0
    //   74: anewarray java/lang/Object
    //   77: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   80: checkcast [I
    //   83: astore #17
    //   85: aload #19
    //   87: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   92: invokevirtual toString : ()Ljava/lang/String;
    //   95: astore #18
    //   97: aload_2
    //   98: ldc_w butterknife/Optional
    //   101: invokeinterface getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   106: ifnonnull -> 115
    //   109: iconst_1
    //   110: istore #10
    //   112: goto -> 118
    //   115: iconst_0
    //   116: istore #10
    //   118: aload_0
    //   119: aload_1
    //   120: ldc_w 'methods'
    //   123: aload_2
    //   124: invokespecial isInaccessibleViaGeneratedCode : (Ljava/lang/Class;Ljava/lang/String;Ljavax/lang/model/element/Element;)Z
    //   127: aload_0
    //   128: aload_1
    //   129: aload_2
    //   130: invokespecial isBindingInWrongPackage : (Ljava/lang/Class;Ljavax/lang/model/element/Element;)Z
    //   133: ior
    //   134: istore #5
    //   136: aload #17
    //   138: invokestatic findDuplicate : ([I)Ljava/lang/Integer;
    //   141: astore #11
    //   143: aload #11
    //   145: ifnull -> 194
    //   148: aload_0
    //   149: aload_2
    //   150: ldc_w '@%s annotation for method contains duplicate ID %d. (%s.%s)'
    //   153: iconst_4
    //   154: anewarray java/lang/Object
    //   157: dup
    //   158: iconst_0
    //   159: aload_1
    //   160: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   163: aastore
    //   164: dup
    //   165: iconst_1
    //   166: aload #11
    //   168: aastore
    //   169: dup
    //   170: iconst_2
    //   171: aload #16
    //   173: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   178: aastore
    //   179: dup
    //   180: iconst_3
    //   181: aload_2
    //   182: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   187: aastore
    //   188: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   191: iconst_1
    //   192: istore #5
    //   194: aload_1
    //   195: ldc_w butterknife/internal/ListenerClass
    //   198: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   201: checkcast butterknife/internal/ListenerClass
    //   204: astore #11
    //   206: aload #11
    //   208: ifnull -> 1493
    //   211: aload #17
    //   213: arraylength
    //   214: istore #8
    //   216: iconst_0
    //   217: istore #7
    //   219: iload #7
    //   221: iload #8
    //   223: if_icmpge -> 441
    //   226: aload #17
    //   228: iload #7
    //   230: iaload
    //   231: istore #9
    //   233: iload #5
    //   235: istore #6
    //   237: iload #9
    //   239: iconst_m1
    //   240: if_icmpne -> 431
    //   243: aload #17
    //   245: arraylength
    //   246: iconst_1
    //   247: if_icmpne -> 382
    //   250: iload #10
    //   252: ifne -> 289
    //   255: aload_0
    //   256: aload_2
    //   257: ldc_w 'ID free injection must not be annotated with @Optional. (%s.%s)'
    //   260: iconst_2
    //   261: anewarray java/lang/Object
    //   264: dup
    //   265: iconst_0
    //   266: aload #16
    //   268: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   273: aastore
    //   274: dup
    //   275: iconst_1
    //   276: aload_2
    //   277: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   282: aastore
    //   283: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   286: iconst_1
    //   287: istore #5
    //   289: aload #11
    //   291: invokeinterface targetType : ()Ljava/lang/String;
    //   296: astore #13
    //   298: iload #5
    //   300: istore #6
    //   302: aload_0
    //   303: aload #16
    //   305: invokeinterface asType : ()Ljavax/lang/model/type/TypeMirror;
    //   310: aload #13
    //   312: invokespecial isSubtypeOfType : (Ljavax/lang/model/type/TypeMirror;Ljava/lang/String;)Z
    //   315: ifne -> 431
    //   318: iload #5
    //   320: istore #6
    //   322: aload_0
    //   323: aload #16
    //   325: invokeinterface asType : ()Ljavax/lang/model/type/TypeMirror;
    //   330: invokespecial isInterface : (Ljavax/lang/model/type/TypeMirror;)Z
    //   333: ifne -> 431
    //   336: aload_0
    //   337: aload_2
    //   338: ldc_w '@%s annotation without an ID may only be used with an object of type "%s" or an interface. (%s.%s)'
    //   341: iconst_4
    //   342: anewarray java/lang/Object
    //   345: dup
    //   346: iconst_0
    //   347: aload_1
    //   348: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   351: aastore
    //   352: dup
    //   353: iconst_1
    //   354: aload #13
    //   356: aastore
    //   357: dup
    //   358: iconst_2
    //   359: aload #16
    //   361: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   366: aastore
    //   367: dup
    //   368: iconst_3
    //   369: aload_2
    //   370: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   375: aastore
    //   376: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   379: goto -> 428
    //   382: aload_0
    //   383: aload_2
    //   384: ldc_w '@%s annotation contains invalid ID %d. (%s.%s)'
    //   387: iconst_4
    //   388: anewarray java/lang/Object
    //   391: dup
    //   392: iconst_0
    //   393: aload_1
    //   394: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   397: aastore
    //   398: dup
    //   399: iconst_1
    //   400: iload #9
    //   402: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   405: aastore
    //   406: dup
    //   407: iconst_2
    //   408: aload #16
    //   410: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   415: aastore
    //   416: dup
    //   417: iconst_3
    //   418: aload_2
    //   419: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   424: aastore
    //   425: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   428: iconst_1
    //   429: istore #6
    //   431: iinc #7, 1
    //   434: iload #6
    //   436: istore #5
    //   438: goto -> 219
    //   441: aload #11
    //   443: invokeinterface method : ()[Lbutterknife/internal/ListenerMethod;
    //   448: astore #13
    //   450: aload #13
    //   452: arraylength
    //   453: iconst_1
    //   454: if_icmpgt -> 1468
    //   457: aload #13
    //   459: arraylength
    //   460: iconst_1
    //   461: if_icmpne -> 511
    //   464: aload #11
    //   466: invokeinterface callbacks : ()Ljava/lang/Class;
    //   471: ldc_w butterknife/internal/ListenerClass$NONE
    //   474: if_acmpne -> 486
    //   477: aload #13
    //   479: iconst_0
    //   480: aaload
    //   481: astore #12
    //   483: goto -> 565
    //   486: new java/lang/IllegalStateException
    //   489: dup
    //   490: ldc_w 'Both method() and callback() defined on @%s.'
    //   493: iconst_1
    //   494: anewarray java/lang/Object
    //   497: dup
    //   498: iconst_0
    //   499: aload_1
    //   500: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   503: aastore
    //   504: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   507: invokespecial <init> : (Ljava/lang/String;)V
    //   510: athrow
    //   511: aload_1
    //   512: ldc_w 'callback'
    //   515: iconst_0
    //   516: anewarray java/lang/Class
    //   519: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   522: aload #12
    //   524: iconst_0
    //   525: anewarray java/lang/Object
    //   528: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   531: checkcast java/lang/Enum
    //   534: astore #13
    //   536: aload #13
    //   538: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   541: aload #13
    //   543: invokevirtual name : ()Ljava/lang/String;
    //   546: invokevirtual getField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   549: ldc_w butterknife/internal/ListenerMethod
    //   552: invokevirtual getAnnotation : (Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
    //   555: checkcast butterknife/internal/ListenerMethod
    //   558: astore #12
    //   560: aload #12
    //   562: ifnull -> 1415
    //   565: aload #19
    //   567: invokeinterface getParameters : ()Ljava/util/List;
    //   572: astore #20
    //   574: aload #20
    //   576: invokeinterface size : ()I
    //   581: aload #12
    //   583: invokeinterface parameters : ()[Ljava/lang/String;
    //   588: arraylength
    //   589: if_icmple -> 647
    //   592: aload_0
    //   593: aload_2
    //   594: ldc_w '@%s methods can have at most %s parameter(s). (%s.%s)'
    //   597: iconst_4
    //   598: anewarray java/lang/Object
    //   601: dup
    //   602: iconst_0
    //   603: aload_1
    //   604: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   607: aastore
    //   608: dup
    //   609: iconst_1
    //   610: aload #12
    //   612: invokeinterface parameters : ()[Ljava/lang/String;
    //   617: arraylength
    //   618: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   621: aastore
    //   622: dup
    //   623: iconst_2
    //   624: aload #16
    //   626: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   631: aastore
    //   632: dup
    //   633: iconst_3
    //   634: aload_2
    //   635: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   640: aastore
    //   641: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   644: iconst_1
    //   645: istore #5
    //   647: aload #19
    //   649: invokeinterface getReturnType : ()Ljavax/lang/model/type/TypeMirror;
    //   654: astore #14
    //   656: aload #14
    //   658: astore #13
    //   660: aload #14
    //   662: instanceof javax/lang/model/type/TypeVariable
    //   665: ifeq -> 680
    //   668: aload #14
    //   670: checkcast javax/lang/model/type/TypeVariable
    //   673: invokeinterface getUpperBound : ()Ljavax/lang/model/type/TypeMirror;
    //   678: astore #13
    //   680: aload #13
    //   682: invokeinterface toString : ()Ljava/lang/String;
    //   687: aload #12
    //   689: invokeinterface returnType : ()Ljava/lang/String;
    //   694: invokevirtual equals : (Ljava/lang/Object;)Z
    //   697: ifne -> 751
    //   700: aload_0
    //   701: aload_2
    //   702: ldc_w '@%s methods must have a '%s' return type. (%s.%s)'
    //   705: iconst_4
    //   706: anewarray java/lang/Object
    //   709: dup
    //   710: iconst_0
    //   711: aload_1
    //   712: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   715: aastore
    //   716: dup
    //   717: iconst_1
    //   718: aload #12
    //   720: invokeinterface returnType : ()Ljava/lang/String;
    //   725: aastore
    //   726: dup
    //   727: iconst_2
    //   728: aload #16
    //   730: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   735: aastore
    //   736: dup
    //   737: iconst_3
    //   738: aload_2
    //   739: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   744: aastore
    //   745: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   748: iconst_1
    //   749: istore #5
    //   751: iload #5
    //   753: ifeq -> 757
    //   756: return
    //   757: getstatic butterknife/internal/Parameter.NONE : [Lbutterknife/internal/Parameter;
    //   760: astore #13
    //   762: aload #20
    //   764: invokeinterface isEmpty : ()Z
    //   769: ifne -> 1286
    //   772: aload #20
    //   774: invokeinterface size : ()I
    //   779: istore #7
    //   781: iload #7
    //   783: anewarray butterknife/internal/Parameter
    //   786: astore #14
    //   788: new java/util/BitSet
    //   791: dup
    //   792: aload #20
    //   794: invokeinterface size : ()I
    //   799: invokespecial <init> : (I)V
    //   802: astore #22
    //   804: aload #12
    //   806: invokeinterface parameters : ()[Ljava/lang/String;
    //   811: astore #21
    //   813: iconst_0
    //   814: istore #5
    //   816: iload #5
    //   818: aload #20
    //   820: invokeinterface size : ()I
    //   825: if_icmpge -> 1276
    //   828: aload #20
    //   830: iload #5
    //   832: invokeinterface get : (I)Ljava/lang/Object;
    //   837: checkcast javax/lang/model/element/VariableElement
    //   840: invokeinterface asType : ()Ljavax/lang/model/type/TypeMirror;
    //   845: astore #15
    //   847: aload #15
    //   849: astore #13
    //   851: aload #15
    //   853: instanceof javax/lang/model/type/TypeVariable
    //   856: ifeq -> 871
    //   859: aload #15
    //   861: checkcast javax/lang/model/type/TypeVariable
    //   864: invokeinterface getUpperBound : ()Ljavax/lang/model/type/TypeMirror;
    //   869: astore #13
    //   871: iconst_0
    //   872: istore #6
    //   874: iload #6
    //   876: aload #21
    //   878: arraylength
    //   879: if_icmpge -> 955
    //   882: aload #22
    //   884: iload #6
    //   886: invokevirtual get : (I)Z
    //   889: ifeq -> 895
    //   892: goto -> 921
    //   895: aload_0
    //   896: aload #13
    //   898: aload #21
    //   900: iload #6
    //   902: aaload
    //   903: invokespecial isSubtypeOfType : (Ljavax/lang/model/type/TypeMirror;Ljava/lang/String;)Z
    //   906: ifne -> 927
    //   909: aload_0
    //   910: aload #13
    //   912: invokespecial isInterface : (Ljavax/lang/model/type/TypeMirror;)Z
    //   915: ifeq -> 921
    //   918: goto -> 927
    //   921: iinc #6, 1
    //   924: goto -> 874
    //   927: aload #14
    //   929: iload #5
    //   931: new butterknife/internal/Parameter
    //   934: dup
    //   935: iload #6
    //   937: aload #13
    //   939: invokeinterface toString : ()Ljava/lang/String;
    //   944: invokespecial <init> : (ILjava/lang/String;)V
    //   947: aastore
    //   948: aload #22
    //   950: iload #6
    //   952: invokevirtual set : (I)V
    //   955: aload #14
    //   957: iload #5
    //   959: aaload
    //   960: ifnonnull -> 1270
    //   963: new java/lang/StringBuilder
    //   966: dup
    //   967: invokespecial <init> : ()V
    //   970: astore_3
    //   971: aload_3
    //   972: ldc_w 'Unable to match @'
    //   975: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   978: pop
    //   979: aload_3
    //   980: aload_1
    //   981: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   984: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   987: pop
    //   988: aload_3
    //   989: ldc_w ' method arguments. ('
    //   992: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   995: pop
    //   996: aload_3
    //   997: aload #16
    //   999: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   1004: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/lang/StringBuilder;
    //   1007: pop
    //   1008: aload_3
    //   1009: bipush #46
    //   1011: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1014: pop
    //   1015: aload_3
    //   1016: aload_2
    //   1017: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   1022: invokevirtual append : (Ljava/lang/CharSequence;)Ljava/lang/StringBuilder;
    //   1025: pop
    //   1026: aload_3
    //   1027: bipush #41
    //   1029: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   1032: pop
    //   1033: iconst_0
    //   1034: istore #5
    //   1036: iload #5
    //   1038: iload #7
    //   1040: if_icmpge -> 1171
    //   1043: aload #14
    //   1045: iload #5
    //   1047: aaload
    //   1048: astore_1
    //   1049: aload_3
    //   1050: ldc_w '\\n\\n  Parameter #'
    //   1053: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1056: pop
    //   1057: iload #5
    //   1059: iconst_1
    //   1060: iadd
    //   1061: istore #6
    //   1063: aload_3
    //   1064: iload #6
    //   1066: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1069: pop
    //   1070: aload_3
    //   1071: ldc_w ': '
    //   1074: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1077: pop
    //   1078: aload_3
    //   1079: aload #20
    //   1081: iload #5
    //   1083: invokeinterface get : (I)Ljava/lang/Object;
    //   1088: checkcast javax/lang/model/element/VariableElement
    //   1091: invokeinterface asType : ()Ljavax/lang/model/type/TypeMirror;
    //   1096: invokeinterface toString : ()Ljava/lang/String;
    //   1101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1104: pop
    //   1105: aload_3
    //   1106: ldc_w '\\n    '
    //   1109: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1112: pop
    //   1113: aload_1
    //   1114: ifnonnull -> 1128
    //   1117: aload_3
    //   1118: ldc_w 'did not match any listener parameters'
    //   1121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1124: pop
    //   1125: goto -> 1164
    //   1128: aload_3
    //   1129: ldc_w 'matched listener parameter #'
    //   1132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1135: pop
    //   1136: aload_3
    //   1137: aload_1
    //   1138: invokevirtual getListenerPosition : ()I
    //   1141: iconst_1
    //   1142: iadd
    //   1143: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1146: pop
    //   1147: aload_3
    //   1148: ldc_w ': '
    //   1151: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1154: pop
    //   1155: aload_3
    //   1156: aload_1
    //   1157: invokevirtual getType : ()Ljava/lang/String;
    //   1160: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1163: pop
    //   1164: iload #6
    //   1166: istore #5
    //   1168: goto -> 1036
    //   1171: aload_3
    //   1172: ldc_w '\\n\\nMethods may have up to '
    //   1175: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1178: pop
    //   1179: aload_3
    //   1180: aload #12
    //   1182: invokeinterface parameters : ()[Ljava/lang/String;
    //   1187: arraylength
    //   1188: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1191: pop
    //   1192: aload_3
    //   1193: ldc_w ' parameter(s):\\n'
    //   1196: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1199: pop
    //   1200: aload #12
    //   1202: invokeinterface parameters : ()[Ljava/lang/String;
    //   1207: astore_1
    //   1208: aload_1
    //   1209: arraylength
    //   1210: istore #6
    //   1212: iconst_0
    //   1213: istore #5
    //   1215: iload #5
    //   1217: iload #6
    //   1219: if_icmpge -> 1247
    //   1222: aload_1
    //   1223: iload #5
    //   1225: aaload
    //   1226: astore_2
    //   1227: aload_3
    //   1228: ldc_w '\\n  '
    //   1231: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1234: pop
    //   1235: aload_3
    //   1236: aload_2
    //   1237: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1240: pop
    //   1241: iinc #5, 1
    //   1244: goto -> 1215
    //   1247: aload_3
    //   1248: ldc_w '\\n\\nThese may be listed in any order but will be searched for from top to bottom.'
    //   1251: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1254: pop
    //   1255: aload_0
    //   1256: aload #19
    //   1258: aload_3
    //   1259: invokevirtual toString : ()Ljava/lang/String;
    //   1262: iconst_0
    //   1263: anewarray java/lang/Object
    //   1266: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1269: return
    //   1270: iinc #5, 1
    //   1273: goto -> 816
    //   1276: aload #11
    //   1278: astore_1
    //   1279: aload #14
    //   1281: astore #11
    //   1283: goto -> 1293
    //   1286: aload #11
    //   1288: astore_1
    //   1289: aload #13
    //   1291: astore #11
    //   1293: new butterknife/internal/ListenerBinding
    //   1296: dup
    //   1297: aload #18
    //   1299: aload #11
    //   1301: invokestatic asList : ([Ljava/lang/Object;)Ljava/util/List;
    //   1304: iload #10
    //   1306: invokespecial <init> : (Ljava/lang/String;Ljava/util/List;Z)V
    //   1309: astore #11
    //   1311: aload_0
    //   1312: aload_3
    //   1313: aload #16
    //   1315: invokespecial getOrCreateTargetClass : (Ljava/util/Map;Ljavax/lang/model/element/TypeElement;)Lbutterknife/internal/ViewInjector;
    //   1318: astore_3
    //   1319: aload #17
    //   1321: arraylength
    //   1322: istore #6
    //   1324: iconst_0
    //   1325: istore #5
    //   1327: iload #5
    //   1329: iload #6
    //   1331: if_icmpge -> 1401
    //   1334: aload #17
    //   1336: iload #5
    //   1338: iaload
    //   1339: istore #7
    //   1341: aload_3
    //   1342: iload #7
    //   1344: aload_1
    //   1345: aload #12
    //   1347: aload #11
    //   1349: invokevirtual addListener : (ILbutterknife/internal/ListenerClass;Lbutterknife/internal/ListenerMethod;Lbutterknife/internal/ListenerBinding;)Z
    //   1352: ifne -> 1395
    //   1355: aload_0
    //   1356: aload_2
    //   1357: ldc_w 'Multiple listener methods with return value specified for ID %d. (%s.%s)'
    //   1360: iconst_3
    //   1361: anewarray java/lang/Object
    //   1364: dup
    //   1365: iconst_0
    //   1366: iload #7
    //   1368: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1371: aastore
    //   1372: dup
    //   1373: iconst_1
    //   1374: aload #16
    //   1376: invokeinterface getQualifiedName : ()Ljavax/lang/model/element/Name;
    //   1381: aastore
    //   1382: dup
    //   1383: iconst_2
    //   1384: aload_2
    //   1385: invokeinterface getSimpleName : ()Ljavax/lang/model/element/Name;
    //   1390: aastore
    //   1391: invokespecial error : (Ljavax/lang/model/element/Element;Ljava/lang/String;[Ljava/lang/Object;)V
    //   1394: return
    //   1395: iinc #5, 1
    //   1398: goto -> 1327
    //   1401: aload #4
    //   1403: aload #16
    //   1405: invokevirtual toString : ()Ljava/lang/String;
    //   1408: invokeinterface add : (Ljava/lang/Object;)Z
    //   1413: pop
    //   1414: return
    //   1415: new java/lang/IllegalStateException
    //   1418: dup
    //   1419: ldc_w 'No @%s defined on @%s's %s.%s.'
    //   1422: iconst_4
    //   1423: anewarray java/lang/Object
    //   1426: dup
    //   1427: iconst_0
    //   1428: ldc_w butterknife/internal/ListenerMethod
    //   1431: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   1434: aastore
    //   1435: dup
    //   1436: iconst_1
    //   1437: aload_1
    //   1438: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   1441: aastore
    //   1442: dup
    //   1443: iconst_2
    //   1444: aload #13
    //   1446: invokevirtual getDeclaringClass : ()Ljava/lang/Class;
    //   1449: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   1452: aastore
    //   1453: dup
    //   1454: iconst_3
    //   1455: aload #13
    //   1457: invokevirtual name : ()Ljava/lang/String;
    //   1460: aastore
    //   1461: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1464: invokespecial <init> : (Ljava/lang/String;)V
    //   1467: athrow
    //   1468: new java/lang/IllegalStateException
    //   1471: dup
    //   1472: ldc_w 'Multiple listener methods specified on @%s.'
    //   1475: iconst_1
    //   1476: anewarray java/lang/Object
    //   1479: dup
    //   1480: iconst_0
    //   1481: aload_1
    //   1482: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   1485: aastore
    //   1486: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1489: invokespecial <init> : (Ljava/lang/String;)V
    //   1492: athrow
    //   1493: new java/lang/IllegalStateException
    //   1496: dup
    //   1497: ldc_w 'No @%s defined on @%s.'
    //   1500: iconst_2
    //   1501: anewarray java/lang/Object
    //   1504: dup
    //   1505: iconst_0
    //   1506: ldc_w butterknife/internal/ListenerClass
    //   1509: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   1512: aastore
    //   1513: dup
    //   1514: iconst_1
    //   1515: aload_1
    //   1516: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   1519: aastore
    //   1520: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1523: invokespecial <init> : (Ljava/lang/String;)V
    //   1526: athrow
    //   1527: new java/lang/IllegalStateException
    //   1530: dup
    //   1531: ldc_w '@%s annotation value() type not int[].'
    //   1534: iconst_1
    //   1535: anewarray java/lang/Object
    //   1538: dup
    //   1539: iconst_0
    //   1540: aload_1
    //   1541: aastore
    //   1542: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1545: invokespecial <init> : (Ljava/lang/String;)V
    //   1548: athrow
    //   1549: new java/lang/IllegalStateException
    //   1552: dup
    //   1553: ldc_w '@%s annotation must be on a method.'
    //   1556: iconst_1
    //   1557: anewarray java/lang/Object
    //   1560: dup
    //   1561: iconst_0
    //   1562: aload_1
    //   1563: invokevirtual getSimpleName : ()Ljava/lang/String;
    //   1566: aastore
    //   1567: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1570: invokespecial <init> : (Ljava/lang/String;)V
    //   1573: athrow
  }
  
  public Set<String> getSupportedAnnotationTypes() {
    LinkedHashSet<String> linkedHashSet = new LinkedHashSet();
    linkedHashSet.add(InjectView.class.getCanonicalName());
    linkedHashSet.add(InjectViews.class.getCanonicalName());
    Iterator<Class<? extends Annotation>> iterator = LISTENERS.iterator();
    while (iterator.hasNext())
      linkedHashSet.add(((Class)iterator.next()).getCanonicalName()); 
    return linkedHashSet;
  }
  
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }
  
  public void init(ProcessingEnvironment paramProcessingEnvironment) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: invokespecial init : (Ljavax/annotation/processing/ProcessingEnvironment;)V
    //   7: aload_0
    //   8: aload_1
    //   9: invokeinterface getElementUtils : ()Ljavax/lang/model/util/Elements;
    //   14: putfield elementUtils : Ljavax/lang/model/util/Elements;
    //   17: aload_0
    //   18: aload_1
    //   19: invokeinterface getTypeUtils : ()Ljavax/lang/model/util/Types;
    //   24: putfield typeUtils : Ljavax/lang/model/util/Types;
    //   27: aload_0
    //   28: aload_1
    //   29: invokeinterface getFiler : ()Ljavax/annotation/processing/Filer;
    //   34: putfield filer : Ljavax/annotation/processing/Filer;
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: astore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	37	40	finally
  }
  
  public boolean process(Set<? extends TypeElement> paramSet, RoundEnvironment paramRoundEnvironment) {
    for (Map.Entry<TypeElement, ViewInjector> entry : findAndParseTargets(paramRoundEnvironment).entrySet()) {
      TypeElement typeElement = (TypeElement)entry.getKey();
      ViewInjector viewInjector = (ViewInjector)entry.getValue();
      try {
        Writer writer = this.filer.createSourceFile(viewInjector.getFqcn(), new Element[] { typeElement }).openWriter();
        writer.write(viewInjector.brewJava());
        writer.flush();
        writer.close();
      } catch (IOException iOException) {
        error(typeElement, "Unable to write injector for type %s: %s", new Object[] { typeElement, iOException.getMessage() });
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\internal\ButterKnifeProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */