package org.apache.xpath.compiler;

import javax.xml.transform.TransformerException;
import org.apache.xpath.Expression;
import org.apache.xpath.functions.Function;

public class FunctionTable {
   public static final int FUNC_CURRENT = 0;
   public static final int FUNC_LAST = 1;
   public static final int FUNC_POSITION = 2;
   public static final int FUNC_COUNT = 3;
   public static final int FUNC_ID = 4;
   public static final int FUNC_KEY = 5;
   public static final int FUNC_LOCAL_PART = 7;
   public static final int FUNC_NAMESPACE = 8;
   public static final int FUNC_QNAME = 9;
   public static final int FUNC_GENERATE_ID = 10;
   public static final int FUNC_NOT = 11;
   public static final int FUNC_TRUE = 12;
   public static final int FUNC_FALSE = 13;
   public static final int FUNC_BOOLEAN = 14;
   public static final int FUNC_NUMBER = 15;
   public static final int FUNC_FLOOR = 16;
   public static final int FUNC_CEILING = 17;
   public static final int FUNC_ROUND = 18;
   public static final int FUNC_SUM = 19;
   public static final int FUNC_STRING = 20;
   public static final int FUNC_STARTS_WITH = 21;
   public static final int FUNC_CONTAINS = 22;
   public static final int FUNC_SUBSTRING_BEFORE = 23;
   public static final int FUNC_SUBSTRING_AFTER = 24;
   public static final int FUNC_NORMALIZE_SPACE = 25;
   public static final int FUNC_TRANSLATE = 26;
   public static final int FUNC_CONCAT = 27;
   public static final int FUNC_SUBSTRING = 29;
   public static final int FUNC_STRING_LENGTH = 30;
   public static final int FUNC_SYSTEM_PROPERTY = 31;
   public static final int FUNC_LANG = 32;
   public static final int FUNC_EXT_FUNCTION_AVAILABLE = 33;
   public static final int FUNC_EXT_ELEM_AVAILABLE = 34;
   public static final int FUNC_UNPARSED_ENTITY_URI = 36;
   public static final int FUNC_DOCLOCATION = 35;
   public static FuncLoader[] m_functions = new FuncLoader[67];
   private static final int NUM_BUILT_IN_FUNCS = 37;
   private static final int NUM_ALLOWABLE_ADDINS = 30;
   static int m_funcNextFreeIndex = 37;

   public static Function getFunction(int which) throws TransformerException {
      return m_functions[which].getFunction();
   }

   public static int installFunction(String name, Expression func) {
      Object funcIndexObj = Keywords.m_functions.get(name);
      int funcIndex;
      if (null != funcIndexObj) {
         funcIndex = (Integer)funcIndexObj;
      } else {
         funcIndex = m_funcNextFreeIndex++;
         Keywords.m_functions.put(name, new Integer(funcIndex));
      }

      FuncLoader loader = new FuncLoader(func.getClass().getName(), funcIndex);
      m_functions[funcIndex] = loader;
      return funcIndex;
   }

   public static void installFunction(Expression func, int funcIndex) {
      FuncLoader loader = new FuncLoader(func.getClass().getName(), funcIndex);
      m_functions[funcIndex] = loader;
   }

   static {
      m_functions[0] = new FuncLoader("FuncCurrent", 0);
      m_functions[1] = new FuncLoader("FuncLast", 1);
      m_functions[2] = new FuncLoader("FuncPosition", 2);
      m_functions[3] = new FuncLoader("FuncCount", 3);
      m_functions[4] = new FuncLoader("FuncId", 4);
      m_functions[5] = new FuncLoader("org.apache.xalan.templates.FuncKey", 5);
      m_functions[7] = new FuncLoader("FuncLocalPart", 7);
      m_functions[8] = new FuncLoader("FuncNamespace", 8);
      m_functions[9] = new FuncLoader("FuncQname", 9);
      m_functions[10] = new FuncLoader("FuncGenerateId", 10);
      m_functions[11] = new FuncLoader("FuncNot", 11);
      m_functions[12] = new FuncLoader("FuncTrue", 12);
      m_functions[13] = new FuncLoader("FuncFalse", 13);
      m_functions[14] = new FuncLoader("FuncBoolean", 14);
      m_functions[32] = new FuncLoader("FuncLang", 32);
      m_functions[15] = new FuncLoader("FuncNumber", 15);
      m_functions[16] = new FuncLoader("FuncFloor", 16);
      m_functions[17] = new FuncLoader("FuncCeiling", 17);
      m_functions[18] = new FuncLoader("FuncRound", 18);
      m_functions[19] = new FuncLoader("FuncSum", 19);
      m_functions[20] = new FuncLoader("FuncString", 20);
      m_functions[21] = new FuncLoader("FuncStartsWith", 21);
      m_functions[22] = new FuncLoader("FuncContains", 22);
      m_functions[23] = new FuncLoader("FuncSubstringBefore", 23);
      m_functions[24] = new FuncLoader("FuncSubstringAfter", 24);
      m_functions[25] = new FuncLoader("FuncNormalizeSpace", 25);
      m_functions[26] = new FuncLoader("FuncTranslate", 26);
      m_functions[27] = new FuncLoader("FuncConcat", 27);
      m_functions[31] = new FuncLoader("FuncSystemProperty", 31);
      m_functions[33] = new FuncLoader("FuncExtFunctionAvailable", 33);
      m_functions[34] = new FuncLoader("FuncExtElementAvailable", 34);
      m_functions[29] = new FuncLoader("FuncSubstring", 29);
      m_functions[30] = new FuncLoader("FuncStringLength", 30);
      m_functions[35] = new FuncLoader("FuncDoclocation", 35);
      m_functions[36] = new FuncLoader("FuncUnparsedEntityURI", 36);
   }
}
