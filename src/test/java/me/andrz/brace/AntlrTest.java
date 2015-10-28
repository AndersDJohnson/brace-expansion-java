package me.andrz.brace;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import me.andrz.brace.antlr.BraceExpansionLexer;
import me.andrz.brace.antlr.BraceExpansionParser;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by anders on 7/31/15.
 */
public class AntlrTest {

    String[] strings = new String[] {
            "",
            "A",
            "AB",
            "b,B",
            "{A}",
            "{A}{B}",
            "{{}}",
            "{{A}}",
            "{b,B}",
            "a{b,B}",
            "{b,B}c",
            "a{b,B}c",
            "a{1..3}b",
            "a{1..4..2}b",
            "a{4..1..-2}b",
            "a{a..d..2}b",
            "a{d..a..-2}b",
            "a\\{b,c\\}d",
            "a{b\\,c}d",
            "a{b,B}c,d",
            "a{b,B{c,C}D{e,E}F,g}h",
            "x,ya{b,{1\\,2,3}B}c,d",
            "x,ya{bb,AA{1,2,33}{4,5}BB}cc,dd",
    };

//    @Ignore
    @Test
    public void testLexer() {
        for (String s : strings) {
//            System.out.println("STRING: \"" + s + "\"");

            BraceExpansionLexer lexer = new BraceExpansionLexer(new ANTLRInputStream(s));

            // Get a list of matched tokens
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Pass the tokens to the parser
            BraceExpansionParser parser = new BraceExpansionParser(tokens);

            // Specify our entry point
            ParserRuleContext context = parser.root();

//            System.out.println("TEXT: " + context.getText());
//            System.out.println("TREE: " + context.toStringTree(parser));
        }
    }

    @Test
    public void testEmpty() {
        assertExpand(
                "",
                Arrays.asList(
                        ""
                )
        );
    }

    @Test
    public void testRangeInt() {
        assertExpand(
                "a{1..3}b",
                Arrays.asList(
                        "a1b",
                        "a2b",
                        "a3b"
                )
        );
    }

    @Test
    public void testRangeIntReverse() {
        assertExpand(
                "a{3..1}b",
                Arrays.asList(
                        "a3b",
                        "a2b",
                        "a1b"
                )
        );
    }

    @Test
    public void testRangeIntIncr() {
        assertExpand(
                "a{1..4..2}b",
                Arrays.asList(
                        "a1b",
                        "a3b"
                )
        );
    }

    @Test
    public void testRangeIntIncrReverse() {
        assertExpand(
                "a{4..1..-2}b",
                Arrays.asList(
                        "a4b",
                        "a2b"
                )
        );
    }

    @Test
    public void testRangeIntIncrInfNeg() {
        assertExpand(
                "a{1..4..-2}e",
                Arrays.asList(
                        "a{1..4..-2}e"
                )
        );
    }

    @Test
    public void testRangeIntIncrInfZero() {
        assertExpand(
                "a{1..3..0}e",
                Arrays.asList(
                        "a{1..3..0}e"
                )
        );
    }

    @Test
    public void testRangeIntIncrInfNegZero() {
        assertExpand(
                "a{1..3..-0}e",
                Arrays.asList(
                        "a{1..3..-0}e"
                )
        );
    }

    @Test
    public void testRangeIntIncrInfPos() {
        assertExpand(
                "a{4..1..2}e",
                Arrays.asList(
                        "a{4..1..2}e"
                )
        );
    }

    @Test
    public void testRangeChar() {
        assertExpand(
                "a{b..d}e",
                Arrays.asList(
                        "abe",
                        "ace",
                        "ade"
                )
        );
    }

    @Test
    public void testRangeCharReverse() {
        assertExpand(
                "a{d..b}e",
                Arrays.asList(
                        "ade",
                        "ace",
                        "abe"
                )
        );
    }

    @Test
    public void testRangeCharIncr() {
        assertExpand(
                "a{b..f..2}e",
                Arrays.asList(
                        "abe",
                        "ade",
                        "afe"
                )
        );
    }

    @Test
    public void testRangeCharIncrReverse() {
        assertExpand(
                "a{f..b..-2}e",
                Arrays.asList(
                        "afe",
                        "ade",
                        "abe"
                )
        );
    }

    @Test
    public void testRangeCharIncrInfNeg() {
        assertExpand(
                "a{b..f..-2}e",
                Arrays.asList(
                        "a{b..f..-2}e"
                )
        );
    }

    @Test
    public void testRangeCharIncrInfPos() {
        assertExpand(
                "a{f..b..2}e",
                Arrays.asList(
                        "a{f..b..2}e"
                )
        );
    }

    @Test
    public void testEmptyBrace() {
        assertExpand(
                "{}",
                Arrays.asList(
                        ""
                )
        );
    }

    @Test
    public void testEmptyBraces() {
        assertExpand(
                "{}{{}}{}",
                Arrays.asList(
                        ""
                )
        );
    }

    @Test
    public void testA() {
        assertExpand(
                "a",
                Arrays.asList(
                        "a"
                )
        );
    }

    @Test
    public void testEscapes() {
        assertExpand(
                "a\\\\a\\\\\\a",
                Arrays.asList(
                        "a\\a\\a"
                )
        );
    }

    @Test
    public void testOuterCommas() {
        assertExpand(
                ",a,b,",
                Arrays.asList(
                        ",a,b,"
                )
        );
    }

    @Test
    public void testEmptyCommas() {
        assertExpand(
                "{a,b{,}}{,,}",
                Arrays.asList(
                        "a",
                        "b",
                        "b",
                        "a",
                        "b",
                        "b",
                        "a",
                        "b",
                        "b"
                )
                // TODO: Should we do this order instead, like zsh?
//                Arrays.asList(
//                        "a",
//                        "a",
//                        "a",
//                        "b",
//                        "b",
//                        "b",
//                        "b",
//                        "b",
//                        "b"
//                )
        );
    }

    @Test
    public void test2() {
        assertExpand(
                "a{b,c}d",
                Arrays.asList(
                        "abd",
                        "acd"
                )
        );
    }

    @Test
    public void testEscapeBrace() {
        assertExpand(
                "a\\{b,c\\}d",
                Arrays.asList(
                        "a{b,c}d"
                )
        );
    }

    @Test
    public void testEscapeComma() {
        assertExpand(
                "a{b\\,c}d",
                Arrays.asList(
                        "ab,cd"
                )
        );
    }

    @Test
    public void test2s() {
        assertExpand(
                "a{b,{c,d},e}f",
                Arrays.asList(
                        "abf",
                        "acf",
                        "adf",
                        "aef"
                )
        );
    }

    @Test
    public void test3() {
        assertExpand(
                "a{b,c,d}e",
                Arrays.asList(
                        "abe",
                        "ace",
                        "ade"
                )
        );
    }

    @Test
    public void test22() {
        assertExpand(
                "a{b,c}d{e,f}g",
                    Arrays.asList(
                    "abdeg",
                    "acdeg",
                    "abdfg",
                    "acdfg"
            )
        );
    }

    @Test
    public void test22s() {
        assertExpand(
                "a{b{c,d}e,fg}h",
                Arrays.asList(
                        "abceh",
                        "abdeh",
                        "afgh"
                )
        );
    }

//        String str = "x,ya{bb,AA{1,2,33}{4,5}BB}cc,dd";

    @Test
    public void testLong() {
        assertExpand(
                "x,ya{bb,AA{1\\\\,2,33}{4,5}BB}cc,dd",
                Arrays.asList(
                        "x,yabbcc,dd",
                        "x,yaAA1\\4BBcc,dd",
                        "x,yaAA24BBcc,dd",
                        "x,yaAA334BBcc,dd",
                        "x,yaAA1\\5BBcc,dd",
                        "x,yaAA25BBcc,dd",
                        "x,yaAA335BBcc,dd"
                )
        );
    }

    @Test
    public void testReal() {
        assertExpand(
                "pic{{,s},ture{,s}}-{0..2}.{jpg,png}",
                Arrays.asList(
                        "pic-0.jpg",
                        "pics-0.jpg",
                        "picture-0.jpg",
                        "pictures-0.jpg",
                        "pic-1.jpg",
                        "pics-1.jpg",
                        "picture-1.jpg",
                        "pictures-1.jpg",
                        "pic-2.jpg",
                        "pics-2.jpg",
                        "picture-2.jpg",
                        "pictures-2.jpg",
                        "pic-0.png",
                        "pics-0.png",
                        "picture-0.png",
                        "pictures-0.png",
                        "pic-1.png",
                        "pics-1.png",
                        "picture-1.png",
                        "pictures-1.png",
                        "pic-2.png",
                        "pics-2.png",
                        "picture-2.png",
                        "pictures-2.png"
                )
        );
    }

    public void assertExpand(String s, List<String> exs) {
        List<String> strs = BraceExpansion.expand(s);
        MatcherAssert.assertThat(strs, Matchers.equalTo(exs));
    }
}
