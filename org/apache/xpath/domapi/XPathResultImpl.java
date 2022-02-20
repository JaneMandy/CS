package org.apache.xpath.domapi;

import javax.xml.transform.TransformerException;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathResult;

public class XPathResultImpl implements XPathResult, EventListener {
   private XObject m_resultObj;
   private short m_resultType = 0;
   private boolean m_isInvalidIteratorState = false;
   private Node m_contextNode;
   private NodeIterator m_iterator = null;
   private NodeList m_list = null;

   XPathResultImpl(short type, XObject result, Node contextNode) {
      String fmsg;
      if (!isValidType(type)) {
         fmsg = XPATHMessages.createXPATHMessage("ER_INVALID_XPATH_TYPE", new Object[]{new Integer(type)});
         throw new XPathException((short)2, fmsg);
      } else if (null == result) {
         fmsg = XPATHMessages.createXPATHMessage("ER_EMPTY_XPATH_RESULT", (Object[])null);
         throw new XPathException((short)1, fmsg);
      } else {
         this.m_resultObj = result;
         this.m_contextNode = contextNode;
         if (type == 0) {
            this.m_resultType = this.getTypeFromXObject(result);
         } else {
            this.m_resultType = type;
         }

         if ((this.m_resultType == 5 || this.m_resultType == 4) && contextNode instanceof EventTarget) {
            ((EventTarget)contextNode).addEventListener("MutationEvents", this, true);
         }

         String fmsg;
         if (this.m_resultType != 5 && this.m_resultType != 4 && this.m_resultType != 8 && this.m_resultType != 9) {
            if (this.m_resultType == 6 || this.m_resultType == 7) {
               try {
                  this.m_list = this.m_resultObj.nodelist();
               } catch (TransformerException var6) {
                  fmsg = XPATHMessages.createXPATHMessage("ER_INCOMPATIBLE_TYPES", new Object[]{this.getTypeString(this.getTypeFromXObject(this.m_resultObj)), this.getTypeString(this.m_resultType)});
                  throw new XPathException((short)2, fmsg);
               }
            }
         } else {
            try {
               this.m_iterator = this.m_resultObj.nodeset();
            } catch (TransformerException var7) {
               fmsg = XPATHMessages.createXPATHMessage("ER_INCOMPATIBLE_TYPES", new Object[]{this.getTypeString(this.getTypeFromXObject(this.m_resultObj)), this.getTypeString(this.m_resultType)});
               throw new XPathException((short)2, fmsg);
            }
         }

      }
   }

   public short getResultType() {
      return this.m_resultType;
   }

   public double getNumberValue() throws XPathException {
      if (this.getResultType() != 1) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_NUMBER", new Object[]{this.getTypeString(this.m_resultType)});
         throw new XPathException((short)2, fmsg);
      } else {
         try {
            return this.m_resultObj.num();
         } catch (Exception var2) {
            throw new XPathException((short)2, var2.getMessage());
         }
      }
   }

   public String getStringValue() throws XPathException {
      if (this.getResultType() != 2) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_STRING", new Object[]{this.m_resultObj.getTypeString()});
         throw new XPathException((short)2, fmsg);
      } else {
         try {
            return this.m_resultObj.str();
         } catch (Exception var2) {
            throw new XPathException((short)2, var2.getMessage());
         }
      }
   }

   public boolean getBooleanValue() throws XPathException {
      if (this.getResultType() != 3) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_BOOLEAN", new Object[]{this.getTypeString(this.m_resultType)});
         throw new XPathException((short)2, fmsg);
      } else {
         try {
            return this.m_resultObj.bool();
         } catch (TransformerException var2) {
            throw new XPathException((short)2, var2.getMessage());
         }
      }
   }

   public Node getSingleNodeValue() throws XPathException {
      if (this.m_resultType != 8 && this.m_resultType != 9) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_CONVERT_TO_SINGLENODE", new Object[]{this.getTypeString(this.m_resultType)});
         throw new XPathException((short)2, fmsg);
      } else {
         NodeIterator result = null;

         try {
            result = this.m_resultObj.nodeset();
         } catch (TransformerException var3) {
            throw new XPathException((short)2, var3.getMessage());
         }

         if (null == result) {
            return null;
         } else {
            Node node = result.nextNode();
            return (Node)(this.isNamespaceNode(node) ? new XPathNamespaceImpl(node) : node);
         }
      }
   }

   public boolean getInvalidIteratorState() {
      return this.m_isInvalidIteratorState;
   }

   public int getSnapshotLength() throws XPathException {
      if (this.m_resultType != 6 && this.m_resultType != 7) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_CANT_GET_SNAPSHOT_LENGTH", new Object[]{this.getTypeString(this.m_resultType)});
         throw new XPathException((short)2, fmsg);
      } else {
         return this.m_list.getLength();
      }
   }

   public Node iterateNext() throws XPathException, DOMException {
      String fmsg;
      if (this.m_resultType != 4 && this.m_resultType != 5) {
         fmsg = XPATHMessages.createXPATHMessage("ER_NON_ITERATOR_TYPE", new Object[]{this.getTypeString(this.m_resultType)});
         throw new XPathException((short)2, fmsg);
      } else if (this.getInvalidIteratorState()) {
         fmsg = XPATHMessages.createXPATHMessage("ER_DOC_MUTATED", (Object[])null);
         throw new DOMException((short)11, fmsg);
      } else {
         Node node = this.m_iterator.nextNode();
         return (Node)(this.isNamespaceNode(node) ? new XPathNamespaceImpl(node) : node);
      }
   }

   public Node snapshotItem(int index) throws XPathException {
      if (this.m_resultType != 6 && this.m_resultType != 7) {
         String fmsg = XPATHMessages.createXPATHMessage("ER_NON_SNAPSHOT_TYPE", new Object[]{this.getTypeString(this.m_resultType)});
         throw new XPathException((short)2, fmsg);
      } else {
         Node node = this.m_list.item(index);
         return (Node)(this.isNamespaceNode(node) ? new XPathNamespaceImpl(node) : node);
      }
   }

   public static boolean isValidType(short type) {
      switch(type) {
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
         return true;
      default:
         return false;
      }
   }

   public void handleEvent(Event event) {
      if (event.getType().equals("MutationEvents")) {
         this.m_isInvalidIteratorState = true;
         ((EventTarget)this.m_contextNode).removeEventListener("MutationEvents", this, true);
      }

   }

   public String getTypeString(int type) {
      switch(type) {
      case 0:
         return "ANY_TYPE";
      case 1:
         return "NUMBER_TYPE";
      case 2:
         return "STRING_TYPE";
      case 3:
         return "BOOLEAN";
      case 4:
         return "UNORDERED_NODE_ITERATOR_TYPE";
      case 5:
         return "ORDERED_NODE_ITERATOR_TYPE";
      case 6:
         return "UNORDERED_NODE_SNAPSHOT_TYPE";
      case 7:
         return "ORDERED_NODE_SNAPSHOT_TYPE";
      case 8:
         return "ANY_UNORDERED_NODE_TYPE";
      case 9:
         return "FIRST_ORDERED_NODE_TYPE";
      default:
         return "#UNKNOWN";
      }
   }

   private short getTypeFromXObject(XObject object) {
      switch(object.getType()) {
      case -1:
         return 0;
      case 0:
      default:
         return 0;
      case 1:
         return 3;
      case 2:
         return 1;
      case 3:
         return 2;
      case 4:
         return 4;
      case 5:
         return 4;
      }
   }

   private boolean isNamespaceNode(Node node) {
      return null != node && node.getNodeType() == 2 && (node.getNodeName().startsWith("xmlns:") || node.getNodeName().equals("xmlns"));
   }
}
