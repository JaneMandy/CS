package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.WrappedRuntimeException;

public class XBooleanStatic extends XBoolean {
   boolean m_val;

   public XBooleanStatic(boolean b) {
      super(b);
      this.m_val = b;
   }

   public boolean equals(XObject obj2) {
      try {
         return this.m_val == obj2.bool();
      } catch (TransformerException var3) {
         throw new WrappedRuntimeException(var3);
      }
   }
}
