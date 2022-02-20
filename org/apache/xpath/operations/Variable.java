package org.apache.xpath.operations;

import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.ElemVariable;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.XPathVisitor;
import org.apache.xpath.axes.PathComponent;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;

public class Variable extends Expression implements PathComponent {
   private boolean m_fixUpWasCalled = false;
   protected QName m_qname;
   protected int m_index;
   protected boolean m_isGlobal = false;
   static final java.lang.String PSUEDOVARNAMESPACE = "http://xml.apache.org/xalan/psuedovar";

   public void setIndex(int index) {
      this.m_index = index;
   }

   public int getIndex() {
      return this.m_index;
   }

   public void setIsGlobal(boolean isGlobal) {
      this.m_isGlobal = isGlobal;
   }

   public boolean getGlobal() {
      return this.m_isGlobal;
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      this.m_fixUpWasCalled = true;
      int sz = vars.size();

      for(int i = vars.size() - 1; i >= 0; --i) {
         QName qn = (QName)vars.elementAt(i);
         if (qn.equals(this.m_qname)) {
            if (i < globalsSize) {
               this.m_isGlobal = true;
               this.m_index = i;
            } else {
               this.m_index = i - globalsSize;
            }

            return;
         }
      }

      java.lang.String msg = XPATHMessages.createXPATHMessage("ER_COULD_NOT_FIND_VAR", new Object[]{this.m_qname.toString()});
      TransformerException te = new TransformerException(msg, this);
      throw new WrappedRuntimeException(te);
   }

   public void setQName(QName qname) {
      this.m_qname = qname;
   }

   public QName getQName() {
      return this.m_qname;
   }

   public XObject execute(XPathContext xctxt) throws TransformerException {
      return this.execute(xctxt, false);
   }

   public XObject execute(XPathContext xctxt, boolean destructiveOK) throws TransformerException {
      PrefixResolver xprefixResolver = xctxt.getNamespaceContext();
      if (this.m_fixUpWasCalled) {
         Object result;
         if (this.m_isGlobal) {
            result = xctxt.getVarStack().getGlobalVariable(xctxt, this.m_index, destructiveOK);
         } else {
            result = xctxt.getVarStack().getLocalVariable(xctxt, this.m_index, destructiveOK);
         }

         if (null == result) {
            this.warn(xctxt, "WG_ILLEGAL_VARIABLE_REFERENCE", new Object[]{this.m_qname.getLocalPart()});
            result = new XNodeSet(xctxt.getDTMManager());
         }

         return (XObject)result;
      } else {
         synchronized(this) {
            ElemVariable vvar = this.getElemVariable();
            if (null != vvar) {
               this.m_index = vvar.getIndex();
               this.m_isGlobal = vvar.getIsTopLevel();
               this.m_fixUpWasCalled = true;
               XObject var6 = this.execute(xctxt);
               return var6;
            }
         }

         throw new TransformerException(XPATHMessages.createXPATHMessage("ER_VAR_NOT_RESOLVABLE", new Object[]{this.m_qname.toString()}));
      }
   }

   public ElemVariable getElemVariable() {
      ExpressionNode owner = this.getExpressionOwner();
      if (null != owner && owner instanceof ElemTemplateElement) {
         ElemTemplateElement prev = (ElemTemplateElement)owner;
         ElemVariable vvar;
         if (!(prev instanceof Stylesheet)) {
            while(!(prev.getParentNode() instanceof Stylesheet)) {
               ElemTemplateElement savedprev = prev;

               while(null != (prev = prev.getPreviousSiblingElem())) {
                  if (prev instanceof ElemVariable) {
                     vvar = (ElemVariable)prev;
                     if (vvar.getName().equals(this.m_qname)) {
                        return vvar;
                     }
                  }
               }

               prev = savedprev.getParentElem();
            }
         }

         vvar = prev.getStylesheetRoot().getVariableOrParamComposed(this.m_qname);
         if (null != vvar) {
            return vvar;
         }
      }

      return null;
   }

   public boolean isStableNumber() {
      return true;
   }

   public int getAnalysisBits() {
      ElemVariable vvar = this.getElemVariable();
      if (null != vvar) {
         XPath xpath = vvar.getSelect();
         if (null != xpath) {
            Expression expr = xpath.getExpression();
            if (null != expr && expr instanceof PathComponent) {
               return ((PathComponent)expr).getAnalysisBits();
            }
         }
      }

      return 67108864;
   }

   public void callVisitors(ExpressionOwner owner, XPathVisitor visitor) {
      visitor.visitVariableRef(owner, this);
   }

   public boolean deepEquals(Expression expr) {
      if (!this.isSameClass(expr)) {
         return false;
      } else if (!this.m_qname.equals(((Variable)expr).m_qname)) {
         return false;
      } else {
         return this.getElemVariable() == ((Variable)expr).getElemVariable();
      }
   }

   public boolean isPsuedoVarRef() {
      java.lang.String ns = this.m_qname.getNamespaceURI();
      return null != ns && ns.equals("http://xml.apache.org/xalan/psuedovar") && this.m_qname.getLocalName().startsWith("#");
   }
}
