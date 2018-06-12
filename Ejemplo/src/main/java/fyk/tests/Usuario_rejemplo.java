package fyk.tests;
import pablosz.xpress.ann.*;

@Table(name="Usuario_rejemplo")
public class Usuario_rejemplo
{
@Id(strategy=Id.IDENTITY)
@Column
private int idUsuario;
@Column
private String username;
@Column
private String password;
@ManyToOne(columnName="id_persona")
private Persona persona;

int getIdUsuario(){
	return idUsuario;
}
String getUsername(){
	return username;
}

void setIdUsuario(int id){
	this.idUsuario = id;
}
void setUsername(String user){
	this.username = user;
}
void setPassword(String pass){
	this.password = pass;
}
}