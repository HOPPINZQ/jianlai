package com.hoppinzq.service.cache;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.hoppinzq.service.bean.SpiderLink;

import java.nio.charset.Charset;
import java.util.*;

/**
 * @author: zq
 */
public class BloomFilterCache {

    public static BloomFilter<CharSequence> urlIndexFilter = BloomFilter.create(Funnels.stringFunnel(Charset.forName("utf-8")), 100000000, 0.0001);
    public static List<SpiderLink> urls= Collections.synchronizedList(new ArrayList<SpiderLink>());
}
