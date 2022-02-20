package org.apache.xalan.xsltc.compiler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;

class XPathLexer implements Scanner {
   private final int YY_BUFFER_SIZE;
   private final int YY_F;
   private final int YY_NO_STATE;
   private final int YY_NOT_ACCEPT;
   private final int YY_START;
   private final int YY_END;
   private final int YY_NO_ANCHOR;
   private final int YY_BOL;
   private final int YY_EOF;
   public final int YYEOF;
   private BufferedReader yy_reader;
   private int yy_buffer_index;
   private int yy_buffer_read;
   private int yy_buffer_start;
   private int yy_buffer_end;
   private char[] yy_buffer;
   private boolean yy_at_bol;
   private int yy_lexical_state;
   private boolean yy_eof_done;
   private final int YYINITIAL;
   private final int[] yy_state_dtrans;
   private boolean yy_last_was_cr;
   private final int YY_E_INTERNAL;
   private final int YY_E_MATCH;
   private String[] yy_error_string;
   private int[] yy_acpt;
   private static int[] yy_cmap = unpackFromString(1, 65538, "54:9,27:2,54,27:2,54:18,27,17,53,54,15,54:2,55,25,26,1,3,11,4,13,2,56:10,10,54,18,16,19,54,12,44,57:3,46,57:3,51,57:4,48,52,43,57,47,50,45,57:3,49,57:2,41,54,42,54,58,54,35,38,29,5,21,39,33,36,6,57,20,37,8,28,9,30,57,31,32,23,34,7,40,24,22,57,54,14,54:58,59,54:8,57:23,54,57:31,54,57:58,54:2,57:11,54:2,57:8,54,57:53,54,57:68,54:9,57:36,54:3,57:2,54:4,57:30,54:56,57:89,54:18,57:7,54:14,59:2,54:46,59:70,54:26,59:2,54:36,57,59,57:3,54,57,54,57:20,54,57:44,54,57:7,54:3,57,54,57,54,57,54,57,54,57:18,54:13,57:12,54,57:66,54,57:12,54,57:36,54,59:4,54:9,57:53,54:2,57:2,54:2,57:2,54:3,57:28,54:2,57:8,54:2,57:2,54:55,57:38,54:2,57,54:7,57:38,54:10,59:17,54,59:23,54,59:3,54,59,54,59:2,54,59,54:11,57:27,54:5,57:3,54:46,57:26,54:5,59,57:10,59:8,54:13,56:10,54:6,59,57:71,54:2,57:5,54,57:15,54,57:4,54,57,59:15,57:2,59:2,54,59:4,54:2,56:10,54:519,59:3,54,57:53,54:2,59,57,59:16,54:3,59:4,54:3,57:10,59:2,54:2,56:10,54:17,59:3,54,57:8,54:2,57:2,54:2,57:22,54,57:7,54,57,54:3,57:4,54:2,59,54,59:7,54:2,59:2,54:2,59:3,54:9,59,54:4,57:2,54,57:3,59:2,54:2,56:10,57:2,54:16,59,54:2,57:6,54:4,57:2,54:2,57:22,54,57:7,54,57:2,54,57:2,54,57:2,54:2,59,54,59:5,54:4,59:2,54:2,59:3,54:11,57:4,54,57,54:7,56:10,59:2,57:3,54:12,59:3,54,57:7,54,57,54,57:3,54,57:22,54,57:7,54,57:2,54,57:5,54:2,59,57,59:8,54,59:3,54,59:3,54:18,57,54:5,56:10,54:17,59:3,54,57:8,54:2,57:2,54:2,57:22,54,57:7,54,57:2,54:2,57:4,54:2,59,57,59:6,54:3,59:2,54:2,59:3,54:8,59:2,54:4,57:2,54,57:3,54:4,56:10,54:18,59:2,54,57:6,54:3,57:3,54,57:4,54:3,57:2,54,57,54,57:2,54:3,57:2,54:3,57:3,54:3,57:8,54,57:3,54:4,59:5,54:3,59:3,54,59:4,54:9,59,54:15,56:9,54:17,59:3,54,57:8,54,57:3,54,57:23,54,57:10,54,57:5,54:4,59:7,54,59:3,54,59:4,54:7,59:2,54:9,57:2,54:4,56:10,54:18,59:2,54,57:8,54,57:3,54,57:23,54,57:10,54,57:5,54:4,59:7,54,59:3,54,59:4,54:7,59:2,54:7,57,54,57:2,54:4,56:10,54:18,59:2,54,57:8,54,57:3,54,57:23,54,57:16,54:4,59:6,54:2,59:3,54,59:4,54:9,59,54:8,57:2,54:4,56:10,54:145,57:46,54,57,59,57:2,59:7,54:5,57:6,59:9,54,56:10,54:39,57:2,54,57,54:2,57:2,54,57,54:2,57,54:6,57:4,54,57:7,54,57:3,54,57,54,57,54:2,57:2,54,57:2,54,57,59,57:2,59:6,54,59:2,57,54:2,57:5,54,59,54,59:6,54:2,56:10,54:62,59:2,54:6,56:10,54:11,59,54,59,54,59,54:4,59:2,57:8,54,57:33,54:7,59:20,54,59:6,54:4,59:6,54,59,54,59:21,54:3,59:7,54,59,54:230,57:38,54:10,57:39,54:9,57,54,57:2,54,57:3,54,57,54,57:2,54,57:5,54:41,57,54,57,54,57,54:11,57,54,57,54,57,54:3,57:2,54:3,57,54:5,57:3,54,57,54,57,54,57,54,57,54:3,57:2,54:3,57:2,54,57,54:40,57,54:9,57,54:2,57,54:2,57:2,54:7,57:2,54,57,54,57:7,54:40,57,54:4,57,54:8,57,54:3078,57:156,54:4,57:90,54:6,57:22,54:2,57:6,54:2,57:38,54:2,57:6,54:2,57:8,54,57,54,57,54,57,54,57:31,54:2,57:53,54,57:7,54,57,54:3,57:3,54,57:7,54:3,57:4,54:2,57:6,54:4,57:13,54:5,57:3,54,57:7,54:211,59:13,54:4,59,54:68,57,54:3,57:2,54:2,57,54:81,57:3,54:3714,59,54,58,54:25,58:9,59:6,54,59:5,54:11,57:84,54:4,59:2,54:2,59:2,54:2,57:90,54,59:3,54:6,57:40,54:7379,58:20902,54:3162,57:11172,54:10332,0:2")[0];
   private static int[] yy_rmap = unpackFromString(1, 232, "0,1:2,2,1:2,3,4,1,5,6,1:3,7,8,1:5,9,1,10:2,1:3,11,1:5,12,10,1,10:5,1:2,10,1:2,13,1,10,1,14,10,15,16,1:2,10:4,17,1:2,18,19,20,21,22,23,24,25,1,23,10,26:2,27,5,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,10,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179")[0];
   private static int[][] yy_nxt = unpackFromString(180, 60, "1,2,3,4,5,6,65,182,202,68,7,8,9,10,11,12,13,66,14,15,209,182:2,213,182,16,17,18,216,218,219,182,220,182:2,221,182:3,222,182,19,20,182:10,69,72,75,21,182:2,72,-1:62,22,-1:61,182:2,71,182:3,64,-1:2,74,-1:6,182,77,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:10,25,-1:50,26,-1:71,27,-1:42,28,-1:19,30,-1:26,67,-1:2,70,-1:29,31,-1:56,34,-1:42,21,-1:7,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:56,28,-1:59,34,-1:7,153,182:5,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,207,182:5,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,231,182:5,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,156,182:5,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,120,-1,122,181,182:12,-1:2,182:10,-1:3,74,182,74:2,-1,36,-1:3,101:5,-1:2,78,-1:7,101:5,-1:3,101:13,-1:2,101:10,-1:4,101:2,-1:5,182,23,182:4,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:16,29,-1:87,80,-1:19,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,24,182:9,-1:2,182:10,-1:3,74,182,74:2,-1,73:52,32,73:6,-1:49,82,-1:14,182:3,35,182:2,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1,76:54,33,76:4,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,103,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182,37,182:4,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:45,183,-1:18,182:6,64,-1:2,74,-1:6,182:2,38,182:2,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:43,84,-1:20,182:6,64,-1:2,74,-1:6,182:4,189,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:47,184,-1:16,182,105,182:4,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:46,94,-1:17,182:4,191,182,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:26,42,-1:37,182:2,203,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:25,98,-1,90,-1:36,182:5,190,64,-1:2,74,-1:6,182,226,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:26,43,-1:37,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,204,182:9,-1:2,182:10,-1:3,74,182,74:2,-1:47,102,-1:16,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,109,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:50,188,-1:13,182:6,64,-1:2,74,-1:6,182:3,111,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:26,45,-1:37,182,39,182:4,64,-1:2,74,-1:6,182:5,-1:3,182,210,182:11,-1:2,182:10,-1:3,74,182,74:2,-1:26,46,-1:37,101:6,-1:3,101,-1:6,101:5,-1:3,101:13,-1:2,101:10,-1:3,101:4,-1:48,104,-1:15,182:6,64,-1:2,74,-1:6,182:5,-1:3,182,214,182:11,-1:2,182:10,-1:3,74,182,74:2,-1:19,48,-1:44,182:6,64,-1:2,74,-1:6,182,117,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:51,112,-1:12,182:4,121,182,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:26,50,-1:37,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:11,40,182,-1:2,182:10,-1:3,74,182,74:2,-1:25,114,-1,110,-1:36,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,126,182:9,-1:2,182:10,-1:3,74,182,74:2,-1:52,116,-1:11,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,127,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:26,55,-1:37,182:6,64,-1:2,74,-1:6,182:3,128,182,88,-1,90,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:48,118,-1:15,182:6,64,-1:2,74,-1:6,182,129,182:3,92,-1,186,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:19,56,-1:44,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,130,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:26,62,-1:37,182:6,64,-1:2,74,-1:6,182,206,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:25,124,-1,122,-1:36,182,41,182:4,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:26,63,-1:37,182:6,64,-1:2,74,-1:6,182:5,-1:3,133,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,134,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,136,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,128,182,-1:2,90,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,129,182:3,-1:2,186,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:2,137,182:10,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,195,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182,138,182:4,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,44,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:10,139,182:2,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,140,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:12,223,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:7,141,182:5,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,143,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:6,144,182:6,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,145,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182,146,182:11,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,147,182,108,-1,110,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,148,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,149,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,47,182:9,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,49,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,147,182,-1:2,110,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:5,51,182:7,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,52,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:5,53,182:7,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,54,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:5,154,182:7,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,155,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,157,182:5,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,158,182:9,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,159,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,160,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,211,182:5,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,224,182:9,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,215,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:10,162,182:2,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,165,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,166,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,168,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,169,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,170,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,171,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,172,182:9,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,173,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:11,57,182,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,175,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:6,176,182:6,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:5,58,182:7,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:5,59,182:7,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:11,60,182,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182,177,182:11,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,178,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,179,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,180,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,61,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:2,122,181,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:45,86,-1:60,96,-1:17,182:4,107,182,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:25,100,-1,186,-1:36,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,113,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:50,106,-1:13,182:6,64,-1:2,74,-1:6,182:3,115,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182,193,182:11,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,119,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,135,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,131,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,196,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,227,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182,198,182:4,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,142,182,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:7,208,182:5,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,150,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,161,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,174,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,79,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,123,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,125,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,132,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,197,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,200,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,151,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,81,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,192,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,163,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,152,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,83,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,194,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,164,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,85,64,-1:2,74,-1:6,182:5,-1:3,182:7,87,182:5,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,167,182:8,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,185,64,-1:2,74,-1:6,182:5,-1:3,182:8,89,182:4,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:3,91,182:3,93,182:5,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182,95,182:3,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:3,97,182,-1:3,99,182:12,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:5,187,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,199,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,217,182:5,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,201,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182,205,182:11,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,212,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:9,225,182:3,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:10,228,182:2,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:2,229,182:3,64,-1:2,74,-1:6,182:5,-1:3,182:13,-1:2,182:10,-1:3,74,182,74:2,-1:4,182:6,64,-1:2,74,-1:6,182:5,-1:3,182:4,230,182:8,-1:2,182:10,-1:3,74,182,74:2");

   XPathLexer(Reader reader) {
      this();
      if (null == reader) {
         throw new Error("Error: Bad input stream initializer.");
      } else {
         this.yy_reader = new BufferedReader(reader);
      }
   }

   XPathLexer(InputStream instream) {
      this();
      if (null == instream) {
         throw new Error("Error: Bad input stream initializer.");
      } else {
         this.yy_reader = new BufferedReader(new InputStreamReader(instream));
      }
   }

   private XPathLexer() {
      this.YY_BUFFER_SIZE = 512;
      this.YY_F = -1;
      this.YY_NO_STATE = -1;
      this.YY_NOT_ACCEPT = 0;
      this.YY_START = 1;
      this.YY_END = 2;
      this.YY_NO_ANCHOR = 4;
      this.YY_BOL = 65536;
      this.YY_EOF = 65537;
      this.YYEOF = -1;
      this.yy_eof_done = false;
      this.YYINITIAL = 0;
      this.yy_state_dtrans = new int[]{0};
      this.yy_last_was_cr = false;
      this.YY_E_INTERNAL = 0;
      this.YY_E_MATCH = 1;
      this.yy_error_string = new String[]{"Error: Internal error.\n", "Error: Unmatched input.\n"};
      this.yy_acpt = new int[]{0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 0, 4, 0, 4, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
      this.yy_buffer = new char[512];
      this.yy_buffer_read = 0;
      this.yy_buffer_index = 0;
      this.yy_buffer_start = 0;
      this.yy_buffer_end = 0;
      this.yy_at_bol = true;
      this.yy_lexical_state = 0;
   }

   private void yybegin(int state) {
      this.yy_lexical_state = state;
   }

   private int yy_advance() throws IOException {
      if (this.yy_buffer_index < this.yy_buffer_read) {
         return this.yy_buffer[this.yy_buffer_index++];
      } else {
         int next_read;
         if (0 != this.yy_buffer_start) {
            int i = this.yy_buffer_start;

            int j;
            for(j = 0; i < this.yy_buffer_read; ++j) {
               this.yy_buffer[j] = this.yy_buffer[i];
               ++i;
            }

            this.yy_buffer_end -= this.yy_buffer_start;
            this.yy_buffer_start = 0;
            this.yy_buffer_read = j;
            this.yy_buffer_index = j;
            next_read = this.yy_reader.read(this.yy_buffer, this.yy_buffer_read, this.yy_buffer.length - this.yy_buffer_read);
            if (-1 == next_read) {
               return 65537;
            }

            this.yy_buffer_read += next_read;
         }

         while(this.yy_buffer_index >= this.yy_buffer_read) {
            if (this.yy_buffer_index >= this.yy_buffer.length) {
               this.yy_buffer = this.yy_double(this.yy_buffer);
            }

            next_read = this.yy_reader.read(this.yy_buffer, this.yy_buffer_read, this.yy_buffer.length - this.yy_buffer_read);
            if (-1 == next_read) {
               return 65537;
            }

            this.yy_buffer_read += next_read;
         }

         return this.yy_buffer[this.yy_buffer_index++];
      }
   }

   private void yy_move_end() {
      if (this.yy_buffer_end > this.yy_buffer_start && '\n' == this.yy_buffer[this.yy_buffer_end - 1]) {
         --this.yy_buffer_end;
      }

      if (this.yy_buffer_end > this.yy_buffer_start && '\r' == this.yy_buffer[this.yy_buffer_end - 1]) {
         --this.yy_buffer_end;
      }

   }

   private void yy_mark_start() {
      this.yy_buffer_start = this.yy_buffer_index;
   }

   private void yy_mark_end() {
      this.yy_buffer_end = this.yy_buffer_index;
   }

   private void yy_to_mark() {
      this.yy_buffer_index = this.yy_buffer_end;
      this.yy_at_bol = this.yy_buffer_end > this.yy_buffer_start && ('\r' == this.yy_buffer[this.yy_buffer_end - 1] || '\n' == this.yy_buffer[this.yy_buffer_end - 1] || 2028 == this.yy_buffer[this.yy_buffer_end - 1] || 2029 == this.yy_buffer[this.yy_buffer_end - 1]);
   }

   private String yytext() {
      return new String(this.yy_buffer, this.yy_buffer_start, this.yy_buffer_end - this.yy_buffer_start);
   }

   private int yylength() {
      return this.yy_buffer_end - this.yy_buffer_start;
   }

   private char[] yy_double(char[] buf) {
      char[] newbuf = new char[2 * buf.length];

      for(int i = 0; i < buf.length; ++i) {
         newbuf[i] = buf[i];
      }

      return newbuf;
   }

   private void yy_error(int code, boolean fatal) {
      System.out.print(this.yy_error_string[code]);
      System.out.flush();
      if (fatal) {
         throw new Error("Fatal Error.\n");
      }
   }

   private static int[][] unpackFromString(int size1, int size2, String st) {
      int colonIndex = true;
      int sequenceLength = 0;
      int sequenceInteger = 0;
      int[][] res = new int[size1][size2];

      for(int i = 0; i < size1; ++i) {
         for(int j = 0; j < size2; ++j) {
            if (sequenceLength != 0) {
               res[i][j] = sequenceInteger;
               --sequenceLength;
            } else {
               int commaIndex = st.indexOf(44);
               String workString = commaIndex == -1 ? st : st.substring(0, commaIndex);
               st = st.substring(commaIndex + 1);
               int colonIndex = workString.indexOf(58);
               if (colonIndex == -1) {
                  res[i][j] = Integer.parseInt(workString);
               } else {
                  String lengthString = workString.substring(colonIndex + 1);
                  sequenceLength = Integer.parseInt(lengthString);
                  workString = workString.substring(0, colonIndex);
                  sequenceInteger = Integer.parseInt(workString);
                  res[i][j] = sequenceInteger;
                  --sequenceLength;
               }
            }
         }
      }

      return res;
   }

   public Symbol next_token() throws IOException, Exception {
      int yy_anchor = true;
      int yy_state = this.yy_state_dtrans[this.yy_lexical_state];
      int yy_next_state = true;
      int yy_last_accept_state = -1;
      boolean yy_initial = true;
      this.yy_mark_start();
      int yy_this_accept = this.yy_acpt[yy_state];
      if (0 != yy_this_accept) {
         yy_last_accept_state = yy_state;
         this.yy_mark_end();
      }

      while(true) {
         int yy_lookahead;
         if (yy_initial && this.yy_at_bol) {
            yy_lookahead = 65536;
         } else {
            yy_lookahead = this.yy_advance();
         }

         yy_next_state = true;
         int yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
         if (65537 == yy_lookahead && yy_initial) {
            return new Symbol(0);
         }

         if (-1 != yy_next_state) {
            yy_state = yy_next_state;
            yy_initial = false;
            yy_this_accept = this.yy_acpt[yy_next_state];
            if (0 != yy_this_accept) {
               yy_last_accept_state = yy_next_state;
               this.yy_mark_end();
            }
         } else {
            if (-1 == yy_last_accept_state) {
               throw new Error("Lexical Error: Unmatched Input.");
            }

            int yy_anchor = this.yy_acpt[yy_last_accept_state];
            if (0 != (2 & yy_anchor)) {
               this.yy_move_end();
            }

            this.yy_to_mark();
            switch(yy_last_accept_state) {
            case -199:
            case -198:
            case -197:
            case -196:
            case -195:
            case -194:
            case -193:
            case -192:
            case -191:
            case -190:
            case -189:
            case -188:
            case -187:
            case -186:
            case -185:
            case -184:
            case -183:
            case -182:
            case -181:
            case -180:
            case -179:
            case -178:
            case -177:
            case -176:
            case -175:
            case -174:
            case -173:
            case -172:
            case -171:
            case -170:
            case -169:
            case -168:
            case -167:
            case -166:
            case -165:
            case -164:
            case -163:
            case -162:
            case -161:
            case -160:
            case -159:
            case -158:
            case -157:
            case -156:
            case -155:
            case -154:
            case -153:
            case -152:
            case -151:
            case -150:
            case -149:
            case -148:
            case -147:
            case -146:
            case -145:
            case -144:
            case -143:
            case -142:
            case -141:
            case -140:
            case -139:
            case -138:
            case -137:
            case -136:
            case -135:
            case -134:
            case -133:
            case -132:
            case -131:
            case -130:
            case -129:
            case -128:
            case -127:
            case -126:
            case -125:
            case -124:
            case -123:
            case -122:
            case -121:
            case -120:
            case -119:
            case -118:
            case -117:
            case -116:
            case -115:
            case -114:
            case -113:
            case -112:
            case -111:
            case -110:
            case -109:
            case -108:
            case -107:
            case -106:
            case -105:
            case -104:
            case -103:
            case -102:
            case -101:
            case -100:
            case -99:
            case -98:
            case -97:
            case -96:
            case -95:
            case -94:
            case -93:
            case -92:
            case -91:
            case -90:
            case -89:
            case -88:
            case -87:
            case -86:
            case -85:
            case -84:
            case -83:
            case -82:
            case -81:
            case -80:
            case -79:
            case -78:
            case -77:
            case -76:
            case -75:
            case -74:
            case -73:
            case -72:
            case -71:
            case -70:
            case -69:
            case -68:
            case -67:
            case -66:
            case -65:
            case -64:
            case -63:
            case -62:
            case -61:
            case -60:
            case -59:
            case -58:
            case -57:
            case -56:
            case -55:
            case -54:
            case -53:
            case -52:
            case -51:
            case -50:
            case -49:
            case -48:
            case -47:
            case -46:
            case -45:
            case -44:
            case -43:
            case -42:
            case -41:
            case -40:
            case -39:
            case -38:
            case -37:
            case -36:
            case -35:
            case -34:
            case -33:
            case -32:
            case -31:
            case -30:
            case -29:
            case -28:
            case -27:
            case -26:
            case -25:
            case -24:
            case -23:
            case -22:
            case -21:
            case -20:
            case -19:
            case -18:
            case -17:
            case -16:
            case -15:
            case -14:
            case -13:
            case -12:
            case -11:
            case -10:
            case -9:
            case -8:
            case -7:
            case -6:
            case -5:
            case -4:
            case -3:
            case -2:
            case -1:
            case 1:
            case 18:
               break;
            case 0:
            case 64:
            case 67:
            case 70:
            case 73:
            case 76:
            case 78:
            case 80:
            case 82:
            case 84:
            case 86:
            case 88:
            case 90:
            case 92:
            case 94:
            case 96:
            case 98:
            case 100:
            case 102:
            case 104:
            case 106:
            case 108:
            case 110:
            case 112:
            case 114:
            case 116:
            case 118:
            case 120:
            case 122:
            case 124:
            case 183:
            case 184:
            case 186:
            case 188:
            default:
               this.yy_error(0, false);
               break;
            case 2:
               return new Symbol(9);
            case 3:
               return new Symbol(2);
            case 4:
               return new Symbol(22);
            case 5:
               return new Symbol(23);
            case 6:
               return new Symbol(27, this.yytext());
            case 7:
               throw new Exception(this.yytext());
            case 8:
               return new Symbol(10);
            case 9:
               return new Symbol(12);
            case 10:
               return new Symbol(3);
            case 11:
               return new Symbol(6);
            case 12:
               return new Symbol(11);
            case 13:
               return new Symbol(16);
            case 14:
               return new Symbol(18);
            case 15:
               return new Symbol(19);
            case 16:
               return new Symbol(7);
            case 17:
               return new Symbol(8);
            case 19:
               return new Symbol(4);
            case 20:
               return new Symbol(5);
            case 21:
               return new Symbol(51, new Long(this.yytext()));
            case 22:
               return new Symbol(15);
            case 23:
               return new Symbol(28);
            case 24:
               return new Symbol(32);
            case 25:
               return new Symbol(14);
            case 26:
               return new Symbol(27, this.yytext());
            case 27:
               return new Symbol(13);
            case 28:
               return new Symbol(50, new Double(this.yytext()));
            case 29:
               return new Symbol(17);
            case 30:
               return new Symbol(20);
            case 31:
               return new Symbol(21);
            case 32:
               return new Symbol(26, this.yytext().substring(1, this.yytext().length() - 1));
            case 33:
               return new Symbol(26, this.yytext().substring(1, this.yytext().length() - 1));
            case 34:
               return new Symbol(50, new Double(this.yytext()));
            case 35:
               return new Symbol(24);
            case 36:
               return new Symbol(27, this.yytext());
            case 37:
               return new Symbol(25);
            case 38:
               return new Symbol(29);
            case 39:
               return new Symbol(33);
            case 40:
               return new Symbol(38);
            case 41:
               return new Symbol(40);
            case 42:
               return new Symbol(30);
            case 43:
               return new Symbol(31);
            case 44:
               return new Symbol(39);
            case 45:
               return new Symbol(30);
            case 46:
               return new Symbol(31);
            case 47:
               return new Symbol(42);
            case 48:
               return new Symbol(52);
            case 49:
               return new Symbol(48);
            case 50:
               return new Symbol(34);
            case 51:
               return new Symbol(49);
            case 52:
               return new Symbol(41);
            case 53:
               return new Symbol(46);
            case 54:
               return new Symbol(44);
            case 55:
               return new Symbol(34);
            case 56:
               return new Symbol(53);
            case 57:
               return new Symbol(43);
            case 58:
               return new Symbol(37);
            case 59:
               return new Symbol(47);
            case 60:
               return new Symbol(45);
            case 61:
               return new Symbol(36);
            case 62:
               return new Symbol(35);
            case 63:
               return new Symbol(35);
            case 65:
               return new Symbol(27, this.yytext());
            case 66:
               throw new Exception(this.yytext());
            case 68:
               return new Symbol(27, this.yytext());
            case 69:
               throw new Exception(this.yytext());
            case 71:
               return new Symbol(27, this.yytext());
            case 72:
               throw new Exception(this.yytext());
            case 74:
               return new Symbol(27, this.yytext());
            case 75:
               throw new Exception(this.yytext());
            case 77:
               return new Symbol(27, this.yytext());
            case 79:
               return new Symbol(27, this.yytext());
            case 81:
               return new Symbol(27, this.yytext());
            case 83:
               return new Symbol(27, this.yytext());
            case 85:
               return new Symbol(27, this.yytext());
            case 87:
               return new Symbol(27, this.yytext());
            case 89:
               return new Symbol(27, this.yytext());
            case 91:
               return new Symbol(27, this.yytext());
            case 93:
               return new Symbol(27, this.yytext());
            case 95:
               return new Symbol(27, this.yytext());
            case 97:
               return new Symbol(27, this.yytext());
            case 99:
               return new Symbol(27, this.yytext());
            case 101:
               return new Symbol(27, this.yytext());
            case 103:
               return new Symbol(27, this.yytext());
            case 105:
               return new Symbol(27, this.yytext());
            case 107:
               return new Symbol(27, this.yytext());
            case 109:
               return new Symbol(27, this.yytext());
            case 111:
               return new Symbol(27, this.yytext());
            case 113:
               return new Symbol(27, this.yytext());
            case 115:
               return new Symbol(27, this.yytext());
            case 117:
               return new Symbol(27, this.yytext());
            case 119:
               return new Symbol(27, this.yytext());
            case 121:
               return new Symbol(27, this.yytext());
            case 123:
               return new Symbol(27, this.yytext());
            case 125:
               return new Symbol(27, this.yytext());
            case 126:
               return new Symbol(27, this.yytext());
            case 127:
               return new Symbol(27, this.yytext());
            case 128:
               return new Symbol(27, this.yytext());
            case 129:
               return new Symbol(27, this.yytext());
            case 130:
               return new Symbol(27, this.yytext());
            case 131:
               return new Symbol(27, this.yytext());
            case 132:
               return new Symbol(27, this.yytext());
            case 133:
               return new Symbol(27, this.yytext());
            case 134:
               return new Symbol(27, this.yytext());
            case 135:
               return new Symbol(27, this.yytext());
            case 136:
               return new Symbol(27, this.yytext());
            case 137:
               return new Symbol(27, this.yytext());
            case 138:
               return new Symbol(27, this.yytext());
            case 139:
               return new Symbol(27, this.yytext());
            case 140:
               return new Symbol(27, this.yytext());
            case 141:
               return new Symbol(27, this.yytext());
            case 142:
               return new Symbol(27, this.yytext());
            case 143:
               return new Symbol(27, this.yytext());
            case 144:
               return new Symbol(27, this.yytext());
            case 145:
               return new Symbol(27, this.yytext());
            case 146:
               return new Symbol(27, this.yytext());
            case 147:
               return new Symbol(27, this.yytext());
            case 148:
               return new Symbol(27, this.yytext());
            case 149:
               return new Symbol(27, this.yytext());
            case 150:
               return new Symbol(27, this.yytext());
            case 151:
               return new Symbol(27, this.yytext());
            case 152:
               return new Symbol(27, this.yytext());
            case 153:
               return new Symbol(27, this.yytext());
            case 154:
               return new Symbol(27, this.yytext());
            case 155:
               return new Symbol(27, this.yytext());
            case 156:
               return new Symbol(27, this.yytext());
            case 157:
               return new Symbol(27, this.yytext());
            case 158:
               return new Symbol(27, this.yytext());
            case 159:
               return new Symbol(27, this.yytext());
            case 160:
               return new Symbol(27, this.yytext());
            case 161:
               return new Symbol(27, this.yytext());
            case 162:
               return new Symbol(27, this.yytext());
            case 163:
               return new Symbol(27, this.yytext());
            case 164:
               return new Symbol(27, this.yytext());
            case 165:
               return new Symbol(27, this.yytext());
            case 166:
               return new Symbol(27, this.yytext());
            case 167:
               return new Symbol(27, this.yytext());
            case 168:
               return new Symbol(27, this.yytext());
            case 169:
               return new Symbol(27, this.yytext());
            case 170:
               return new Symbol(27, this.yytext());
            case 171:
               return new Symbol(27, this.yytext());
            case 172:
               return new Symbol(27, this.yytext());
            case 173:
               return new Symbol(27, this.yytext());
            case 174:
               return new Symbol(27, this.yytext());
            case 175:
               return new Symbol(27, this.yytext());
            case 176:
               return new Symbol(27, this.yytext());
            case 177:
               return new Symbol(27, this.yytext());
            case 178:
               return new Symbol(27, this.yytext());
            case 179:
               return new Symbol(27, this.yytext());
            case 180:
               return new Symbol(27, this.yytext());
            case 181:
               return new Symbol(27, this.yytext());
            case 182:
               return new Symbol(27, this.yytext());
            case 185:
               return new Symbol(27, this.yytext());
            case 187:
               return new Symbol(27, this.yytext());
            case 189:
               return new Symbol(27, this.yytext());
            case 190:
               return new Symbol(27, this.yytext());
            case 191:
               return new Symbol(27, this.yytext());
            case 192:
               return new Symbol(27, this.yytext());
            case 193:
               return new Symbol(27, this.yytext());
            case 194:
               return new Symbol(27, this.yytext());
            case 195:
               return new Symbol(27, this.yytext());
            case 196:
               return new Symbol(27, this.yytext());
            case 197:
               return new Symbol(27, this.yytext());
            case 198:
               return new Symbol(27, this.yytext());
            case 199:
               return new Symbol(27, this.yytext());
            case 200:
               return new Symbol(27, this.yytext());
            case 201:
               return new Symbol(27, this.yytext());
            case 202:
               return new Symbol(27, this.yytext());
            case 203:
               return new Symbol(27, this.yytext());
            case 204:
               return new Symbol(27, this.yytext());
            case 205:
               return new Symbol(27, this.yytext());
            case 206:
               return new Symbol(27, this.yytext());
            case 207:
               return new Symbol(27, this.yytext());
            case 208:
               return new Symbol(27, this.yytext());
            case 209:
               return new Symbol(27, this.yytext());
            case 210:
               return new Symbol(27, this.yytext());
            case 211:
               return new Symbol(27, this.yytext());
            case 212:
               return new Symbol(27, this.yytext());
            case 213:
               return new Symbol(27, this.yytext());
            case 214:
               return new Symbol(27, this.yytext());
            case 215:
               return new Symbol(27, this.yytext());
            case 216:
               return new Symbol(27, this.yytext());
            case 217:
               return new Symbol(27, this.yytext());
            case 218:
               return new Symbol(27, this.yytext());
            case 219:
               return new Symbol(27, this.yytext());
            case 220:
               return new Symbol(27, this.yytext());
            case 221:
               return new Symbol(27, this.yytext());
            case 222:
               return new Symbol(27, this.yytext());
            case 223:
               return new Symbol(27, this.yytext());
            case 224:
               return new Symbol(27, this.yytext());
            case 225:
               return new Symbol(27, this.yytext());
            case 226:
               return new Symbol(27, this.yytext());
            case 227:
               return new Symbol(27, this.yytext());
            case 228:
               return new Symbol(27, this.yytext());
            case 229:
               return new Symbol(27, this.yytext());
            case 230:
               return new Symbol(27, this.yytext());
            case 231:
               return new Symbol(27, this.yytext());
            }

            yy_initial = true;
            yy_state = this.yy_state_dtrans[this.yy_lexical_state];
            yy_next_state = true;
            yy_last_accept_state = -1;
            this.yy_mark_start();
            yy_this_accept = this.yy_acpt[yy_state];
            if (0 != yy_this_accept) {
               yy_last_accept_state = yy_state;
               this.yy_mark_end();
            }
         }
      }
   }
}
