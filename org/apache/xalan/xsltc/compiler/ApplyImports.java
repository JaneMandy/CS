package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
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
import org.apache.xalan.xsltc.compiler.util.Util;

final class ApplyImports extends Instruction {
   private QName _modeName;
   private String _functionName;
   private int _precedence;

   public void display(int indent) {
      this.indent(indent);
      Util.println("ApplyTemplates");
      this.indent(indent + 4);
      if (this._modeName != null) {
         this.indent(indent + 4);
         Util.println("mode " + this._modeName);
      }

   }

   public boolean hasWithParams() {
      return this.hasContents();
   }

   private int getMinPrecedence(int max) {
      Stylesheet stylesheet = this.getStylesheet();
      Stylesheet root = this.getParser().getTopLevelStylesheet();
      int min = max;
      Enumeration templates = root.getContents().elements();

      while(true) {
         SyntaxTreeNode child;
         do {
            if (!templates.hasMoreElements()) {
               return min;
            }

            child = (SyntaxTreeNode)templates.nextElement();
         } while(!(child instanceof Template));

         Stylesheet curr = child.getStylesheet();

         while(curr != null && curr != stylesheet) {
            if (curr._importedFrom != null) {
               curr = curr._importedFrom;
            } else if (curr._includedFrom != null) {
               curr = curr._includedFrom;
            } else {
               curr = null;
            }
         }

         if (curr == stylesheet) {
            int prec = child.getStylesheet().getImportPrecedence();
            if (prec < min) {
               min = prec;
            }
         }
      }
   }

   public void parseContents(Parser parser) {
      Stylesheet stylesheet = this.getStylesheet();
      stylesheet.setTemplateInlining(false);
      Template template = this.getTemplate();
      this._modeName = template.getModeName();
      this._precedence = template.getImportPrecedence();
      stylesheet = parser.getTopLevelStylesheet();
      int maxPrecedence = this._precedence;
      int minPrecedence = this.getMinPrecedence(maxPrecedence);
      Mode mode = stylesheet.getMode(this._modeName);
      this._functionName = mode.functionName(minPrecedence, maxPrecedence);
      this.parseChildren(parser);
   }

   public Type typeCheck(SymbolTable stable) throws TypeCheckError {
      this.typeCheckContents(stable);
      return Type.Void;
   }

   public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
      Stylesheet stylesheet = classGen.getStylesheet();
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int current = methodGen.getLocalIndex("current");
      il.append(classGen.loadTranslet());
      il.append(methodGen.loadDOM());
      int init = cpg.addMethodref("org.apache.xalan.xsltc.dom.SingletonIterator", "<init>", "(I)V");
      il.append((org.apache.bcel.generic.Instruction)(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.SingletonIterator"))));
      il.append((org.apache.bcel.generic.Instruction)InstructionConstants.DUP);
      il.append(methodGen.loadCurrentNode());
      il.append((org.apache.bcel.generic.Instruction)(new INVOKESPECIAL(init)));
      il.append(methodGen.loadHandler());
      String className = classGen.getStylesheet().getClassName();
      String signature = classGen.getApplyTemplatesSig();
      int applyTemplates = cpg.addMethodref(className, this._functionName, signature);
      il.append((org.apache.bcel.generic.Instruction)(new INVOKEVIRTUAL(applyTemplates)));
   }
}
