package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

final class AttributeValueTemplate extends AttributeValue {
   public AttributeValueTemplate(String value, Parser parser, SyntaxTreeNode parent) {
      this.setParent(parent);
      this.setParser(parser);
      if (this.check(value, parser)) {
         this.parseAVTemplate(0, value, parser);
      }

   }

   private void parseAVTemplate(int start, String text, Parser parser) {
      if (text != null) {
         int open = start - 2;

         do {
            open = text.indexOf(123, open + 2);
         } while(open != -1 && open < text.length() - 1 && text.charAt(open + 1) == '{');

         String str;
         if (open != -1) {
            int close = open - 2;

            do {
               close = text.indexOf(125, close + 2);
            } while(close != -1 && close < text.length() - 1 && text.charAt(close + 1) == '}');

            if (open > start) {
               str = this.removeDuplicateBraces(text.substring(start, open));
               this.addElement(new LiteralExpr(str));
            }

            if (close > open + 1) {
               text.substring(open + 1, close);
               str = this.removeDuplicateBraces(text.substring(open + 1, close));
               this.addElement(parser.parseExpression(this, str));
            }

            this.parseAVTemplate(close + 1, text, parser);
         } else if (start < text.length()) {
            str = this.removeDuplicateBraces(text.substring(start));
            this.addElement(new LiteralExpr(str));
         }

      }
   }

   public String removeDuplicateBraces(String orig) {
      String result;
      int index;
      for(result = orig; (index = result.indexOf("{{")) != -1; result = result.substring(0, index) + result.substring(index + 1, result.length())) {
      }

      while((index = result.indexOf("}}")) != -1) {
         result = result.substring(0, index) + result.substring(index + 1, result.length());
      }

      return result;
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      Vector contents = this.getContents();
      int n = contents.size();

      for(int i = 0; i < n; ++i) {
         Expression exp = (Expression)contents.elementAt(i);
         if (!exp.typeCheck(stable).identicalTo(Type.String)) {
            contents.setElementAt(new CastExpr(exp, Type.String), i);
         }
      }

      return super._type = Type.String;
   }

   public String toString() {
      StringBuffer buffer = new StringBuffer("AVT:[");
      int count = this.elementCount();

      for(int i = 0; i < count; ++i) {
         buffer.append(this.elementAt(i).toString());
         if (i < count - 1) {
            buffer.append(' ');
         }
      }

      return buffer.append(']').toString();
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      if (this.elementCount() == 1) {
         Expression exp = (Expression)this.elementAt(0);
         exp.translate(classGen, methodGen);
      } else {
         ConstantPoolGen cpg = classGen.getConstantPool();
         InstructionList il = methodGen.getInstructionList();
         int initBuffer = cpg.addMethodref("java.lang.StringBuffer", "<init>", "()V");
         org.apache.bcel.generic.Instruction append = new INVOKEVIRTUAL(cpg.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
         int toString = cpg.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
         il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("java.lang.StringBuffer"))));
         il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
         il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(initBuffer)));
         Enumeration elements = this.elements();

         while(elements.hasMoreElements()) {
            Expression exp = (Expression)elements.nextElement();
            exp.translate(classGen, methodGen);
            il.append((org.apache.bcel.generic.Instruction)append);
         }

         il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(toString)));
      }

   }

   private boolean check(String value, Parser parser) {
      if (value == null) {
         return true;
      } else {
         char[] chars = value.toCharArray();
         int level = 0;

         for(int i = 0; i < chars.length; ++i) {
            switch(chars[i]) {
            case '{':
               if (i + 1 != chars.length && chars[i + 1] == '{') {
                  ++i;
               } else {
                  ++level;
               }
               break;
            case '}':
               if (i + 1 != chars.length && chars[i + 1] == '}') {
                  ++i;
               } else {
                  --level;
               }
               break;
            default:
               continue;
            }

            switch(level) {
            case 0:
            case 1:
               break;
            default:
               this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", value);
               return false;
            }
         }

         if (level != 0) {
            this.reportError(this.getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", value);
            return false;
         } else {
            return true;
         }
      }
   }
}
