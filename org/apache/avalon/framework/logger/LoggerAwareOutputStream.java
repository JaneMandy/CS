package org.apache.avalon.framework.logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class LoggerAwareOutputStream extends OutputStream {
   private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
   protected final Logger m_logger;

   public LoggerAwareOutputStream(Logger logger) {
      this.m_logger = logger;
   }

   public void write(int b) throws IOException {
      if (b == 10) {
         byte[] content = this.bos.toByteArray();
         this.logMessage(new String(content));
         this.bos.reset();
      } else {
         this.bos.write(b);
      }
   }

   public void flush() throws IOException {
      byte[] content = this.bos.toByteArray();
      this.logMessage(new String(content));
      this.bos.reset();
   }

   public void close() throws IOException {
      this.flush();
   }

   protected abstract void logMessage(String var1);
}
