package org.apache.xalan.processor;

import java.io.IOException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.DOM2Helper;
import org.apache.xml.utils.SystemIDResolver;
import org.apache.xml.utils.TreeWalker;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

class ProcessorInclude extends XSLTElementProcessor {
   private String m_href = null;

   public String getHref() {
      return this.m_href;
   }

   public void setHref(String baseIdent) {
      this.m_href = baseIdent;
   }

   protected int getStylesheetType() {
      return 2;
   }

   protected String getStylesheetInclErr() {
      return "ER_STYLESHEET_INCLUDES_ITSELF";
   }

   public void startElement(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      this.setPropertiesFromAttributes(handler, rawName, attributes, this);

      try {
         String hrefUrl = SystemIDResolver.getAbsoluteURI(this.getHref(), handler.getBaseIdentifier());
         if (handler.importStackContains(hrefUrl)) {
            throw new SAXException(XSLMessages.createMessage(this.getStylesheetInclErr(), new Object[]{hrefUrl}));
         }

         handler.pushImportURL(hrefUrl);
         int savedStylesheetType = handler.getStylesheetType();
         handler.setStylesheetType(this.getStylesheetType());
         handler.pushNewNamespaceSupport();

         try {
            this.parse(handler, uri, localName, rawName, attributes);
         } finally {
            handler.setStylesheetType(savedStylesheetType);
            handler.popImportURL();
            handler.popNamespaceSupport();
         }
      } catch (TransformerException var13) {
         handler.error(var13.getMessage(), var13);
      }

   }

   protected void parse(StylesheetHandler handler, String uri, String localName, String rawName, Attributes attributes) throws SAXException {
      TransformerFactoryImpl processor = handler.getStylesheetProcessor();
      URIResolver uriresolver = processor.getURIResolver();

      try {
         Source source = null;
         if (null != uriresolver) {
            source = uriresolver.resolve(this.getHref(), handler.getBaseIdentifier());
            if (null != source && source instanceof DOMSource) {
               Node node = ((DOMSource)source).getNode();
               String systemId = ((Source)source).getSystemId();
               if (systemId == null) {
                  systemId = SystemIDResolver.getAbsoluteURI(this.getHref(), handler.getBaseIdentifier());
               }

               TreeWalker walker = new TreeWalker(handler, new DOM2Helper(), systemId);

               try {
                  walker.traverse(node);
                  return;
               } catch (SAXException var25) {
                  throw new TransformerException(var25);
               }
            }
         }

         if (null == source) {
            String absURL = SystemIDResolver.getAbsoluteURI(this.getHref(), handler.getBaseIdentifier());
            source = new StreamSource(absURL);
         }

         XMLReader reader = null;
         if (source instanceof SAXSource) {
            SAXSource saxSource = (SAXSource)source;
            reader = saxSource.getXMLReader();
         }

         InputSource inputSource = SAXSource.sourceToInputSource((Source)source);
         if (null == reader) {
            try {
               SAXParserFactory factory = SAXParserFactory.newInstance();
               factory.setNamespaceAware(true);
               SAXParser jaxpParser = factory.newSAXParser();
               reader = jaxpParser.getXMLReader();
            } catch (ParserConfigurationException var27) {
               throw new SAXException(var27);
            } catch (FactoryConfigurationError var28) {
               throw new SAXException(var28.toString());
            } catch (NoSuchMethodError var29) {
            } catch (AbstractMethodError var30) {
            }
         }

         if (null == reader) {
            reader = XMLReaderFactory.createXMLReader();
         }

         if (null != reader) {
            reader.setContentHandler(handler);
            handler.pushBaseIndentifier(inputSource.getSystemId());

            try {
               reader.parse(inputSource);
            } finally {
               handler.popBaseIndentifier();
            }
         }
      } catch (IOException var31) {
         handler.error("ER_IOEXCEPTION", new Object[]{this.getHref()}, var31);
      } catch (TransformerException var32) {
         handler.error(var32.getMessage(), var32);
      }

   }
}
