package fyk.framework.funciones;
import java.io.FileInputStream;
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
		Object clave = obtenerCampoId(instancia);
		String hql = String.format("SELECT * FROM %s WHERE %s=%s",instancia,clave,id);
		Connection conn = hacerConexion();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(hql);
		while(rs.next()){
	         // crear instancia con lo encontrado?
	         Field[] listaAtributos = instancia.getFields();
	         try {
	        		Constructor<T> constructorSinParametros = instancia.getConstructor(); // agarra que clase es
	        				
	        		Object nuevoObjetoDeMiClase=constructorSinParametros.newInstance(); // instancia el objeto

	        		// faltaria setear todos los atributos

	        		System.out.println("\nNuevo objeto creado");

	        	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException | NoSuchMethodException e) {
	        		e.printStackTrace();
	        	}
	      }
	      // cierre de la conexion
	      rs.close();
	      stmt.close();
	      conn.close();
		return null;
	}
	
	public static <T> Object obtenerCampoId(Class<T> instancia){
		Field[] fields = instancia.getFields(); // agrupa todos los atributos de la clase en una coleccion
		for (Field f : fields) // para todos los atributos de la clase deseada:
		{
			if (f.isAnnotationPresent(Id.class)) // si el atributo tiene annotation id...
			{
				return f; // retorna el campo clave
			}
	}
		return -1;
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