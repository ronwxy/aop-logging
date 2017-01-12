package annotation;

import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;

/**
 * Created by zzy on 16-12-28.
 */
public class Bar implements IBar {

    @LogInfo
    public void doFoo() {

    }
}
