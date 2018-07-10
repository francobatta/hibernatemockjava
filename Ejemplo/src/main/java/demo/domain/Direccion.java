package demo.domain;

import java.util.List;

import pablosz.xpress.ann.*;

@Table(name="direccion")
public class Direccion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_direccion")
	public int idDireccion;

	@Column(name="calle")
	public String calle;

	@Column(name="numero")
	public int numero;
	
	@OneToMany(mappedBy="direccion")
	public List<PersonaDireccion> personas;

	public List<PersonaDireccion> getPersonas()
	{
		return personas;
	}

	public void setPersonas(List<PersonaDireccion> personas)
	{
		this.personas=personas;
	}

	public int getIdDireccion()
	{
		return idDireccion;
	}

	public void setIdDireccion(int idDireccion)
	{
		this.idDireccion=idDireccion;
	}

	public String getCalle()
	{
		return calle;
	}

	public void setCalle(String calle)
	{
		this.calle=calle;
	}

	public int getNumero()
	{
		return numero;
	}

	public void setNumero(int numero)
	{
		this.numero=numero;
	}

	@Override
	public String toString()
	{
		return getCalle()+" "+getNumero();
	}

	@Override
	public boolean equals(Object obj)
	{
		Direccion other=(Direccion)obj;
		return other.getIdDireccion()==this.getIdDireccion()
			&& other.getCalle().equals(getCalle())
			&& other.getNumero()==this.getNumero();
	}






}
