import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * http://www.giocc.com/writing-a-lexer-in-java-1-7-using-regex-named-capturing-groups.html
 */
public class BraceExpansion {
    public static enum TokenType {
        // Token types cannot have underscores
//        NUMBER("-?[0-9]+"),
//        BINARYOP("[*|/|+|-]"),
//        WHITESPACE("[ \t\f\r\n]+");
        ESCAPED("\\\\."),
//        BRACE("(?<!\\\\)[{}]"),
//        BRACE("[{}]"),
        BRACE_OPEN("\\{"),
        BRACE_CLOSE("\\}"),
        COMMA(","),
        DOTS("\\.\\."),
        ANY(".");
//        ANY("[^{}]+");

        public final String pattern;

        private TokenType(String pattern) {
            this.pattern = pattern;
        }
    }

    public static class Token {
        public TokenType type;
        public String data;

        public Token(TokenType type, String data) {
            this.type = type;
            this.data = data;
        }

        @Override
        public String toString() {
            return String.format("(%s %s)", type.name(), data);
        }
    }

    public static String makeRegexGroupName(String input) {
        return input.replace("_", "");
    }

    public static ArrayList<Token> lex(String input) {
        // The tokens to return
        ArrayList<Token> tokens = new ArrayList<Token>();

        // Lexer logic begins here
        StringBuffer tokenPatternsBuffer = new StringBuffer();
        for (TokenType tokenType : TokenType.values())
            tokenPatternsBuffer.append(String.format("|(?<%s>%s)", makeRegexGroupName(tokenType.name()), tokenType.pattern));
        Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

        // Begin matching tokens
        Matcher matcher = tokenPatterns.matcher(input);
        while (matcher.find()) {
            for (TokenType tokenType : TokenType.values()) {
                String group = matcher.group(makeRegexGroupName(tokenType.name()));
                if (group != null) {
//                    if (tokenType == TokenType.WHITESPACE) continue;
                    tokens.add(new Token(tokenType, group));
                }
            }
        }

        return tokens;
    }

    public static void main(String[] args) {
        String input = "a{0,1{,-1}}b{c}\\{o\\{o\\\\k";
//        String input = "11 + 22 - 33";

        // Create tokens and print them
        List<Token> tokens = lex(input);
        for (Token token : tokens)
            System.out.println(token);

        Iterator<Token> tokenIterator = tokens.iterator();

        List<StringBuffer> res = recurse(tokenIterator);

        for (StringBuffer sb : res) {
            System.out.println(sb.toString());
        }
    }


    public static List<StringBuffer> recurse(Iterator<Token> tokenIterator) {
        return recurse(tokenIterator, 0);
    }

    public static List<StringBuffer> recurse(Iterator<Token> tokenIterator, int depth) {

        StringBuffer commaString = new StringBuffer();
        List<StringBuffer> commaStrings = new ArrayList<StringBuffer>();
        commaStrings.add(commaString);

        while (tokenIterator.hasNext()) {
            Token token = tokenIterator.next();
            if (token.type == TokenType.BRACE_OPEN) {
                System.out.println("depth: " + depth + " -> " + (depth+1));
                depth += 1;
                // open sub set, consume to matching brace
                List<StringBuffer> subs = recurse(tokenIterator, depth);
                List<StringBuffer> newStrings = new ArrayList<StringBuffer>();
                for (StringBuffer string : commaStrings) {
                    for (StringBuffer sub : subs) {
                        StringBuffer ns = new StringBuffer(string);
                        ns.append(sub);
                        newStrings.add(ns);
                    }
                }
                commaStrings = newStrings;
            }
            else if (token.type == TokenType.BRACE_CLOSE) {
                System.out.println("depth: " + depth + " -> " + (depth-1));
                depth -= 1;
            }
            else if (token.type == TokenType.COMMA) {
                if (depth > 0) {
                    commaString = new StringBuffer();
                    commaStrings.add(commaString);
                }
            }
            else if (token.type == TokenType.ESCAPED) {
                commaString.append(token.data.charAt(1));
            }
            else if (token.type == TokenType.ANY) {
                commaString.append(token.data);
            }
        }

        System.out.println("depth: " + depth);
        return commaStrings;
    }
}
