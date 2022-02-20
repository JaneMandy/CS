package org.apache.xalan.processor;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TrAXFilter;
import org.apache.xalan.transformer.TransformerIdentityImpl;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.dtm.DTMManager;
import org.apache.xml.dtm.ref.sax2dtm.SAX2DTM;
import org.apache.xml.utils.DOM2Helper;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.StopParseException;
import org.apache.xml.utils.StylesheetPIHandler;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.TreeWalker;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class TransformerFactoryImpl extends SAXTransformerFactory {
   public static final String XSLT_PROPERTIES = "org/apache/xalan/res/XSLTInfo.properties";
   public static final String FEATURE_INCREMENTAL = "http://xml.apache.org/xalan/features/incremental";
   public static final String FEATURE_OPTIMIZE = "http://xml.apache.org/xalan/features/optimize";
   public static final String FEATURE_SOURCE_LOCATION = "http://xml.apache.org/xalan/properties/source-location";
   private String m_DOMsystemID = null;
   public static boolean m_optimize = true;
   public static boolean m_source_location = false;
   URIResolver m_uriResolver;
   private ErrorListener m_errorListener = new DefaultErrorHandler();

   public Templates processFromNode(Node node) throws TransformerConfigurationException {
      try {
         TemplatesHandler builder = this.newTemplatesHandler();
         TreeWalker walker = new TreeWalker(builder, new DOM2Helper(), builder.getSystemId());
         walker.traverse(node);
         return builder.getTemplates();
      } catch (SAXException var8) {
         SAXException se = var8;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(new TransformerException(se));
               return null;
            } catch (TransformerException var7) {
               throw new TransformerConfigurationException(var7);
            }
         } else {
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_PROCESSFROMNODE_FAILED", (Object[])null), var8);
         }
      } catch (TransformerConfigurationException var9) {
         throw var9;
      } catch (Exception var10) {
         Exception e = var10;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(new TransformerException(e));
               return null;
            } catch (TransformerException var6) {
               throw new TransformerConfigurationException(var6);
            }
         } else {
            throw new TransformerConfigurationException(XSLMessages.createMessage("ER_PROCESSFROMNODE_FAILED", (Object[])null), var10);
         }
      }
   }

   String getDOMsystemID() {
      return this.m_DOMsystemID;
   }

   Templates processFromNode(Node node, String systemID) throws TransformerConfigurationException {
      this.m_DOMsystemID = systemID;
      return this.processFromNode(node);
   }

   public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
      InputSource isource = null;
      Node node = null;
      XMLReader reader = null;
      String baseID;
      if (source instanceof DOMSource) {
         DOMSource dsource = (DOMSource)source;
         node = dsource.getNode();
         baseID = dsource.getSystemId();
      } else {
         isource = SAXSource.sourceToInputSource(source);
         baseID = isource.getSystemId();
      }

      StylesheetPIHandler handler = new StylesheetPIHandler(baseID, media, title, charset);
      if (this.m_uriResolver != null) {
         handler.setURIResolver(this.m_uriResolver);
      }

      try {
         if (null != node) {
            TreeWalker walker = new TreeWalker(handler, new DOM2Helper(), baseID);
            walker.traverse(node);
         } else {
            try {
               SAXParserFactory factory = SAXParserFactory.newInstance();
               factory.setNamespaceAware(true);
               SAXParser jaxpParser = factory.newSAXParser();
               reader = jaxpParser.getXMLReader();
            } catch (ParserConfigurationException var14) {
               throw new SAXException(var14);
            } catch (FactoryConfigurationError var15) {
               throw new SAXException(var15.toString());
            } catch (NoSuchMethodError var16) {
            } catch (AbstractMethodError var17) {
            }

            if (null == reader) {
               reader = XMLReaderFactory.createXMLReader();
            }

            reader.setContentHandler(handler);
            reader.parse(isource);
         }
      } catch (StopParseException var18) {
      } catch (SAXException var19) {
         throw new TransformerConfigurationException("getAssociatedStylesheets failed", var19);
      } catch (IOException var20) {
         throw new TransformerConfigurationException("getAssociatedStylesheets failed", var20);
      }

      return handler.getAssociatedStylesheet();
   }

   public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
      return new StylesheetHandler(this);
   }

   public boolean getFeature(String name) {
      if ("http://javax.xml.transform.dom.DOMResult/feature" != name && "http://javax.xml.transform.dom.DOMSource/feature" != name && "http://javax.xml.transform.sax.SAXResult/feature" != name && "http://javax.xml.transform.sax.SAXSource/feature" != name && "http://javax.xml.transform.stream.StreamResult/feature" != name && "http://javax.xml.transform.stream.StreamSource/feature" != name && "http://javax.xml.transform.sax.SAXTransformerFactory/feature" != name && "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter" != name) {
         return "http://javax.xml.transform.dom.DOMResult/feature".equals(name) || "http://javax.xml.transform.dom.DOMSource/feature".equals(name) || "http://javax.xml.transform.sax.SAXResult/feature".equals(name) || "http://javax.xml.transform.sax.SAXSource/feature".equals(name) || "http://javax.xml.transform.stream.StreamResult/feature".equals(name) || "http://javax.xml.transform.stream.StreamSource/feature".equals(name) || "http://javax.xml.transform.sax.SAXTransformerFactory/feature".equals(name) || "http://javax.xml.transform.sax.SAXTransformerFactory/feature/xmlfilter".equals(name);
      } else {
         return true;
      }
   }

   public void setAttribute(String name, Object value) throws IllegalArgumentException {
      if (name.equals("http://xml.apache.org/xalan/features/incremental")) {
         if (value instanceof Boolean) {
            DTMManager.setIncremental((Boolean)value);
         } else {
            if (!(value instanceof String)) {
               throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{name, value}));
            }

            DTMManager.setIncremental(new Boolean((String)value));
         }
      } else if (name.equals("http://xml.apache.org/xalan/features/optimize")) {
         if (value instanceof Boolean) {
            m_optimize = (Boolean)value;
         } else {
            if (!(value instanceof String)) {
               throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{name, value}));
            }

            m_optimize = new Boolean((String)value);
         }
      } else {
         if (!name.equals("http://xml.apache.org/xalan/properties/source-location")) {
            throw new IllegalArgumentException(XSLMessages.createMessage("ER_NOT_SUPPORTED", new Object[]{name}));
         }

         if (value instanceof Boolean) {
            m_source_location = (Boolean)value;
            SAX2DTM.setUseSourceLocation(m_source_location);
         } else {
            if (!(value instanceof String)) {
               throw new IllegalArgumentException(XSLMessages.createMessage("ER_BAD_VALUE", new Object[]{name, value}));
            }

            m_source_location = new Boolean((String)value);
            SAX2DTM.setUseSourceLocation(m_source_location);
         }
      }

   }

   public Object getAttribute(String name) throws IllegalArgumentException {
      if (name.equals("http://xml.apache.org/xalan/features/incremental")) {
         return new Boolean(DTMManager.getIncremental());
      } else if (name.equals("http://xml.apache.org/xalan/features/optimize")) {
         return new Boolean(m_optimize);
      } else if (name.equals("http://xml.apache.org/xalan/properties/source-location")) {
         return new Boolean(m_source_location);
      } else {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_ATTRIB_VALUE_NOT_RECOGNIZED", new Object[]{name}));
      }
   }

   public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
      Templates templates = this.newTemplates(src);
      return templates == null ? null : this.newXMLFilter(templates);
   }

   public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
      try {
         return new TrAXFilter(templates);
      } catch (TransformerConfigurationException var5) {
         TransformerConfigurationException ex = var5;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(ex);
               return null;
            } catch (TransformerException var4) {
               new TransformerConfigurationException(var4);
            }
         }

         throw var5;
      }
   }

   public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException {
      Templates templates = this.newTemplates(src);
      return templates == null ? null : this.newTransformerHandler(templates);
   }

   public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
      try {
         TransformerImpl transformer = (TransformerImpl)templates.newTransformer();
         transformer.setURIResolver(this.m_uriResolver);
         TransformerHandler th = (TransformerHandler)transformer.getInputContentHandler(true);
         return th;
      } catch (TransformerConfigurationException var5) {
         TransformerConfigurationException ex = var5;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(ex);
               return null;
            } catch (TransformerException var4) {
               ex = new TransformerConfigurationException(var4);
            }
         }

         throw ex;
      }
   }

   public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
      return new TransformerIdentityImpl();
   }

   public Transformer newTransformer(Source source) throws TransformerConfigurationException {
      try {
         Templates tmpl = this.newTemplates(source);
         if (tmpl == null) {
            return null;
         } else {
            Transformer transformer = tmpl.newTransformer();
            transformer.setURIResolver(this.m_uriResolver);
            return transformer;
         }
      } catch (TransformerConfigurationException var5) {
         TransformerConfigurationException ex = var5;
         if (this.m_errorListener != null) {
            try {
               this.m_errorListener.fatalError(ex);
               return null;
            } catch (TransformerException var4) {
               ex = new TransformerConfigurationException(var4);
            }
         }

         throw ex;
      }
   }

   public Transformer newTransformer() throws TransformerConfigurationException {
      return new TransformerIdentityImpl();
   }

   public Templates newTemplates(Source source) throws TransformerConfigurationException {
      String baseID = source.getSystemId();
      if (null != baseID) {
         baseID = SystemIDResolver.getAbsoluteURI(baseID);
      }

      if (source instanceof DOMSource) {
         DOMSource dsource = (DOMSource)source;
         Node node = dsource.getNode();
         if (null != node) {
            return this.processFromNode(node, baseID);
         } else {
            String messageStr = XSLMessages.createMessage("ER_ILLEGAL_DOMSOURCE_INPUT", (Object[])null);
            throw new IllegalArgumentException(messageStr);
         }
      } else {
         TemplatesHandler builder = this.newTemplatesHandler();
         builder.setSystemId(baseID);

         try {
            InputSource isource = SAXSource.sourceToInputSource(source);
            isource.setSystemId(baseID);
            XMLReader reader = null;
            if (source instanceof SAXSource) {
               reader = ((SAXSource)source).getXMLReader();
            }

            if (null == reader) {
               try {
                  SAXParserFactory factory = SAXParserFactory.newInstance();
                  factory.setNamespaceAware(true);
                  SAXParser jaxpParser = factory.newSAXParser();
                  reader = jaxpParser.getXMLReader();
               } catch (ParserConfigurationException var12) {
                  throw new SAXException(var12);
               } catch (FactoryConfigurationError var13) {
                  throw new SAXException(var13.toString());
               } catch (NoSuchMethodError var14) {
               } catch (AbstractMethodError var15) {
               }
            }

            if (null == reader) {
               reader = XMLReaderFactory.createXMLReader();
            }

            reader.setContentHandler(builder);
            reader.parse(isource);
         } catch (SAXException var16) {
            SAXException se = var16;
            if (this.m_errorListener == null) {
               throw new TransformerConfigurationException(var16.getMessage(), var16);
            }

            try {
               this.m_errorListener.fatalError(new TransformerException(se));
            } catch (TransformerException var11) {
               throw new TransformerConfigurationException(var11);
            }
         } catch (Exception var17) {
            Exception e = var17;
            if (this.m_errorListener != null) {
               try {
                  this.m_errorListener.fatalError(new TransformerException(e));
                  return null;
               } catch (TransformerException var10) {
                  throw new TransformerConfigurationException(var10);
               }
            }

            throw new TransformerConfigurationException(var17.getMessage(), var17);
         }

         return builder.getTemplates();
      }
   }

   public void setURIResolver(URIResolver resolver) {
      this.m_uriResolver = resolver;
   }

   public URIResolver getURIResolver() {
      return this.m_uriResolver;
   }

   public ErrorListener getErrorListener() {
      return this.m_errorListener;
   }

   public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
      if (null == listener) {
         throw new IllegalArgumentException(XSLMessages.createMessage("ER_ERRORLISTENER", (Object[])null));
      } else {
         this.m_errorListener = listener;
      }
   }
}
