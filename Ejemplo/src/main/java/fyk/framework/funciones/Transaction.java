package fyk.framework.funciones;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction
{
		Connection conn;
		Statement stmt;
		void setConnection(Connection conexion){
			conn=conexion;
		}
		void desabilitarAutoCommits() throws SQLException{
			conn.setAutoCommit(false);
		}
		void hacerStatement() throws SQLException{
			stmt=conn.createStatement();
		}
		Connection getConn(){
			return conn;
		}
		Statement getStmt(){
			return stmt;
		}
		public void commit() throws SQLException{
			conn.commit();
		}
}		
