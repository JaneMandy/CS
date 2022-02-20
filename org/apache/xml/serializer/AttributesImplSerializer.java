package org.apache.xml.serializer;

import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class AttributesImplSerializer extends AttributesImpl {
   private Hashtable m_indexFromQName = new Hashtable();
   public static final int MAX = 12;
   private static final int MAXMinus1 = 11;

   public int getIndex(String qname) {
      int index;
      if (super.getLength() < 12) {
         index = super.getIndex(qname);
         return index;
      } else {
         Integer i = (Integer)this.m_indexFromQName.get(qname);
         if (i == null) {
            index = -1;
         } else {
            index = i;
         }

         return index;
      }
   }

   public void addAttribute(String uri, String local, String qname, String type, String val) {
      int index = super.getLength();
      super.addAttribute(uri, local, qname, type, val);
      if (index >= 11) {
         if (index == 11) {
            this.switchOverToHash(12);
         } else {
            Integer i = new Integer(index);
            this.m_indexFromQName.put(qname, i);
         }

      }
   }

   private void switchOverToHash(int numAtts) {
      for(int index = 0; index < numAtts; ++index) {
         String qName = super.getQName(index);
         Integer i = new Integer(index);
         this.m_indexFromQName.put(qName, i);
      }

   }

   public void clear() {
      int len = super.getLength();
      super.clear();
      if (12 <= len) {
         this.m_indexFromQName.clear();
      }

   }

   public void setAttributes(Attributes atts) {
      super.setAttributes(atts);
      int numAtts = atts.getLength();
      if (12 <= numAtts) {
         this.switchOverToHash(numAtts);
      }

   }
}
