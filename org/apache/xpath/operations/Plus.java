package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

public class Plus extends Operation {
   public XObject operate(XObject left, XObject right) throws TransformerException {
      return new XNumber(left.num() + right.num());
   }

   public double num(XPathContext xctxt) throws TransformerException {
      return super.m_right.num(xctxt) + super.m_left.num(xctxt);
   }
}
