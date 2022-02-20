package org.apache.avalon.framework.availability;

public class UnavailableException extends RuntimeException {
   private String m_Key;

   public UnavailableException(String message, String key) {
      super(message);
      this.m_Key = key;
   }

   public String getLookupKey() {
      return this.m_Key;
   }
}
