package demo.domain;

import java.util.List;

import pablosz.xpress.ann.*;

@Table(name="persona")
public class Persona
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_persona")
	public int idPersona;
	
	@Column
	public String nombre;
		
	@Column(fetchType=Column.LAZY)
	public Ocupacion ocupacion;

	@OneToMany(mappedBy="persona" )
	public List<PersonaDireccion> direcciones;
	
	public List<PersonaDireccion> getDirecciones()
	{
		return direcciones;
	}

	public void setDirecciones(List<PersonaDireccion> direcciones)
	{
		this.direcciones=direcciones;
	}

	public int getIdPersona()
	{
		return idPersona;
	}

	public void setIdPersona(int idPersona)
	{
		this.idPersona=idPersona;
	}

	public String getNombre()
	{
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre=nombre;
	}

	public Ocupacion getOcupacion()
	{
		return ocupacion;
	}

	public void setOcupacion(Ocupacion ocupacion)
	{
		this.ocupacion=ocupacion;
	}

	@Override
	public boolean equals(Object o)
	{
		Persona other = (Persona)o;
		return other.getIdPersona()==this.getIdPersona()
			&& other.getNombre().equals(getNombre())
			&& other.getOcupacion().equals(getOcupacion());
	}
	
	public String toString()
	{
		return getNombre();
	}
}
