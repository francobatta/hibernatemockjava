package fyk.tests;

import java.sql.Date;

import pablosz.xpress.ann.*;

@Table(name="TEST_PERSONA")
public class Persona
{
public Persona(String nombre,String direccion,Date fechaAlta)
	{
		this.nombre=nombre;
		this.direccion=direccion;
		this.fechaAlta=fechaAlta;
	}
@Id(strategy=Id.IDENTITY)
@Column(name="id_persona")
private int idPersona;

@Column(name="nombre")
private String nombre;

@Column(name="direccion")
private String direccion;

@Column(name="fecha_alta")
private Date fechaAlta;

public String getDireccion(){
	return direccion;
}
public Date getFechaAlta(){
	return fechaAlta;
}
public String getNombre(){
	return nombre;
}
public int getIdPersona(){
	return idPersona;
}
public void setNombre(String nombre){
	this.nombre=nombre;
}
public Persona(){}
// otros constructores, setters y getters
}