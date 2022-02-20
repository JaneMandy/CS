package org.apache.xml.dtm;

public interface Axis {
   int ANCESTOR = 0;
   int ANCESTORORSELF = 1;
   int ATTRIBUTE = 2;
   int CHILD = 3;
   int DESCENDANT = 4;
   int DESCENDANTORSELF = 5;
   int FOLLOWING = 6;
   int FOLLOWINGSIBLING = 7;
   int NAMESPACEDECLS = 8;
   int NAMESPACE = 9;
   int PARENT = 10;
   int PRECEDING = 11;
   int PRECEDINGSIBLING = 12;
   int SELF = 13;
   int ALLFROMNODE = 14;
   int PRECEDINGANDANCESTOR = 15;
   int ALL = 16;
   int DESCENDANTSFROMROOT = 17;
   int DESCENDANTSORSELFFROMROOT = 18;
   int ROOT = 19;
   int FILTEREDLIST = 20;
   String[] names = new String[]{"ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace-decls", "namespace", "parent", "preceding", "preceding-sibling", "self", "all-from-node", "preceding-and-ancestor", "all", "descendants-from-root", "descendants-or-self-from-root", "root", "filtered-list"};
}
