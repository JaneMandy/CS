package org.apache.xalan.xsltc.compiler;

public final class CompilerException extends Exception {
   private String _msg;

   public CompilerException() {
   }

   public CompilerException(Exception e) {
      super(e.toString());
      this._msg = e.toString();
   }

   public CompilerException(String message) {
      super(message);
      this._msg = message;
   }

   public String getMessage() {
      int col = this._msg.indexOf(58);
      return col > -1 ? this._msg.substring(col) : this._msg;
   }
}
