package annotation;

import com.github.nickvl.xspring.core.log.aop.annotation.LogInfo;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by zzy on 16-12-27.
 */
public class Foo1 extends Bar {

//    @Override
//    public void doFoo() {
//
//    }

    public static void main(String[] args) {
        Method method = ReflectionUtils.findMethod(Foo1.class, "doFoo");

        System.out.println("method class:" +method.getClass());
        System.out.println("method declaring class:" + method.getDeclaringClass());

        System.out.println("annotations:" + Arrays.toString(method.getAnnotations()));


        Set<Method> methods = MethodUtils.getOverrideHierarchy(method, ClassUtils.Interfaces.INCLUDE);
        System.out.println(methods);
        methods.forEach(m -> System.out.println(m.getAnnotation(LogInfo.class)));

        System.out.println(AnnotationUtils.getAnnotation(method, LogInfo.class));
        System.out.println(Arrays.toString(method.getAnnotations()));


        //#####################################################################//
        System.out.println("declaringClass annotations:" + Arrays.toString(method.getDeclaringClass().getAnnotations()));


        System.out.println("method getDeclaringClass:" + method.getDeclaringClass());

        System.out.println(ClassUtils.getAllInterfaces(method.getDeclaringClass()));

        methods.forEach(m -> System.out.println(Arrays.toString(m.getDeclaringClass().getAnnotations())));




    }
}
