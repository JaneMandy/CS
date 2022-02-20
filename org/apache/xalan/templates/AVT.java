package org.apache.xalan.templates;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.processor.StylesheetHandler;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.StringBufferPool;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.xml.sax.SAXException;

public class AVT implements Serializable, XSLTVisitable {
   private String m_simpleString = null;
   private Vector m_parts = null;
   private String m_rawName;
   private String m_name;
   private String m_uri;

   public String getRawName() {
      return this.m_rawName;
   }

   public void setRawName(String rawName) {
      this.m_rawName = rawName;
   }

   public String getName() {
      return this.m_name;
   }

   public void setName(String name) {
      this.m_name = name;
   }

   public String getURI() {
      return this.m_uri;
   }

   public void setURI(String uri) {
      this.m_uri = uri;
   }

   public AVT(StylesheetHandler handler, String uri, String name, String rawName, String stringedValue, ElemTemplateElement owner) throws TransformerException {
      this.m_uri = uri;
      this.m_name = name;
      this.m_rawName = rawName;
      StringTokenizer tokenizer = new StringTokenizer(stringedValue, "{}\"'", true);
      int nTokens = tokenizer.countTokens();
      if (nTokens < 2) {
         this.m_simpleString = stringedValue;
      } else {
         FastStringBuffer buffer = StringBufferPool.get();
         FastStringBuffer exprBuffer = StringBufferPool.get();

         try {
            this.m_parts = new Vector(nTokens + 1);
            String t = null;
            String lookahead = null;
            String error = null;

            while(tokenizer.hasMoreTokens()) {
               if (lookahead != null) {
                  t = lookahead;
                  lookahead = null;
               } else {
                  t = tokenizer.nextToken();
               }

               if (t.length() != 1) {
                  buffer.append(t);
               } else {
                  label219:
                  switch(t.charAt(0)) {
                  case '"':
                  case '\'':
                     buffer.append(t);
                     break;
                  case '{':
                     try {
                        lookahead = tokenizer.nextToken();
                        if (lookahead.equals("{")) {
                           buffer.append(lookahead);
                           lookahead = null;
                        } else {
                           if (buffer.length() > 0) {
                              this.m_parts.addElement(new AVTPartSimple(buffer.toString()));
                              buffer.setLength(0);
                           }

                           exprBuffer.setLength(0);

                           while(true) {
                              while(true) {
                                 while(null != lookahead) {
                                    if (lookahead.length() == 1) {
                                       switch(lookahead.charAt(0)) {
                                       case '"':
                                       case '\'':
                                          exprBuffer.append(lookahead);
                                          String quote = lookahead;

                                          for(lookahead = tokenizer.nextToken(); !lookahead.equals(quote); lookahead = tokenizer.nextToken()) {
                                             exprBuffer.append(lookahead);
                                          }

                                          exprBuffer.append(lookahead);
                                          lookahead = tokenizer.nextToken();
                                          break;
                                       case '{':
                                          error = XSLMessages.createMessage("ER_NO_CURLYBRACE", (Object[])null);
                                          lookahead = null;
                                          break;
                                       case '}':
                                          buffer.setLength(0);
                                          XPath xpath = handler.createXPath(exprBuffer.toString(), owner);
                                          this.m_parts.addElement(new AVTPartXPath(xpath));
                                          lookahead = null;
                                          break;
                                       default:
                                          exprBuffer.append(lookahead);
                                          lookahead = tokenizer.nextToken();
                                       }
                                    } else {
                                       exprBuffer.append(lookahead);
                                       lookahead = tokenizer.nextToken();
                                    }
                                 }

                                 if (error != null) {
                                 }
                                 break label219;
                              }
                           }
                        }
                     } catch (NoSuchElementException var23) {
                        error = XSLMessages.createMessage("ER_ILLEGAL_ATTRIBUTE_VALUE", new Object[]{name, stringedValue});
                     }
                     break;
                  case '}':
                     lookahead = tokenizer.nextToken();
                     if (lookahead.equals("}")) {
                        buffer.append(lookahead);
                        lookahead = null;
                     } else {
                        try {
                           handler.warn("WG_FOUND_CURLYBRACE", (Object[])null);
                        } catch (SAXException var22) {
                           throw new TransformerException(var22);
                        }

                        buffer.append("}");
                     }
                     break;
                  default:
                     buffer.append(t);
                  }
               }

               if (null != error) {
                  try {
                     handler.warn("WG_ATTR_TEMPLATE", new Object[]{error});
                     break;
                  } catch (SAXException var21) {
                     throw new TransformerException(var21);
                  }
               }
            }

            if (buffer.length() > 0) {
               this.m_parts.addElement(new AVTPartSimple(buffer.toString()));
               buffer.setLength(0);
            }
         } finally {
            StringBufferPool.free(buffer);
            StringBufferPool.free(exprBuffer);
         }
      }

      if (null == this.m_parts && null == this.m_simpleString) {
         this.m_simpleString = "";
      }

   }

   public String getSimpleString() {
      if (null != this.m_simpleString) {
         return this.m_simpleString;
      } else if (null != this.m_parts) {
         FastStringBuffer buf = StringBufferPool.get();

         try {
            buf.setLength(0);
            int n = this.m_parts.size();

            for(int i = 0; i < n; ++i) {
               AVTPart part = (AVTPart)this.m_parts.elementAt(i);
               buf.append(part.getSimpleString());
            }

            String s = buf.toString();
            return s;
         } finally {
            StringBufferPool.free(buf);
         }
      } else {
         return "";
      }
   }

   public String evaluate(XPathContext xctxt, int context, PrefixResolver nsNode) throws TransformerException {
      FastStringBuffer buf = StringBufferPool.get();

      String var5;
      try {
         if (null == this.m_simpleString) {
            if (null != this.m_parts) {
               buf.setLength(0);
               int n = this.m_parts.size();

               for(int i = 0; i < n; ++i) {
                  AVTPart part = (AVTPart)this.m_parts.elementAt(i);
                  part.evaluate(xctxt, buf, context, nsNode);
               }

               String var13 = buf.toString();
               return var13;
            }

            var5 = "";
            return var5;
         }

         var5 = this.m_simpleString;
      } finally {
         StringBufferPool.free(buf);
      }

      return var5;
   }

   public boolean isContextInsensitive() {
      return null != this.m_simpleString;
   }

   public boolean canTraverseOutsideSubtree() {
      if (null != this.m_parts) {
         int n = this.m_parts.size();

         for(int i = 0; i < n; ++i) {
            AVTPart part = (AVTPart)this.m_parts.elementAt(i);
            if (part.canTraverseOutsideSubtree()) {
               return true;
            }
         }
      }

      return false;
   }

   public void fixupVariables(Vector vars, int globalsSize) {
      if (null != this.m_parts) {
         int n = this.m_parts.size();

         for(int i = 0; i < n; ++i) {
            AVTPart part = (AVTPart)this.m_parts.elementAt(i);
            part.fixupVariables(vars, globalsSize);
         }
      }

   }

   public void callVisitors(XSLTVisitor visitor) {
      if (visitor.visitAVT(this) && null != this.m_parts) {
         int n = this.m_parts.size();

         for(int i = 0; i < n; ++i) {
            AVTPart part = (AVTPart)this.m_parts.elementAt(i);
            part.callVisitors(visitor);
         }
      }

   }

   public boolean isSimple() {
      return this.m_simpleString != null;
   }
}
