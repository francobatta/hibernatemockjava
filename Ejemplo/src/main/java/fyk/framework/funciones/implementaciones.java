package fyk.framework.funciones;
import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.*;
import java.util.Properties;

import pablosz.xpress.util.*;

import pablosz.xpress.ann.*;

public class implementaciones
{
	public static void main(String[] args)
	{

	}
	
	// funcion que retorna una instancia de T si se encuentra en la fila buscada
	//ESTO ANDA
	public static <T> T find(Class<T> instancia, Object id) throws ClassNotFoundException, SQLException{
		String nombreDeLaTabla = instancia.getSimpleName(); // obtiene el nombre de la clase (tabla) en String
		String clave = obtenerCampoId(instancia); // obtiene cual es el nombre del campo que contiene la clave
		String idString = String.valueOf(id); // convierte el id que pasaron a String para asegurar igualdad
		String hql = String.format("SELECT * FROM %s WHERE %s=%s",nombreDeLaTabla,clave,idString); // string que va a ser pasado como query
		
		Connection conn = hacerConexion(); // conecta con la base de datos
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(hql); // manda query al motor
		
		// creacion del objeto nuevo:
        try {
			
      		Constructor<T> constructorSinParametros = instancia.getConstructor(); // agarra constructor	
      		Object nuevoObjetoDeMiClase= (Object) constructorSinParametros.newInstance(); // instancia el objeto a partir de constructor
      	
      		Field[] listaAtributos = instancia.getDeclaredFields(); // array de atributos
      		Annotation[] anotaciones = instancia.getAnnotations();	// array de annotations

       		System.out.println("\nNuevo objeto creado");

       	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
       		e.printStackTrace();
       	}
		while(rs.next()){
			// mapear los resultados del SQL al objeto creado...
	      }
	      // cierre de la conexion
	      rs.close();
	      stmt.close();
	      conn.close();
		return null;
	}
	
	public static <T> String obtenerCampoId(Class<T> instancia){
		Field[] fields = instancia.getDeclaredFields(); // agrupa todos los atributos de la clase en una coleccion
		for (Field f : fields) // para todos los atributos de la clase deseada:
		{
			if (f.isAnnotationPresent(Id.class)) // si el atributo tiene annotation id...
			{
				return f.getName(); // retorna NOMBRE del campo clave
			}
	}
		return "Tirar error";
}
	public static Connection hacerConexion() throws SQLException, ClassNotFoundException{
		Properties props = new Properties();
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