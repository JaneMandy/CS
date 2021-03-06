package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;

public class Bool extends UnaryOperation {
   public XObject operate(XObject right) throws TransformerException {
      if (1 == right.getType()) {
         return right;
      } else {
         return right.bool() ? XBoolean.S_TRUE : XBoolean.S_FALSE;
      }
   }

   public boolean bool(XPathContext xctxt) throws TransformerException {
      return super.m_right.bool(xctxt);
   }
}
