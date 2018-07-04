package fyk.framework.funciones;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class MiInterceptor implements MethodInterceptor {
	  private Auditor auditor;
	  private String service;
	  public AuditingInterceptor(Auditor auditor, String service) {
	    this.auditor = auditor;
	    this.service = service;
	  }
	  public Object intercept(Object target, Method method,Object[] args, MethodProxy proxy) throws Throwable {
	    auditor.audit(service, "before " + method.getName());
	    targetReturn = proxy.invokeSuper(target, args);
	    auditor.audit(service, "after " + method.getName());
	    return targetReturn;
	  }
	}

// ver https://dzone.com/articles/cglib-proxies-and-hibernate-lazy-fetching