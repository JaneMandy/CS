package org.apache.avalon.framework.context;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class DefaultContext implements Context, Serializable {
   private static final DefaultContext.Hidden HIDDEN_MAKER = new DefaultContext.Hidden();
   private final Map m_contextData;
   private final Context m_parent;
   private boolean m_readOnly;

   public DefaultContext(Map contextData, Context parent) {
      this.m_parent = parent;
      this.m_contextData = contextData;
   }

   public DefaultContext(Map contextData) {
      this(contextData, (Context)null);
   }

   public DefaultContext(Context parent) {
      this(new Hashtable(), parent);
   }

   public DefaultContext() {
      this((Context)null);
   }

   public Object get(Object key) throws ContextException {
      Object data = this.m_contextData.get(key);
      String message;
      if (null != data) {
         if (data instanceof DefaultContext.Hidden) {
            message = "Unable to locate " + key;
            throw new ContextException(message);
         } else {
            return data instanceof Resolvable ? ((Resolvable)data).resolve(this) : data;
         }
      } else if (null == this.m_parent) {
         message = "Unable to resolve context key: " + key;
         throw new ContextException(message);
      } else {
         return this.m_parent.get(key);
      }
   }

   public void put(Object key, Object value) throws IllegalStateException {
      this.checkWriteable();
      if (null == value) {
         this.m_contextData.remove(key);
      } else {
         this.m_contextData.put(key, value);
      }

   }

   public void hide(Object key) throws IllegalStateException {
      this.checkWriteable();
      this.m_contextData.put(key, HIDDEN_MAKER);
   }

   protected final Map getContextData() {
      return this.m_contextData;
   }

   protected final Context getParent() {
      return this.m_parent;
   }

   public void makeReadOnly() {
      this.m_readOnly = true;
   }

   protected final void checkWriteable() throws IllegalStateException {
      if (this.m_readOnly) {
         String message = "Context is read only and can not be modified";
         throw new IllegalStateException("Context is read only and can not be modified");
      }
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!o.getClass().equals(this.getClass())) {
         return false;
      } else {
         DefaultContext other = (DefaultContext)o;
         if (!this.m_contextData.equals(other.m_contextData)) {
            return false;
         } else {
            if (this.m_parent == null) {
               if (other.m_parent != null) {
                  return false;
               }
            } else if (!this.m_parent.equals(other.m_parent)) {
               return false;
            }

            return this.m_readOnly == other.m_readOnly;
         }
      }
   }

   public int hashCode() {
      int hash = this.m_contextData.hashCode();
      if (this.m_parent != null) {
         hash ^= this.m_parent.hashCode();
      } else {
         hash >>>= 3;
      }

      hash >>>= this.m_readOnly ? 7 : 13;
      return hash;
   }

   private static final class Hidden implements Serializable {
      private Hidden() {
      }

      // $FF: synthetic method
      Hidden(Object x0) {
         this();
      }
   }
}
