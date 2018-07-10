package pablosz.xpress.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction
{		
		public static Transaction trans;
		Connection conn;
		Statement stmt;
		public Transaction setConnection(Connection conexion){
			conn=conexion;
			return trans;
		}
		public Transaction desabilitarAutoCommits() throws SQLException{
			conn.setAutoCommit(false);
			return trans;
		}
		public Transaction hacerStatement() throws SQLException{
			stmt=conn.createStatement();
			return trans;
		}
		public Connection getConn(){
			return conn;
		}
		public Statement getStmt(){
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
