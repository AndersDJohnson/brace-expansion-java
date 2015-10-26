package me.andrz.brace;

import me.andrz.brace.antlr.BraceExpansionBaseListener;
import me.andrz.brace.antlr.BraceExpansionParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

/**
 * Created by anders on 8/2/15.
 */
public class AntlrBraceListener extends BraceExpansionBaseListener {

    private Deque<List<StringBuffer>> stack = new ArrayDeque<List<StringBuffer>>();

    private Value<List<String>> value;

    public AntlrBraceListener(Value<List<String>> value) {
        this.value = value;
    }

    @Override public void enterStr(BraceExpansionParser.StrContext ctx) {
        String s = ctx.getText();
        append(s);
    }
    @Override public void exitStr(BraceExpansionParser.StrContext ctx) { }

    @Override public void enterSub(BraceExpansionParser.SubContext ctx) {
        String text = ctx.getText();
        List<StringBuffer> strings = stack.peekFirst();
        strings.add(new StringBuffer(text));
    }
    @Override public void exitSub(BraceExpansionParser.SubContext ctx) {
    }

    @Override public void enterOt(BraceExpansionParser.OtContext ctx) {
        String s = ctx.getText();
        ott(s);
    }
    @Override public void exitOt(BraceExpansionParser.OtContext ctx) { }

    @Override public void enterRoot(BraceExpansionParser.RootContext ctx) {
        List<StringBuffer> layer = new ArrayList<StringBuffer>();
        layer.add(new StringBuffer(""));
        stack.offerFirst(layer);
    }
    @Override public void exitRoot(BraceExpansionParser.RootContext ctx) {
        List<StringBuffer> strings = stack.peekFirst();
        List<String> strs = new ArrayList<String>();
        for (StringBuffer sb : strings) {
            strs.add(sb.toString());
        }
        value.setValue(strs);
    }

    @Override public void enterBrace(BraceExpansionParser.BraceContext ctx) {
        List<StringBuffer> layer = new ArrayList<StringBuffer>();
        stack.offerFirst(layer);
    }
    @Override public void exitBrace(BraceExpansionParser.BraceContext ctx) {
        List<StringBuffer> head = stack.pollFirst();
        append(head);
    }

    @Override public void enterEveryRule(ParserRuleContext ctx) { }
    @Override public void exitEveryRule(ParserRuleContext ctx) { }
    @Override public void visitTerminal(TerminalNode node) { }
    @Override public void visitErrorNode(ErrorNode node) { }

    private void ott(String s) {
        List<StringBuffer> newStrings = new ArrayList<StringBuffer>();
        List<StringBuffer> strings = stack.pollFirst();
        for (StringBuffer sb : strings) {
            newStrings.add(new StringBuffer(sb.toString() + s));
        }
        stack.offerFirst(newStrings);
    }

    private void append(List<StringBuffer> next) {
        if (next.size() < 2) {
            return;
        }
        List<StringBuffer> newStrings = new ArrayList<StringBuffer>();
        List<StringBuffer> strings = stack.pollFirst();
        for (StringBuffer sb : next) {
            for (StringBuffer s : strings) {
                newStrings.add(new StringBuffer(s.toString() + sb.toString()));
            }
        }
        stack.offerFirst(newStrings);
    }

    private void append(StringBuffer sb) {
        append(Arrays.asList(sb));
    }
    private void append(String s) {
        append(new StringBuffer(s));
    }

}
