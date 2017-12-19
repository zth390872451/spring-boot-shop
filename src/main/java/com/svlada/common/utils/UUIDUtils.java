package com.svlada.common.utils;

import java.util.UUID;

public class UUIDUtils {

    public static String getOrderCode(){
        UUID uuid = UUID.randomUUID();
        String orderCode = uuid.toString().replace("-","");
        return orderCode;
    }
}
