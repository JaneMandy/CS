package org.apache.xalan.templates;

import java.util.Enumeration;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.serializer.SerializationHandler;
import org.apache.xml.utils.StringVector;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class ElemLiteralResult extends ElemUse {
   private boolean isLiteralResultAsStylesheet = false;
   private Vector m_avts = null;
   private Vector m_xslAttr = null;
   private String m_namespace;
   private String m_localName;
   private String m_rawName;
   private StringVector m_ExtensionElementURIs;
   private String m_version;
   private StringVector m_excludeResultPrefixes;

   public void setIsLiteralResultAsStylesheet(boolean b) {
      this.isLiteralResultAsStylesheet = b;
   }

   public boolean getIsLiteralResultAsStylesheet() {
      return this.isLiteralResultAsStylesheet;
   }

   public void compose(StylesheetRoot sroot) throws TransformerException {
      super.compose(sroot);
      StylesheetRoot.ComposeState cstate = sroot.getComposeState();
      Vector vnames = cstate.getVariableNames();
      if (null != this.m_avts) {
         int nAttrs = this.m_avts.size();

         for(int i = nAttrs - 1; i >= 0; --i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            avt.fixupVariables(vnames, cstate.getGlobalsSize());
         }
      }

   }

   public void addLiteralResultAttribute(AVT avt) {
      if (null == this.m_avts) {
         this.m_avts = new Vector();
      }

      this.m_avts.addElement(avt);
   }

   public void addLiteralResultAttribute(String att) {
      if (null == this.m_xslAttr) {
         this.m_xslAttr = new Vector();
      }

      this.m_xslAttr.addElement(att);
   }

   public void setXmlSpace(AVT avt) {
      this.addLiteralResultAttribute(avt);
      String val = avt.getSimpleString();
      if (val.equals("default")) {
         super.setXmlSpace(2);
      } else if (val.equals("preserve")) {
         super.setXmlSpace(1);
      }

   }

   public AVT getLiteralResultAttribute(String name) {
      if (null != this.m_avts) {
         int nAttrs = this.m_avts.size();

         for(int i = nAttrs - 1; i >= 0; --i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            if (avt.getRawName().equals(name)) {
               return avt;
            }
         }
      }

      return null;
   }

   public boolean containsExcludeResultPrefix(String prefix, String uri) {
      if (uri == null || null == this.m_excludeResultPrefixes && null == this.m_ExtensionElementURIs) {
         return super.containsExcludeResultPrefix(prefix, uri);
      } else {
         if (prefix.length() == 0) {
            prefix = "#default";
         }

         if (this.m_excludeResultPrefixes != null) {
            for(int i = 0; i < this.m_excludeResultPrefixes.size(); ++i) {
               if (uri.equals(this.getNamespaceForPrefix(this.m_excludeResultPrefixes.elementAt(i)))) {
                  return true;
               }
            }
         }

         return this.m_ExtensionElementURIs != null && this.m_ExtensionElementURIs.contains(uri) ? true : super.containsExcludeResultPrefix(prefix, uri);
      }
   }

   public void resolvePrefixTables() throws TransformerException {
      super.resolvePrefixTables();
      StylesheetRoot stylesheet = this.getStylesheetRoot();
      if (null != this.m_namespace && this.m_namespace.length() > 0) {
         NamespaceAlias nsa = stylesheet.getNamespaceAliasComposed(this.m_namespace);
         if (null != nsa) {
            this.m_namespace = nsa.getResultNamespace();
            String resultPrefix = nsa.getStylesheetPrefix();
            if (null != resultPrefix && resultPrefix.length() > 0) {
               this.m_rawName = resultPrefix + ":" + this.m_localName;
            } else {
               this.m_rawName = this.m_localName;
            }
         }
      }

      if (null != this.m_avts) {
         int n = this.m_avts.size();

         for(int i = 0; i < n; ++i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            String ns = avt.getURI();
            if (null != ns && ns.length() > 0) {
               NamespaceAlias nsa = stylesheet.getNamespaceAliasComposed(this.m_namespace);
               if (null != nsa) {
                  String namespace = nsa.getResultNamespace();
                  String resultPrefix = nsa.getStylesheetPrefix();
                  String rawName = avt.getName();
                  if (null != resultPrefix && resultPrefix.length() > 0) {
                     rawName = resultPrefix + ":" + rawName;
                  }

                  avt.setURI(namespace);
                  avt.setRawName(rawName);
               }
            }
         }
      }

   }

   boolean needToCheckExclude() {
      if (null == this.m_excludeResultPrefixes && null == super.m_prefixTable && this.m_ExtensionElementURIs == null) {
         return false;
      } else {
         if (null == super.m_prefixTable) {
            super.m_prefixTable = new Vector();
         }

         return true;
      }
   }

   public void setNamespace(String ns) {
      if (null == ns) {
         ns = "";
      }

      this.m_namespace = ns;
   }

   public String getNamespace() {
      return this.m_namespace;
   }

   public void setLocalName(String localName) {
      this.m_localName = localName;
   }

   public String getLocalName() {
      return this.m_localName;
   }

   public void setRawName(String rawName) {
      this.m_rawName = rawName;
   }

   public String getRawName() {
      return this.m_rawName;
   }

   public String getPrefix() {
      int len = this.m_rawName.length() - this.m_localName.length() - 1;
      return len > 0 ? this.m_rawName.substring(0, len) : "";
   }

   public void setExtensionElementPrefixes(StringVector v) {
      this.m_ExtensionElementURIs = v;
   }

   public String getExtensionElementPrefix(int i) throws ArrayIndexOutOfBoundsException {
      if (null == this.m_ExtensionElementURIs) {
         throw new ArrayIndexOutOfBoundsException();
      } else {
         return this.m_ExtensionElementURIs.elementAt(i);
      }
   }

   public int getExtensionElementPrefixCount() {
      return null != this.m_ExtensionElementURIs ? this.m_ExtensionElementURIs.size() : 0;
   }

   public boolean containsExtensionElementURI(String uri) {
      return null == this.m_ExtensionElementURIs ? false : this.m_ExtensionElementURIs.contains(uri);
   }

   public int getXSLToken() {
      return 77;
   }

   public String getNodeName() {
      return this.m_rawName;
   }

   public void setVersion(String v) {
      this.m_version = v;
   }

   public String getVersion() {
      return this.m_version;
   }

   public void setExcludeResultPrefixes(StringVector v) {
      this.m_excludeResultPrefixes = v;
   }

   private boolean excludeResultNSDecl(String prefix, String uri) throws TransformerException {
      return null != this.m_excludeResultPrefixes ? this.containsExcludeResultPrefix(prefix, uri) : false;
   }

   public void execute(TransformerImpl transformer) throws TransformerException {
      SerializationHandler rhandler = transformer.getSerializationHandler();

      try {
         if (TransformerImpl.S_DEBUG) {
            rhandler.flushPending();
            transformer.getTraceManager().fireTraceEvent((ElemTemplateElement)this);
         }

         rhandler.startPrefixMapping(this.getPrefix(), this.getNamespace());
         this.executeNSDecls(transformer);
         rhandler.startElement(this.getNamespace(), this.getLocalName(), this.getRawName());
      } catch (SAXException var14) {
         throw new TransformerException(var14);
      }

      TransformerException tException = null;

      try {
         super.execute(transformer);
         if (null != this.m_avts) {
            int nAttrs = this.m_avts.size();

            for(int i = nAttrs - 1; i >= 0; --i) {
               AVT avt = (AVT)this.m_avts.elementAt(i);
               XPathContext xctxt = transformer.getXPathContext();
               int sourceNode = xctxt.getCurrentNode();
               String stringedValue = avt.evaluate(xctxt, sourceNode, this);
               if (null != stringedValue) {
                  rhandler.addAttribute(avt.getURI(), avt.getName(), avt.getRawName(), "CDATA", stringedValue);
               }
            }
         }

         transformer.executeChildTemplates(this, true);
      } catch (TransformerException var12) {
         tException = var12;
      } catch (SAXException var13) {
         tException = new TransformerException(var13);
      }

      try {
         if (TransformerImpl.S_DEBUG) {
            transformer.getTraceManager().fireTraceEndEvent((ElemTemplateElement)this);
         }

         rhandler.endElement(this.getNamespace(), this.getLocalName(), this.getRawName());
      } catch (SAXException var11) {
         if (tException != null) {
            throw tException;
         }

         throw new TransformerException(var11);
      }

      if (tException != null) {
         throw tException;
      } else {
         this.unexecuteNSDecls(transformer);

         try {
            rhandler.endPrefixMapping(this.getPrefix());
         } catch (SAXException var10) {
            throw new TransformerException(var10);
         }
      }
   }

   public Enumeration enumerateLiteralResultAttributes() {
      return null == this.m_avts ? null : this.m_avts.elements();
   }

   protected boolean accept(XSLTVisitor visitor) {
      return visitor.visitLiteralResultElement(this);
   }

   protected void callChildVisitors(XSLTVisitor visitor, boolean callAttrs) {
      if (callAttrs && null != this.m_avts) {
         int nAttrs = this.m_avts.size();

         for(int i = nAttrs - 1; i >= 0; --i) {
            AVT avt = (AVT)this.m_avts.elementAt(i);
            avt.callVisitors(visitor);
         }
      }

      super.callChildVisitors(visitor, callAttrs);
   }
}
