package butterknife.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ViewInjector {
  private final String className;
  
  private final String classPackage;
  
  private final Map<CollectionBinding, int[]> collectionBindings = (Map)new LinkedHashMap<CollectionBinding, int>();
  
  private String parentInjector;
  
  private final String targetClass;
  
  private final Map<Integer, ViewInjection> viewIdMap = new LinkedHashMap<Integer, ViewInjection>();
  
  ViewInjector(String paramString1, String paramString2, String paramString3) {
    this.classPackage = paramString1;
    this.className = paramString2;
    this.targetClass = paramString3;
  }
  
  private void emitCollectionBinding(StringBuilder paramStringBuilder, CollectionBinding paramCollectionBinding, int[] paramArrayOfint) {
    paramStringBuilder.append("    target.");
    paramStringBuilder.append(paramCollectionBinding.getName());
    paramStringBuilder.append(" = ");
    int i = null.$SwitchMap$butterknife$internal$CollectionBinding$Kind[paramCollectionBinding.getKind().ordinal()];
    if (i != 1) {
      if (i == 2) {
        paramStringBuilder.append("Finder.listOf(");
      } else {
        paramStringBuilder = new StringBuilder();
        paramStringBuilder.append("Unknown kind: ");
        paramStringBuilder.append(paramCollectionBinding.getKind());
        throw new IllegalStateException(paramStringBuilder.toString());
      } 
    } else {
      paramStringBuilder.append("Finder.arrayOf(");
    } 
    for (i = 0; i < paramArrayOfint.length; i++) {
      String str;
      if (i > 0)
        paramStringBuilder.append(','); 
      paramStringBuilder.append("\n        finder.<");
      paramStringBuilder.append(paramCollectionBinding.getType());
      paramStringBuilder.append(">");
      if (paramCollectionBinding.isRequired()) {
        str = "findRequiredView";
      } else {
        str = "findOptionalView";
      } 
      paramStringBuilder.append(str);
      paramStringBuilder.append("(source, ");
      paramStringBuilder.append(paramArrayOfint[i]);
      paramStringBuilder.append(", \"");
      emitHumanDescription(paramStringBuilder, Collections.singleton(paramCollectionBinding));
      paramStringBuilder.append("\")");
    } 
    paramStringBuilder.append("\n    );\n");
  }
  
  static void emitHumanDescription(StringBuilder paramStringBuilder, Collection<? extends Binding> paramCollection) {
    Iterator<? extends Binding> iterator = paramCollection.iterator();
    int i = paramCollection.size();
    if (i != 1) {
      if (i != 2) {
        i = 0;
        int j = paramCollection.size();
        while (i < j) {
          if (i != 0)
            paramStringBuilder.append(", "); 
          if (i == j - 1)
            paramStringBuilder.append("and "); 
          paramStringBuilder.append(((Binding)iterator.next()).getDescription());
          i++;
        } 
      } else {
        paramStringBuilder.append(((Binding)iterator.next()).getDescription());
        paramStringBuilder.append(" and ");
        paramStringBuilder.append(((Binding)iterator.next()).getDescription());
      } 
    } else {
      paramStringBuilder.append(((Binding)iterator.next()).getDescription());
    } 
  }
  
  private void emitInject(StringBuilder paramStringBuilder) {
    paramStringBuilder.append("  @Override ");
    paramStringBuilder.append("public void inject(final Finder finder, final T target, Object source) {\n");
    if (this.parentInjector != null)
      paramStringBuilder.append("    super.inject(finder, target, source);\n\n"); 
    paramStringBuilder.append("    View view;\n");
    null = this.viewIdMap.values().iterator();
    while (null.hasNext())
      emitViewInjection(paramStringBuilder, null.next()); 
    for (Map.Entry<CollectionBinding, int> entry : this.collectionBindings.entrySet())
      emitCollectionBinding(paramStringBuilder, (CollectionBinding)entry.getKey(), (int[])entry.getValue()); 
    paramStringBuilder.append("  }\n");
  }
  
  private void emitListenerBindings(StringBuilder paramStringBuilder, ViewInjection paramViewInjection) {
    String str;
    Map<ListenerClass, Map<ListenerMethod, Set<ListenerBinding>>> map = paramViewInjection.getListenerBindings();
    if (map.isEmpty())
      return; 
    boolean bool = paramViewInjection.getRequiredBindings().isEmpty();
    if (bool) {
      paramStringBuilder.append("    if (view != null) {\n");
      str = "  ";
    } else {
      str = "";
    } 
    Iterator<Map.Entry> iterator = map.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = iterator.next();
      ListenerClass listenerClass = (ListenerClass)entry.getKey();
      Map map1 = (Map)entry.getValue();
      int i = "android.view.View".equals(listenerClass.targetType()) ^ true;
      paramStringBuilder.append(str);
      paramStringBuilder.append("    ");
      if (i != 0) {
        paramStringBuilder.append("((");
        paramStringBuilder.append(listenerClass.targetType());
        if (listenerClass.genericArguments() > 0) {
          paramStringBuilder.append('<');
          for (byte b = 0; b < listenerClass.genericArguments(); b++) {
            if (b > 0)
              paramStringBuilder.append(", "); 
            paramStringBuilder.append('?');
          } 
          paramStringBuilder.append('>');
        } 
        paramStringBuilder.append(") ");
      } 
      paramStringBuilder.append("view");
      if (i != 0)
        paramStringBuilder.append(')'); 
      paramStringBuilder.append('.');
      paramStringBuilder.append(listenerClass.setter());
      paramStringBuilder.append("(\n");
      paramStringBuilder.append(str);
      paramStringBuilder.append("      new ");
      paramStringBuilder.append(listenerClass.type());
      paramStringBuilder.append("() {\n");
      Iterator<ListenerMethod> iterator1 = getListenerMethods(listenerClass).iterator();
      while (iterator1.hasNext()) {
        Iterator<ListenerMethod> iterator4;
        Iterator<Map.Entry> iterator3;
        Iterator<Map.Entry> iterator7;
        Iterator<ListenerMethod> iterator6;
        ListenerMethod listenerMethod = iterator1.next();
        paramStringBuilder.append(str);
        paramStringBuilder.append("        @Override public ");
        paramStringBuilder.append(listenerMethod.returnType());
        paramStringBuilder.append(' ');
        paramStringBuilder.append(listenerMethod.name());
        paramStringBuilder.append("(\n");
        String[] arrayOfString = listenerMethod.parameters();
        i = arrayOfString.length;
        int j;
        for (j = 0; j < i; j++) {
          paramStringBuilder.append(str);
          paramStringBuilder.append("          ");
          paramStringBuilder.append(arrayOfString[j]);
          paramStringBuilder.append(" p");
          paramStringBuilder.append(j);
          if (j < i - 1)
            paramStringBuilder.append(','); 
          paramStringBuilder.append('\n');
        } 
        paramStringBuilder.append(str);
        paramStringBuilder.append("        ) {\n");
        paramStringBuilder.append(str);
        paramStringBuilder.append("          ");
        j = "void".equals(listenerMethod.returnType()) ^ true;
        if (j != 0)
          paramStringBuilder.append("return "); 
        if (map1.containsKey(listenerMethod)) {
          Iterator<ListenerBinding> iterator10 = ((Set)map1.get(listenerMethod)).iterator();
          while (iterator10.hasNext()) {
            ListenerBinding listenerBinding = iterator10.next();
            paramStringBuilder.append("target.");
            paramStringBuilder.append(listenerBinding.getName());
            paramStringBuilder.append('(');
            List<Parameter> list = listenerBinding.getParameters();
            String[] arrayOfString1 = listenerMethod.parameters();
            i = list.size();
            for (j = 0; j < i; j++) {
              Parameter parameter = list.get(j);
              int k = parameter.getListenerPosition();
              if (parameter.requiresCast(arrayOfString1[k])) {
                paramStringBuilder.append("finder.<");
                paramStringBuilder.append(parameter.getType());
                paramStringBuilder.append(">castParam(p");
                paramStringBuilder.append(k);
                paramStringBuilder.append(", \"");
                paramStringBuilder.append(listenerMethod.name());
                paramStringBuilder.append("\", ");
                paramStringBuilder.append(k);
                paramStringBuilder.append(", \"");
                paramStringBuilder.append(listenerBinding.getName());
                paramStringBuilder.append("\", ");
                paramStringBuilder.append(j);
                paramStringBuilder.append(")");
              } else {
                paramStringBuilder.append('p');
                paramStringBuilder.append(k);
              } 
              if (j < i - 1)
                paramStringBuilder.append(", "); 
            } 
            paramStringBuilder.append(");");
            if (iterator10.hasNext()) {
              paramStringBuilder.append("\n");
              paramStringBuilder.append("          ");
            } 
          } 
          Iterator<ListenerMethod> iterator9 = iterator1;
          iterator7 = iterator;
          iterator4 = iterator9;
        } else {
          Iterator<ListenerMethod> iterator9 = iterator4;
          iterator3 = iterator7;
          if (j != 0) {
            paramStringBuilder.append(listenerMethod.defaultReturn());
            paramStringBuilder.append(';');
          } 
          iterator6 = iterator9;
        } 
        paramStringBuilder.append('\n');
        paramStringBuilder.append(str);
        paramStringBuilder.append("        }\n");
        Iterator<ListenerMethod> iterator8 = iterator6;
        Iterator<Map.Entry> iterator5 = iterator3;
        Iterator<ListenerMethod> iterator2 = iterator8;
      } 
      paramStringBuilder.append(str);
      paramStringBuilder.append("      });\n");
    } 
    if (bool)
      paramStringBuilder.append("    }\n"); 
  }
  
  private void emitReset(StringBuilder paramStringBuilder) {
    paramStringBuilder.append("  @Override public void reset(T target) {\n");
    if (this.parentInjector != null)
      paramStringBuilder.append("    super.reset(target);\n\n"); 
    null = this.viewIdMap.values().iterator();
    while (null.hasNext()) {
      for (ViewBinding viewBinding : ((ViewInjection)null.next()).getViewBindings()) {
        paramStringBuilder.append("    target.");
        paramStringBuilder.append(viewBinding.getName());
        paramStringBuilder.append(" = null;\n");
      } 
    } 
    for (CollectionBinding collectionBinding : this.collectionBindings.keySet()) {
      paramStringBuilder.append("    target.");
      paramStringBuilder.append(collectionBinding.getName());
      paramStringBuilder.append(" = null;\n");
    } 
    paramStringBuilder.append("  }\n");
  }
  
  private void emitViewBindings(StringBuilder paramStringBuilder, ViewInjection paramViewInjection) {
    Collection<ViewBinding> collection = paramViewInjection.getViewBindings();
    if (collection.isEmpty())
      return; 
    for (ViewBinding viewBinding : collection) {
      paramStringBuilder.append("    target.");
      paramStringBuilder.append(viewBinding.getName());
      paramStringBuilder.append(" = ");
      if (viewBinding.requiresCast()) {
        paramStringBuilder.append("finder.castView(view");
        paramStringBuilder.append(", ");
        paramStringBuilder.append(paramViewInjection.getId());
        paramStringBuilder.append(", \"");
        emitHumanDescription(paramStringBuilder, (Collection)collection);
        paramStringBuilder.append("\");\n");
        continue;
      } 
      paramStringBuilder.append("view;\n");
    } 
  }
  
  private void emitViewInjection(StringBuilder paramStringBuilder, ViewInjection paramViewInjection) {
    paramStringBuilder.append("    view = ");
    List<Binding> list = paramViewInjection.getRequiredBindings();
    if (list.isEmpty()) {
      paramStringBuilder.append("finder.findOptionalView(source, ");
      paramStringBuilder.append(paramViewInjection.getId());
      paramStringBuilder.append(", null);\n");
    } else if (paramViewInjection.getId() == -1) {
      paramStringBuilder.append("target;\n");
    } else {
      paramStringBuilder.append("finder.findRequiredView(source, ");
      paramStringBuilder.append(paramViewInjection.getId());
      paramStringBuilder.append(", \"");
      emitHumanDescription(paramStringBuilder, list);
      paramStringBuilder.append("\");\n");
    } 
    emitViewBindings(paramStringBuilder, paramViewInjection);
    emitListenerBindings(paramStringBuilder, paramViewInjection);
  }
  
  static List<ListenerMethod> getListenerMethods(ListenerClass paramListenerClass) {
    if ((paramListenerClass.method()).length == 1)
      return Arrays.asList(paramListenerClass.method()); 
    try {
      IllegalStateException illegalStateException;
      ArrayList<ListenerMethod> arrayList = new ArrayList();
      this();
      Class<? extends Enum<?>> clazz = paramListenerClass.callbacks();
      Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
      int i = arrayOfEnum.length;
      byte b = 0;
      while (b < i) {
        Enum enum_ = arrayOfEnum[b];
        ListenerMethod listenerMethod = clazz.getField(enum_.name()).<ListenerMethod>getAnnotation(ListenerMethod.class);
        if (listenerMethod != null) {
          arrayList.add(listenerMethod);
          b++;
          continue;
        } 
        illegalStateException = new IllegalStateException();
        this(String.format("@%s's %s.%s missing @%s annotation.", new Object[] { clazz.getEnclosingClass().getSimpleName(), clazz.getSimpleName(), enum_.name(), ListenerMethod.class.getSimpleName() }));
        throw illegalStateException;
      } 
      return (List<ListenerMethod>)illegalStateException;
    } catch (NoSuchFieldException noSuchFieldException) {
      throw new AssertionError(noSuchFieldException);
    } 
  }
  
  private ViewInjection getOrCreateViewInjection(int paramInt) {
    ViewInjection viewInjection2 = this.viewIdMap.get(Integer.valueOf(paramInt));
    ViewInjection viewInjection1 = viewInjection2;
    if (viewInjection2 == null) {
      viewInjection1 = new ViewInjection(paramInt);
      this.viewIdMap.put(Integer.valueOf(paramInt), viewInjection1);
    } 
    return viewInjection1;
  }
  
  void addCollection(int[] paramArrayOfint, CollectionBinding paramCollectionBinding) {
    this.collectionBindings.put(paramCollectionBinding, paramArrayOfint);
  }
  
  boolean addListener(int paramInt, ListenerClass paramListenerClass, ListenerMethod paramListenerMethod, ListenerBinding paramListenerBinding) {
    ViewInjection viewInjection = getOrCreateViewInjection(paramInt);
    if (viewInjection.hasListenerBinding(paramListenerClass, paramListenerMethod) && !"void".equals(paramListenerMethod.returnType()))
      return false; 
    viewInjection.addListenerBinding(paramListenerClass, paramListenerMethod, paramListenerBinding);
    return true;
  }
  
  void addView(int paramInt, ViewBinding paramViewBinding) {
    getOrCreateViewInjection(paramInt).addViewBinding(paramViewBinding);
  }
  
  String brewJava() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("// Generated code from Butter Knife. Do not modify!\n");
    stringBuilder.append("package ");
    stringBuilder.append(this.classPackage);
    stringBuilder.append(";\n\n");
    stringBuilder.append("import android.view.View;\n");
    stringBuilder.append("import butterknife.ButterKnife.Finder;\n");
    if (this.parentInjector == null)
      stringBuilder.append("import butterknife.ButterKnife.Injector;\n"); 
    stringBuilder.append('\n');
    stringBuilder.append("public class ");
    stringBuilder.append(this.className);
    stringBuilder.append("<T extends ");
    stringBuilder.append(this.targetClass);
    stringBuilder.append(">");
    if (this.parentInjector != null) {
      stringBuilder.append(" extends ");
      stringBuilder.append(this.parentInjector);
      stringBuilder.append("<T>");
    } else {
      stringBuilder.append(" implements Injector<T>");
    } 
    stringBuilder.append(" {\n");
    emitInject(stringBuilder);
    stringBuilder.append('\n');
    emitReset(stringBuilder);
    stringBuilder.append("}\n");
    return stringBuilder.toString();
  }
  
  String getFqcn() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.classPackage);
    stringBuilder.append(".");
    stringBuilder.append(this.className);
    return stringBuilder.toString();
  }
  
  ViewInjection getViewInjection(int paramInt) {
    return this.viewIdMap.get(Integer.valueOf(paramInt));
  }
  
  void setParentInjector(String paramString) {
    this.parentInjector = paramString;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\internal\ViewInjector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */