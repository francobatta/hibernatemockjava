package demo.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;


import pablosz.xpress.*;
import pablosz.xpress.util.Transaction;
import demo.domain.*;
public class Test
{
	@org.junit.Test
	public void testFind() throws IOException,SQLException,InvocationTargetException,NoSuchMethodException,InstantiationException,IllegalAccessException, ClassNotFoundException
	{
		// verifico el find
		Persona p = XPress.find(Persona.class,12);
		Assert.assertEquals(p.getNombre(),"Pablo");

		// ocupacion es LAZY => debe permanecer NULL hasta que haga el get
		Assert.assertNull(p.ocupacion);

		// debe traer el objeto
		Ocupacion o=p.getOcupacion();
		Assert.assertNotNull(o);

		// verifico que lo haya traido bien
		Assert.assertEquals(o.getDescripcion(),"Ingeniero");
		Assert.assertEquals((Integer)p.getOcupacion().getIdOcupacion(),(Integer)4);

		// tipoOcupacion (por default) es EAGER => no debe ser null
		Assert.assertNotNull(o.getTipoOcupacion());
		TipoOcupacion to=o.getTipoOcupacion();

		// verifico que venga bien...
		Assert.assertEquals(to.getDescripcion(),"Profesional");

		// -- Relation --

		// las relaciones son LAZY si o si!
		Assert.assertNull(p.direcciones);

		List<PersonaDireccion> dirs=p.getDirecciones();
		Assert.assertNotNull(dirs);

		// debe tener 2 elementos
		Assert.assertEquals(dirs.size(),2);

		for(PersonaDireccion pd:dirs)
		{
			Persona p1=pd.getPersona();
			Direccion d=pd.getDireccion();

			Assert.assertNotNull(p1);
			Assert.assertNotNull(d);

			Assert.assertEquals(p1.getNombre(),p.getNombre());
		}

	}

	@org.junit.Test
	public void testFindAll() throws IllegalAccessException,InstantiationException,NoSuchMethodException,InvocationTargetException,SQLException,IOException,IllegalArgumentException,
			NoSuchFieldException,ClassNotFoundException
	{
		 List<Persona> lst = XPress.findAll(Persona.class);
		 for(Persona p : lst) {
		 System.out.println(p);
		}
		Ocupacion o=new Ocupacion();
		o.setIdOcupacion(1036);
		o.setDescripcion("Estudiante");

		Persona p=new Persona();
		p.setIdPersona(78);
		p.setNombre("PabloTest");
		p.setOcupacion(o);
		// nuestra dicion
		TipoOcupacion l = new TipoOcupacion();
		l.setDescripcion("asdasdasd");
		l.setIdTipoOcupacion(1008);
		//
		o.setTipoOcupacion(l);
		// fin nuestra adicion
		Transaction trans = XPress.beginTransaction();
		int i=XPress.insert(p);
		trans.commit();
		System.out.println("Se inserto "+i+" registros");
		
		p.setNombre("Julian");
		Transaction trans2 = XPress.beginTransaction();
		i=XPress.update(p);
		trans2.commit();
		System.out.println("Se actualizo "+i+" registros");

		i=XPress.delete(Persona.class,22);
		System.out.println("Se elimino "+i+" registros");
	}
}
