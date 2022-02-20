package org.example.ClassPathXmlApplicationContext.childParentContainer;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Demo {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("ClassPathXmlApplicationContext/parent.xml");
		context.refresh();

		ClassPathXmlApplicationContext childCtx = new ClassPathXmlApplicationContext("ClassPathXmlApplicationContext/child.xml");
		childCtx.setParent(context);
		childCtx.refresh();

		ClassPathXmlApplicationContext childCtx2 = new ClassPathXmlApplicationContext("ClassPathXmlApplicationContext/child_2.xml");
		childCtx2.setParent(context);
		childCtx2.refresh();

		// 子容器能获取父容器的bean
		ParentBean parentBean = (ParentBean) childCtx.getBean("parentBean");
		assert "parentBean".equals(parentBean.getName());

		// 父容器不能获取子容器的bean
		try {
			Object childBean = context.getBean("childBean");
		}catch (Exception e) {
			assert e instanceof NoSuchBeanDefinitionException;
		}

		// 子容器之间相互隔离
		try {
			Object childBean2 = childCtx.getBean("childBean2");
		}catch (Exception e) {
			assert e instanceof NoSuchBeanDefinitionException;
		}

	}
}
