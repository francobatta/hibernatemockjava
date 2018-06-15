package fyk.framework.funciones;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction
{		
		public static Transaction trans;
		Connection conn;
		Statement stmt;
		Transaction setConnection(Connection conexion){
			conn=conexion;
			return trans;
		}
		Transaction desabilitarAutoCommits() throws SQLException{
			conn.setAutoCommit(false);
			return trans;
		}
		Transaction hacerStatement() throws SQLException{
			stmt=conn.createStatement();
			return trans;
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
		public static Transaction getInstance(){
			if(trans == null) {
				trans = new Transaction();
			}
			return trans;
		}
}		
