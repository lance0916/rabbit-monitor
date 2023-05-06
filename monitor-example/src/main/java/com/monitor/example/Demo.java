package com.monitor.example;

import java.util.regex.Pattern;

/**
 * @author WuQinglong
 * @since 2023/3/22 09:41
 */
public class Demo {

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("[/:=, \r\n\t]");

        System.out.println(pattern.matcher("a/v").find());
        System.out.println(pattern.matcher("a:v").find());
        System.out.println(pattern.matcher("a=v").find());
        System.out.println(pattern.matcher("a v").find());
        System.out.println(pattern.matcher("a\rv").find());
        System.out.println(pattern.matcher("a\nv").find());
        System.out.println(pattern.matcher("a\rv").find());
        System.out.println(pattern.matcher("avv").find());


    }

}
