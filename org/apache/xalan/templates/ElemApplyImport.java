package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;

public class ElemApplyImport extends ElemTemplateElement {
   public int getXSLToken() {
      return 72;
   }

   public String getNodeName() {
      return "apply-imports";
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      if (transformer.currentTemplateRuleIsNull()) {
         transformer.getMsgMgr().error(this, "ER_NO_APPLY_IMPORT_IN_FOR_EACH");
      }

      if (TransformerImpl.S_DEBUG) {
         transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
      }

      int sourceNode = transformer.getXPathContext().getCurrentNode();
      if (-1 != sourceNode) {
         transformer.applyTemplateToNode(this, (ElemTemplate)null, sourceNode);
      } else {
         transformer.getMsgMgr().error(this, "ER_NULL_SOURCENODE_APPLYIMPORTS");
      }

      if (TransformerImpl.S_DEBUG) {
         transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
      }

   }

   public ElemTemplateElement appendChild(ElemTemplateElement newChild) {
      this.error("ER_CANNOT_ADD", new Object[]{newChild.getNodeName(), this.getNodeName()});
      return null;
   }
}
