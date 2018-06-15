package fyk.tests;
import pablosz.xpress.ann.*;

@Table(name="Usuario_rejemplo")
public class Usuario_rejemplo
{
public Usuario_rejemplo(String username,String password,Persona persona)
	{
		this.username=username;
		this.password=password;
		this.persona=persona;
	}
public String getPassword()
{
	return password;
}
public Persona getPersona()
{
	return persona;
}
public void setPersona(Persona persona)
{
	this.persona=persona;
}
@Id(strategy=Id.IDENTITY)
@Column
private int idUsuario;
@Column
private String username;
@Column
private String password;
@ManyToOne(columnName="id_persona")
private Persona persona;

public int getIdUsuario(){
	return idUsuario;
}
public String getUsername(){
	return username;
}
public void setIdUsuario(int id){
	this.idUsuario = id;
}
public void setUsername(String user){
	this.username = user;
}
public void setPassword(String pass){
	this.password = pass;
}
}