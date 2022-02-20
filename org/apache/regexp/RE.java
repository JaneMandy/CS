package org.apache.regexp;

import java.util.Vector;

public class RE {
   public static final int MATCH_NORMAL = 0;
   public static final int MATCH_CASEINDEPENDENT = 1;
   public static final int MATCH_MULTILINE = 2;
   public static final int MATCH_SINGLELINE = 4;
   static final char OP_END = 'E';
   static final char OP_BOL = '^';
   static final char OP_EOL = '$';
   static final char OP_ANY = '.';
   static final char OP_ANYOF = '[';
   static final char OP_BRANCH = '|';
   static final char OP_ATOM = 'A';
   static final char OP_STAR = '*';
   static final char OP_PLUS = '+';
   static final char OP_MAYBE = '?';
   static final char OP_ESCAPE = '\\';
   static final char OP_OPEN = '(';
   static final char OP_CLOSE = ')';
   static final char OP_BACKREF = '#';
   static final char OP_GOTO = 'G';
   static final char OP_NOTHING = 'N';
   static final char OP_RELUCTANTSTAR = '8';
   static final char OP_RELUCTANTPLUS = '=';
   static final char OP_RELUCTANTMAYBE = '/';
   static final char OP_POSIXCLASS = 'P';
   static final char E_ALNUM = 'w';
   static final char E_NALNUM = 'W';
   static final char E_BOUND = 'b';
   static final char E_NBOUND = 'B';
   static final char E_SPACE = 's';
   static final char E_NSPACE = 'S';
   static final char E_DIGIT = 'd';
   static final char E_NDIGIT = 'D';
   static final char POSIX_CLASS_ALNUM = 'w';
   static final char POSIX_CLASS_ALPHA = 'a';
   static final char POSIX_CLASS_BLANK = 'b';
   static final char POSIX_CLASS_CNTRL = 'c';
   static final char POSIX_CLASS_DIGIT = 'd';
   static final char POSIX_CLASS_GRAPH = 'g';
   static final char POSIX_CLASS_LOWER = 'l';
   static final char POSIX_CLASS_PRINT = 'p';
   static final char POSIX_CLASS_PUNCT = '!';
   static final char POSIX_CLASS_SPACE = 's';
   static final char POSIX_CLASS_UPPER = 'u';
   static final char POSIX_CLASS_XDIGIT = 'x';
   static final char POSIX_CLASS_JSTART = 'j';
   static final char POSIX_CLASS_JPART = 'k';
   static final int maxNode = 65536;
   static final int maxParen = 16;
   static final int offsetOpcode = 0;
   static final int offsetOpdata = 1;
   static final int offsetNext = 2;
   static final int nodeSize = 3;
   static final String NEWLINE = System.getProperty("line.separator");
   REProgram program;
   CharacterIterator search;
   int idx;
   int matchFlags;
   int parenCount;
   int start0;
   int end0;
   int start1;
   int end1;
   int start2;
   int end2;
   int[] startn;
   int[] endn;
   int[] startBackref;
   int[] endBackref;
   public static final int REPLACE_ALL = 0;
   public static final int REPLACE_FIRSTONLY = 1;

   public RE() {
      this((REProgram)null, 0);
   }

   public RE(String var1) throws RESyntaxException {
      this((String)var1, 0);
   }

   public RE(String var1, int var2) throws RESyntaxException {
      this((new RECompiler()).compile(var1));
      this.setMatchFlags(var2);
   }

   public RE(REProgram var1) {
      this((REProgram)var1, 0);
   }

   public RE(REProgram var1, int var2) {
      this.setProgram(var1);
      this.setMatchFlags(var2);
   }

   private final void allocParens() {
      this.startn = new int[16];
      this.endn = new int[16];

      for(int var1 = 0; var1 < 16; ++var1) {
         this.startn[var1] = -1;
         this.endn[var1] = -1;
      }

   }

   public int getMatchFlags() {
      return this.matchFlags;
   }

   public String getParen(int var1) {
      int var2;
      return var1 < this.parenCount && (var2 = this.getParenStart(var1)) >= 0 ? this.search.substring(var2, this.getParenEnd(var1)) : null;
   }

   public int getParenCount() {
      return this.parenCount;
   }

   public final int getParenEnd(int var1) {
      if (var1 < this.parenCount) {
         switch(var1) {
         case 0:
            return this.end0;
         case 1:
            return this.end1;
         case 2:
            return this.end2;
         default:
            if (this.endn == null) {
               this.allocParens();
            }

            return this.endn[var1];
         }
      } else {
         return -1;
      }
   }

   public final int getParenLength(int var1) {
      return var1 < this.parenCount ? this.getParenEnd(var1) - this.getParenStart(var1) : -1;
   }

   public final int getParenStart(int var1) {
      if (var1 < this.parenCount) {
         switch(var1) {
         case 0:
            return this.start0;
         case 1:
            return this.start1;
         case 2:
            return this.start2;
         default:
            if (this.startn == null) {
               this.allocParens();
            }

            return this.startn[var1];
         }
      } else {
         return -1;
      }
   }

   public REProgram getProgram() {
      return this.program;
   }

   public String[] grep(Object[] var1) {
      Vector var2 = new Vector();

      for(int var3 = 0; var3 < var1.length; ++var3) {
         String var4 = var1[var3].toString();
         if (this.match(var4)) {
            var2.addElement(var4);
         }
      }

      String[] var5 = new String[var2.size()];
      var2.copyInto(var5);
      return var5;
   }

   protected void internalError(String var1) throws Error {
      throw new Error("RE internal error: " + var1);
   }

   private boolean isNewline(int var1) {
      if (var1 < NEWLINE.length() - 1) {
         return false;
      } else if (this.search.charAt(var1) == '\n') {
         return true;
      } else {
         for(int var2 = NEWLINE.length() - 1; var2 >= 0; --var1) {
            if (NEWLINE.charAt(var2) != this.search.charAt(var1)) {
               return false;
            }

            --var2;
         }

         return true;
      }
   }

   public boolean match(String var1) {
      return this.match((String)var1, 0);
   }

   public boolean match(String var1, int var2) {
      return this.match((CharacterIterator)(new StringCharacterIterator(var1)), var2);
   }

   public boolean match(CharacterIterator var1, int var2) {
      if (this.program == null) {
         this.internalError("No RE program to run!");
      }

      this.search = var1;
      if (this.program.prefix == null) {
         while(!var1.isEnd(var2 - 1)) {
            if (this.matchAt(var2)) {
               return true;
            }

            ++var2;
         }

         return false;
      } else {
         boolean var3 = (this.matchFlags & 1) != 0;

         for(char[] var4 = this.program.prefix; !var1.isEnd(var2 + var4.length - 1); ++var2) {
            boolean var5 = false;
            if (var3) {
               var5 = Character.toLowerCase(var1.charAt(var2)) == Character.toLowerCase(var4[0]);
            } else {
               var5 = var1.charAt(var2) == var4[0];
            }

            if (var5) {
               int var6 = var2++;
               int var7 = 1;

               while(var7 < var4.length) {
                  if (var3) {
                     var5 = Character.toLowerCase(var1.charAt(var2++)) == Character.toLowerCase(var4[var7++]);
                  } else {
                     var5 = var1.charAt(var2++) == var4[var7++];
                  }

                  if (!var5) {
                     break;
                  }
               }

               if (var7 == var4.length && this.matchAt(var6)) {
                  return true;
               }

               var2 = var6;
            }
         }

         return false;
      }
   }

   protected boolean matchAt(int var1) {
      this.start0 = -1;
      this.end0 = -1;
      this.start1 = -1;
      this.end1 = -1;
      this.start2 = -1;
      this.end2 = -1;
      this.startn = null;
      this.endn = null;
      this.parenCount = 1;
      this.setParenStart(0, var1);
      if ((this.program.flags & 1) != 0) {
         this.startBackref = new int[16];
         this.endBackref = new int[16];
      }

      int var2;
      if ((var2 = this.matchNodes(0, 65536, var1)) != -1) {
         this.setParenEnd(0, var2);
         return true;
      } else {
         this.parenCount = 0;
         return false;
      }
   }

   protected int matchNodes(int var1, int var2, int var3) {
      int var4 = var3;
      char[] var9 = this.program.instruction;
      int var10 = var1;

      while(var10 < var2) {
         int var5;
         char var6 = var9[var10];
         var5 = var10 + (short)var9[var10 + 2];
         char var7 = var9[var10 + 1];
         int var8;
         int var13;
         int var14;
         char var19;
         int var21;
         int var23;
         label393:
         switch(var6) {
         case '#':
            var23 = this.startBackref[var7];
            var21 = this.endBackref[var7];
            if (var23 == -1 || var21 == -1) {
               return -1;
            }

            if (var23 != var21) {
               var13 = var21 - var23;
               if (this.search.isEnd(var4 + var13 - 1)) {
                  return -1;
               }

               if ((this.matchFlags & 1) != 0) {
                  for(var14 = 0; var14 < var13; ++var14) {
                     if (Character.toLowerCase(this.search.charAt(var4++)) != Character.toLowerCase(this.search.charAt(var23 + var14))) {
                        return -1;
                     }
                  }
               } else {
                  for(var14 = 0; var14 < var13; ++var14) {
                     if (this.search.charAt(var4++) != this.search.charAt(var23 + var14)) {
                        return -1;
                     }
                  }
               }
            }
            break;
         case '$':
            if (!this.search.isEnd(0) && !this.search.isEnd(var4)) {
               if ((this.matchFlags & 2) != 2) {
                  return -1;
               }

               if (!this.isNewline(var4)) {
                  return -1;
               }
            }
            break;
         case '(':
            if ((this.program.flags & 1) != 0) {
               this.startBackref[var7] = var4;
            }

            if ((var8 = this.matchNodes(var5, 65536, var4)) != -1) {
               if (var7 + 1 > this.parenCount) {
                  this.parenCount = var7 + 1;
               }

               if (this.getParenStart(var7) == -1) {
                  this.setParenStart(var7, var4);
               }
            }

            return var8;
         case ')':
            if ((this.program.flags & 1) != 0) {
               this.endBackref[var7] = var4;
            }

            if ((var8 = this.matchNodes(var5, 65536, var4)) != -1) {
               if (var7 + 1 > this.parenCount) {
                  this.parenCount = var7 + 1;
               }

               if (this.getParenEnd(var7) == -1) {
                  this.setParenEnd(var7, var4);
               }
            }

            return var8;
         case '.':
            if ((this.matchFlags & 4) == 4) {
               if (this.search.isEnd(var4)) {
                  return -1;
               }

               ++var4;
            } else if (this.search.isEnd(var4) || this.search.charAt(var4++) == '\n') {
               return -1;
            }
            break;
         case '/':
            var23 = 0;

            do {
               if ((var8 = this.matchNodes(var5, 65536, var4)) != -1) {
                  return var8;
               }
            } while(var23++ == 0 && (var4 = this.matchNodes(var10 + 3, var5, var4)) != -1);

            return -1;
         case '8':
            while((var8 = this.matchNodes(var5, 65536, var4)) == -1) {
               if ((var4 = this.matchNodes(var10 + 3, var5, var4)) == -1) {
                  return -1;
               }
            }

            return var8;
         case '=':
            while((var4 = this.matchNodes(var10 + 3, var5, var4)) != -1) {
               if ((var8 = this.matchNodes(var5, 65536, var4)) != -1) {
                  return var8;
               }
            }

            return -1;
         case 'A':
            if (this.search.isEnd(var4)) {
               return -1;
            }

            var19 = var7;
            var21 = var10 + 3;
            if (this.search.isEnd(var7 + var4 - 1)) {
               return -1;
            }

            if ((this.matchFlags & 1) != 0) {
               var13 = 0;

               while(true) {
                  if (var13 >= var19) {
                     break label393;
                  }

                  if (Character.toLowerCase(this.search.charAt(var4++)) != Character.toLowerCase(var9[var21 + var13])) {
                     return -1;
                  }

                  ++var13;
               }
            } else {
               var13 = 0;

               while(true) {
                  if (var13 >= var19) {
                     break label393;
                  }

                  if (this.search.charAt(var4++) != var9[var21 + var13]) {
                     return -1;
                  }

                  ++var13;
               }
            }
         case 'E':
            this.setParenEnd(0, var4);
            return var4;
         case 'G':
         case 'N':
            break;
         case 'P':
            if (this.search.isEnd(var4)) {
               return -1;
            }

            label324:
            switch(var7) {
            case '!':
               var23 = Character.getType(this.search.charAt(var4));
               switch(var23) {
               case 20:
               case 21:
               case 22:
               case 23:
               case 24:
                  break label324;
               default:
                  return -1;
               }
            case 'a':
               if (!Character.isLetter(this.search.charAt(var4))) {
                  return -1;
               }
               break;
            case 'b':
               if (!Character.isSpaceChar(this.search.charAt(var4))) {
                  return -1;
               }
               break;
            case 'c':
               if (Character.getType(this.search.charAt(var4)) != 15) {
                  return -1;
               }
               break;
            case 'd':
               if (!Character.isDigit(this.search.charAt(var4))) {
                  return -1;
               }
               break;
            case 'g':
               switch(Character.getType(this.search.charAt(var4))) {
               case 25:
               case 26:
               case 27:
               case 28:
                  break label324;
               default:
                  return -1;
               }
            case 'j':
               if (!Character.isJavaIdentifierStart(this.search.charAt(var4))) {
                  return -1;
               }
               break;
            case 'k':
               if (!Character.isJavaIdentifierPart(this.search.charAt(var4))) {
                  return -1;
               }
               break;
            case 'l':
               if (Character.getType(this.search.charAt(var4)) != 2) {
                  return -1;
               }
               break;
            case 'p':
               if (Character.getType(this.search.charAt(var4)) == 15) {
                  return -1;
               }
               break;
            case 's':
               if (!Character.isWhitespace(this.search.charAt(var4))) {
                  return -1;
               }
               break;
            case 'u':
               if (Character.getType(this.search.charAt(var4)) != 1) {
                  return -1;
               }
               break;
            case 'w':
               if (!Character.isLetterOrDigit(this.search.charAt(var4))) {
                  return -1;
               }
               break;
            case 'x':
               boolean var22 = this.search.charAt(var4) >= '0' && this.search.charAt(var4) <= '9' || this.search.charAt(var4) >= 'a' && this.search.charAt(var4) <= 'f' || this.search.charAt(var4) >= 'A' && this.search.charAt(var4) <= 'F';
               if (!var22) {
                  return -1;
               }
               break;
            default:
               this.internalError("Bad posix class");
            }

            ++var4;
            break;
         case '[':
            if (this.search.isEnd(var4)) {
               return -1;
            }

            var19 = this.search.charAt(var4);
            boolean var20 = (this.matchFlags & 1) != 0;
            if (var20) {
               var19 = Character.toLowerCase(var19);
            }

            var13 = var10 + 3;
            var14 = var13 + var7 * 2;
            boolean var15 = false;
            int var16 = var13;

            while(var16 < var14) {
               char var17 = var9[var16++];
               char var18 = var9[var16++];
               if (var20) {
                  var17 = Character.toLowerCase(var17);
                  var18 = Character.toLowerCase(var18);
               }

               if (var19 >= var17 && var19 <= var18) {
                  var15 = true;
                  break;
               }
            }

            if (!var15) {
               return -1;
            }

            ++var4;
            break;
         case '\\':
            switch(var7) {
            case 'B':
            case 'b':
               var19 = var4 == this.getParenStart(0) ? 10 : this.search.charAt(var4 - 1);
               char var12 = this.search.isEnd(var4) ? 10 : this.search.charAt(var4);
               if (Character.isLetterOrDigit(var19) == Character.isLetterOrDigit(var12) == (var7 == 'b')) {
                  return -1;
               }
               break label393;
            case 'D':
            case 'S':
            case 'W':
            case 'd':
            case 's':
            case 'w':
               if (this.search.isEnd(var4)) {
                  return -1;
               }

               switch(var7) {
               case 'D':
               case 'd':
                  if (Character.isDigit(this.search.charAt(var4)) != (var7 == 'd')) {
                     return -1;
                  }
                  break;
               case 'S':
               case 's':
                  if (Character.isWhitespace(this.search.charAt(var4)) != (var7 == 's')) {
                     return -1;
                  }
                  break;
               case 'W':
               case 'w':
                  if (Character.isLetterOrDigit(this.search.charAt(var4)) != (var7 == 'w')) {
                     return -1;
                  }
               }

               ++var4;
               break label393;
            default:
               this.internalError("Unrecognized escape '" + var7 + "'");
               break label393;
            }
         case '^':
            if (var4 != 0) {
               if ((this.matchFlags & 2) != 2) {
                  return -1;
               }

               if (var4 <= 0 || !this.isNewline(var4 - 1)) {
                  return -1;
               }
            }
            break;
         case '|':
            if (var9[var5] == '|') {
               short var11;
               do {
                  if ((var8 = this.matchNodes(var10 + 3, 65536, var4)) != -1) {
                     return var8;
                  }

                  var11 = (short)var9[var10 + 2];
                  var10 += var11;
               } while(var11 != 0 && var9[var10] == '|');

               return -1;
            }

            var10 += 3;
            continue;
         default:
            this.internalError("Invalid opcode '" + var6 + "'");
         }

         var10 = var5;
      }

      this.internalError("Corrupt program");
      return -1;
   }

   public void setMatchFlags(int var1) {
      this.matchFlags = var1;
   }

   protected final void setParenEnd(int var1, int var2) {
      if (var1 < this.parenCount) {
         switch(var1) {
         case 0:
            this.end0 = var2;
            break;
         case 1:
            this.end1 = var2;
            break;
         case 2:
            this.end2 = var2;
            break;
         default:
            if (this.endn == null) {
               this.allocParens();
            }

            this.endn[var1] = var2;
         }
      }

   }

   protected final void setParenStart(int var1, int var2) {
      if (var1 < this.parenCount) {
         switch(var1) {
         case 0:
            this.start0 = var2;
            break;
         case 1:
            this.start1 = var2;
            break;
         case 2:
            this.start2 = var2;
            break;
         default:
            if (this.startn == null) {
               this.allocParens();
            }

            this.startn[var1] = var2;
         }
      }

   }

   public void setProgram(REProgram var1) {
      this.program = var1;
   }

   public static String simplePatternToFullRegularExpression(String var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         switch(var3) {
         case '$':
         case '(':
         case ')':
         case '+':
         case '.':
         case '?':
         case '[':
         case '\\':
         case ']':
         case '^':
         case '{':
         case '|':
         case '}':
            var1.append('\\');
         default:
            var1.append(var3);
            break;
         case '*':
            var1.append(".*");
         }
      }

      return var1.toString();
   }

   public String[] split(String var1) {
      Vector var2 = new Vector();
      int var3 = 0;

      int var6;
      for(int var4 = var1.length(); var3 < var4 && this.match(var1, var3); var3 = var6) {
         int var5 = this.getParenStart(0);
         var6 = this.getParenEnd(0);
         if (var6 == var3) {
            var2.addElement(var1.substring(var3, var5 + 1));
            ++var6;
         } else {
            var2.addElement(var1.substring(var3, var5));
         }
      }

      String var7 = var1.substring(var3);
      if (var7.length() != 0) {
         var2.addElement(var7);
      }

      String[] var8 = new String[var2.size()];
      var2.copyInto(var8);
      return var8;
   }

   public String subst(String var1, String var2) {
      return this.subst(var1, var2, 0);
   }

   public String subst(String var1, String var2, int var3) {
      StringBuffer var4 = new StringBuffer();
      int var5 = 0;
      int var6 = var1.length();

      while(var5 < var6 && this.match(var1, var5)) {
         var4.append(var1.substring(var5, this.getParenStart(0)));
         var4.append(var2);
         int var7 = this.getParenEnd(0);
         if (var7 == var5) {
            ++var7;
         }

         var5 = var7;
         if ((var3 & 1) != 0) {
            break;
         }
      }

      if (var5 < var6) {
         var4.append(var1.substring(var5));
      }

      return var4.toString();
   }
}
