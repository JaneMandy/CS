package org.apache.xpath.functions;

import javax.xml.transform.TransformerException;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;

public class FuncSubstringBefore extends Function2Args {
   public XObject execute(XPathContext xctxt) throws TransformerException {
      String s1 = super.m_arg0.execute(xctxt).str();
      String s2 = super.m_arg1.execute(xctxt).str();
      int index = s1.indexOf(s2);
      return -1 == index ? XString.EMPTYSTRING : new XString(s1.substring(0, index));
   }
}
