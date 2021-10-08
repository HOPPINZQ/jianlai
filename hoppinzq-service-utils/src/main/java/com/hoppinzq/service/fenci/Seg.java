package com.hoppinzq.service.fenci;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:ZhangQi
 **/
public class Seg {
    private Map<Character, Map> d = new TreeMap();
    private Set<Character> stopWords = new HashSet();

    public Seg() {
    }

    public void useDefaultDict() {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("dic/main.dic");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
            ArrayList words = new ArrayList();

            while(true) {
                String word = reader.readLine();
                if (word == null || word.equals("")) {
                    this.set(words);
                    is.close();
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream("dic/suffix.dic");
                    reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    new ArrayList();

                    while(true) {
                        word = reader.readLine();
                        if (word == null || word.equals("")) {
                            return;
                        }

                        this.stopWords.add(word.charAt(0));
                    }
                }

                if (word.length() <= 4) {
                    words.add(word);
                }
            }
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

    }

    public void set(List<String> words) {
        Map<Character, Map> p = this.d;
        Map<Character, Map> q = null;
        Character k = null;
        Iterator var6 = words.iterator();

        while(var6.hasNext()) {
            String word = (String)var6.next();
            word = '\u000b' + word;
            p = this.d;

            for(int i = word.length() - 1; i >= 0; --i) {
                Character cc = Character.toLowerCase(word.charAt(i));
                if (p == null) {
                    q.put(k, new TreeMap());
                    p = (Map)q.get(k);
                }

                if (!p.containsKey(cc)) {
                    p.put(cc,null);
                    q = p;
                    k = cc;
                }

                p = (Map)p.get(cc);
            }
        }

    }

    private List<String> _binary_seg(String s) {
        int ln = s.length();
        List<String> R = new ArrayList();
        if (ln == 1) {
            R.add(s);
            return R;
        } else {
            for(int i = ln; i > 1; --i) {
                String tmp = s.substring(i - 2, i);
                R.add(tmp);
            }

            return R;
        }
    }

    private List<String> findAll(String pattern, String text) {
        List<String> R = new ArrayList();
        Matcher mc = Pattern.compile(pattern).matcher(text);

        while(mc.find()) {
            R.add(mc.group(1));
        }

        return R;
    }

    private List<String> _pro_unreg(String piece) {
        List<String> R = new ArrayList();
        String[] tmp = piece.replaceAll("。|，|,|！|…|!|《|》|<|>|\"|'|:|：|？|\\?|、|\\||“|”|‘|’|；|—|（|）|·|\\(|\\)|　", " ").split("\\s");
        Splitter spliter = new Splitter("([0-9A-Za-z\\-\\+#@_\\.]+)", true);

        for(int i = tmp.length - 1; i > -1; --i) {
            String[] mc = spliter.split(tmp[i]);

            for(int j = mc.length - 1; j > -1; --j) {
                String r = mc[j];
                if (Pattern.matches("([0-9A-Za-z\\-\\+#@_\\.]+)", r)) {
                    R.add(r);
                } else {
                    R.addAll(this._binary_seg(r));
                }
            }
        }

        return R;
    }

    public List<String> cut(String text) {
        Map<Character, Map> p = this.d;
        int ln = text.length();
        int i = ln;
        int j = 0;
        int z = ln;
        Boolean q = false;
        List<String> recognised = new ArrayList();
        Integer[] mem = (Integer[])null;
        Integer[] mem2 = (Integer[])null;

        while(true) {
            while(true) {
                while(i - j > 0) {
                    Character t = Character.toLowerCase(text.charAt(i - 1 - j));
                    List unreg_tmp;
                    if (!p.containsKey(t)) {
                        if (mem == null && mem2 == null) {
                            j = 0;
                            --i;
                            p = this.d;
                        } else {
                            if (mem != null) {
                                i = mem[0];
                                j = mem[1];
                                z = mem[2];
                                mem = (Integer[])null;
                            } else if (mem2 != null) {
                                int delta = mem2[0] - i;
                                if (delta >= 1) {
                                    if (delta < 5 && Pattern.matches("[\\w\\u2E80-\\u9FFF]", t.toString())) {
                                        Character pre = text.charAt(i - j);
                                        if (!this.stopWords.contains(pre)) {
                                            i = mem2[0];
                                            j = mem2[1];
                                            z = mem2[2];
                                            int q1 = mem2[3];

                                            while(recognised.size() > q1) {
                                                recognised.remove(recognised.size() - 1);
                                            }
                                        }
                                    }

                                    mem2 = (Integer[])null;
                                }
                            }

                            p = this.d;
                            if (i < ln && i < z) {
                                unreg_tmp = this._pro_unreg(text.substring(i, z));
                                recognised.addAll(unreg_tmp);
                            }

                            recognised.add(text.substring(i - j, i));
                            i -= j;
                            z = i;
                            j = 0;
                        }
                    } else {
                        p = (Map)p.get(t);
                        ++j;
                        if (p.containsKey('\u000b')) {
                            if (j <= 2) {
                                mem = new Integer[]{i, j, z};
                                Character xsuffix = text.charAt(i - 1);
                                if (z - i < 2 && this.stopWords.contains(xsuffix) && (mem2 == null || mem2 != null && mem2[0] - i > 1)) {
                                    mem = (Integer[])null;
                                    mem2 = new Integer[]{i, j, z, recognised.size()};
                                    p = this.d;
                                    --i;
                                    j = 0;
                                }
                            } else {
                                p = this.d;
                                if (i < ln && i < z) {
                                    unreg_tmp = this._pro_unreg(text.substring(i, z));
                                    recognised.addAll(unreg_tmp);
                                }

                                recognised.add(text.substring(i - j, i));
                                i -= j;
                                z = i;
                                j = 0;
                                mem = (Integer[])null;
                                mem2 = (Integer[])null;
                            }
                        }
                    }
                }

                if (mem != null) {
                    i = mem[0];
                    j = mem[1];
                    z = mem[2];
                    recognised.addAll(this._pro_unreg(text.substring(i, z)));
                    recognised.add(text.substring(i - j, i));
                } else {
                    recognised.addAll(this._pro_unreg(text.substring(i - j, z)));
                }

                return recognised;
            }
        }
    }

    public static void main(String[] args) {
        Seg seg = new Seg();
        List<String> words = new ArrayList();
        words.add("ab");
        words.add("abc");
        words.add("adf");
        words.add("北京");
        seg.set(words);
        System.out.println(seg.d);
        System.out.println(seg.cut("abc lxx adf我爱北京"));
    }
}
