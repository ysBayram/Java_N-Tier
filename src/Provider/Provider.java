package Provider;

import java.sql.*;

public class Provider { 

	public static Connection CreateCon(){ 
		Connection Con = null;
		try{
			Class.forName("org.sqlite.JDBC");
			Con = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Mirza\\workspace\\N-Tier_Java\\db\\Ntier.db");
		}
		catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		return Con;
	}

	public static Statement CreateStatement() throws ClassNotFoundException, SQLException{ 
		Statement stmt = CreateCon().createStatement();
		return stmt;
	}

	public static ResultSet CreateResultSet(String sql) throws ClassNotFoundException, SQLException{ 
		if(sql.intern() == "" | sql.intern() == null){
 			return null;
		} else {
			ResultSet rs = CreateStatement().executeQuery(sql);
			return rs;
 		}
	}

}