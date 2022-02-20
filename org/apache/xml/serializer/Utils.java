package org.apache.xml.serializer;

import java.util.Hashtable;

class Utils {
   static Class ClassForName(String classname) throws ClassNotFoundException {
      Object o = Utils.CacheHolder.cache.get(classname);
      Class c;
      if (o == null) {
         c = Class.forName(classname);
         Utils.CacheHolder.cache.put(classname, c);
      } else {
         c = (Class)o;
      }

      return c;
   }

   private static class CacheHolder {
      static final Hashtable cache = new Hashtable();
   }
}
