package org.apache.xalan.xsltc.compiler.util;

import java.util.ListResourceBundle;

public class ErrorMessages_pt_BR extends ListResourceBundle {
   public Object[][] getContents() {
      return new Object[][]{{"MULTIPLE_STYLESHEET_ERR", "Mais de uma página de estilo definida no mesmo arquivo. "}, {"TEMPLATE_REDEF_ERR", "O gabarito ''{0}'' já está definido nesta página de estilo."}, {"TEMPLATE_UNDEF_ERR", "O gabarito ''{0}'' não está definido nesta página de estilo. "}, {"VARIABLE_REDEF_ERR", "A variável ''{0}'' tem sua multiplicação definida no mesmo escopo. "}, {"VARIABLE_UNDEF_ERR", "A variável ou parâmetro ''{0}'' não está definido. "}, {"CLASS_NOT_FOUND_ERR", "Impossível localizar a classe ''{0}''."}, {"METHOD_NOT_FOUND_ERR", "Impossível localizar método externo ''{0}'' (deve ser público)."}, {"ARGUMENT_CONVERSION_ERR", "Impossível converter tipo de argumento/retorno na chamada do método para o método ''{0}''"}, {"FILE_NOT_FOUND_ERR", "Arquivo ou URI ''{0}'' não encontrado."}, {"INVALID_URI_ERR", "URI inválido ''{0}''."}, {"FILE_ACCESS_ERR", "Impossível abrir arquivo ou URI ''{0}''."}, {"MISSING_ROOT_ERR", "Esperado elemento <xsl:stylesheet> ou <xsl:transform>."}, {"NAMESPACE_UNDEF_ERR", "Prefixo de namespace ''{0}'' não foi declarado."}, {"FUNCTION_RESOLVE_ERR", "Impossível resolver chamada para a função ''{0}''."}, {"NEED_LITERAL_ERR", "O argumento para ''{0}'' deve ser uma cadeia literal."}, {"XPATH_PARSER_ERR", "Erro ao analisar a expressão XPath ''{0}''."}, {"REQUIRED_ATTR_ERR", "O atributo requerido ''{0}'' está ausente. "}, {"ILLEGAL_CHAR_ERR", "Caractere inválido ''{0}'' na expressão XPath. "}, {"ILLEGAL_PI_ERR", "Atributo inválido ''{0}'' para instrução de processamento. "}, {"STRAY_ATTRIBUTE_ERR", "Atributo ''{0}'' fora do elemento. "}, {"ILLEGAL_ATTRIBUTE_ERR", "Atributo inválido ''{0}''."}, {"CIRCULAR_INCLUDE_ERR", "Import/include circular. A página de estilo ''{0}'' já foi carregada. "}, {"RESULT_TREE_SORT_ERR", "Os fragmentos da árvore de resultados não podem ser ordenados (os elementos <xsl:sort> serão ignorados). Você deve ordenar os nós quando criar a árvore de resultados. "}, {"SYMBOLS_REDEF_ERR", "A formatação decimal ''{0}'' já está definida. "}, {"XSL_VERSION_ERR", "A versão de XSL ''{0}'' não é suportada por XSLTC."}, {"CIRCULAR_VARIABLE_ERR", "Referência à variável/parâmetro circular em ''{0}''."}, {"ILLEGAL_BINARY_OP_ERR", "Operador desconhecido para expressão binária. "}, {"ILLEGAL_ARG_ERR", "Argumentos inválidos para chamada de função. "}, {"DOCUMENT_ARG_ERR", "O segundo argumento para a função document() deve ser um node-set."}, {"MISSING_WHEN_ERR", "Pelo menos um elemento <xsl:when> é requerido em <xsl:choose>."}, {"MULTIPLE_OTHERWISE_ERR", "Apenas um elemento <xsl:otherwise> é permitido em <xsl:choose>."}, {"STRAY_OTHERWISE_ERR", "<xsl:otherwise> somente pode ser utilizado em <xsl:choose>."}, {"STRAY_WHEN_ERR", "<xsl:when> somente pode ser utilizado em <xsl:choose>."}, {"WHEN_ELEMENT_ERR", "Apenas os elementos <xsl:when> e <xsl:otherwise> são permitidos em <xsl:choose>."}, {"UNNAMED_ATTRIBSET_ERR", "<xsl:attribute-set> não possui o atributo 'name'."}, {"ILLEGAL_CHILD_ERR", "Elemento filho inválido. "}, {"ILLEGAL_ELEM_NAME_ERR", "Impossível chamar um elemento ''{0}''"}, {"ILLEGAL_ATTR_NAME_ERR", "Impossível chamar um atributo ''{0}''"}, {"ILLEGAL_TEXT_NODE_ERR", "Dados de texto fora do elemento <xsl:stylesheet> de nível superior. "}, {"SAX_PARSER_CONFIG_ERR", "Analisador JAXP não configurado corretamente "}, {"INTERNAL_ERR", "Erro interno de XSLTC irrecuperável: ''{0}''"}, {"UNSUPPORTED_XSL_ERR", "Elemento XSL não suportado ''{0}''."}, {"UNSUPPORTED_EXT_ERR", "Extensão XSLTC não reconhecida ''{0}''."}, {"MISSING_XSLT_URI_ERR", "O documento de entrada não é uma página de estilo (o namespace XSL não está declarado no elemento root)."}, {"MISSING_XSLT_TARGET_ERR", "Não foi possível localizar o destino da página de estilo ''{0}''."}, {"NOT_IMPLEMENTED_ERR", "Não implementado: ''{0}''."}, {"NOT_STYLESHEET_ERR", "O documento de entrada não contém uma página de estilo XSL."}, {"ELEMENT_PARSE_ERR", "Não foi possível analisar o elemento ''{0}''"}, {"KEY_USE_ATTR_ERR", "O atributo use de <key> deve ser node, node-set, string ou number."}, {"OUTPUT_VERSION_ERR", "A versão do documento XML de saída deve ser 1.0"}, {"ILLEGAL_RELAT_OP_ERR", "Operador desconhecido para expressão relacional "}, {"ATTRIBSET_UNDEF_ERR", "Tentando utilizar um conjunto de atributos não existente ''{0}''."}, {"ATTR_VAL_TEMPLATE_ERR", "Impossível analisar gabarito de valor de atributo ''{0}''."}, {"UNKNOWN_SIG_TYPE_ERR", "Tipo de dados desconhecido na assinatura para a classe ''{0}''."}, {"DATA_CONVERSION_ERR", "Impossível converter tipo de dados ''{0}'' em ''{1}''."}, {"NO_TRANSLET_CLASS_ERR", "Este Gabarito não contém uma definição de classe translet válida. "}, {"NO_MAIN_TRANSLET_ERR", "Este Gabarito não contém uma classe com o nome ''{0}''."}, {"TRANSLET_CLASS_ERR", "Não foi possível carregar a classe translet ''{0}''."}, {"TRANSLET_OBJECT_ERR", "Classe translet carregada, mas é impossível criar a instância de translet. "}, {"ERROR_LISTENER_NULL_ERR", "Tentando definir ErrorListener para ''{0}'' como nulo "}, {"JAXP_UNKNOWN_SOURCE_ERR", "Apenas StreamSource, SAXSource e DOMSource são suportados por XSLTC"}, {"JAXP_NO_SOURCE_ERR", "O objeto Source passado para ''{0}'' não possui conteúdo. "}, {"JAXP_COMPILE_ERR", "Não foi possível compilar a página de estilo "}, {"JAXP_INVALID_ATTR_ERR", "TransformerFactory não reconhece o atributo ''{0}''."}, {"JAXP_SET_RESULT_ERR", "setResult() deve ser chamado antes de startDocument()."}, {"JAXP_NO_TRANSLET_ERR", "Transformer não possui nenhum objeto translet encapsulado. "}, {"JAXP_NO_HANDLER_ERR", "Nenhuma rotina de tratamento de saída definida para o resultado de transformação. "}, {"JAXP_NO_RESULT_ERR", "O objeto Result passado para ''{0}'' é inválido. "}, {"JAXP_UNKNOWN_PROP_ERR", "Tentando acessar a propriedade Transformer inválida ''{0}''."}, {"SAX2DOM_ADAPTER_ERR", "Não foi possível criar o adaptador SAX2DOM: ''{0}''."}, {"XSLTC_SOURCE_ERR", "XSLTCSource.build() foi chamado sem systemId estar definido. "}, {"COMPILE_STDIN_ERR", "A opção -i deve ser utilizada com a opção -o."}, {"COMPILE_USAGE_STR", "RESUMO\n   java org.apache.xalan.xsltc.cmdline.Compile [-o <saída>]\n      [-d <diretório>] [-j <arquivojar>] [-p <pacote>]\n      [-n] [-x] [-s] [-u] [-v] [-h] { <folha de estilo> | -i }\n\nOPÇÕES\n   -o <saída> atribui o nome <saída> ao translet\n gerado. Por padrão, o nome do translet\né obtido do nome <folha de estilo>. Esta opção\nserá ignorada se estiverem sendo compiladas várias páginas de estilo.\n   -d <diretório> especifica um diretório de destino para translet\n   -j <arquivo jar>   empacota classes translet em um arquivo jar do\nnome especificado como <arquivo jar>\n   -p <pacote>  especifica um prefixo de nome de pacote para todas as\nclasses translet geradas.\n   -n ativa a seqüência de gabaritos (melhor comportamento padrão\nna média).\n   -x ativa a saída de mensagem de depuração adicional\n  -s desativa a chamada de System.exit\n   -u interpreta argumentos <stylesheet> como URLs\n   -i força o compilador a ler a página de estilo de stdin\n   -v imprime a versão do compilador\n   -h imprime esta instrução de uso\n"}, {"TRANSFORM_USAGE_STR", "RESUMO \n   java org.apache.xalan.xsltc.cmdline.Transform [-j <arquivo jar>]\n      [-x] [-s] [-n <iterações>] {-u <document_url> | <document>}\n      <classe> [<param1>=<valor1> ...]\n\n   utiliza a <classe> translet para transformar um documento XML \n  especificado como <documento>. A <classe> translet no\n  CLASSPATH do usuário ou no <arquivo jar> opcionalmente especificado.\nOPÇÕES\n   -j <arquivo jar> especifica um arquivo jar file a partir do qual será carregado o translet\n   -x ativa a saída de mensagem de depuração adicional\n   -s desativa a chamada de System.exit\n   -n <iterações> executa os horários de transformação <iterações> e\n exibe informações sobre criação de perfil\n   -u <url_de_documento> especifica o documento XML de entrada como uma URL\n"}, {"STRAY_SORT_ERR", "<xsl:sort> somente pode ser utilizado em <xsl:for-each> ou <xsl:apply-templates>."}, {"UNSUPPORTED_ENCODING", "A codificação de saída ''{0}'' não é suportada nesta JVM."}, {"SYNTAX_ERR", "Erro de sintaxe em ''{0}''."}, {"CONSTRUCTOR_NOT_FOUND", "Impossível localizar o construtor externo ''{0}''."}, {"NO_JAVA_FUNCT_THIS_REF", "O primeiro argumento para a função Java não estática ''{0}'' não é uma referência de objeto válida. "}, {"TYPE_CHECK_ERR", "Erro ao verificar o tipo de expressão ''{0}''."}, {"TYPE_CHECK_UNK_LOC_ERR", "Erro ao verificar tipo de expressão em uma localização desconhecida. "}, {"ILLEGAL_CMDLINE_OPTION_ERR", "A opção da linha de comando ''{0}'' não é válida. "}, {"CMDLINE_OPT_MISSING_ARG_ERR", "A opção da linha de comando ''{0}'' não contém um argumento requerido. "}, {"WARNING_PLUS_WRAPPED_MSG", "AVISO:  ''{0}''\n       :{1}"}, {"WARNING_MSG", "AVISO:  ''{0}''"}, {"FATAL_ERR_PLUS_WRAPPED_MSG", "ERRO FATAL:  ''{0}''\n           :{1}"}, {"FATAL_ERR_MSG", "ERRO FATAL:  ''{0}''"}, {"ERROR_PLUS_WRAPPED_MSG", "ERRO:  ''{0}''\n     :{1}"}, {"ERROR_MSG", "ERRO:  ''{0}''"}, {"TRANSFORM_WITH_TRANSLET_STR", "Transformar utilizando translet ''{0}'' "}, {"TRANSFORM_WITH_JAR_STR", "Transformar utilizando translet ''{0}'' de arquivo jar ''{1}''"}, {"COULD_NOT_CREATE_TRANS_FACT", "Não foi possível criar uma instância da classe TransformerFactory ''{0}''."}, {"COMPILER_ERROR_KEY", "Erros do compilador:"}, {"COMPILER_WARNING_KEY", "Avisos do compilador:"}, {"RUNTIME_ERROR_KEY", "Erros de translet:"}};
   }
}
