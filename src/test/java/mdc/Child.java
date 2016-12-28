package mdc;

import com.github.nickvl.xspring.core.log.aop.ObjectId;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;

public class Child implements Runnable {
    private Logger logger = LoggerFactory.getLogger(Child.class);

    // contextMap is set when new Child() is called
    private Map<String, String> contextMap = MDC.getCopyOfContextMap();

    public void run() {
        MDC.setContextMap(contextMap);  // set contextMap when thread start
        System.out.println(Thread.currentThread().getName() + ":" + contextMap);
        MDC.put("reqId", ObjectId.get().toHexString());
        MDC.put("IP", RandomStringUtils.randomAlphabetic(5));
        logger.info("Running in the child thread");
    }
}