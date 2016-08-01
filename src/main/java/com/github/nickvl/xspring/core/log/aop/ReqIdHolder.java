package com.github.nickvl.xspring.core.log.aop;

import java.security.SecureRandom;

public class ReqIdHolder {

    private ReqIdHolder() {

    }

    private static final ThreadLocal<String> _reqIds = new ThreadLocal<>();

    public static String get() {
        String value = _reqIds.get();
        if(value == null){
            value = System.currentTimeMillis() + "," + new SecureRandom().nextInt();
            _reqIds.set(value);
        }
        return value;
    }

    public static void set(String reqId) {
        _reqIds.set(reqId);
    }

    public static void remove() {
        _reqIds.remove();
    }
}
