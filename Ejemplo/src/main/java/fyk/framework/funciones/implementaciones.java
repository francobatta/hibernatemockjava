package fyk.framework.funciones;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.Properties;
import java.util.jar.Attributes.Name;

import fyk.tests.Persona;
import pablosz.xpress.util.*;

import pablosz.xpress.ann.*;
public class implementaciones
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
      		T nuevoObjetoDeMiClase= constructorSinParametros.newInstance(); // instancia el objeto a partir de constructor
      		Annotation[] annotations = instancia.getAnnotations();
      		
      		Field[] listaAtributos = instancia.getDeclaredFields(); // array de atributos
       		rs.next();
			// mapear los resultados del SQL al objeto creado y retornarlo...
       		// este metodo de aca agarra cada atributo y dice que ese atributo en el objeto nuevo va a tener el valor sql:
       		for (Field variable : listaAtributos) {System.out.println("Itero sobre lista de atributos");
       			if(variable.isAnnotationPresent(Column.class))
       				obtenerValorColumn(rs,nuevoObjetoDeMiClase,variable);
       			if(variable.isAnnotationPresent(ManyToOne.class))
       				obtenerValorManyToOne(rs,nuevoObjetoDeMiClase,variable);
       		}
	      rs.close();
	      stmt.close();
	      conn.close();
	      System.out.println("\nNuevo objeto creado");
    		return nuevoObjetoDeMiClase;	      
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
       		e.printStackTrace();
       	}
		return null;
	}
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
	}
	private static String darNombre(Field variable)
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
}
