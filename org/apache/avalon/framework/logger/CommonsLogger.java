package org.apache.avalon.framework.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CommonsLogger implements Logger {
   private final Log log;
   private final String name;

   public CommonsLogger(Log log, String name) {
      this.log = log;
      this.name = name;
   }

   public void debug(String message) {
      this.log.debug(message);
   }

   public void debug(String message, Throwable throwable) {
      this.log.debug(message, throwable);
   }

   public boolean isDebugEnabled() {
      return this.log.isDebugEnabled();
   }

   public void info(String message) {
      this.log.info(message);
   }

   public void info(String message, Throwable throwable) {
      this.log.info(message, throwable);
   }

   public boolean isInfoEnabled() {
      return this.log.isInfoEnabled();
   }

   public void warn(String message) {
      this.log.warn(message);
   }

   public void warn(String message, Throwable throwable) {
      this.log.warn(message, throwable);
   }

   public boolean isWarnEnabled() {
      return this.log.isWarnEnabled();
   }

   public void error(String message) {
      this.log.error(message);
   }

   public void error(String message, Throwable throwable) {
      this.log.error(message, throwable);
   }

   public boolean isErrorEnabled() {
      return this.log.isErrorEnabled();
   }

   public void fatalError(String message) {
      this.log.fatal(message);
   }

   public void fatalError(String message, Throwable throwable) {
      this.log.fatal(message, throwable);
   }

   public boolean isFatalErrorEnabled() {
      return this.log.isFatalEnabled();
   }

   public Logger getChildLogger(String name) {
      String newName = this.name + '.' + name;
      return new CommonsLogger(LogFactory.getLog(newName), newName);
   }
}
