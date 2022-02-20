package org.apache.xalan.templates;

import java.util.Vector;
import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.QName;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.ExpressionOwner;
import org.apache.xpath.XPath;
import org.apache.xpath.axes.AxesWalker;
import org.apache.xpath.axes.FilterExprIteratorSimple;
import org.apache.xpath.axes.FilterExprWalker;
import org.apache.xpath.axes.LocPathIterator;
import org.apache.xpath.axes.SelfIteratorNoPredicate;
import org.apache.xpath.axes.WalkerFactory;
import org.apache.xpath.axes.WalkingIterator;
import org.apache.xpath.operations.Variable;
import org.apache.xpath.operations.VariableSafeAbsRef;
import org.w3c.dom.DOMException;

public class RedundentExprEliminator extends XSLTVisitor {
   Vector m_paths = null;
   Vector m_absPaths = new Vector();
   boolean m_isSameContext = true;
   AbsPathChecker m_absPathChecker = new AbsPathChecker();
   static int m_uniquePsuedoVarID = 1;
   static final String PSUEDOVARNAMESPACE = "http://xml.apache.org/xalan/psuedovar";
   public static boolean DEBUG = false;
   public static boolean DIAGNOSE_NUM_PATHS_REDUCED = false;
   public static boolean DIAGNOSE_MULTISTEPLIST = false;
   VarNameCollector m_varNameCollector = new VarNameCollector();

   public void eleminateRedundentLocals(ElemTemplateElement psuedoVarRecipient) {
      this.eleminateRedundent(psuedoVarRecipient, this.m_paths);
   }

   public void eleminateRedundentGlobals(StylesheetRoot stylesheet) {
      this.eleminateRedundent(stylesheet, this.m_absPaths);
   }

   protected void eleminateRedundent(ElemTemplateElement psuedoVarRecipient, Vector paths) {
      int n = paths.size();
      int numPathsEliminated = 0;
      int numUniquePathsEliminated = 0;

      for(int i = 0; i < n; ++i) {
         ExpressionOwner owner = (ExpressionOwner)paths.elementAt(i);
         if (null != owner) {
            int found = this.findAndEliminateRedundant(i + 1, i, owner, psuedoVarRecipient, paths);
            if (found > 0) {
               ++numUniquePathsEliminated;
            }

            numPathsEliminated += found;
         }
      }

      this.eleminateSharedPartialPaths(psuedoVarRecipient, paths);
      if (DIAGNOSE_NUM_PATHS_REDUCED) {
         this.diagnoseNumPaths(paths, numPathsEliminated, numUniquePathsEliminated);
      }

   }

   protected void eleminateSharedPartialPaths(ElemTemplateElement psuedoVarRecipient, Vector paths) {
      RedundentExprEliminator.MultistepExprHolder list = this.createMultistepExprList(paths);
      if (null != list) {
         if (DIAGNOSE_MULTISTEPLIST) {
            list.diagnose();
         }

         boolean isGlobal = paths == this.m_absPaths;
         int longestStepsCount = list.m_stepCount;

         for(int i = longestStepsCount - 1; i >= 1; --i) {
            for(RedundentExprEliminator.MultistepExprHolder next = list; null != next && next.m_stepCount >= i; next = next.m_next) {
               list = this.matchAndEliminatePartialPaths(next, list, isGlobal, i, psuedoVarRecipient);
            }
         }
      }

   }

   protected RedundentExprEliminator.MultistepExprHolder matchAndEliminatePartialPaths(RedundentExprEliminator.MultistepExprHolder testee, RedundentExprEliminator.MultistepExprHolder head, boolean isGlobal, int lengthToTest, ElemTemplateElement varScope) {
      if (null == testee.m_exprOwner) {
         return head;
      } else {
         WalkingIterator iter1 = (WalkingIterator)testee.m_exprOwner.getExpression();
         if (this.partialIsVariable(testee, lengthToTest)) {
            return head;
         } else {
            RedundentExprEliminator.MultistepExprHolder matchedPaths = null;
            RedundentExprEliminator.MultistepExprHolder matchedPathsTail = null;

            for(RedundentExprEliminator.MultistepExprHolder meh = head; null != meh; meh = meh.m_next) {
               if (meh != testee && null != meh.m_exprOwner) {
                  WalkingIterator iter2 = (WalkingIterator)meh.m_exprOwner.getExpression();
                  if (this.stepsEqual(iter1, iter2, lengthToTest)) {
                     if (null == matchedPaths) {
                        try {
                           matchedPaths = (RedundentExprEliminator.MultistepExprHolder)testee.clone();
                           testee.m_exprOwner = null;
                        } catch (CloneNotSupportedException var19) {
                        }

                        matchedPathsTail = matchedPaths;
                        matchedPaths.m_next = null;
                     }

                     try {
                        matchedPathsTail.m_next = (RedundentExprEliminator.MultistepExprHolder)meh.clone();
                        meh.m_exprOwner = null;
                     } catch (CloneNotSupportedException var18) {
                     }

                     matchedPathsTail = matchedPathsTail.m_next;
                     matchedPathsTail.m_next = null;
                  }
               }
            }

            int matchCount = 0;
            if (null != matchedPaths) {
               ElemTemplateElement root = isGlobal ? varScope : this.findCommonAncestor(matchedPaths);
               WalkingIterator sharedIter = (WalkingIterator)matchedPaths.m_exprOwner.getExpression();
               WalkingIterator newIter = this.createIteratorFromSteps(sharedIter, lengthToTest);
               ElemVariable var = this.createPsuedoVarDecl(root, newIter, isGlobal);
               if (DIAGNOSE_MULTISTEPLIST) {
                  System.err.println("Created var: " + var.getName() + (isGlobal ? "(Global)" : ""));
               }

               while(null != matchedPaths) {
                  ExpressionOwner owner = matchedPaths.m_exprOwner;
                  WalkingIterator iter = (WalkingIterator)owner.getExpression();
                  if (DIAGNOSE_MULTISTEPLIST) {
                     this.diagnoseLineNumber(iter);
                  }

                  LocPathIterator newIter2 = this.changePartToRef(var.getName(), iter, lengthToTest, isGlobal);
                  owner.setExpression(newIter2);
                  matchedPaths = matchedPaths.m_next;
               }
            }

            if (DIAGNOSE_MULTISTEPLIST) {
               this.diagnoseMultistepList(matchCount, lengthToTest, isGlobal);
            }

            return head;
         }
      }
   }

   boolean partialIsVariable(RedundentExprEliminator.MultistepExprHolder testee, int lengthToTest) {
      if (1 == lengthToTest) {
         WalkingIterator wi = (WalkingIterator)testee.m_exprOwner.getExpression();
         if (wi.getFirstWalker() instanceof FilterExprWalker) {
            return true;
         }
      }

      return false;
   }

   protected void diagnoseLineNumber(Expression expr) {
      ElemTemplateElement e = this.getElemFromExpression(expr);
      System.err.println("   " + e.getSystemId() + " Line " + e.getLineNumber());
   }

   protected ElemTemplateElement findCommonAncestor(RedundentExprEliminator.MultistepExprHolder head) {
      int numExprs = head.getLength();
      ElemTemplateElement[] elems = new ElemTemplateElement[numExprs];
      int[] ancestorCounts = new int[numExprs];
      RedundentExprEliminator.MultistepExprHolder next = head;
      int shortestAncestorCount = 10000;

      int numStepCorrection;
      for(int i = 0; i < numExprs; ++i) {
         ElemTemplateElement elem = this.getElemFromExpression(next.m_exprOwner.getExpression());
         elems[i] = elem;
         numStepCorrection = this.countAncestors(elem);
         ancestorCounts[i] = numStepCorrection;
         if (numStepCorrection < shortestAncestorCount) {
            shortestAncestorCount = numStepCorrection;
         }

         next = next.m_next;
      }

      for(int i = 0; i < numExprs; ++i) {
         if (ancestorCounts[i] > shortestAncestorCount) {
            numStepCorrection = ancestorCounts[i] - shortestAncestorCount;

            for(int j = 0; j < numStepCorrection; ++j) {
               elems[i] = elems[i].getParentElem();
            }
         }
      }

      ElemTemplateElement first = null;

      while(shortestAncestorCount-- >= 0) {
         boolean areEqual = true;
         first = elems[0];

         for(int i = 1; i < numExprs; ++i) {
            if (first != elems[i]) {
               areEqual = false;
               break;
            }
         }

         if (areEqual && this.isNotSameAsOwner(head, first) && first.canAcceptVariables()) {
            if (DIAGNOSE_MULTISTEPLIST) {
               System.err.print(first.getClass().getName());
               System.err.println(" at   " + first.getSystemId() + " Line " + first.getLineNumber());
            }

            return first;
         }

         for(int i = 0; i < numExprs; ++i) {
            elems[i] = elems[i].getParentElem();
         }
      }

      assertion(false, "Could not find common ancestor!!!");
      return null;
   }

   protected boolean isNotSameAsOwner(RedundentExprEliminator.MultistepExprHolder head, ElemTemplateElement ete) {
      for(RedundentExprEliminator.MultistepExprHolder next = head; null != next; next = next.m_next) {
         ElemTemplateElement elemOwner = this.getElemFromExpression(next.m_exprOwner.getExpression());
         if (elemOwner == ete) {
            return false;
         }
      }

      return true;
   }

   protected int countAncestors(ElemTemplateElement elem) {
      int count;
      for(count = 0; null != elem; elem = elem.getParentElem()) {
         ++count;
      }

      return count;
   }

   protected void diagnoseMultistepList(int matchCount, int lengthToTest, boolean isGlobal) {
      if (matchCount > 0) {
         System.err.print("Found multistep matches: " + matchCount + ", " + lengthToTest + " length");
         if (isGlobal) {
            System.err.println(" (global)");
         } else {
            System.err.println();
         }
      }

   }

   protected LocPathIterator changePartToRef(QName uniquePsuedoVarName, WalkingIterator wi, int numSteps, boolean isGlobal) {
      Variable var = new Variable();
      var.setQName(uniquePsuedoVarName);
      var.setIsGlobal(isGlobal);
      if (isGlobal) {
         ElemTemplateElement elem = this.getElemFromExpression(wi);
         StylesheetRoot root = elem.getStylesheetRoot();
         Vector vars = root.getVariablesAndParamsComposed();
         var.setIndex(vars.size() - 1);
      }

      AxesWalker walker = wi.getFirstWalker();

      for(int i = 0; i < numSteps; ++i) {
         assertion(null != walker, "Walker should not be null!");
         walker = walker.getNextWalker();
      }

      if (null != walker) {
         FilterExprWalker few = new FilterExprWalker(wi);
         few.setInnerExpression(var);
         few.exprSetParent(wi);
         few.setNextWalker(walker);
         walker.setPrevWalker(few);
         wi.setFirstWalker(few);
         return wi;
      } else {
         FilterExprIteratorSimple feis = new FilterExprIteratorSimple(var);
         feis.exprSetParent(wi.exprGetParent());
         return feis;
      }
   }

   protected WalkingIterator createIteratorFromSteps(WalkingIterator wi, int numSteps) {
      WalkingIterator newIter = new WalkingIterator(wi.getPrefixResolver());

      try {
         AxesWalker walker = (AxesWalker)wi.getFirstWalker().clone();
         newIter.setFirstWalker(walker);
         walker.setLocPathIterator(newIter);

         for(int i = 1; i < numSteps; ++i) {
            AxesWalker next = (AxesWalker)walker.getNextWalker().clone();
            walker.setNextWalker(next);
            next.setLocPathIterator(newIter);
            walker = next;
         }

         walker.setNextWalker((AxesWalker)null);
         return newIter;
      } catch (CloneNotSupportedException var7) {
         throw new WrappedRuntimeException(var7);
      }
   }

   protected boolean stepsEqual(WalkingIterator iter1, WalkingIterator iter2, int numSteps) {
      AxesWalker aw1 = iter1.getFirstWalker();
      AxesWalker aw2 = iter2.getFirstWalker();

      for(int i = 0; i < numSteps; ++i) {
         if (null == aw1 || null == aw2) {
            return false;
         }

         if (!aw1.deepEquals(aw2)) {
            return false;
         }

         aw1 = aw1.getNextWalker();
         aw2 = aw2.getNextWalker();
      }

      assertion(null != aw1 || null != aw2, "Total match is incorrect!");
      return true;
   }

   protected RedundentExprEliminator.MultistepExprHolder createMultistepExprList(Vector paths) {
      RedundentExprEliminator.MultistepExprHolder first = null;
      int n = paths.size();

      for(int i = 0; i < n; ++i) {
         ExpressionOwner eo = (ExpressionOwner)paths.elementAt(i);
         if (null != eo) {
            LocPathIterator lpi = (LocPathIterator)eo.getExpression();
            int numPaths = this.countSteps(lpi);
            if (numPaths > 1) {
               if (null == first) {
                  first = new RedundentExprEliminator.MultistepExprHolder(eo, numPaths, (RedundentExprEliminator.MultistepExprHolder)null);
               } else {
                  first = first.addInSortedOrder(eo, numPaths);
               }
            }
         }
      }

      if (null != first && first.getLength() > 1) {
         return first;
      } else {
         return null;
      }
   }

   protected int findAndEliminateRedundant(int start, int firstOccuranceIndex, ExpressionOwner firstOccuranceOwner, ElemTemplateElement psuedoVarRecipient, Vector paths) throws DOMException {
      RedundentExprEliminator.MultistepExprHolder head = null;
      RedundentExprEliminator.MultistepExprHolder tail = null;
      int numPathsFound = 0;
      int n = paths.size();
      Expression expr1 = firstOccuranceOwner.getExpression();
      if (DEBUG) {
         this.assertIsLocPathIterator(expr1, firstOccuranceOwner);
      }

      boolean isGlobal = paths == this.m_absPaths;
      LocPathIterator lpi = (LocPathIterator)expr1;
      int stepCount = this.countSteps(lpi);

      for(int j = start; j < n; ++j) {
         ExpressionOwner owner2 = (ExpressionOwner)paths.elementAt(j);
         if (null != owner2) {
            Expression expr2 = owner2.getExpression();
            boolean isEqual = expr2.deepEquals(lpi);
            if (isEqual) {
               LocPathIterator lpi2 = (LocPathIterator)expr2;
               if (null == head) {
                  head = new RedundentExprEliminator.MultistepExprHolder(firstOccuranceOwner, stepCount, (RedundentExprEliminator.MultistepExprHolder)null);
                  tail = head;
                  ++numPathsFound;
               }

               tail.m_next = new RedundentExprEliminator.MultistepExprHolder(owner2, stepCount, (RedundentExprEliminator.MultistepExprHolder)null);
               tail = tail.m_next;
               paths.setElementAt((Object)null, j);
               ++numPathsFound;
            }
         }
      }

      if (0 == numPathsFound && isGlobal) {
         head = new RedundentExprEliminator.MultistepExprHolder(firstOccuranceOwner, stepCount, (RedundentExprEliminator.MultistepExprHolder)null);
         ++numPathsFound;
      }

      if (null != head) {
         ElemTemplateElement root = isGlobal ? psuedoVarRecipient : this.findCommonAncestor(head);
         LocPathIterator sharedIter = (LocPathIterator)head.m_exprOwner.getExpression();
         ElemVariable var = this.createPsuedoVarDecl(root, sharedIter, isGlobal);
         if (DIAGNOSE_MULTISTEPLIST) {
            System.err.println("Created var: " + var.getName() + (isGlobal ? "(Global)" : ""));
         }

         for(QName uniquePsuedoVarName = var.getName(); null != head; head = head.m_next) {
            ExpressionOwner owner = head.m_exprOwner;
            if (DIAGNOSE_MULTISTEPLIST) {
               this.diagnoseLineNumber(owner.getExpression());
            }

            this.changeToVarRef(uniquePsuedoVarName, owner, paths, root);
         }

         paths.setElementAt(var.getSelect(), firstOccuranceIndex);
      }

      return numPathsFound;
   }

   protected int oldFindAndEliminateRedundant(int start, int firstOccuranceIndex, ExpressionOwner firstOccuranceOwner, ElemTemplateElement psuedoVarRecipient, Vector paths) throws DOMException {
      QName uniquePsuedoVarName = null;
      boolean foundFirst = false;
      int numPathsFound = 0;
      int n = paths.size();
      Expression expr1 = firstOccuranceOwner.getExpression();
      if (DEBUG) {
         this.assertIsLocPathIterator(expr1, firstOccuranceOwner);
      }

      boolean isGlobal = paths == this.m_absPaths;
      LocPathIterator lpi = (LocPathIterator)expr1;

      for(int j = start; j < n; ++j) {
         ExpressionOwner owner2 = (ExpressionOwner)paths.elementAt(j);
         if (null != owner2) {
            Expression expr2 = owner2.getExpression();
            boolean isEqual = expr2.deepEquals(lpi);
            if (isEqual) {
               LocPathIterator lpi2 = (LocPathIterator)expr2;
               if (!foundFirst) {
                  foundFirst = true;
                  ElemVariable var = this.createPsuedoVarDecl(psuedoVarRecipient, lpi, isGlobal);
                  if (null == var) {
                     return 0;
                  }

                  uniquePsuedoVarName = var.getName();
                  this.changeToVarRef(uniquePsuedoVarName, firstOccuranceOwner, paths, psuedoVarRecipient);
                  paths.setElementAt(var.getSelect(), firstOccuranceIndex);
                  ++numPathsFound;
               }

               this.changeToVarRef(uniquePsuedoVarName, owner2, paths, psuedoVarRecipient);
               paths.setElementAt((Object)null, j);
               ++numPathsFound;
            }
         }
      }

      if (0 == numPathsFound && paths == this.m_absPaths) {
         ElemVariable var = this.createPsuedoVarDecl(psuedoVarRecipient, lpi, true);
         if (null == var) {
            return 0;
         }

         uniquePsuedoVarName = var.getName();
         this.changeToVarRef(uniquePsuedoVarName, firstOccuranceOwner, paths, psuedoVarRecipient);
         paths.setElementAt(var.getSelect(), firstOccuranceIndex);
         ++numPathsFound;
      }

      return numPathsFound;
   }

   protected int countSteps(LocPathIterator lpi) {
      if (!(lpi instanceof WalkingIterator)) {
         return 1;
      } else {
         WalkingIterator wi = (WalkingIterator)lpi;
         AxesWalker aw = wi.getFirstWalker();

         int count;
         for(count = 0; null != aw; aw = aw.getNextWalker()) {
            ++count;
         }

         return count;
      }
   }

   protected void changeToVarRef(QName varName, ExpressionOwner owner, Vector paths, ElemTemplateElement psuedoVarRecipient) {
      Variable varRef = paths == this.m_absPaths ? new VariableSafeAbsRef() : new Variable();
      ((Variable)varRef).setQName(varName);
      if (paths == this.m_absPaths) {
         StylesheetRoot root = (StylesheetRoot)psuedoVarRecipient;
         Vector globalVars = root.getVariablesAndParamsComposed();
         ((Variable)varRef).setIndex(globalVars.size() - 1);
         ((Variable)varRef).setIsGlobal(true);
      }

      owner.setExpression((Expression)varRef);
   }

   protected ElemVariable createPsuedoVarDecl(ElemTemplateElement psuedoVarRecipient, LocPathIterator lpi, boolean isGlobal) throws DOMException {
      QName uniquePsuedoVarName = new QName("http://xml.apache.org/xalan/psuedovar", "#" + m_uniquePsuedoVarID);
      ++m_uniquePsuedoVarID;
      return isGlobal ? this.createGlobalPsuedoVarDecl(uniquePsuedoVarName, (StylesheetRoot)psuedoVarRecipient, lpi) : this.createLocalPsuedoVarDecl(uniquePsuedoVarName, psuedoVarRecipient, lpi);
   }

   protected ElemVariable createGlobalPsuedoVarDecl(QName uniquePsuedoVarName, StylesheetRoot stylesheetRoot, LocPathIterator lpi) throws DOMException {
      ElemVariable psuedoVar = new ElemVariable();
      psuedoVar.setIsTopLevel(true);
      XPath xpath = new XPath(lpi);
      psuedoVar.setSelect(xpath);
      psuedoVar.setName(uniquePsuedoVarName);
      Vector globalVars = stylesheetRoot.getVariablesAndParamsComposed();
      psuedoVar.setIndex(globalVars.size());
      globalVars.addElement(psuedoVar);
      return psuedoVar;
   }

   protected ElemVariable createLocalPsuedoVarDecl(QName uniquePsuedoVarName, ElemTemplateElement psuedoVarRecipient, LocPathIterator lpi) throws DOMException {
      ElemVariable psuedoVar = new ElemVariablePsuedo();
      XPath xpath = new XPath(lpi);
      psuedoVar.setSelect(xpath);
      psuedoVar.setName(uniquePsuedoVarName);
      ElemVariable var = this.addVarDeclToElem(psuedoVarRecipient, lpi, psuedoVar);
      lpi.exprSetParent(var);
      return var;
   }

   protected ElemVariable addVarDeclToElem(ElemTemplateElement psuedoVarRecipient, LocPathIterator lpi, ElemVariable psuedoVar) throws DOMException {
      ElemTemplateElement ete = psuedoVarRecipient.getFirstChildElem();
      lpi.callVisitors((ExpressionOwner)null, this.m_varNameCollector);
      if (this.m_varNameCollector.getVarCount() > 0) {
         ElemTemplateElement baseElem = this.getElemFromExpression(lpi);

         for(ElemVariable varElem = this.getPrevVariableElem(baseElem); null != varElem; varElem = this.getPrevVariableElem(varElem)) {
            if (this.m_varNameCollector.doesOccur(varElem.getName())) {
               psuedoVarRecipient = varElem.getParentElem();
               ete = varElem.getNextSiblingElem();
               break;
            }
         }
      }

      if (null != ete && 41 == ete.getXSLToken()) {
         if (this.isParam(lpi)) {
            return null;
         }

         while(null != ete) {
            ete = ete.getNextSiblingElem();
            if (null != ete && 41 != ete.getXSLToken()) {
               break;
            }
         }
      }

      psuedoVarRecipient.insertBefore(psuedoVar, ete);
      this.m_varNameCollector.reset();
      return psuedoVar;
   }

   protected boolean isParam(ExpressionNode expr) {
      while(null != expr && !(expr instanceof ElemTemplateElement)) {
         expr = expr.exprGetParent();
      }

      if (null != expr) {
         ElemTemplateElement ete = (ElemTemplateElement)expr;

         while(null != ete) {
            int type = ete.getXSLToken();
            switch(type) {
            case 19:
            case 25:
               return false;
            case 41:
               return true;
            default:
               ete = ete.getParentElem();
            }
         }
      }

      return false;
   }

   protected ElemVariable getPrevVariableElem(ElemTemplateElement elem) {
      int type;
      do {
         if (null == (elem = this.getPrevElementWithinContext(elem))) {
            return null;
         }

         type = elem.getXSLToken();
      } while(73 != type && 41 != type);

      return (ElemVariable)elem;
   }

   protected ElemTemplateElement getPrevElementWithinContext(ElemTemplateElement elem) {
      ElemTemplateElement prev = elem.getPreviousSiblingElem();
      if (null == prev) {
         prev = elem.getParentElem();
      }

      if (null != prev) {
         int type = prev.getXSLToken();
         if (28 == type || 19 == type || 25 == type) {
            prev = null;
         }
      }

      return prev;
   }

   protected ElemTemplateElement getElemFromExpression(Expression expr) {
      for(ExpressionNode parent = expr.exprGetParent(); null != parent; parent = parent.exprGetParent()) {
         if (parent instanceof ElemTemplateElement) {
            return (ElemTemplateElement)parent;
         }
      }

      throw new RuntimeException(XSLMessages.createMessage("ER_ASSERT_NO_TEMPLATE_PARENT", (Object[])null));
   }

   public boolean isAbsolute(LocPathIterator path) {
      int analysis = path.getAnalysisBits();
      boolean isAbs = WalkerFactory.isSet(analysis, 134217728) || WalkerFactory.isSet(analysis, 536870912);
      if (isAbs) {
         isAbs = this.m_absPathChecker.checkAbsolute(path);
      }

      return isAbs;
   }

   public boolean visitLocationPath(ExpressionOwner owner, LocPathIterator path) {
      if (path instanceof SelfIteratorNoPredicate) {
         return true;
      } else {
         if (path instanceof WalkingIterator) {
            WalkingIterator wi = (WalkingIterator)path;
            AxesWalker aw = wi.getFirstWalker();
            if (aw instanceof FilterExprWalker && null == aw.getNextWalker()) {
               FilterExprWalker few = (FilterExprWalker)aw;
               Expression exp = few.getInnerExpression();
               if (exp instanceof Variable) {
                  return true;
               }
            }
         }

         if (this.isAbsolute(path) && null != this.m_absPaths) {
            if (DEBUG) {
               validateNewAddition(this.m_absPaths, owner, path);
            }

            this.m_absPaths.addElement(owner);
         } else if (this.m_isSameContext && null != this.m_paths) {
            if (DEBUG) {
               validateNewAddition(this.m_paths, owner, path);
            }

            this.m_paths.addElement(owner);
         }

         return true;
      }
   }

   public boolean visitPredicate(ExpressionOwner owner, Expression pred) {
      boolean savedIsSame = this.m_isSameContext;
      this.m_isSameContext = false;
      pred.callVisitors(owner, this);
      this.m_isSameContext = savedIsSame;
      return false;
   }

   public boolean visitTopLevelInstruction(ElemTemplateElement elem) {
      int type = elem.getXSLToken();
      switch(type) {
      case 19:
         return this.visitInstruction(elem);
      default:
         return true;
      }
   }

   public boolean visitInstruction(ElemTemplateElement elem) {
      int type = elem.getXSLToken();
      switch(type) {
      case 17:
      case 19:
      case 28:
         if (type == 28) {
            ElemForEach efe = (ElemForEach)elem;
            Expression select = efe.getSelect();
            select.callVisitors(efe, this);
         }

         Vector savedPaths = this.m_paths;
         this.m_paths = new Vector();
         elem.callChildVisitors(this, false);
         this.eleminateRedundentLocals(elem);
         this.m_paths = savedPaths;
         return false;
      case 35:
      case 64:
         boolean savedIsSame = this.m_isSameContext;
         this.m_isSameContext = false;
         elem.callChildVisitors(this);
         this.m_isSameContext = savedIsSame;
         return false;
      default:
         return true;
      }
   }

   protected void diagnoseNumPaths(Vector paths, int numPathsEliminated, int numUniquePathsEliminated) {
      if (numPathsEliminated > 0) {
         if (paths == this.m_paths) {
            System.err.println("Eliminated " + numPathsEliminated + " total paths!");
            System.err.println("Consolodated " + numUniquePathsEliminated + " redundent paths!");
         } else {
            System.err.println("Eliminated " + numPathsEliminated + " total global paths!");
            System.err.println("Consolodated " + numUniquePathsEliminated + " redundent global paths!");
         }
      }

   }

   private final void assertIsLocPathIterator(Expression expr1, ExpressionOwner eo) throws RuntimeException {
      if (!(expr1 instanceof LocPathIterator)) {
         String errMsg;
         if (expr1 instanceof Variable) {
            errMsg = "Programmer's assertion: expr1 not an iterator: " + ((Variable)expr1).getQName();
         } else {
            errMsg = "Programmer's assertion: expr1 not an iterator: " + expr1.getClass().getName();
         }

         throw new RuntimeException(errMsg + ", " + eo.getClass().getName() + " " + expr1.exprGetParent());
      }
   }

   private static void validateNewAddition(Vector paths, ExpressionOwner owner, LocPathIterator path) throws RuntimeException {
      assertion(owner.getExpression() == path, "owner.getExpression() != path!!!");
      int n = paths.size();

      for(int i = 0; i < n; ++i) {
         ExpressionOwner ew = (ExpressionOwner)paths.elementAt(i);
         assertion(ew != owner, "duplicate owner on the list!!!");
         assertion(ew.getExpression() != path, "duplicate expression on the list!!!");
      }

   }

   protected static void assertion(boolean b, String msg) {
      if (!b) {
         throw new RuntimeException(XSLMessages.createMessage("ER_ASSERT_REDUNDENT_EXPR_ELIMINATOR", new Object[]{msg}));
      }
   }

   class MultistepExprHolder implements Cloneable {
      ExpressionOwner m_exprOwner;
      final int m_stepCount;
      RedundentExprEliminator.MultistepExprHolder m_next;

      public Object clone() throws CloneNotSupportedException {
         return super.clone();
      }

      MultistepExprHolder(ExpressionOwner exprOwner, int stepCount, RedundentExprEliminator.MultistepExprHolder next) {
         this.m_exprOwner = exprOwner;
         RedundentExprEliminator.assertion(null != this.m_exprOwner, "exprOwner can not be null!");
         this.m_stepCount = stepCount;
         this.m_next = next;
      }

      RedundentExprEliminator.MultistepExprHolder addInSortedOrder(ExpressionOwner exprOwner, int stepCount) {
         RedundentExprEliminator.MultistepExprHolder first = this;
         RedundentExprEliminator.MultistepExprHolder next = this;

         RedundentExprEliminator.MultistepExprHolder prev;
         for(prev = null; null != next; next = next.m_next) {
            if (stepCount >= next.m_stepCount) {
               RedundentExprEliminator.MultistepExprHolder newholder = RedundentExprEliminator.this.new MultistepExprHolder(exprOwner, stepCount, next);
               if (null == prev) {
                  first = newholder;
               } else {
                  prev.m_next = newholder;
               }

               return first;
            }

            prev = next;
         }

         prev.m_next = RedundentExprEliminator.this.new MultistepExprHolder(exprOwner, stepCount, (RedundentExprEliminator.MultistepExprHolder)null);
         return this;
      }

      RedundentExprEliminator.MultistepExprHolder unlink(RedundentExprEliminator.MultistepExprHolder itemToRemove) {
         RedundentExprEliminator.MultistepExprHolder first = this;
         RedundentExprEliminator.MultistepExprHolder next = this;

         for(RedundentExprEliminator.MultistepExprHolder prev = null; null != next; next = next.m_next) {
            if (next == itemToRemove) {
               if (null == prev) {
                  first = next.m_next;
               } else {
                  prev.m_next = next.m_next;
               }

               next.m_next = null;
               return first;
            }

            prev = next;
         }

         RedundentExprEliminator.assertion(false, "unlink failed!!!");
         return null;
      }

      int getLength() {
         int count = 0;

         for(RedundentExprEliminator.MultistepExprHolder next = this; null != next; next = next.m_next) {
            ++count;
         }

         return count;
      }

      protected void diagnose() {
         System.err.print("Found multistep iterators: " + this.getLength() + "  ");
         RedundentExprEliminator.MultistepExprHolder next = this;

         while(null != next) {
            System.err.print("" + next.m_stepCount);
            next = next.m_next;
            if (null != next) {
               System.err.print(", ");
            }
         }

         System.err.println();
      }
   }
}
