package fyk.tests;

import java.sql.Date;

import pablosz.xpress.ann.*;

@Table(name="TEST_PERSONA")
public class Persona
{
@Id(strategy=Id.IDENTITY)
@Column(name="id_persona")
private int idPersona;

@Column(name="nombre")
private String nombre;

@Column(name="direccion")
private String direccion;

@Column(name="fecha_alta")
private Date fechaAlta;

public Persona(){}
// otros constructores, setters y getters
}