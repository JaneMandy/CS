package org.apache.xpath.functions;

public class WrongNumberArgsException extends Exception {
   public WrongNumberArgsException(String argsExpected) {
      super(argsExpected);
   }
}
