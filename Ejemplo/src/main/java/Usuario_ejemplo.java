import pablosz.xpress.ann.*;

@Table(name="TEST_USUARIO")
public class Usuario_ejemplo
{
@Id(strategy=Id.IDENTITY)
@Column
private int idUsuario;
@Column
public String username;
@Column
public String password;


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