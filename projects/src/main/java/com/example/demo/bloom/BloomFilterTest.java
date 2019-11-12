package com.example.demo.bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.nio.charset.Charset;

/**
 * @author kejun
 * @date 2019/11/8 上午9:43
 */
public class BloomFilterTest {
    private static int total = 1000000;
    private static BloomFilter<Integer> bf = BloomFilter.create(Funnels.integerFunnel(), total);
//    private static BloomFilterTest<Integer> bf = BloomFilterTest.create(Funnels.integerFunnel(), total, 0.001);
    private static BloomFilter<Email> emailBloomFilter = BloomFilter.create((Funnel<Email>)(from, into)-> into.putString(from.getThings(), Charsets.UTF_8),10000,0.001);

    public static void main(String[] args) {
        // 初始化1000000条数据到过滤器中
        for (int i = 0; i < total; i++) {
            bf.put(i);
        }

        // 匹配已在过滤器中的值，是否有匹配不上的
        for (int i = 0; i < total; i++) {
            if (!bf.mightContain(i)) {
                System.out.println("有坏人逃脱了~~~");
            }
        }

        // 匹配不在过滤器中的10000个值，有多少匹配出来
        int count = 0;
        for (int i = total; i < total + 10000; i++) {
            if (bf.mightContain(i)) {
                count++;
            }
        }
        System.out.println("误伤的数量：" + count);

        emailBloomFilter.put(new Email("aaa","abcd"));
        boolean isContainEmail = emailBloomFilter.mightContain(new Email("aaa","aa"));
        System.out.println(isContainEmail);
    }

    @Builder
    @Data
    @ToString
    @AllArgsConstructor
    public static class Email{
        String name;
        String things;
    }

}
