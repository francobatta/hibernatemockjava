package demo.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import demo.domain.Direccion;
import demo.domain.Ocupacion;
import demo.domain.Persona;
import demo.domain.PersonaDireccion;
import demo.domain.TipoOcupacion;
import pablosz.xpress.XPress;

public class NuestrosTest
{
	Persona p;
	@Before
	public void init() throws ClassNotFoundException, SQLException, IOException {
		p = XPress.find(Persona.class,12);
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
	public void getid(){
		Assert.assertEquals((int)p.getIdPersona(),12);
	}
	@org.junit.Test
	public void laOcupacionEsCorrecta(){
		Assert.assertEquals((Integer)p.getOcupacion().getIdOcupacion(),(Integer)4);
	}
	@org.junit.Test
	public void descripcionDeLaOcupacionesIngeniero(){
		Assert.assertEquals(p.getOcupacion().getDescripcion(),"Ingeniero");
	}
	@org.junit.Test
	public void elNombreDeLaPersonaEsPablo(){
		Assert.assertEquals(p.getNombre(),"Pablo");
	}
	@org.junit.Test
	public void elIdOcupacionEs4(){
		Assert.assertEquals((Integer)p.getOcupacion().getIdOcupacion(),(Integer)4);;
	}
	@org.junit.Test
	public void verificoOcupacionLazyNull(){
		// la operacion es LAZY => debe permanecer en NULL
		Assert.assertNull(p.ocupacion);
	}
	@org.junit.Test
	public void traigoOcupacionLazyYverifico(){
		// me traigo el lazy
		Ocupacion o = p.getOcupacion();
		Assert.assertNotNull(o);
	}
	@org.junit.Test
	public void verificoValorDeOcupacionLazyTraida(){
		// me traigo el lazy
		Ocupacion o = p.getOcupacion();
		Assert.assertNotNull(o);
		Assert.assertEquals(o.getDescripcion(),"Ingeniero");
	}
	@org.junit.Test
	public void ocupacionEagerNoNull(){
		// tipoOcupacion es eager, ya tiene que andar
		Ocupacion o = p.getOcupacion();
		Assert.assertNotNull(o.getTipoOcupacion());
	}
	@org.junit.Test
	public void descripcionTipoOcupacionEagerEsProfesional(){
		// tipoOcupacion es eager, ya tiene que andar
		Ocupacion o = p.getOcupacion();
		TipoOcupacion to = o.getTipoOcupacion();
		Assert.assertEquals(to.getDescripcion(),"Profesional");
	}
	@org.junit.Test
	public void relacionesNullLazy(){
		// las relaciones son LAZY si o si!
		Assert.assertNull(p.direcciones);
	}
	@org.junit.Test
	public void traigoListaOneToMany(){
		List<PersonaDireccion> dirs = p.getDirecciones();
		Assert.assertNotNull(dirs);
	}	
	@org.junit.Test
	public void listaOneToManyTiene2Elementos(){
		List<PersonaDireccion> dirs = p.getDirecciones();
		Assert.assertEquals(dirs.size(),2);
	}	

	@org.junit.Test
	public void miroTodasLasDirecciones() throws ClassNotFoundException, SQLException, IOException
	{		
		List<PersonaDireccion> dirs = p.getDirecciones();
		for(PersonaDireccion pd:dirs)
		{
			Persona p1 = pd.getPersona();
			Direccion d = pd.getDireccion();
			Assert.assertNotNull(p1);
			Assert.assertNotNull(d);
			Assert.assertEquals(p1.getNombre(),p.getNombre());
		}
		
	}
}
