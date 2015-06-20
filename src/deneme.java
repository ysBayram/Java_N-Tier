import Provider.*;
import Entity.*;
import Facade.OGRCRUD;

import java.sql.*;

import javax.swing.JOptionPane;

public class deneme{
	
	public static void anavoid() throws ClassNotFoundException, SQLException{
		
		/*
		OGR gb = new OGR();
		gb.AD =JOptionPane.showInputDialog(null,"Öðrenci Ad", "Ad Gir",JOptionPane.DEFAULT_OPTION);
		gb.SOYAD =JOptionPane.showInputDialog(null,"Öðrenci SoyAd", "soyAd Gir",JOptionPane.DEFAULT_OPTION);
		gb.NO =JOptionPane.showInputDialog(null,"Öðrenci No", "No Gir",JOptionPane.DEFAULT_OPTION);
		gb.ID = Integer.parseInt(JOptionPane.showInputDialog(null,"Öðrenci ID", "ID Gir",JOptionPane.DEFAULT_OPTION));
		
		OGRCRUD.Insert(gb);
		OGRCRUD.Update(gb);
		OGRCRUD.Delete(1);
		
		ResultSet rs = Provider.CreateResultSet("SELECT * FROM OGR");
		while(rs.next())
		{
			String Tname = rs.getString("AD");
			System.out.println(Tname);
		}
		rs.close();
		rs.getStatement().getConnection().close();
		*/
		int id = Integer.parseInt(JOptionPane.showInputDialog(null,"Öðrenci ID", "ID Gir",JOptionPane.DEFAULT_OPTION));
		OGR og = OGRCRUD.getOGRwithID(id);
		JOptionPane.showMessageDialog(null, og.ID);
		JOptionPane.showMessageDialog(null, og.AD);
		JOptionPane.showMessageDialog(null, og.SOYAD);
		JOptionPane.showMessageDialog(null, og.NO);
	}
}