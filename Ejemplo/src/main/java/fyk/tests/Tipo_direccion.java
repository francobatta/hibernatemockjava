package fyk.tests;

import pablosz.xpress.ann.Column;
import pablosz.xpress.ann.Id;
import pablosz.xpress.ann.Table;

@Table(name="tipo_direccion")
public class Tipo_direccion
{
	@Id(strategy=Id.IDENTITY)
	@Column(name="id_tipo_direccion")
	private int idPersona;
}
