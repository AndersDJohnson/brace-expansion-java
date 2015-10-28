package me.andrz.brace;

import me.andrz.brace.antlr.BraceExpansionBaseListener;
import me.andrz.brace.antlr.BraceExpansionParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

/**
 * Created by anders on 8/2/15.
 */
public class AntlrBraceListener extends BraceExpansionBaseListener {

    private Deque<List<StringBuffer>> braces = new ArrayDeque<List<StringBuffer>>();
    private Deque<List<StringBuffer>> commas = new ArrayDeque<List<StringBuffer>>();

    private Value<List<String>> value;

    public AntlrBraceListener(Value<List<String>> value) {
        this.value = value;
    }

    @Override public void enterStr(BraceExpansionParser.StrContext ctx) {
        String s = ctx.getText();
        if (s == null || s.length() == 0) return;
        s = unescape(s);
        List<StringBuffer> comma = commas.pollFirst();
        List<StringBuffer> newComma = new ArrayList<StringBuffer>();
        for (StringBuffer c : comma) {
            newComma.add(new StringBuffer(c.toString() + s));
        }
        commas.offerFirst(newComma);
    }
    @Override public void exitStr(BraceExpansionParser.StrContext ctx) { }

    @Override public void enterSub(BraceExpansionParser.SubContext ctx) {
        List<StringBuffer> comma = new ArrayList<StringBuffer>();
        comma.add(new StringBuffer(""));
        commas.offerFirst(comma);
    }
    @Override public void exitSub(BraceExpansionParser.SubContext ctx) {
        List<StringBuffer> brace = braces.peekFirst();
        List<StringBuffer> comma = commas.pollFirst();
        brace.addAll(comma);
    }

    @Override public void enterOt(BraceExpansionParser.OtContext ctx) {
        String s = ctx.getText();
        s = unescape(s);
        List<StringBuffer> comma = commas.peekFirst();
        for (StringBuffer c : comma) {
            c.append(s);
        }
    }
    @Override public void exitOt(BraceExpansionParser.OtContext ctx) { }

    @Override public void enterRoot(BraceExpansionParser.RootContext ctx) {
        List<StringBuffer> comma = new ArrayList<StringBuffer>();
        comma.add(new StringBuffer(""));
        commas.offerFirst(comma);
    }
    @Override public void exitRoot(BraceExpansionParser.RootContext ctx) {
        List<StringBuffer> comma = commas.peekFirst();
        List<String> strings = stringBuffersToStrings(comma);
        value.setValue(strings);
    }

    @Override public void enterBrace(BraceExpansionParser.BraceContext ctx) {
        List<StringBuffer> list = new ArrayList<StringBuffer>();
        braces.offerFirst(list);
    }
    @Override public void exitBrace(BraceExpansionParser.BraceContext ctx) {
        List<StringBuffer> brace = braces.pollFirst();
        List<StringBuffer> comma = commas.pollFirst();
        List<StringBuffer> newComma = new ArrayList<StringBuffer>();
        for (StringBuffer b : brace) {
            for (StringBuffer c : comma) {
                newComma.add(new StringBuffer(c.toString() + b.toString()));
            }
        }
        commas.offerFirst(newComma);
    }

    @Override public void enterRangeNum(BraceExpansionParser.RangeNumContext ctx) {
        ParseTree child;
        child = ctx.getChild(0);
        Integer start = child == null ? null : Integer.valueOf(child.getText());
        child = ctx.getChild(2);
        Integer end = child == null ? null : Integer.valueOf(child.getText());
        child = ctx.getChild(4);
        Integer incr = child == null ? null : Integer.valueOf(child.getText());

        Incrementor<Integer> incrementor = new IntegerIncrementor(start, end, incr);
        incrementing(ctx.getText(), incrementor);
    }
    @Override public void exitRangeNum(BraceExpansionParser.RangeNumContext ctx) { }

    @Override public void enterRangeChar(BraceExpansionParser.RangeCharContext ctx) {
        ParseTree child;
        child = ctx.getChild(0);
        Character start = child == null ? null : child.getText().charAt(0);
        child = ctx.getChild(2);
        Character end = child == null ? null : child.getText().charAt(0);
        child = ctx.getChild(4);
        Integer incr = child == null ? null : Integer.valueOf(child.getText());

        Incrementor<Character> incrementor = new CharacterIncrementor(start, end, incr);
        incrementing(ctx.getText(), incrementor);
    }
    @Override public void exitRangeChar(BraceExpansionParser.RangeCharContext ctx) { }

    @Override public void enterEveryRule(ParserRuleContext ctx) { }
    @Override public void exitEveryRule(ParserRuleContext ctx) { }
    @Override public void visitTerminal(TerminalNode node) { }
    @Override public void visitErrorNode(ErrorNode node) { }

    public void incrementing(String s, Incrementor incrementor) {
        List<StringBuffer> brace = braces.peekFirst();

        String error = incrementor.valid();
        if (error != null) {
            s = "{" + s + "}"; // re-wrap
            s = unescape(s);
            brace.add(new StringBuffer(s));
        }
        List<StringBuffer> comma = new ArrayList<StringBuffer>();
        while (incrementor.hasNext()) {
            comma.add(new StringBuffer(incrementor.getStart().toString()));
            incrementor.next();
        }
        brace.addAll(comma);
    }

    public List<String> stringBuffersToStrings(List<StringBuffer> sbs) {
        List<String> sts = new ArrayList<String>();
        for (StringBuffer sb : sbs) {
            sts.add(sb.toString());
        }
        return sts;
    }

    public String unescape(String s) {
        s = s.replaceAll("\\\\([^\\\\])", "$1");
        s = s.replaceAll("\\\\\\\\", "\\\\");
        return s;
    }

}
