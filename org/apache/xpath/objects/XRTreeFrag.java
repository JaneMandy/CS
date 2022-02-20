package org.apache.xpath.objects;

import javax.xml.transform.TransformerException;
import org.apache.xml.dtm.DTM;
import org.apache.xml.dtm.DTMIterator;
import org.apache.xml.dtm.ref.DTMNodeIterator;
import org.apache.xml.dtm.ref.DTMNodeList;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xml.utils.XMLString;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.NodeSetDTM;
import org.apache.xpath.XPathContext;
import org.apache.xpath.axes.RTFIterator;
import org.w3c.dom.NodeList;

public class XRTreeFrag extends XObject implements Cloneable {
   DTM m_dtm;
   int m_dtmRoot;
   XPathContext m_xctxt;
   boolean m_allowRelease = false;
   private XMLString m_xmlStr = null;

   public XRTreeFrag(int root, XPathContext xctxt, ExpressionNode parent) {
      super((Object)null);
      this.exprSetParent(parent);
      this.m_dtmRoot = root;
      this.m_xctxt = xctxt;
      this.m_dtm = xctxt.getDTM(root);
   }

   public XRTreeFrag(int root, XPathContext xctxt) {
      super((Object)null);
      this.m_dtmRoot = root;
      this.m_xctxt = xctxt;
      this.m_dtm = xctxt.getDTM(root);
   }

   public Object object() {
      return this.m_xctxt != null ? new DTMNodeIterator(new NodeSetDTM(this.m_dtmRoot, this.m_xctxt.getDTMManager())) : super.object();
   }

   public XRTreeFrag(Expression expr) {
      super(expr);
   }

   protected void finalize() throws Throwable {
      try {
         this.destruct();
      } finally {
         super.finalize();
      }

   }

   public void allowDetachToRelease(boolean allowRelease) {
      this.m_allowRelease = allowRelease;
   }

   public void detach() {
      if (this.m_allowRelease) {
         int ident = this.m_xctxt.getDTMIdentity(this.m_dtm);
         DTM foundDTM = this.m_xctxt.getDTM(ident);
         if (foundDTM == this.m_dtm) {
            this.m_xctxt.release(this.m_dtm, true);
            this.m_dtm = null;
            this.m_xctxt = null;
         }

         super.m_obj = null;
      }

   }

   public void destruct() {
      if (null != this.m_dtm) {
         int ident = this.m_xctxt.getDTMIdentity(this.m_dtm);
         DTM foundDTM = this.m_xctxt.getDTM(ident);
         if (foundDTM == this.m_dtm) {
            this.m_xctxt.release(this.m_dtm, true);
            this.m_dtm = null;
            this.m_xctxt = null;
         }
      }

      super.m_obj = null;
   }

   public int getType() {
      return 5;
   }

   public String getTypeString() {
      return "#RTREEFRAG";
   }

   public double num() throws TransformerException {
      XMLString s = this.xstr();
      return s.toDouble();
   }

   public boolean bool() {
      return true;
   }

   public XMLString xstr() {
      if (null == this.m_xmlStr) {
         this.m_xmlStr = this.m_dtm.getStringValue(this.m_dtmRoot);
      }

      return this.m_xmlStr;
   }

   public void appendToFsb(FastStringBuffer fsb) {
      XString xstring = (XString)this.xstr();
      xstring.appendToFsb(fsb);
   }

   public String str() {
      String str = this.m_dtm.getStringValue(this.m_dtmRoot).toString();
      return null == str ? "" : str;
   }

   public int rtf() {
      return this.m_dtmRoot;
   }

   public DTMIterator asNodeIterator() {
      return new RTFIterator(this.m_dtmRoot, this.m_xctxt.getDTMManager());
   }

   public NodeList convertToNodeset() {
      return (NodeList)(super.m_obj instanceof NodeList ? (NodeList)super.m_obj : new DTMNodeList(this.asNodeIterator()));
   }

   public boolean equals(XObject obj2) {
      try {
         if (4 == obj2.getType()) {
            return obj2.equals(this);
         } else if (1 == obj2.getType()) {
            return this.bool() == obj2.bool();
         } else if (2 == obj2.getType()) {
            return this.num() == obj2.num();
         } else if (4 == obj2.getType()) {
            return this.xstr().equals(obj2.xstr());
         } else if (3 == obj2.getType()) {
            return this.xstr().equals(obj2.xstr());
         } else {
            return 5 == obj2.getType() ? this.xstr().equals(obj2.xstr()) : super.equals(obj2);
         }
      } catch (TransformerException var3) {
         throw new WrappedRuntimeException(var3);
      }
   }
}
