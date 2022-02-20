package org.apache.xpath.objects;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.XPathContext;
import org.apache.xpath.res.XPATHMessages;

public class XRTreeFragSelectWrapper extends XRTreeFrag implements Cloneable {
   XObject m_selected;

   public XRTreeFragSelectWrapper(Expression expr) {
      super(expr);
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      ((Expression)super.m_obj).fixupVariables(vars, globalsSize);
   }

   public XObject execute(XPathContext xctxt) throws TransformerException {
      this.m_selected = ((Expression)super.m_obj).execute(xctxt);
      this.m_selected.allowDetachToRelease(super.m_allowRelease);
      return (XObject)(this.m_selected.getType() == 3 ? this.m_selected : new XString(this.m_selected.str()));
   }

   public void detach() {
      if (super.m_allowRelease) {
         this.m_selected.detach();
         this.m_selected = null;
      }

      super.detach();
   }

   public double num() throws TransformerException {
      return this.m_selected.num();
   }

   public XMLString xstr() {
      return this.m_selected.xstr();
   }

   public String str() {
      return this.m_selected.str();
   }

   public int getType() {
      return 3;
   }

   public int rtf() {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }

   public DTMIterator asNodeIterator() {
      throw new RuntimeException(XPATHMessages.createXPATHMessage("ER_RTF_NOT_SUPPORTED_XRTREEFRAGSELECTWRAPPER", (Object[])null));
   }
}
