package org.apache.xpath.operations;

import javax.xml.transform.TransformerException;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;

/** @deprecated */
public class Quo extends Operation {
   public XObject operate(XObject left, XObject right) throws TransformerException {
      return new XNumber((double)((int)(left.num() / right.num())));
   }
}
