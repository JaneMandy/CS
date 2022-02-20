package org.apache.xalan.xsltc.compiler.util;

import java.util.ListResourceBundle;

public class ErrorMessages_hu extends ListResourceBundle {
   private static final String[][] m_errorMessages = new String[][]{{"MULTIPLE_STYLESHEET_ERR", "Egynél több stíluslap van definiálva ugyanabban a fájlban."}, {"TEMPLATE_REDEF_ERR", "A(z) ''{0}'' sablon már definiált ebben a stíluslapban."}, {"TEMPLATE_UNDEF_ERR", "A(z) ''{0}'' sablon nem definiált ebben a stíluslapban."}, {"VARIABLE_REDEF_ERR", "A(z) ''{0}'' változó többször definiált ugyanabban a hatókörben."}, {"VARIABLE_UNDEF_ERR", "A(z) ''{0}'' változó vagy paraméter nem definiált."}, {"CLASS_NOT_FOUND_ERR", "Nem található a(z) ''{0}'' osztály."}, {"METHOD_NOT_FOUND_ERR", "Nem található a(z) ''{0}'' külső metódus (public-nak kellene lenni)."}, {"ARGUMENT_CONVERSION_ERR", "Nem lehet konvertálni az argumentum/visszatérési kód típusát a(z) ''{0}'' metódus hívásában."}, {"FILE_NOT_FOUND_ERR", "A(z) ''{0}'' fájl vagy URI nem található."}, {"INVALID_URI_ERR", "Érvénytelen URI: ''{0}''."}, {"FILE_ACCESS_ERR", "Nem lehet megnyitni a(z) ''{0}'' fájlt vagy URI-t."}, {"MISSING_ROOT_ERR", "Hiányzik az <xsl:stylesheet> vagy <xsl:transform> elem."}, {"NAMESPACE_UNDEF_ERR", "A(z) ''{0}'' névtér-prefix nincs deklarálva."}, {"FUNCTION_RESOLVE_ERR", "Nem lehet feloldani a(z) ''{0}'' függvény hívását."}, {"NEED_LITERAL_ERR", "A(z) ''{0}'' argumentuma egy literál kell legyen."}, {"XPATH_PARSER_ERR", "Hiba történt a(z) ''{0}'' XPath kifejezés elemzésekor."}, {"REQUIRED_ATTR_ERR", "Hiányzik a(z) ''{0}'' kötelező attribútum."}, {"ILLEGAL_CHAR_ERR", "Nem megengedett karakter (''{0}'') szerepel az XPath kifejezésben."}, {"ILLEGAL_PI_ERR", "Nem megengedett név (''{0}'') szerepelt a feldolgozási utasításokban."}, {"STRAY_ATTRIBUTE_ERR", "A(z) ''{0}'' attribútum kívül esik az elemen."}, {"ILLEGAL_ATTRIBUTE_ERR", "Nem megengedett attribútum: ''{0}''."}, {"CIRCULAR_INCLUDE_ERR", "Körkörös import/include. A(z) ''{0}'' stíluslap már be van töltve."}, {"RESULT_TREE_SORT_ERR", "Az eredményfa-részleteket nem lehet rendezni (az <xsl:sort> elemek figyelmen kívül maradnak). Rendeznie kell a node-okat, amikor eredményfát hoz létre."}, {"SYMBOLS_REDEF_ERR", "Már definiálva van a(z) ''{0}'' decimális formázás."}, {"XSL_VERSION_ERR", "Az XSLTC nem támogatja a(z) ''{0}'' XSL verziót."}, {"CIRCULAR_VARIABLE_ERR", "Körkörös változó/paraméter-hivatkozás; helye: ''{0}''."}, {"ILLEGAL_BINARY_OP_ERR", "Ismeretlen operátort használt a bináris kifejezésben."}, {"ILLEGAL_ARG_ERR", "Nem megengedett argumentumo(ka)t használt a függvényhívásban."}, {"DOCUMENT_ARG_ERR", "A document() függvény második argumentuma egy node-készlet kell legyen."}, {"MISSING_WHEN_ERR", "Legalább egy <xsl:when> elem szükséges az <xsl:choose>-ban."}, {"MULTIPLE_OTHERWISE_ERR", "Csak egy <xsl:otherwise> elem megengedett <xsl:choose>-ban."}, {"STRAY_OTHERWISE_ERR", "Az <xsl:otherwise> csak <xsl:choose>-on belül használható."}, {"STRAY_WHEN_ERR", "Az <xsl:when> csak <xsl:choose>-on belül használható."}, {"WHEN_ELEMENT_ERR", "Csak <xsl:when> és <xsl:otherwise> elemek megengedettek az <xsl:choose>-ban."}, {"UNNAMED_ATTRIBSET_ERR", "Hiányzik az <xsl:attribute-set>-ből a 'name' attribútum."}, {"ILLEGAL_CHILD_ERR", "Nem megengedett gyermek elem."}, {"ILLEGAL_ELEM_NAME_ERR", "Nem hívhat ''{0}''-nek elemet."}, {"ILLEGAL_ATTR_NAME_ERR", "Nem hívhat ''{0}''-nek attribútumot."}, {"ILLEGAL_TEXT_NODE_ERR", "Szövegadat szerepel a felső szintű <xsl:stylesheet> elemen kívül."}, {"SAX_PARSER_CONFIG_ERR", "Nincs megfelelően konfigurálva a JAXP elemző."}, {"INTERNAL_ERR", "Helyrehozhatatlan XSLTC-belső hiba történt: ''{0}''"}, {"UNSUPPORTED_XSL_ERR", "Nem támogatott XSL elem: ''{0}''."}, {"UNSUPPORTED_EXT_ERR", "Ismeretlen XSLTC kiterjesztés: ''{0}''."}, {"MISSING_XSLT_URI_ERR", "A bemenő dokumentum nem stíluslap (az XSL névtér nincs deklarálva a root elemben)."}, {"MISSING_XSLT_TARGET_ERR", "Nem található a(z) ''{0}'' stíluslap-célban."}, {"NOT_IMPLEMENTED_ERR", "Nincs megvalósítva: ''{0}''."}, {"NOT_STYLESHEET_ERR", "A bemenő dokumentum nem tartalmaz XSL stíluslapot."}, {"ELEMENT_PARSE_ERR", "Nem lehet elemezni a(z) ''{0}'' elemet."}, {"KEY_USE_ATTR_ERR", "A(z) <key> attribútuma node, node-készlet, szöveg vagy szám lehet."}, {"OUTPUT_VERSION_ERR", "A kimenő XML dokumentum-verzió 1.0 kell legyen."}, {"ILLEGAL_RELAT_OP_ERR", "Ismeretlen operátort használt a relációs kifejezésben."}, {"ATTRIBSET_UNDEF_ERR", "Nemlétező attribútumkészletet (''{0}'') próbált használni."}, {"ATTR_VAL_TEMPLATE_ERR", "Nem lehet elemezni a(z) ''{0}'' attribútumérték-sablont."}, {"UNKNOWN_SIG_TYPE_ERR", "Ismeretlen adattípus szerepel a(z) ''{0}'' osztály aláírásában."}, {"DATA_CONVERSION_ERR", "Nem lehet a(z) ''{0}'' adattípust ''{1}'' típusra konvertálni."}, {"NO_TRANSLET_CLASS_ERR", "Ez a sablon nem tartalmaz érvényes translet osztálydefiníciót."}, {"NO_MAIN_TRANSLET_ERR", "Ez a sablon nem tartalmaz ''{0}'' nevű osztályt."}, {"TRANSLET_CLASS_ERR", "Nem lehet betölteni a(z) ''{0}'' translet osztályt."}, {"TRANSLET_OBJECT_ERR", "A translet osztály betöltődött, de nem sikerült létrehozni a translet példányt."}, {"ERROR_LISTENER_NULL_ERR", "Megpróbálta null-ra állítani ''{0}'' ErrorListener objektumát."}, {"JAXP_UNKNOWN_SOURCE_ERR", "Az XSLTC csak a StreamSource, SAXSource és DOMSource interfészeket támogatja."}, {"JAXP_NO_SOURCE_ERR", "A(z) ''{0}'' metódusnak átadott source objektum nem tartalmaz semmit."}, {"JAXP_COMPILE_ERR", "Nem sikerült lefordítani a stíluslapot."}, {"JAXP_INVALID_ATTR_ERR", "A TransformerFactory objektum nem ismer ''{0}'' attribútumot."}, {"JAXP_SET_RESULT_ERR", "A setResult() metódust a startDocument() hívása előtt kell meghívni."}, {"JAXP_NO_TRANSLET_ERR", "A transformer interfész nem tartalmaz beágyazott translet objektumot."}, {"JAXP_NO_HANDLER_ERR", "Nincs definiálva kimenetkezelő az átalakítás eredményéhez."}, {"JAXP_NO_RESULT_ERR", "A(z) ''{0}'' metódusnak átadott result objektum érvénytelen."}, {"JAXP_UNKNOWN_PROP_ERR", "Érvénytelen Transformer tulajdonságot (''{0}'') próbált meg elérni."}, {"SAX2DOM_ADAPTER_ERR", "Nem lehet létrehozni a SAX2DOM adaptert: ''{0}''."}, {"XSLTC_SOURCE_ERR", "XSLTCSource.build() hívása systemId beállítása nélkül történt."}, {"COMPILE_STDIN_ERR", "A -i kapcsolót a -o kapcsolóval együtt kell használni."}, {"COMPILE_USAGE_STR", "Használat:\n   java org.apache.xalan.xsltc.cmdline.Compile [-o <kimenet>]\n      [-d <alkönyvtár>] [-j <jarfájl>] [-p <csomag>]\n      [-n] [-x] [-s] [-u] [-v] [-h] { <stíluslap> | -i }\n\nOPCIÓK\n   -o <kimenet>    összerendeli a <kimenetet> a létrehozott\n                  translet-tel. Alapértelmezés szerint a translet neve\n                  a <stíluslap> nevéből jön. Ez az opció\n                  figyelmen kívól marad, ha több stíluslapot fordít.\n   -d <alkönyvtár> meghatározza a translet cél-alkönyvtárát\n   -j <jarfájl>   a translet osztályokat egy jar fájlba csomagolja,\n                  aminek a nevét a <jarfájl> attribútum adja meg\n   -p <csomag>    meghatározza az összes generált translet osztály\n                  prefixnevét.\n   -n             engedélyezi a template inlining optimalizálást\n                  (az alapértelmezett viselkedés általában jobb).\n   -x             bekapcsolja a további hibakeresési üzenet-kimenetet\n   -s             letiltja a System.exit hívását\n   -u             a <stíluslap> argumentumokat URL-ként értelmezi\n   -i             kényszeríti a fordítót, hogy a stíluslapokat az stdin-ről olvassa\n   -v             kiírja a fordító  verzióját\n   -h             kiírja ezt a használati üzenetet\n"}, {"TRANSFORM_USAGE_STR", "Használat \n   java org.apache.xalan.xsltc.cmdline.Transform [-j <jarfájl>]\n      [-x] [-s] [-n <iterációk>] {-u <dokumentum_url> | <dokumentum>}\n      <osztály> [<param1>=<érték1> ...]\n\n   a translet <osztályt> használja a <dokumentum> \n   attribútumban megadott XML dokumentum fordítására. A translet <oszály> vagy a\n   felhasználó CLASSPATH változója alapján vagy a megadott <jarfájl>-ban található meg.\nOpciók:\n   -j <jarfájl>      megadja azt a jarfájlt, amiből a translet-et be kell tölteni\n   -x                bekapcsolja a további hibakeresési üzeneteket\n   -s                letiltja a System.exit hívását\n   -n <iterációk>    az átalakítást <iterációk> alkalommal végzi el\n                     és megjeleníti a  teljesítmény-információkat\n   -u <dokumentum_url> a bemeneti XML dokumentumot URL-ként adja meg\n"}, {"STRAY_SORT_ERR", "Az <xsl:sort> csak <xsl:for-each>-en vagy <xsl:apply-templates>-en belül használható."}, {"UNSUPPORTED_ENCODING", "A(z) ''{0}'' kimeneti kódolást nem támogatja ez a JVM."}, {"SYNTAX_ERR", "Szintaktikai hiba történt ''{0}''-ben."}, {"CONSTRUCTOR_NOT_FOUND", "Nem található a(z) ''{0}'' külső konstruktor."}, {"NO_JAVA_FUNCT_THIS_REF", "A(z ''{0}'' nem statikus Jáva függvény első argumentuma nem egy érvényes objektum-hivatkozás."}, {"TYPE_CHECK_ERR", "Hiba történt a(z) ''{0}'' kifejezés típusának ellenőrzésekor."}, {"TYPE_CHECK_UNK_LOC_ERR", "Hiba történt egy ismeretlen helyen lévő kifejezés típusának ellenőrzésekor."}, {"ILLEGAL_CMDLINE_OPTION_ERR", "A(z) ''{0}'' parancssori opció érvénytelen."}, {"CMDLINE_OPT_MISSING_ARG_ERR", "A(z) ''{0}'' parancssori opcióhoz hiányzik egy kötelező argumentum."}, {"WARNING_PLUS_WRAPPED_MSG", "FIGYELEM:  ''{0}''\n       :{1}"}, {"WARNING_MSG", "FIGYELEM:  ''{0}''"}, {"FATAL_ERR_PLUS_WRAPPED_MSG", "VÉGZETES HIBA:  ''{0}''\n           :{1}"}, {"FATAL_ERR_MSG", "VÉGZETES HIBA:  ''{0}''"}, {"ERROR_PLUS_WRAPPED_MSG", "HIBA:  ''{0}''\n     :{1}"}, {"ERROR_MSG", "HIBA:  ''{0}''"}, {"TRANSFORM_WITH_TRANSLET_STR", "Átalakítás a(z) ''{0}'' translet segítségével. "}, {"TRANSFORM_WITH_JAR_STR", "Átalakítás a(z) ''{1}'' jar fájlból a(z) ''{0}'' translet segítségével."}, {"COULD_NOT_CREATE_TRANS_FACT", "Nem lehet létrehozni a(z) ''{0}'' TransformerFactory osztály példányát."}, {"COMPILER_ERROR_KEY", "Fordítás hibák:"}, {"COMPILER_WARNING_KEY", "Fordítási figyelmeztetések:"}, {"RUNTIME_ERROR_KEY", "Translet hibák:"}};

   public Object[][] getContents() {
      return m_errorMessages;
   }
}
