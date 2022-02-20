package org.apache.xml.serializer;

import java.io.IOException;
import java.io.Writer;
import org.apache.xml.res.XMLMessages;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ToTextStream extends ToStream {
   protected void startDocumentInternal() throws SAXException {
      super.startDocumentInternal();
      super.m_needToCallStartDocument = false;
   }

   public void endDocument() throws SAXException {
      this.flushPending();
      this.flushWriter();
      if (super.m_tracer != null) {
         super.fireEndDoc();
      }

   }

   public void startElement(String namespaceURI, String localName, String name, Attributes atts) throws SAXException {
      if (super.m_tracer != null) {
         super.fireStartElem(name);
         this.firePseudoAttributes();
      }

   }

   public void endElement(String namespaceURI, String localName, String name) throws SAXException {
      if (super.m_tracer != null) {
         super.fireEndElem(name);
      }

   }

   public void characters(char[] ch, int start, int length) throws SAXException {
      this.flushPending();

      try {
         this.writeNormalizedChars(ch, start, length, false, super.m_lineSepUse);
         if (super.m_tracer != null) {
            super.fireCharEvent(ch, start, length);
         }

      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void charactersRaw(char[] ch, int start, int length) throws SAXException {
      try {
         this.writeNormalizedChars(ch, start, length, false, super.m_lineSepUse);
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   void writeNormalizedChars(char[] ch, int start, int length, boolean isCData, boolean useLineSep) throws IOException, SAXException {
      Writer writer = super.m_writer;
      int end = start + length;
      char S_LINEFEED = true;
      int M_MAXCHARACTER = super.m_maxCharacter;
      int i;
      char c;
      String encoding;
      String integralValue;
      if (isCData) {
         for(i = start; i < end; ++i) {
            c = ch[i];
            if ('\n' == c && useLineSep) {
               writer.write(super.m_lineSep, 0, super.m_lineSepLen);
            } else if (c > M_MAXCHARACTER) {
               if (i != 0) {
                  this.closeCDATA();
               }

               if (ToStream.isUTF16Surrogate(c)) {
                  this.writeUTF16Surrogate(c, ch, i, end);
                  ++i;
               } else {
                  writer.write(c);
               }

               if (i != 0 && i < end - 1) {
                  writer.write("<![CDATA[");
                  super.m_cdataTagOpen = true;
               }
            } else if (i < end - 2 && ']' == c && ']' == ch[i + 1] && '>' == ch[i + 2]) {
               writer.write("]]]]><![CDATA[>");
               i += 2;
            } else if (c <= M_MAXCHARACTER) {
               writer.write(c);
            } else if (ToStream.isUTF16Surrogate(c)) {
               this.writeUTF16Surrogate(c, ch, i, end);
               ++i;
            } else {
               encoding = this.getEncoding();
               if (encoding != null) {
                  integralValue = Integer.toString(c);
                  throw new SAXException(XMLMessages.createXMLMessage("ER_ILLEGAL_CHARACTER", new Object[]{integralValue, encoding}));
               }

               writer.write(c);
            }
         }
      } else {
         for(i = start; i < end; ++i) {
            c = ch[i];
            if ('\n' == c && useLineSep) {
               writer.write(super.m_lineSep, 0, super.m_lineSepLen);
            } else if (c <= M_MAXCHARACTER) {
               writer.write(c);
            } else if (ToStream.isUTF16Surrogate(c)) {
               this.writeUTF16Surrogate(c, ch, i, end);
               ++i;
            } else {
               encoding = this.getEncoding();
               if (encoding != null) {
                  integralValue = Integer.toString(c);
                  throw new SAXException(XMLMessages.createXMLMessage("ER_ILLEGAL_CHARACTER", new Object[]{integralValue, encoding}));
               }

               writer.write(c);
            }
         }
      }

   }

   public void cdata(char[] ch, int start, int length) throws SAXException {
      try {
         this.writeNormalizedChars(ch, start, length, false, super.m_lineSepUse);
         if (super.m_tracer != null) {
            super.fireCDATAEvent(ch, start, length);
         }

      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      try {
         this.writeNormalizedChars(ch, start, length, false, super.m_lineSepUse);
      } catch (IOException var5) {
         throw new SAXException(var5);
      }
   }

   public void processingInstruction(String target, String data) throws SAXException {
      this.flushPending();
      if (super.m_tracer != null) {
         super.fireEscapingEvent(target, data);
      }

   }

   public void comment(String data) throws SAXException {
      int length = data.length();
      if (length > super.m_charsBuff.length) {
         super.m_charsBuff = new char[length * 2 + 1];
      }

      data.getChars(0, length, super.m_charsBuff, 0);
      this.comment(super.m_charsBuff, 0, length);
   }

   public void comment(char[] ch, int start, int length) throws SAXException {
      this.flushPending();
      if (super.m_tracer != null) {
         super.fireCommentEvent(ch, start, length);
      }

   }

   public void entityReference(String name) throws SAXException {
      if (super.m_tracer != null) {
         super.fireEntityReference(name);
      }

   }

   public void addAttribute(String uri, String localName, String rawName, String type, String value) {
   }

   public void endCDATA() throws SAXException {
   }

   public void endElement(String elemName) throws SAXException {
      if (super.m_tracer != null) {
         super.fireEndElem(elemName);
      }

   }

   public void startElement(String elementNamespaceURI, String elementLocalName, String elementName) throws SAXException {
      if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
      }

      if (super.m_tracer != null) {
         super.fireStartElem(elementName);
         this.firePseudoAttributes();
      }

   }

   public void characters(String characters) throws SAXException {
      int length = characters.length();
      if (length > super.m_charsBuff.length) {
         super.m_charsBuff = new char[length * 2 + 1];
      }

      characters.getChars(0, length, super.m_charsBuff, 0);
      this.characters(super.m_charsBuff, 0, length);
   }

   public void addAttribute(String name, String value) {
   }

   public void addUniqueAttribute(String qName, String value, int flags) throws SAXException {
   }

   public boolean startPrefixMapping(String prefix, String uri, boolean shouldFlush) throws SAXException {
      return false;
   }

   public void startPrefixMapping(String prefix, String uri) throws SAXException {
   }

   public void namespaceAfterStartElement(String prefix, String uri) throws SAXException {
   }

   public void flushPending() throws SAXException {
      if (super.m_needToCallStartDocument) {
         this.startDocumentInternal();
         super.m_needToCallStartDocument = false;
      }

   }
}
