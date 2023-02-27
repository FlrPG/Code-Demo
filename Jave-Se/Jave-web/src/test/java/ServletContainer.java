import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet容器
 */
public class ServletContainer {

    public static void main(String[] args) throws IllegalAccessException, InstantiationException {

        // 模拟容器的扫描过程，假设得到了所有Class（包含Servlet类和普通类，普通类是用来做对照的，实际开发项目中肯定不全是Servlet）
        List<Class<?>> servletList = scanServletsInPath();
        System.out.println("在Container中线程执行到main方法啦：" + Thread.currentThread().getName());
        
        // 从中选出Servlet：类上标注了@MyWebServlet注解 && 继承自HttpServlet
        for (Class<?> clazz : servletList) {
            boolean hasMyWebServletAnnotation = clazz.isAnnotationPresent(MyWebServlet.class);
            boolean extendsHttpServlet = HttpServlet.class.isAssignableFrom(clazz);
            if (hasMyWebServletAnnotation && extendsHttpServlet) {
                // 确定是一个Servlet，为它创建实例并调用init
                HttpServlet servletObj = (HttpServlet) clazz.newInstance();
                servletObj.init();
            }
        }
    }

    private static List<Class<?>> scanServletsInPath() {
        // 假装扫描到了Class
        return Arrays.asList(BookServlet.class, OtherClass.class);
    }
}

/**
 * 山寨的Servlet接口，你可以在JDK的javax包下找到正版的Servlet，它定义了5个生命周期方法
 * 这里我们只定义一个init方法，其他方法同理，不做演示
 */
interface Servlet {
    void init();
}

/**
 * 模拟javax包的GenericServlet
 */
abstract class GenericServlet implements Servlet {
    @Override
    public void init() {
        System.out.println("线程执行到GenericServlet.init方法啦：" + Thread.currentThread().getName());
        System.out.println("如果子类不重写，那么你就会看到GenericServlet的init被调用");
        System.out.println("由" + this.getClass().getName() + "调用");
    }
}

/**
 * 模拟javax包的HttpServlet
 */
abstract class HttpServlet extends GenericServlet {
}

/**
 * 很多时候我们会抽取一个BaseServlet，把公共的代码抽取一下，这里不做任何处理，就是一个空的类
 */
class BaseServlet extends HttpServlet {
}

/**
 * 假设这是我们自己写的Servlet，类似于BookController
 * 使用@MyWebServlet标识这是一个Servlet，让Tomcat容器帮我们创建和管理
 */
@MyWebServlet(name = "bookServlet")
class BookServlet extends BaseServlet {
}

/**
 * 用来做对照，没什么意义
 */
class OtherClass {
}

/**
 * 山寨版@WebServlet
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface MyWebServlet {
    String name() default "";
}