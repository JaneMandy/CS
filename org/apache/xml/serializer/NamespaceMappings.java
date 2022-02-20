package org.apache.xml.serializer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class NamespaceMappings {
   private int count = 0;
   private Stack m_prefixStack = new Stack();
   private Hashtable m_namespaces = new Hashtable();
   private Stack m_nodeStack = new Stack();
   private static final String EMPTYSTRING = "";
   private static final String XML_PREFIX = "xml";

   public NamespaceMappings() {
      this.initNamespaces();
   }

   private void initNamespaces() {
      Stack stack;
      this.m_namespaces.put("", stack = new Stack());
      stack.push("");
      this.m_prefixStack.push("");
      this.m_namespaces.put("xml", stack = new Stack());
      stack.push("http://www.w3.org/XML/1998/namespace");
      this.m_prefixStack.push("xml");
      this.m_nodeStack.push(new Integer(-1));
   }

   public String lookupNamespace(String prefix) {
      Stack stack = (Stack)this.m_namespaces.get(prefix);
      return stack != null && !stack.isEmpty() ? (String)stack.peek() : null;
   }

   public String lookupPrefix(String uri) {
      String foundPrefix = null;
      Enumeration prefixes = this.m_namespaces.keys();

      while(prefixes.hasMoreElements()) {
         String prefix = (String)prefixes.nextElement();
         String uri2 = this.lookupNamespace(prefix);
         if (uri2 != null && uri2.equals(uri)) {
            foundPrefix = prefix;
            break;
         }
      }

      return foundPrefix;
   }

   public boolean popNamespace(String prefix) {
      if (prefix.startsWith("xml")) {
         return false;
      } else {
         Stack stack;
         if ((stack = (Stack)this.m_namespaces.get(prefix)) != null) {
            stack.pop();
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean pushNamespace(String prefix, String uri, int elemDepth) {
      if (prefix.startsWith("xml")) {
         return false;
      } else {
         Stack stack;
         if ((stack = (Stack)this.m_namespaces.get(prefix)) == null) {
            this.m_namespaces.put(prefix, stack = new Stack());
         }

         if (!stack.empty() && uri.equals(stack.peek())) {
            return false;
         } else {
            stack.push(uri);
            this.m_prefixStack.push(prefix);
            this.m_nodeStack.push(new Integer(elemDepth));
            return true;
         }
      }
   }

   public void popNamespaces(int elemDepth, ContentHandler saxHandler) {
      while(!this.m_nodeStack.isEmpty()) {
         Integer i = (Integer)this.m_nodeStack.peek();
         if (i < elemDepth) {
            return;
         }

         this.m_nodeStack.pop();
         String prefix = (String)this.m_prefixStack.pop();
         this.popNamespace(prefix);
         if (saxHandler != null) {
            try {
               saxHandler.endPrefixMapping(prefix);
            } catch (SAXException var6) {
            }
         }
      }

   }

   public String generateNextPrefix() {
      return "ns" + this.count++;
   }

   public Object clone() throws CloneNotSupportedException {
      NamespaceMappings clone = new NamespaceMappings();
      clone.m_prefixStack = (Stack)this.m_prefixStack.clone();
      clone.m_nodeStack = (Stack)this.m_nodeStack.clone();
      clone.m_namespaces = (Hashtable)this.m_namespaces.clone();
      clone.count = this.count;
      return clone;
   }

   public final void reset() {
      this.count = 0;
      this.m_namespaces.clear();
      this.m_nodeStack.clear();
      this.m_prefixStack.clear();
      this.initNamespaces();
   }
}
