import java.sql.*;//use all database process
import java.util.*;//to crate ArrayList
import java.io.*;//create and investigate file and folder process
import javax.swing.*;//create JFileChooser and JOptionPane

public class AnaEkran {

	//public variables
	public static String dbDirectory;//our database which we create its classes and objects
	public static String ProjectName;//project name :)
	public static Connection dbConnection;//connection of our selected database which on "dbDirectory" path

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try
		{
			MainProcess();
		} 
		catch (Exception e) 
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	private static void MainProcess(){
		try {	
			ProjectName = JOptionPane.showInputDialog(null,"Enter Project Name","Project Name",JOptionPane.QUESTION_MESSAGE);//identify project name
			JFileChooser dbChooser = new JFileChooser();//create chooser for choose database
			if(dbChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				File db = dbChooser.getSelectedFile();
				dbDirectory = db.getAbsolutePath();
			}
			else
			{
				System.exit(0);
			}
			//if our selection is null then program will be terminated but if not "dbDirectory" variable is identified
			
			Class.forName("org.sqlite.JDBC");//add sqlite JDBC class
			dbConnection = DriverManager.getConnection("jdbc:sqlite:"+ dbDirectory);// identify our "dbConnection" with our sqlite.JDBC and "dbDirectory"
						
			ArrayList<String> T_Names = new ArrayList<String>();//Database Tables name array list
			
			Statement stmt = dbConnection.createStatement();//Statement for get information about selected database
			ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_master");//ResultSet which is filled by statement's query
			while (rs.next()) {
				String Tname = rs.getString("tbl_name");
				if (!Tname.equals("sqlite_sequence")) {
					T_Names.add(Tname);
				}
			}//investigate result set and get tables name then add names to ArrayList which is name T_Names
			
			//Create directories		
			File dirProvider = new File("C:\\JAVA_NTier\\" + ProjectName + "\\Provider");
			File dirEntity = new File("C:\\JAVA_NTier\\" + ProjectName + "\\Entity");
			File dirFacade = new File("C:\\JAVA_NTier\\" + ProjectName + "\\Facade");
			if(dirEntity.exists() & dirFacade.exists() & dirProvider.exists())
			{System.out.println("Directory is Already Created.");}
			else{
				if(dirEntity.mkdirs() & dirFacade.mkdirs() & dirProvider.mkdirs())
					{System.out.println("Create Directory is Successful.");}			
				else{
					System.out.println("Create Directory is Failed.");}
			}// if folder exist or not these codes give report for situation
			
			//First Create Provider 
			CreateProvider();
			//Sending Table Names To Create Tier Method
			Create_Tier(T_Names);
			
			rs.close();//ResultSet close
			stmt.close();//Statement close
			dbConnection.close(); //Connection close
			System.out.println("Process is successful.");//Success message :)		
			
		} 
		catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
			
	}
	
	private static void CreateProvider() throws FileNotFoundException{
		//Create Provider 
		String Provider = "";
		Provider += "package Provider;\n\n";
		Provider += "import java.sql.*;\n\n";
		Provider += "public class Provider { \n\n";
		Provider += "\tpublic static Connection CreateCon(){ \n";
		Provider += "\t\tConnection Con = null;\n";
		Provider += "\t\ttry{\n";
		Provider += "\t\t\tClass.forName(\"org.sqlite.JDBC\");\n";
		Provider += "\t\t\tCon = DriverManager.getConnection(\"jdbc:sqlite:" + dbDirectory.replace("\\" ,"\\\\" ) + "\");\n";
		Provider += "\t\t}\n";
		Provider += "\t\tcatch (Exception e) {\n";
		Provider += "\t\t\tSystem.err.println(e.getClass().getName() + \": \" + e.getMessage());\n";
		Provider += "\t\t\tSystem.exit(0);\n";
		Provider += "\t\t}\n";
		Provider += "\t\treturn Con;\n";
		Provider += "\t}\n\n";
		Provider += "\tpublic static Statement CreateStatement() throws ClassNotFoundException, SQLException{ \n";
		Provider += "\t\tStatement stmt = CreateCon().createStatement();\n";
		Provider += "\t\treturn stmt;\n\t}\n\n";		
		Provider += "\tpublic static ResultSet CreateResultSet(String sql) throws ClassNotFoundException, SQLException{ \n";
		Provider += "\t\tif(sql.intern() == \"\" | sql.intern() == null){\n \t\t\treturn null;\n";
		Provider += "\t\t} else {\n";
		Provider += "\t\t\tResultSet rs = CreateStatement().executeQuery(sql);\n";
		Provider += "\t\t\treturn rs;\n \t\t}\n\t}\n\n";
		Provider += "}";
		
		File ProviderFile = new File("C:\\JAVA_NTier\\" + ProjectName + "\\Provider\\Provider.java" );
		PrintWriter ProviderPrint = new PrintWriter(ProviderFile);
		ProviderPrint.print(Provider);
		ProviderPrint.close();
	}

	private static void Create_Tier(ArrayList<String> Table_Names) {
		try {
			Statement stmt = dbConnection.createStatement();

			for (int i = 0; i < Table_Names.size(); i++) {
				ResultSet rs = stmt.executeQuery("PRAGMA table_info("+ Table_Names.get(i) + ")");//get all information (such as column names, types and  primary key or not) about table which comes from Main Process.
				
				//Entity Tier Creating...
				String Entity="";
				Entity = "package Entity;\n\n";
				Entity += "public class "+ Table_Names.get(i) +" {\n";							
				
				while (rs.next()) {	
					//Get Coloumn Type and Change
					String GetType = SetType(rs.getString("type"),"Entity");				
					String cName = rs.getString("name");				
					Entity += "\tpublic " + GetType + " " + cName+";\n";					
				}									
				Entity+="}";				
				File EntityFile = new File("C:\\JAVA_NTier\\" + ProjectName + "\\Entity\\" + Table_Names.get(i) +".java" );
				PrintWriter EntityPrint = new PrintWriter(EntityFile);
				EntityPrint.print(Entity);
				EntityPrint.close();
				//Entity Tier Finished
								
				//Facade Tier Creating...
				String Facade = "";
				Facade = "package Facade;\n\n";
				Facade += "import java.sql.*;\n";
				Facade += "import Provider.*;\n";
				Facade += "import Entity.*;\n\n";
				Facade += "public class " + Table_Names.get(i) + "CRUD {\n\n";
				Facade += "\tpublic static void Insert(" + Table_Names.get(i) + " p ) throws ClassNotFoundException, SQLException\n";
				Facade += "\t{\n";
				Facade += "\t\tStatement stmt = Provider.CreateStatement();\n";
				
				String cNames = "";
				String cValues = "";
				rs = stmt.executeQuery("PRAGMA table_info("+ Table_Names.get(i) + ")");
				while (rs.next()) {	
					if(rs.getString("pk").intern() != "1")
					{
						cNames += rs.getString("name") + ",";
						cValues += "'\"+p." + rs.getString("name") + "+\"',";
					}
				}// get all column names and types except primary key column then create string for insert void query
				
				Facade += "\t\tstmt.executeUpdate(\"INSERT INTO "
						+ Table_Names.get(i)
						+ " (" 
						+ cNames.substring(0, (cNames.length() - 1))
						+ ") VALUES ("
						+ cValues.substring(0, (cValues.length() - 1))
						+ ")\");\n";
				
				Facade += "\t\tstmt.close();\n";
				Facade += "\t\tstmt.getConnection().close();\n";
				Facade += "\t}\n\n";
				Facade += "\tpublic static void Update(" + Table_Names.get(i) + " p ) throws ClassNotFoundException, SQLException\n";
				Facade += "\t{\n";
				Facade += "\t\tStatement stmt = Provider.CreateStatement();\n";
				
				String upQuery = "";//query for update void
				String Pkey = "";//query for using primary key on update void
				rs = stmt.executeQuery("PRAGMA table_info("
						+ Table_Names.get(i) + ")");
				while (rs.next()) {
					if (rs.getString("pk").intern() != "1") {
						upQuery += rs.getString("name") + " = " + "'\"+p."
								+ rs.getString("name") + "+\"',";
					} else {
						Pkey = rs.getString("name");
					}
				}
				
				Facade += "\t\tstmt.executeUpdate(\"UPDATE "
						+ Table_Names.get(i)
						+ " SET "
						+ upQuery.substring(0, (upQuery.length()-1))
						+ " WHERE "
						+ Pkey+" = "+ "'\"+p."+Pkey+"+\"'\""
						+");\n";
				
				Facade += "\t\tstmt.close();\n";
				Facade += "\t\tstmt.getConnection().close();\n";
				Facade += "\t}\n\n";
				
				Facade += "\tpublic static void Delete(int ID) throws ClassNotFoundException, SQLException\n";
				Facade += "\t{\n";
				Facade += "\t\tStatement stmt = Provider.CreateStatement();\n";
				
				String Prmkey = "";// primary key for delete void
				rs = stmt.executeQuery("PRAGMA table_info("
						+ Table_Names.get(i) + ")");
				while (rs.next()) {
					if (rs.getString("pk").intern() == "1")
					{
						Prmkey = rs.getString("name");
					}
				}
				
				Facade += "\t\tstmt.executeUpdate(\"DELETE FROM "
						+ Table_Names.get(i)
						+ " WHERE "
						+ Prmkey+" = "+ "'\"+ID+\"'\""
						+");\n";
				
				Facade += "\t\tstmt.close();\n";
				Facade += "\t\tstmt.getConnection().close();\n";
				Facade += "\t}\n\n";
				
				Facade += "\tpublic static "
						+ Table_Names.get(i)
						+ " get"
						+ Table_Names.get(i)
						+ "withID (int ID) throws ClassNotFoundException, SQLException\n";
				Facade += "\t{\n";

				String Prmkey2 = "";// primary key for getXXXwithID void
				String getRow = "";//query for getXXXwithID void
				rs = stmt.executeQuery("PRAGMA table_info("
						+ Table_Names.get(i) + ")");
				while (rs.next()) {
					if (rs.getString("pk").intern() == "1") {
						Prmkey2 = rs.getString("name");
					}
					getRow += "\t\t\tp."+rs.getString("name")+" = rs.get"+SetType(rs.getString("type"),"ID")+"(\""+rs.getString("name")+"\");\n";
				}

				Facade += "\t\tResultSet rs = Provider.CreateResultSet(\"SELECT * FROM "
						+ Table_Names.get(i)
						+ " WHERE "
						+ Prmkey2
						+ " = "
						+ "'\"+ID+\"'\");\n";
				
				Facade += "\t\tif(rs != null){\n";
				Facade += "\t\t\t"+ Table_Names.get(i) +" p = new "+Table_Names.get(i)+"();\n";
				Facade += getRow;				
				Facade += "\t\t\treturn p;\n";
				Facade += "\t\t}\n";
				Facade += "\t\telse {return null;}\n";
				Facade += "\t}\n\n";
												
				Facade += "}";
				
				File FacadeFile = new File("C:\\JAVA_NTier\\" + ProjectName + "\\Facade\\" + Table_Names.get(i)+"CRUD" +".java" );//Facade File Creating
				PrintWriter FacadePrint = new PrintWriter(FacadeFile);
				FacadePrint.print(Facade);//create Facade File
				FacadePrint.close();//Close PrinterWriter
				
				rs.close();			
			}
			stmt.close();
						
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
	}
	
	private static String SetType(String Type,String Req){
		/*
		 * This method for convert types to Java from Sqlite database for using on classes and objects.
		 * */
		switch(Type){
		case "REAL": Type = "double";break;
		case "INTEGER": if(Req.intern() == "ID" ){Type = "Int";}else{Type = "int";};break;
		case "TEXT": Type = "String";break;
		default: Type = "Object";
		}
		return Type;
	}

}
