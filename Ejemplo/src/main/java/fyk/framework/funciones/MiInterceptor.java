package fyk.framework.funciones;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import pablosz.xpress.ann.ManyToOne;
import pablosz.xpress.ann.OneToMany;

public class MiInterceptor implements MethodInterceptor {
	  public Object intercept(Object target, Method method,Object[] args, MethodProxy proxy) throws Throwable {
		String nombreVariableDelMetodo = method.getName().substring(3).toLowerCase();
		Field miVariableAChequear = target.getClass().getField(nombreVariableDelMetodo);
		if(miVariableAChequear.isAnnotationPresent(ManyToOne.class))
		{
			if(miVariableAChequear.getAnnotation(ManyToOne.class).fetchType()==1) // AHI ES LAZY
			{
				String clase = miVariableAChequear.getGenericType().getTypeName();
				Class clazz=Class.forName(clase);
				String nombreCF = implementaciones.dameNombreClaveForanea(clazz);
				Field clave = implementaciones.obtenerCampoId(target.getClass()); // obtiene cual es el campo que contiene la clave
				String claveValor = implementaciones.darNombre(clave);
				Method miMetodo = implementaciones.dameGetter(target,clave);
				Object miValor = miMetodo.invoke(target);
				String hql = String.format("SELECT %s WHERE %s=%s",nombreCF,claveValor,miValor);
				Connection conn = implementaciones.hacerConexion(); // conecta con la base de datos
				Statement stmt = conn.createStatement(); // conecta
				System.out.println(hql);
				ResultSet rs = stmt.executeQuery(hql); // devuelve en rs todos los datos que matchean (en este caso 1 registro entero)
			    rs.next();
			    Object id = rs.getInt(nombreCF);
				rs.close();
			    stmt.close();
			    conn.close();
			    return implementaciones.find(clazz,id);
			}
		}
		if(miVariableAChequear.isAnnotationPresent(OneToMany.class))
		{
			
		}
	    Object targetReturn = proxy.invokeSuper(target, args);
	    return targetReturn;
	  }
	}