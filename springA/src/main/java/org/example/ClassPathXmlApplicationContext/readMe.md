ApplicationContext接口提供多个实现类。
而在stand-alone模式下，通常会创建ClassPathXMlApplicationContext或FileSystemXMLApplicationContext的实例。  

###init container
```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
or
ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/com/acme/system-test-config.xml");
```
service.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- services -->

    <bean id="petStore" class="org.springframework.samples.jpetstore.services.PetStoreServiceImpl">
        <property name="accountDao" ref="accountDao"/>
        <property name="itemDao" ref="itemDao"/>
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for services go here -->

</beans>
```
daos.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="accountDao"
        class="org.springframework.samples.jpetstore.dao.jpa.JpaAccountDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <bean id="itemDao" class="org.springframework.samples.jpetstore.dao.jpa.JpaItemDao">
        <!-- additional collaborators and configuration for this bean go here -->
    </bean>

    <!-- more bean definitions for data access objects go here -->

</beans>
```
###Using the Container
```java
// create and configure beans
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");

// retrieve configured instance
PetStoreService service = context.getBean("petStore", PetStoreService.class);

// use configured instance
List<String> userList = service.getUsernameList();
```

###Shutting Down the Spring IoC Container Gracefully in Non-Web Applications
1. 仅适用于standalone模式的container，web模式container有自己的优雅关闭方式。
2. 使用registerShutdownHook()可以将容器销毁时的回调钩子注册到jvm中，实现优雅的容器销毁。
```java
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Boot {

    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        // add a shutdown hook for the above context...
        ctx.registerShutdownHook();

        // app runs here...

        // main method exits, hook is called prior to the app shutting down...
    }
}
```

-----------
###Request, Session, Application, and WebSocket Scopes
bean scope中的request, session, application, 和 websocket 不适用于ClassPathXmlApplicationContext这种standalone模式的上下文，会直接抛出IllegalStateException。这些scope仅适用于XmlWebApplicationContext。  

--------------------------------
###The ResourceLoader Interface
ClassPathXmlApplicationContext实现了ResourceLoader接口，也具备了读取资源的能力。
```java
public interface ResourceLoader {

    Resource getResource(String location);

    ClassLoader getClassLoader();
}
```
假设applicationContext是基于ClassPathXmlApplicationContext形成的容器。  
它将会返回一个ClasspathResource，而基于FileSystemXmlApplicationContext形成的容器将会返回FileSystemResource。
```java
Resource template = applicationContext.getResource("some/resource/path/myTemplate.txt");
```
--------------------
### The classpath*: Prefix
表示编译结果中所有类路径下的classpath:conf/appContext.xml均会被识别。
```java
ApplicationContext ctx =
    new ClassPathXmlApplicationContext("classpath*:conf/appContext.xml");
```




