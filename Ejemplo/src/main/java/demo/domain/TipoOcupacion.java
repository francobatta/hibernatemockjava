package demo.domain;

import pablosz.xpress.ann.Column;
import pablosz.xpress.ann.Id;
import pablosz.xpress.ann.Table;

@Table(name="tipo_ocupacion")
public class TipoOcupacion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_tipoocupacion")
	public int idTipoOcupacion;

	@Column(name="descripcion")
	public String descripcion;
	
	public int getIdTipoOcupacion()
	{
		return idTipoOcupacion;
	}

	public void setIdTipoOcupacion(int idTipoOcupacion)
	{
		this.idTipoOcupacion=idTipoOcupacion;
	}

	public String getDescripcion()
	{
		return descripcion;
	}

	public void setDescripcion(String descripcion)
	{
		this.descripcion=descripcion;
	}

	@Override
	public String toString()
	{
		return getDescripcion();
	}

	@Override
	public boolean equals(Object obj)
	{
		TipoOcupacion other=(TipoOcupacion)obj;
		return other.getIdTipoOcupacion()==this.getIdTipoOcupacion()
			&& other.getDescripcion().equals(getDescripcion());
	}
	
	
}
