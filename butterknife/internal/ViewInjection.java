package butterknife.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class ViewInjection {
  private final int id;
  
  private final LinkedHashMap<ListenerClass, Map<ListenerMethod, Set<ListenerBinding>>> listenerBindings = new LinkedHashMap<ListenerClass, Map<ListenerMethod, Set<ListenerBinding>>>();
  
  private final Set<ViewBinding> viewBindings = new LinkedHashSet<ViewBinding>();
  
  ViewInjection(int paramInt) {
    this.id = paramInt;
  }
  
  public void addListenerBinding(ListenerClass paramListenerClass, ListenerMethod paramListenerMethod, ListenerBinding paramListenerBinding) {
    Set<ListenerBinding> set1;
    Map<Object, Object> map = (Map)this.listenerBindings.get(paramListenerClass);
    if (map == null) {
      map = new LinkedHashMap<Object, Object>();
      this.listenerBindings.put(paramListenerClass, map);
      paramListenerClass = null;
    } else {
      set1 = (Set)map.get(paramListenerMethod);
    } 
    Set<ListenerBinding> set2 = set1;
    if (set1 == null) {
      set2 = new LinkedHashSet();
      map.put(paramListenerMethod, set2);
    } 
    set2.add(paramListenerBinding);
  }
  
  public void addViewBinding(ViewBinding paramViewBinding) {
    this.viewBindings.add(paramViewBinding);
  }
  
  public int getId() {
    return this.id;
  }
  
  public Map<ListenerClass, Map<ListenerMethod, Set<ListenerBinding>>> getListenerBindings() {
    return this.listenerBindings;
  }
  
  public List<Binding> getRequiredBindings() {
    ArrayList<ViewBinding> arrayList = new ArrayList();
    for (ViewBinding viewBinding : this.viewBindings) {
      if (viewBinding.isRequired())
        arrayList.add(viewBinding); 
    } 
    Iterator<Map> iterator = this.listenerBindings.values().iterator();
    while (iterator.hasNext()) {
      Iterator<Set> iterator1 = ((Map)iterator.next()).values().iterator();
      while (iterator1.hasNext()) {
        for (ListenerBinding listenerBinding : iterator1.next()) {
          if (listenerBinding.isRequired())
            arrayList.add(listenerBinding); 
        } 
      } 
    } 
    return (List)arrayList;
  }
  
  public Collection<ViewBinding> getViewBindings() {
    return this.viewBindings;
  }
  
  public boolean hasListenerBinding(ListenerClass paramListenerClass, ListenerMethod paramListenerMethod) {
    boolean bool;
    Map map = this.listenerBindings.get(paramListenerClass);
    if (map != null && map.containsKey(paramListenerMethod)) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool;
  }
}


/* Location:              C:\Users\admin\Desktop\Test\dex-tools-v2.4\classes-dex2jar.jar!\butterknife\internal\ViewInjection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */