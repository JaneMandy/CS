package org.apache.xml.utils;

public class WrappedRuntimeException extends RuntimeException {
   private Exception m_exception;

   public WrappedRuntimeException(Exception e) {
      super(e.getMessage());
      this.m_exception = e;
   }

   public WrappedRuntimeException(String msg, Exception e) {
      super(msg);
      this.m_exception = e;
   }

   public Exception getException() {
      return this.m_exception;
   }
}
