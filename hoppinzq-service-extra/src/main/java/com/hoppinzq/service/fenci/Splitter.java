package com.hoppinzq.service.fenci;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author:ZhangQi
 **/
public class Splitter {
    private static final Pattern DEFAULT_PATTERN = Pattern.compile("\\s+");
    private Pattern pattern;
    private boolean keep_delimiters;

    public Splitter(Pattern pattern, boolean keep_delimiters) {
        this.pattern = pattern;
        this.keep_delimiters = keep_delimiters;
    }

    public Splitter(String pattern, boolean keep_delimiters) {
        this(Pattern.compile(pattern == null ? "" : pattern), keep_delimiters);
    }

    public Splitter(Pattern pattern) {
        this(pattern, true);
    }

    public Splitter(String pattern) {
        this(pattern, true);
    }

    public Splitter(boolean keep_delimiters) {
        this(DEFAULT_PATTERN, keep_delimiters);
    }

    public Splitter() {
        this(DEFAULT_PATTERN);
    }

    public String[] split(String text) {
        if (text == null) {
            text = "";
        }

        int last_match = 0;
        LinkedList<String> splitted = new LinkedList();

        for(Matcher m = this.pattern.matcher(text); m.find(); last_match = m.end()) {
            splitted.add(text.substring(last_match, m.start()));
            if (this.keep_delimiters) {
                splitted.add(m.group());
            }
        }

        splitted.add(text.substring(last_match));
        return (String[])splitted.toArray(new String[splitted.size()]);
    }

    public static void main(String[] argv) {
        if (argv.length != 2) {
            System.err.println("Syntax: java Splitter <pattern> <text>");
        } else {
            Pattern pattern = null;

            try {
                pattern = Pattern.compile(argv[0]);
            } catch (PatternSyntaxException var9) {
                System.err.println(var9);
                return;
            }

            Splitter splitter = new Splitter(pattern);
            String text = argv[1];
            int counter = 1;
            String[] var8;
            int var7 = (var8 = splitter.split(text)).length;

            for(int var6 = 0; var6 < var7; ++var6) {
                String part = var8[var6];
                System.out.printf("Part %d: \"%s\"\n", counter++, part);
            }

        }
    }
}
