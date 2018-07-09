package demo.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import demo.domain.Direccion;
import demo.domain.Ocupacion;
import demo.domain.Persona;
import demo.domain.PersonaDireccion;
import demo.domain.TipoOcupacion;
import pablosz.xpress.XPress;

public class Test
{
	Persona p;
	@Before
	public void init() throws ClassNotFoundException, SQLException, IOException {
		p = XPress.find2(Persona.class,12);
	}
	@org.junit.Test
	public void elNombreEsCorrecto(){
		Assert.assertEquals(p.getNombre(),"Pablo");
	}
	
	@org.junit.Test
	public void estaEnLazy(){
		Assert.assertNull(p.ocupacion);
	}
	@org.junit.Test
	public void elObjetoEsNoNulo(){
		Ocupacion o = p.getOcupacion();
		Assert.assertNotNull(o);
	}
	
	@org.junit.Test
	public void laOcupacionEsCorrecta(){
		Assert.assertEquals((Integer)p.getOcupacion().getIdOcupacion(),(Integer)4);
	}
	@org.junit.Test
	public void verificoQueLoHayaTraidoBien(){
		Assert.assertEquals(p.getOcupacion().getDescripcion(),"Ingeniero");
	}
	//@org.junit.Test
	/*public void testFind() throws ClassNotFoundException, SQLException, IOException
	{
		
		// verifico el find
		Assert.assertEquals(p.getNombre(),"Pablo");
		Assert.assertEquals((Integer)p.getOcupacion().getIdOcupacion(),(Integer)4);

		// ocupacion es LAZY => debe permanecer NULL hasta que haga el get
		Assert.assertNull(p.ocupacion);

		// debe traer el objeto
		Ocupacion o = p.getOcupacion();
		Assert.assertNotNull(o);
	
		// verifico que lo haya traido bien
		Assert.assertEquals(o.getDescripcion(),"Ingeniero");
	
		// tipoOcupacion (por default) es EAGER => no debe ser null
		Assert.assertNotNull(o.getTipoOcupacion());
		TipoOcupacion to = o.getTipoOcupacion();
		
		// verifico que venga bien...
		 TipoOcupacion to = o.getTipoOcupacion();
		Assert.assertEquals(to.getDescripcion(),"Profesional");
		
		// -- Relation --
		
		// las relaciones son LAZY si o si!
		Assert.assertNull(p.direcciones);
		
		List<PersonaDireccion> dirs = p.getDirecciones();
		Assert.assertNotNull(dirs);
		
		// debe tener 2 elementos
		Assert.assertEquals(dirs.size(),2);
		
		for(PersonaDireccion pd:dirs)
		{
			Persona p1 = pd.getPersona();
			Direccion d = pd.getDireccion();
			
			Assert.assertNotNull(p1);
			Assert.assertNotNull(d);
		
			Assert.assertEquals(p1.getNombre(),p.getNombre());
		}
		
	}
	*/
}
