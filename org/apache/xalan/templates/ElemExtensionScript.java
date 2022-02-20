package org.apache.xalan.templates;

public class ElemExtensionScript extends ElemTemplateElement {
   private String m_lang = null;
   private String m_src = null;

   public void setLang(String v) {
      this.m_lang = v;
   }

   public String getLang() {
      return this.m_lang;
   }

   public void setSrc(String v) {
      this.m_src = v;
   }

   public String getSrc() {
      return this.m_src;
   }

   public int getXSLToken() {
      return 86;
   }
}
