package demo.domain;

import pablosz.xpress.ann.Column;
import pablosz.xpress.ann.Id;
import pablosz.xpress.ann.ManyToOne;
import pablosz.xpress.ann.Table;

@Table(name="persona_direccion")
public class PersonaDireccion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_persona_direccion")
	public int idPersonaDireccion;
	
	@ManyToOne(columnName="id_persona")
	public Persona persona;
	
	@ManyToOne(columnName="id_direccion")
	public Direccion direccion;
	
	@ManyToOne(columnName="id_tipo_direccion")
	public TipoDireccion tipoDireccion;
	
	public TipoDireccion getTipoDireccion()
	{
		return tipoDireccion;
	}

	public void setTipoDireccion(TipoDireccion tipoDireccion)
	{
		this.tipoDireccion=tipoDireccion;
	}

	public int getIdPersonaDireccion()
	{
		return idPersonaDireccion;
	}

	public void setIdPersonaDireccion(int idPersonaDireccion)
	{
		this.idPersonaDireccion=idPersonaDireccion;
	}

	public Persona getPersona()
	{
		return persona;
	}

	public void setPersona(Persona persona)
	{
		this.persona=persona;
	}

	public Direccion getDireccion()
	{
		return direccion;
	}

	public void setDireccion(Direccion direccion)
	{
		this.direccion=direccion;
	}

	@Override
	public boolean equals(Object o)
	{
		PersonaDireccion other = (PersonaDireccion)o;		
		return other.getIdPersonaDireccion()==this.getIdPersonaDireccion()
			&& other.getPersona().equals(getPersona())
			&& other.getDireccion().equals(getDireccion());
	}	
}
