package com.svlada.interview;

import java.util.Arrays;
import java.util.List;

public class T
{

    public static void main(String[] args) {
        String orderCodes ="a,b,c";
        String[] split = orderCodes.split(",");
        List<String> collection = Arrays.asList(split);
        System.out.println("collection = " + collection);
    }
}
