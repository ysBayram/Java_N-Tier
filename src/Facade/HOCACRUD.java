package Facade;

import java.sql.*;
import Provider.*;
import Entity.*;

public class HOCACRUD {

	public static void Insert(HOCA p ) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("INSERT INTO HOCA (AD,SOY,DERS) VALUES ('"+p.AD+"','"+p.SOY+"','"+p.DERS+"')");
		stmt.close();
		stmt.getConnection().close();
	}

	public static void Update(HOCA p ) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("UPDATE HOCA SET AD = '"+p.AD+"',SOY = '"+p.SOY+"',DERS = '"+p.DERS+"' WHERE ID = '"+p.ID+"'");
		stmt.close();
		stmt.getConnection().close();
	}

	public static void Delete(int ID) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("DELETE FROM HOCA WHERE ID = '"+ID+"'");
		stmt.close();
		stmt.getConnection().close();
	}

	public static HOCA getHOCAwithID (int ID) throws ClassNotFoundException, SQLException
	{
		ResultSet rs = Provider.CreateResultSet("SELECT * FROM HOCA WHERE ID = '"+ID+"'");
		if(rs != null){
			HOCA p = new HOCA();
			p.ID = rs.getInt("ID");
			p.AD = rs.getString("AD");
			p.SOY = rs.getString("SOY");
			p.DERS = rs.getString("DERS");
			return p;
		}
		else {return null;}
	}

}