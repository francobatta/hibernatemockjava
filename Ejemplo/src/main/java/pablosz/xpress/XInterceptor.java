package pablosz.xpress;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import pablosz.xpress.ann.*;


public class XInterceptor implements MethodInterceptor {
	  public Object intercept(Object target, Method method,Object[] args, MethodProxy proxy) throws Throwable {
		  System.out.println("******************************");
		System.out.println(target.getClass().getName());
		  
		  System.out.println(method.getName());
		  String var = method.getName().substring(3);
		  String nombreVariableDelMetodo=var.substring(0, 1).toLowerCase() + var.substring(1);
		  //Obtengo nombre de la clase que llama al metodo
		  int dondeComienza = target.getClass().getSimpleName().indexOf("$");
		  String nombreClase = target.getClass().getSimpleName().substring(0,dondeComienza);
		  Class clazz = Class.forName("demo.domain."+nombreClase);
		  String nombreDeMiTabla = XPress.obtenerNombreTabla(clazz);
		  //
		  Field miVariableAChequear = clazz.getField(nombreVariableDelMetodo);
		  System.out.println(miVariableAChequear.getName());
		  //
		if(miVariableAChequear.isAnnotationPresent(ManyToOne.class))
		{
			if(miVariableAChequear.getAnnotation(ManyToOne.class).fetchType()==1) // AHI ES LAZY
			{

				String clase = miVariableAChequear.getGenericType().getTypeName();
				Class clazzz=Class.forName(clase);
				
				String nombreCF = XPress.dameNombreClaveForanea(clazzz);
				System.out.println("Esto busco");
				System.out.println(nombreCF);
				Field clave = XPress.obtenerCampoId(clazz); // obtiene cual es el campo que contiene la clave

				String claveValor = XPress.darNombre(clave);
				System.out.println("----------");
				Method miMetodo = XPress.dameGetter(clazz,clave);
				System.out.println("----------");
				System.out.println(miMetodo.getName());
				Object miValor = miMetodo.invoke(target);
				System.out.println(String.format("%s",miValor));
				String hql = String.format("SELECT %s FROM %s WHERE %s=%s",nombreCF,nombreDeMiTabla,claveValor,miValor);
				
				Connection conn = XPress.hacerConexion(); // conecta con la base de datos
				Statement stmt = conn.createStatement(); // conecta
				System.out.println(hql);
				ResultSet rs = stmt.executeQuery(hql); // devuelve en rs todos los datos que matchean (en este caso 1 registro entero)
			    rs.next();
			    Object id = rs.getInt(nombreCF);
				rs.close();
			    stmt.close();
			    conn.close();
			    return XPress.find(clazzz,id);
			}
		}
		if(miVariableAChequear.isAnnotationPresent(Column.class)&&miVariableAChequear.getAnnotation(Column.class).fetchType()==1){
			
			String clase = miVariableAChequear.getGenericType().getTypeName();
			Class clazzz=Class.forName(clase);
			
			String nombreCF = XPress.dameNombreClaveForanea(clazzz);
			System.out.println("Esto busco");
			System.out.println(nombreCF);
			Field clave = XPress.obtenerCampoId(clazz); // obtiene cual es el campo que contiene la clave

			String claveValor = XPress.darNombre(clave);
			System.out.println("----------");
			Method miMetodo = XPress.dameGetter(clazz,clave);
			System.out.println("----------");
			System.out.println(miMetodo.getName());
			Object miValor = miMetodo.invoke(target);
			System.out.println(String.format("%s",miValor));
			String hql = String.format("SELECT %s FROM %s WHERE %s=%s",nombreCF,nombreDeMiTabla,claveValor,miValor);
			
			Connection conn = XPress.hacerConexion(); // conecta con la base de datos
			Statement stmt = conn.createStatement(); // conecta
			System.out.println(hql);
			ResultSet rs = stmt.executeQuery(hql); // devuelve en rs todos los datos que matchean (en este caso 1 registro entero)
		    rs.next();
		    Object id = rs.getInt(nombreCF);
			rs.close();
		    stmt.close();
		    conn.close();
		    return XPress.find(clazzz,id);
		}
		if(miVariableAChequear.isAnnotationPresent(OneToMany.class))
		{
			System.out.println("zzzzzzzzzzzzzzzzz");
			//ESTO ES PARA EL MUCHOS
			//clazz soi io, clazzz el del medio, clazzOtra es el otro
			String clase = miVariableAChequear.getGenericType().getTypeName();
			clase=clase.replace("java.util.List<","").replace(">","");
			Class clazzz=Class.forName(clase);
			String laOtraClase = clazzz.getName().replace(clazz.getSimpleName(),"");
			System.out.println(laOtraClase);
			
			Class clazzOtra = Class.forName(laOtraClase);
			//Valores para el hql
			Field claveDelMedio = XPress.obtenerCampoId(clazzz); 
			String idClaseDelMedio = XPress.darNombre(claveDelMedio);
			
			String nombreTablaDelMedio = XPress.obtenerNombreTabla(clazzz);
			
			Field clave = XPress.obtenerCampoId(clazz); 
			String claveValor = XPress.darNombre(clave);
			
			Method miMetodo = XPress.dameGetter(clazz,clave);
			Object miValor = miMetodo.invoke(target);
			
			String hql = String.format("SELECT %s FROM %s WHERE %s = %s ",idClaseDelMedio,nombreTablaDelMedio,claveValor,miValor);
			System.out.println(hql);
			
			//Me Conecto
			Connection conn = XPress.hacerConexion(); // conecta con la base de datos
			Statement stmt = conn.createStatement(); // conecta
			ResultSet rs = stmt.executeQuery(hql); // devuelve en rs todos los datos que matchean (en este caso 1 registro entero)
			List<Object> miLista= new ArrayList<Object>();
			while(rs.next()){
				miLista.add(XPress.find(clazzz,rs.getInt(idClaseDelMedio)));
			}
			
			rs.close();
		    stmt.close();
		    conn.close();
		    return miLista;
			
		}
		System.out.println("Me llamaron");
	    Object targetReturn = proxy.invokeSuper(target, args);
	    return targetReturn;
	  }
	}
/*
 			System.out.println("zzzzzzzzzzzzzzzzz");
			//ESTO ES PARA EL MUCHOS
			//clazz soi io, clazzz el del medio, clazzOtra es el otro
			String clase = miVariableAChequear.getGenericType().getTypeName();
			clase=clase.replace("java.util.List<","").replace(">","");
			Class clazzz=Class.forName(clase);
			String laOtraClase = clazzz.getName().replace(clazz.getSimpleName(),"");
			System.out.println(laOtraClase);
			
			Class clazzOtra = Class.forName(laOtraClase);
			//Valores para el hql
			Field claveOtraClase = XPress.obtenerCampoId(clazzOtra); 
			String idOtraClase = XPress.darNombre(claveOtraClase);
			
			String nombreTablaDelMedio = XPress.obtenerNombreTabla(clazzz);
			
			Field clave = XPress.obtenerCampoId(clazz); 
			String claveValor = XPress.darNombre(clave);
			
			Method miMetodo = XPress.dameGetter(clazz,clave);
			Object miValor = miMetodo.invoke(target);
			
			String hql = String.format("SELECT %s FROM %s WHERE %s = %s ",idOtraClase,nombreTablaDelMedio,claveValor,miValor);
			System.out.println(hql);
			
			//Me Conecto
			Connection conn = XPress.hacerConexion(); // conecta con la base de datos
			Statement stmt = conn.createStatement(); // conecta
			ResultSet rs = stmt.executeQuery(hql); // devuelve en rs todos los datos que matchean (en este caso 1 registro entero)
			List<Object> miLista= new ArrayList<Object>();
			while(rs.next()){
				miLista.add(XPress.find(clazzOtra,rs.getInt(idOtraClase)));
			}
			
			rs.close();
		    stmt.close();
		    conn.close();
		    return miLista;
*/
