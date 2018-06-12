package fyk.framework.funciones;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.Properties;

import pablosz.xpress.util.*;

import pablosz.xpress.ann.*;

public class implementaciones
{
	
	// funcion que retorna una instancia de T si se encuentra en la fila buscada
	// otra solucion que se me ocurrio es ir tirando un string a la base de datos para cada atributo y en vez de usar SELECT *
	// usar SELECT nombreAtributo FROM... y asi ir armando y toda la info rescatada del rs iria directamente al valor del set
	public static <T> T find(Class<T> instancia, Object id) throws ClassNotFoundException, SQLException, IOException{
		String nombreDeLaTabla = obtenerNombreTabla(instancia); // obtiene el nombre de la clase (tabla) en String
		String clave = obtenerCampoId(instancia); // obtiene cual es el nombre del campo que contiene la clave
		String idString = String.valueOf(id); // convierte el id que pasaron a String para asegurar igualdad
		String hql = String.format("SELECT * FROM %s WHERE %s=%s",nombreDeLaTabla,clave,idString); // string que va a ser pasado como query
		System.out.println(hql);
		Connection conn = hacerConexion(); // conecta con la base de datos
		Statement stmt = conn.createStatement(); // conecta
		ResultSet rs = stmt.executeQuery(hql); // devuelve en rs todos los datos que matchean (en este caso 1 registro entero)
		
		// creacion del objeto nuevo:
        try {
      		Constructor<T> constructorSinParametros = instancia.getConstructor(); // agarra constructor	
      		T nuevoObjetoDeMiClase= constructorSinParametros.newInstance(); // instancia el objeto a partir de constructor
      	
      		Field[] listaAtributos = instancia.getDeclaredFields(); // array de atributos
 		
       		System.out.println("\nNuevo objeto creado");
       		rs.next();
			// mapear los resultados del SQL al objeto creado y retornarlo...
       		// este metodo de aca agarra cada atributo y dice que ese atributo en el objeto nuevo va a tener el valor sql:
       		for (Field variable : listaAtributos) {
       			variable.setAccessible(true);
       			String nombreVariable = variable.getName();
       			Type tipo = variable.getGenericType();
       			if (String.class == tipo) {
					variable.set(nuevoObjetoDeMiClase,rs.getString(nombreVariable));
				} else if (int.class == tipo) {
					variable.setInt(nuevoObjetoDeMiClase,rs.getInt(nombreVariable));
				}
       		}
       		// listaAtributos[cantAtributos].set(nuevoObjetoDeMiClase, "valor que va a tener cada atributo desde sql");
       		// cantAtributos--;
       		// nota: si esto fuera 1 solo atributo, no habria que contemplar que valor, porque el SQL devolveria ya todo
       		// el valor del atributo y nada mas. Aca devuelve todo un registro entero y quizas sea mejor cambiarlo
       		// asi no hacemos lista de atributos sino que iteramos dentro para cada atributo con varios strings SELECT SQL

	      // cierre de la conexion
	      rs.close();
	      stmt.close();
	      conn.close();
    		return nuevoObjetoDeMiClase;	      
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
       		e.printStackTrace();
       	}
		return null;
	}
	
	public static <T> String obtenerCampoId(Class<T> instancia){
		Field[] fields = instancia.getDeclaredFields(); // agrupa todos los atributos de la clase en una coleccion
		for (Field f : fields) // para todos los atributos de la clase deseada:
		{
			f.setAccessible(true);
			if (f.isAnnotationPresent(Id.class)) // si el atributo tiene annotation id...
			{
				return f.getName(); // retorna NOMBRE del campo clave
			}
	}
		return "Tirar error";
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
