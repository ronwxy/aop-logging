package mdc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Parent {
    private static Logger logger = LoggerFactory.getLogger(Parent.class);
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public Parent() {
        // Mimic Web app, save common info in MDC
        MDC.put("IP", "192.168.1.1");
    }

    public void runMultiThreadByExecutor() throws InterruptedException {


        executorService.submit(new Child());


//        executorService.shutdown();
//        executorService.awaitTermination(1, TimeUnit.SECONDS);
//        logger.info("ExecutorService is over");
    }

    public static void main(String[] args) throws InterruptedException {
        Map<String,String> map1 = MDC.getCopyOfContextMap();

        Map<String,String> map2 = MDC.getCopyOfContextMap();

        System.out.println(map1 == null);

        System.out.println(map1 == map2);

        logger.info("Before start child thread");
        Parent parent = new Parent();

        parent.runMultiThreadByExecutor();  //MDC OK
        MDC.put("ct", "1");
        logger.info("After start child thread");
        MDC.clear();
        parent.runMultiThreadByExecutor();  //MDC OK
        MDC.put("ct", "2");
        logger.info("After start child thread");
        parent.runMultiThreadByExecutor();  //MDC OK
        MDC.put("ct", "3");
        logger.info("After start child thread");
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);
        logger.info("ExecutorService is over");
    }
}
