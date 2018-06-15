import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import fyk.framework.funciones.*;
import fyk.tests.*;
import pablosz.xpress.ann.*;
import pablosz.xpress.util.*;
public class main
{
	public static void creoTablas() throws ClassNotFoundException, SQLException, IOException
	{
		Connection conn = implementaciones.hacerConexion();
		Statement stmt = conn.createStatement(); // conecta
		String hql = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS";
		boolean estoyConectado = stmt.execute(hql);
        hql = "DROP TABLE Usuario_rejemplo IF EXISTS";
        stmt.executeUpdate(hql);
        hql = "DROP TABLE TEST_PERSONA IF EXISTS";
        stmt.executeUpdate(hql);
        hql = "CREATE TABLE TEST_PERSONA (id_persona int IDENTITY PRIMARY KEY,nombre nvarchar(100),direccion nvarchar(100),fecha_alta date)";
        stmt.executeUpdate(hql);
        hql = "INSERT INTO TEST_PERSONA (nombre,direccion,fecha_alta) VALUES ('Nombre', 'Dire','2017-12-31')";
        stmt.executeUpdate(hql);
        hql = "CREATE TABLE Usuario_rejemplo (idUsuario int IDENTITY PRIMARY KEY,username nvarchar(100),password nvarchar(100),id_persona int, "+
         " FOREIGN KEY(id_persona) REFERENCES TEST_PERSONA)";
        stmt.executeUpdate(hql);
        hql = "INSERT INTO Usuario_rejemplo (username,password,id_persona) VALUES ('miUser', 'miPass','0')";
        stmt.executeUpdate(hql);
        return;
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		creoTablas();
		Usuario_rejemplo u = new Usuario_rejemplo();
		Persona p = new Persona();
		List<Usuario_rejemplo> lista;
		
		//otro test
		GregorianCalendar gc = new GregorianCalendar(1996,1,1);
		Date d = new Date(gc.getTimeInMillis());
		Persona p2 = new Persona("Willy","Urquiza",d);
		Transaction trans = implementaciones.beginTransaction();
		// el segundo parametro de insert esta de "mas" trans deberia ser global pero no se me
		//
		implementaciones.insert(p2);
		trans.commit();
		
		Persona p3 = implementaciones.find2(Persona.class,1);
		System.out.println(p3.getNombre());
		System.out.println(p3.getFechaAlta());
		/*try
		{
			
			// Esto anda 
			lista = implementaciones.findAll2(Usuario_rejemplo.class);
			System.out.println(lista.get(0).getClass().getSimpleName());
			p = implementaciones.find2(Persona.class,1);
			if(p==null)
			System.out.println("No se encontro");
			else
				System.out.println(p.getClass().getSimpleName());
			
		}
		catch(SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

}
