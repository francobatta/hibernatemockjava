package pablosz.xpress;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.jar.Attributes.Name;

import fyk.framework.funciones.MiInterceptor;
import fyk.framework.funciones.Transaction;
import fyk.tests.Persona;
import net.sf.cglib.proxy.Enhancer;
import pablosz.xpress.util.*;

import pablosz.xpress.ann.*;
public class XPress
{
	
	// funcion que retorna una instancia de T si se encuentra en la fila buscada
	// otra solucion que se me ocurrio es ir tirando un string a la base de datos para cada atributo y en vez de usar SELECT *
	// usar SELECT nombreAtributo FROM... y asi ir armando y toda la info rescatada del rs iria directamente al valor del set
	public static <T> T find(Class<T> instancia, Object id) throws ClassNotFoundException, SQLException, IOException{
		String nombreDeLaTabla = obtenerNombreTabla(instancia); // obtiene el nombre de la clase (tabla) en String
		Field clave = obtenerCampoId(instancia); // obtiene cual es el campo que contiene la clave
		String claveValor = darNombre(clave);
		System.out.println(claveValor);
		String idString = String.valueOf(id); // convierte el id que pasaron a String para asegurar igualdad
		String hql = String.format("SELECT * FROM %s WHERE %s=%s",nombreDeLaTabla,claveValor,idString); // string que va a ser pasado como query
		Connection conn = hacerConexion(); // conecta con la base de datos
		Statement stmt = conn.createStatement(); // conecta
		System.out.println(hql);
		ResultSet rs = stmt.executeQuery(hql); // devuelve en rs todos los datos que matchean (en este caso 1 registro entero)
		
		// creacion del objeto nuevo:
        try {
      		Constructor<T> constructorSinParametros = instancia.getConstructor(); // agarra constructor	
      		MiInterceptor interceptor = new MiInterceptor();
			Enhancer objetoMejorado = new Enhancer();
			objetoMejorado.setSuperclass(instancia);
			objetoMejorado.setCallback(interceptor);
			T nuevoObjetoDeMiClase = (T) objetoMejorado.create();
			//nuevoObjetoDeMiClase = constructorSinParametros.newInstance(); // instancia el objeto a partir de constructor
      		Annotation[] annotations = instancia.getAnnotations();
      		
      		Field[] listaAtributos = instancia.getDeclaredFields(); // array de atributos
       		rs.next();
			// mapear los resultados del SQL al objeto creado y retornarlo...
       		// este metodo de aca agarra cada atributo y dice que ese atributo en el objeto nuevo va a tener el valor sql:


       		for (Field variable : listaAtributos) {System.out.println("Itero sobre lista de atributos");
       			if(variable.isAnnotationPresent(Column.class))
       				obtenerValorColumn(rs,nuevoObjetoDeMiClase,variable);
       			if(variable.isAnnotationPresent(ManyToOne.class))
       				//Pregunto si el ManyToOneEsEager
       				if(variable.getAnnotation(ManyToOne.class).fetchType()==2)
       					obtenerValorManyToOne(rs,nuevoObjetoDeMiClase,variable);  
       		}
       		
	      rs.close();
	      stmt.close();
	      conn.close();
	      System.out.println("\nNuevo objeto creado");
    		return nuevoObjetoDeMiClase;	      
        } catch (Exception e) {
        	e.printStackTrace();
       	}
		return null;
	}
	//findall
	public static <T> List<T> findAll(Class<T> instancia) throws ClassNotFoundException, SQLException, IOException{
		String nombreDeLaTabla = obtenerNombreTabla(instancia);
		String hql = String.format("SELECT * FROM %s",nombreDeLaTabla);
		Connection conn = hacerConexion(); 
		Statement stmt = conn.createStatement(); 
		System.out.println(hql);
		ResultSet rs = stmt.executeQuery(hql);
		List<T> lista= new ArrayList<T>();
		while (rs.next()) {
			 try {
		      		Constructor<T> constructorSinParametros = instancia.getConstructor(); // agarra constructor	
		      		T nuevoObjetoDeMiClase= constructorSinParametros.newInstance(); // instancia el objeto a partir de constructor
		      		Annotation[] annotations = instancia.getAnnotations();
		      		Field[] listaAtributos = instancia.getDeclaredFields(); // array de atributos
					// mapear los resultados del SQL al objeto creado y retornarlo...
		       		// este metodo de aca agarra cada atributo y dice que ese atributo en el objeto nuevo va a tener el valor sql:
		       		for (Field variable : listaAtributos) {System.out.println("Itero sobre lista de atributos");
		       			if(variable.isAnnotationPresent(Column.class))
		       				obtenerValorColumn(rs,nuevoObjetoDeMiClase,variable);
		       			if(variable.isAnnotationPresent(ManyToOne.class))
		       				//Pregunto si el ManyToOneEsEager
		       				if(variable.getAnnotation(ManyToOne.class).fetchType()==2)
		       					obtenerValorManyToOne(rs,nuevoObjetoDeMiClase,variable);       				
		       		}
		       		

			      System.out.println("\nNuevo objetito creado");
		    	lista.add(nuevoObjetoDeMiClase); 
		        } catch (Exception e) {
		        	e.printStackTrace();
		       	}
		}
	    rs.close();
	    stmt.close();
	    conn.close();
		return lista;
	}
	//
	//Esto no se maesito
	private static Object dameValorClave(Field variable) throws IllegalArgumentException, IllegalAccessException{
		Object valor = null;
		Field[] listaCampos=variable.getClass().getFields();
   		for (Field f : listaCampos) {System.out.println("b");
			if(f.isAnnotationPresent(Id.class))
				valor = variable.getInt(f.getName());
		}
		return valor;
	}
	//
	private static <T> void obtenerValorManyToOne(ResultSet rs, T nuevoObjetoDeMiClase, Field variable) throws ClassNotFoundException, SQLException, IOException, IllegalArgumentException, IllegalAccessException{
		variable.setAccessible(true);
		String nombreId=variable.getAnnotation(ManyToOne.class).columnName();
		String idRelacion=rs.getString(nombreId);
		String clase = variable.getGenericType().getTypeName();
		Class clazz=Class.forName(clase);
		//Object miObjetito= find(clazz,idRelacion);
		System.out.println(clazz.getSimpleName());
		System.out.println(idRelacion);
		variable.set(nuevoObjetoDeMiClase,find(clazz,idRelacion));
	}
	
	private static <T> void obtenerValorColumn(ResultSet rs, T nuevoObjetoDeMiClase, Field variable) throws IllegalAccessException,SQLException
	{
		String nombreVariable=darNombre(variable); 
		Type tipo = variable.getGenericType();
		if (String.class == tipo) {
			variable.set(nuevoObjetoDeMiClase,rs.getString(nombreVariable));
		} else if (int.class == tipo) {
			variable.setInt(nuevoObjetoDeMiClase,rs.getInt(nombreVariable));
		} 
		 else if (java.sql.Date.class == tipo) {
				variable.set(nuevoObjetoDeMiClase,rs.getDate(nombreVariable));
		}
	}
	static String darNombre(Field variable)
	{
		String nombreVariable;
		variable.setAccessible(true);
		boolean col;
		String colValor = null;
		col = variable.isAnnotationPresent(Column.class);
		if(col)
			{colValor = variable.getAnnotation(Column.class).name();}
		if(col && !colValor.equals(""))
			{
			nombreVariable = colValor;
			}
		else
		{
			nombreVariable = variable.getName();
		}
		return nombreVariable;
	}
	
	public static <T> Field obtenerCampoId(Class<T> instancia){
		Field[] fields = instancia.getDeclaredFields(); // agrupa todos los atributos de la clase en una coleccion
		for (Field f : fields) // para todos los atributos de la clase deseada:
		{
			f.setAccessible(true);
			if (f.isAnnotationPresent(Id.class)) // si el atributo tiene annotation id...
			{
				return f;
			}
	}
		return null;
}
	public static <T> String obtenerNombreTabla(Class<T> instancia){ // agrupa todos los atributos de la clase en una coleccion
			Table tabla=instancia.getAnnotation(Table.class);
				return tabla.name(); // retorna nombre de la tabla
}
	public static Connection hacerConexion() throws SQLException, ClassNotFoundException, IOException{
		InputStream input = new FileInputStream("JdbcUtil.properties");
		Properties props = new Properties();
		props.load(input);
		String driver = props.getProperty("jdbc.connection.driver");
		if (driver != null) {
		    Class.forName(driver) ;
		}

		String url = props.getProperty("jdbc.connection.url");
		String username = props.getProperty("jdbc.connection.username");
		String password = props.getProperty("jdbc.connection.password");

		Connection con = DriverManager.getConnection(url, username, password);
		return con;
	}

	//*****************************************************
	// De aca en mas todo con query
	public static <T> List<T> query(Class<T> instancia,String xql,Object ...args) throws ClassNotFoundException, SQLException, IOException{
		List<T> lista= new ArrayList<T>();
		String consulta = xql.replaceAll("\\?", "%s");
		String hql = String.format(consulta,args);
		System.out.println("Consulta :");
		System.out.println(hql);
		Connection conn = hacerConexion(); 
		Statement stmt = conn.createStatement(); 
		ResultSet rs = stmt.executeQuery(hql);
		while (rs.next()) {
			 try {
		      		Constructor<T> constructorSinParametros = instancia.getConstructor(); // agarra constructor	
		      		T nuevoObjetoDeMiClase= constructorSinParametros.newInstance(); // instancia el objeto a partir de constructor
		      		Annotation[] annotations = instancia.getAnnotations();
		      		Field[] listaAtributos = instancia.getDeclaredFields(); // array de atributos
					// mapear los resultados del SQL al objeto creado y retornarlo...
		       		// este metodo de aca agarra cada atributo y dice que ese atributo en el objeto nuevo va a tener el valor sql:
		       		for (Field variable : listaAtributos) {System.out.println("Itero sobre lista de atributos");
		       			if(variable.isAnnotationPresent(Column.class))
		       				obtenerValorColumn(rs,nuevoObjetoDeMiClase,variable);
		       			if(variable.isAnnotationPresent(ManyToOne.class))
		       				//Pregunto si el ManyToOneEsEager
		       				if(variable.getAnnotation(ManyToOne.class).fetchType()==2)
		       					obtenerValorManyToOne2(rs,nuevoObjetoDeMiClase,variable);       				
		       		}
			      System.out.println("\nNuevo objetito creado");
		    	lista.add(nuevoObjetoDeMiClase); 
		        } catch (Exception e) {
		        	e.printStackTrace();
		       	}
		}
	    rs.close();
	    stmt.close();
	    conn.close();
		return lista;
	}
	public static <T> T queryForSingleRow(Class<T> dtoClass,String xql,Object ...args) throws ClassNotFoundException, SQLException, IOException{
		List<T> lista =query(dtoClass,xql,args);
		if(lista.isEmpty())
			return null;
		else
			return lista.get(0);
	}
	public static <T> T find2(Class<T> instancia, Object id) throws ClassNotFoundException, SQLException, IOException{
		String nombreDeLaTabla = obtenerNombreTabla(instancia); // obtiene el nombre de la clase (tabla) en String
		Field clave = obtenerCampoId(instancia); // obtiene cual es el campo que contiene la clave
		String claveValor = darNombre(clave);
		System.out.println(claveValor);
		List<T> lista = query(instancia,"SELECT * FROM ? WHERE ?=?",nombreDeLaTabla,claveValor,id);
		if(lista.isEmpty())
			return null;
		else
			return lista.get(0);
	
	}
	public static <T> List<T> findAll2(Class<T> instancia) throws ClassNotFoundException, SQLException, IOException{
		String nombreDeLaTabla = obtenerNombreTabla(instancia);
		return query(instancia,"SELECT * FROM ?",nombreDeLaTabla);
	}
	private static <T> void obtenerValorManyToOne2(ResultSet rs, T nuevoObjetoDeMiClase, Field variable) throws ClassNotFoundException, SQLException, IOException, IllegalArgumentException, IllegalAccessException{
		variable.setAccessible(true);
		String nombreId=variable.getAnnotation(ManyToOne.class).columnName();
		String idRelacion=rs.getString(nombreId);
		String clase = variable.getGenericType().getTypeName();
		Class clazz=Class.forName(clase);
		//Object miObjetito= find(clazz,idRelacion);
		System.out.println(clazz.getSimpleName());
		System.out.println(idRelacion);
		variable.set(nuevoObjetoDeMiClase,find2(clazz,idRelacion));
	}
	//Mira esa jeva maesito
	public static Transaction beginTransaction() throws ClassNotFoundException, SQLException, IOException{
	return Transaction.getInstance().setConnection(hacerConexion()).desabilitarAutoCommits().hacerStatement();
		//Importante el orden
		
	}
	//We are going to lear jiava lenguage
	public static int insert(Object p) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, SQLException{
		String nombreDeLaTabla = obtenerNombreTabla(p.getClass());
		String xql=String.format("INSERT INTO %s ",nombreDeLaTabla);
		//
		
		//
		Field[] listaAtributos = p.getClass().getDeclaredFields();
		
		//"INSERT INTO TEST_PERSONA (id_persona,nombre,direccion,fecha_alta) VALUES ('1','Nombre', 'Dire','2017-12-31')";
		String nombres="";
		String valores="";
		
		for (Field variable : listaAtributos){
			String formato="%s%s";
			Method getter=dameGetter(p,variable);
			if(variable.isAnnotationPresent(Column.class)){
				if(variable.isAnnotationPresent(Id.class)&&variable.getAnnotation(Id.class).strategy()==0){
				}
				else{
					if(!nombres.isEmpty())
						formato="%s,%s";
					nombres=String.format(formato,nombres,darNombre(variable));
					valores=String.format(formato,valores,getter.invoke(p));
				}
			}
			else if(variable.isAnnotationPresent(ManyToOne.class)){
				if(variable.getAnnotation(ManyToOne.class).fetchType()==2){//2 es tipo eager
					if(!nombres.isEmpty())
						formato="%s,%s";
					String campo=variable.getAnnotation(ManyToOne.class).columnName();
					Object objetito=getter.invoke(p);
					
					String valor = String.format("%s",obtenerClaveForeign(objetito,campo));
					nombres=String.format(formato,nombres,campo);
					valores=String.format(formato,valores,valor);
				}
			}
		}
		valores=prepararValores(valores);
		System.out.println(valores);
		xql=String.format("%s(%s) VALUES %s",xql,nombres,valores);
		System.out.println(xql);
		Transaction.getInstance().getStmt().executeUpdate(xql);
		System.out.println(xql);
		System.out.println("Se agrego");
		return 1;
		
	}
	public static Method dameGetter(Object p,Field variable) throws NoSuchMethodException, SecurityException{
		String nombreVariable=variable.getName();
		String nombreGetter = nombreVariable.substring(0, 1).toUpperCase() + nombreVariable.substring(1); 
		return p.getClass().getMethod("get"+nombreGetter);	
		
	}
	public static String prepararValores(String valores){
		valores=valores.replaceAll(",", "','");
		return String.format("('%s') ",valores);
	}
	public static int insertIfNotExists(Object objetito,String xql,Object ...args) throws ClassNotFoundException, SQLException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		if(queryForSingleRow(objetito.getClass(),xql,args)!=null){
			return 0; // ya existe maesito
		}
		insert(objetito);
		return 1;//mm que rico mae se inserto
	}
	public static Object obtenerClaveForeign(Object p,String campo) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Field[] fields = p.getClass().getDeclaredFields(); 
		for (Field f : fields) 
		{
			f.setAccessible(true);
			if (f.isAnnotationPresent(Column.class)&&f.getAnnotation(Column.class).name().equals(campo))
			{
				Method getter = dameGetter(p,f);
				return getter.invoke(p);
			}
	}
		return null;
	}
	public static Object dameId(Object p) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Field[] fields = p.getClass().getDeclaredFields(); 
		for (Field f : fields) 
		{
			f.setAccessible(true);
			if (f.isAnnotationPresent(Id.class))
			{
				Method getter = dameGetter(p,f);
				return getter.invoke(p);
			}
	}
		return null;
	}

	public static <T> String dameNombreClaveForanea(Class<T> p){
		Field[] fields = p.getDeclaredFields(); 
		for (Field f : fields) 
		{
			f.setAccessible(true);
			if (f.isAnnotationPresent(Id.class))
			{
				return f.getAnnotation(Column.class).name();
			}
	}
		return null;
	}

public static <T> void delete(Class<T> instancia,Object id) throws SQLException{
	String nombreDeLaTabla = obtenerNombreTabla(instancia);
	Field clave = obtenerCampoId(instancia);
	String claveValor = darNombre(clave);
	String xql=String.format("DELETE FROM %s WHERE %s = %s",nombreDeLaTabla,claveValor,id);
	System.out.println(xql);
	Transaction.getInstance().getStmt().executeUpdate(xql);
}
public static <T> void update(Object p) throws SQLException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	
	String nombreDeLaTabla = obtenerNombreTabla(p.getClass());
	Field clave = obtenerCampoId(p.getClass());
	String claveValor = darNombre(clave);
	Object id = dameId(p);
	String xql="";
	
	Field[] fields = p.getClass().getDeclaredFields(); 
	for (Field f : fields) {
		if (f.isAnnotationPresent(Column.class)){
			Method getter = dameGetter(p,f);
			String nombreColumna = f.getAnnotation(Column.class).name();
			Type tipo = f.getGenericType();
			if(String.class==tipo)
				xql=String.format("UPDATE %s SET %s = '%s' WHERE %s = %s",nombreDeLaTabla,nombreColumna,getter.invoke(p),claveValor,id);
			 else if (java.sql.Date.class ==tipo) {
				 //
			}
			else
				xql=String.format("UPDATE %s SET %s = %s WHERE %s = %s",nombreDeLaTabla,nombreColumna,getter.invoke(p),claveValor,id);

			System.out.println(xql);
			Transaction.getInstance().getStmt().executeUpdate(xql);
		}
}
}
}
//
