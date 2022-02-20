package org.apache.xpath.objects;

import org.apache.xpath.XPathContext;

public class XNull extends XNodeSet {
   public int getType() {
      return -1;
   }

   public String getTypeString() {
      return "#CLASS_NULL";
   }

   public double num() {
      return 0.0D;
   }

   public boolean bool() {
      return false;
   }

   public String str() {
      return "";
   }

   public int rtf(XPathContext support) {
      return -1;
   }

   public boolean equals(XObject obj2) {
      return obj2.getType() == -1;
   }
}
