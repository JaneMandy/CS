package org.apache.xml.serializer;

public class EncodingInfo {
   final String name;
   final String javaName;
   final int lastPrintable;

   public EncodingInfo(String name, String javaName, int lastPrintable) {
      this.name = name;
      this.javaName = javaName;
      this.lastPrintable = lastPrintable;
   }
}
