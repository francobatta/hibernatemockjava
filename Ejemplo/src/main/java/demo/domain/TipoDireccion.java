package demo.domain;

import pablosz.xpress.ann.Column;
import pablosz.xpress.ann.Id;
import pablosz.xpress.ann.Table;

@Table(name="tipo_direccion")
public class TipoDireccion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_tipo_direccion")
	private int idTipoDireccion;
	
	@Column(name="descripcion")
	private String descripcion;

	public int getIdTipoDireccion()
	{
		return idTipoDireccion;
	}

	public void setIdTipoDireccion(int idTipoDireccion)
	{
		this.idTipoDireccion=idTipoDireccion;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion=descripcion;
	}

	public boolean equals(Object o)
	{
		TipoDireccion other = (TipoDireccion)o;
		return other.getIdTipoDireccion()==this.getIdTipoDireccion()
			&& other.getDescripcion().equals(getDescripcion());
	}
	
	public String toString()
	{
		return getDescripcion();
	}
}
