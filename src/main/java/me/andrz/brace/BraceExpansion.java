package me.andrz.brace;

import me.andrz.brace.antlr.BraceExpansionLexer;
import me.andrz.brace.antlr.BraceExpansionParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 *
 */
public class BraceExpansion {

    public static List<String> expand(String s) {
        BraceExpansionLexer lexer = new BraceExpansionLexer(
                new ANTLRInputStream(s)
        );

        // Get a list of matched tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Pass the tokens to the parser
        BraceExpansionParser parser = new BraceExpansionParser(tokens);

        // Specify our entry point
        BraceExpansionParser.RootContext context = parser.root();

        System.out.println("TREE: " + context.toStringTree(parser));

        // Walk it and attach our listener
        ParseTreeWalker walker = new ParseTreeWalker();
        Value<List<String>> value = new Value<List<String>>();
        ParseTreeListener listener = new AntlrBraceListener(value);
        walker.walk(listener, context);
        List<String> strings = value.getValue();
        return strings;
    }

}
