package Facade;

import java.sql.*;
import Provider.*;
import Entity.*;

public class OGRCRUD {

	public static void Insert(OGR p ) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("INSERT INTO OGR (AD,SOYAD,NO) VALUES ('"+p.AD+"','"+p.SOYAD+"','"+p.NO+"')");
		stmt.close();
		stmt.getConnection().close();
	}

	public static void Update(OGR p ) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("UPDATE OGR SET AD = '"+p.AD+"',SOYAD = '"+p.SOYAD+"',NO = '"+p.NO+"' WHERE ID = '"+p.ID+"'");
		stmt.close();
		stmt.getConnection().close();
	}

	public static void Delete(int ID) throws ClassNotFoundException, SQLException
	{
		Statement stmt = Provider.CreateStatement();
		stmt.executeUpdate("DELETE FROM OGR WHERE ID = '"+ID+"'");
		stmt.close();
		stmt.getConnection().close();
	}

	public static OGR getOGRwithID (int ID) throws ClassNotFoundException, SQLException
	{
		ResultSet rs = Provider.CreateResultSet("SELECT * FROM OGR WHERE ID = '"+ID+"'");
		if(rs != null){
			OGR p = new OGR();
			p.ID = rs.getInt("ID");
			p.AD = rs.getString("AD");
			p.SOYAD = rs.getString("SOYAD");
			p.NO = rs.getString("NO");
			return p;
		}
		else {return null;}
	}

}