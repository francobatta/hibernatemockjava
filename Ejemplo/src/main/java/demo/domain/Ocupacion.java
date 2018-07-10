package demo.domain;

import java.util.List;

import pablosz.xpress.ann.*;

@Table(name="ocupacion")
public class Ocupacion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_ocupacion")
	private int idOcupacion;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="id_tipoocupacion")
	public TipoOcupacion tipoOcupacion;

	@OneToMany(mappedBy="ocupacion")
	public List<Persona> personas;
	
	public List<Persona> getPersonas()
	{
		return personas;
	}

	public void setPersonas(List<Persona> personas)
	{
		this.personas=personas;
	}

	public int getIdOcupacion()
	{
		return idOcupacion;
	}

	public void setIdOcupacion(int idOcupacion)
	{
		this.idOcupacion=idOcupacion;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion=descripcion;
	}

	public TipoOcupacion getTipoOcupacion()
	{
		return tipoOcupacion;
	}

	public void setTipoOcupacion(TipoOcupacion tipoOcupacion)
	{
		this.tipoOcupacion=tipoOcupacion;
	}

	@Override
	public String toString()
	{
		return getDescripcion();
	}

	@Override
	public boolean equals(Object o)
	{
		Ocupacion other = (Ocupacion)o;		
		return other.getIdOcupacion()==this.getIdOcupacion()
			&& other.getDescripcion().equals(getDescripcion())
			&& other.getTipoOcupacion().equals(getTipoOcupacion());
	}	
}
