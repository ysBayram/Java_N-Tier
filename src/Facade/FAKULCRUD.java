package Facade;

import java.sql.*;
import Provider.*;
import Entity.*;

public class FAKULCRUD {

	public static void Insert(FAKUL p ) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("INSERT INTO FAKUL (AD,BOLUM) VALUES ('"+p.AD+"','"+p.BOLUM+"')");
		stmt.close();
		stmt.getConnection().close();
	}

	public static void Update(FAKUL p ) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("UPDATE FAKUL SET AD = '"+p.AD+"',BOLUM = '"+p.BOLUM+"' WHERE ID = '"+p.ID+"'");
		stmt.close();
		stmt.getConnection().close();
	}

	public static void Delete(int ID) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("DELETE FROM FAKUL WHERE ID = '"+ID+"'");
		stmt.close();
		stmt.getConnection().close();
	}

	public static FAKUL getFAKULwithID (int ID) throws ClassNotFoundException, SQLException
	{
		ResultSet rs = Provider.CreateResultSet("SELECT * FROM FAKUL WHERE ID = '"+ID+"'");
		if(rs != null){
			FAKUL p = new FAKUL();
			p.ID = rs.getInt("ID");
			p.AD = rs.getString("AD");
			p.BOLUM = rs.getString("BOLUM");
			return p;
		}
		else {return null;}
	}

}