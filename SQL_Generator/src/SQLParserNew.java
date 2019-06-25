import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class SQLParserNew
{
  public static Vector<SQLTable> sqlVector=new Vector<SQLTable>();
  //public static sqlGUI gui;
  public static String subjectArea;
  public static String initials;
  public static String descriptionFilename;
  public static String author;
  
  
  
  public static boolean vectorHasElement(String elementName)
  {
    boolean vectorContainsElement = false;
    SQLTable sqlElement = null;
    int vectorSize;
	vectorSize = sqlVector.size();
	
    if (vectorSize > 0) {
      for (int i = 0; i < vectorSize; i++)
      {
        sqlElement = (SQLTable)sqlVector.elementAt(i);
        if (sqlVector.elementAt(i) != null) {
          if (((SQLTable)sqlVector.elementAt(i)).name.equalsIgnoreCase(elementName)) {
            vectorContainsElement = true;
          }
        }
      }
    } 
    return vectorContainsElement;
  }
  
  public static boolean attributeIsDoubled(String tableName, String attributeName)
  {
    boolean vectorElementContainsAttribute = false;
    SQLTable sqlElement = null;
    
    int vectorSize = sqlVector.size();
    if (vectorSize > 0) {
      for (int i = 0; i < vectorSize; i++) {
        if ((sqlVector.elementAt(i) != null) && 
          (((SQLTable)sqlVector.elementAt(i)).name.equalsIgnoreCase(tableName))) {
          if ((((SQLTable)sqlVector.elementAt(i)).primaryKeys.contains(attributeName)) || 
          
            (((SQLTable)sqlVector.elementAt(i)).attributes.contains(attributeName))) {
            vectorElementContainsAttribute = true;
          }
        }
      }
    }
    return vectorElementContainsAttribute;
  }
  
//add PK attribute to the table object
  public static void addPrimaryKey(String tableName, String PKName)
  {
    String tempString = PKName;
    int vectorSize = sqlVector.size();
    for (int i = 0; i < vectorSize; i++) {
      if (sqlVector.elementAt(i) != null) {
        if (((SQLTable)sqlVector.elementAt(i)).name.equalsIgnoreCase(tableName)) {
          if ((PKName != null) || (PKName.length() != 0) || 
            (sqlVector.elementAt(i) != null) || 
            (sqlVector != null)) {
            ((SQLTable)sqlVector.elementAt(i)).primaryKeys.add(PKName);
          } else {
            System.out.println("PK null or empty");
          }
        }
      }
    }
  }
  
  //add non-PK attribute to the table object
  public static void addAttribute(String tableName, String attributeName)
  {
    SQLTable sqlElement = null;
    
    int vectorSize = sqlVector.size();
    for (int i = 0; i < vectorSize; i++) {
      if ((sqlVector.elementAt(i) != null) && 
        (((SQLTable)sqlVector.elementAt(i)).name.equalsIgnoreCase(tableName))) {
        ((SQLTable)sqlVector.elementAt(i)).attributes.add(attributeName);
      }
    }
  }
  
  public static void parseTextFile(String filename)
  {
    try
    {
      boolean attributeDoubled = false;
      String line = null;
      BufferedReader input = new BufferedReader(new FileReader(filename));
      StringTokenizer strToken;
      
      do {
    	  line=input.readLine();
    	  
    	  //check if there is no other line to read from the file
    	  if(line==null) {
    		  continue;
    	  }
    	  
    	  int localLoop = 0;  //localLoop values: 0-parsing table name, 1-parsing attribute name, 2-parsing yes/no indicator if the attribute is PK
          String tableName = null;
          String attributeName = null;
          strToken = new StringTokenizer(line);
          
          while(strToken.hasMoreTokens()) {

        	  String item = strToken.nextToken();              
        	  System.out.println(item);
        	  if (localLoop == 0)  //check if a SQLTable object for the current table exist already in the sqlVector, if not - add it
              {
                if ((!vectorHasElement(item)) || 
                  (sqlVector.size() == 0))
                {
                  SQLTable sqlTable = new SQLTable(item);
                  sqlVector.add(sqlTable);
                }
                tableName = item;
              }
              else if (localLoop == 1) //just check if the current attribute already exists in the table object
              {
                attributeName = item.toUpperCase();
                attributeDoubled = attributeIsDoubled(tableName, 
                  attributeName);
              }
              else if (localLoop == 2)
              {
                if (!attributeDoubled) //if the current attribute does not exist in the table object - add it.
                {
                  if (item.equalsIgnoreCase("yes"))
                  {
                    if ((tableName != null) || (attributeName != null)) {
                      addPrimaryKey(tableName, attributeName.toUpperCase());
                    } else {
                      System.out.println("table name or attribute name is NULL");
                    }
                  }
                  else {
                    addAttribute(tableName, attributeName.toUpperCase());
                  }
                }
                else {
                  System.out.println("Attribute " + attributeName + " is doubled in table " + tableName + " and is SKIPPED!!!");
                }
              }
        	  
        	  localLoop++;                           
          }
    	  
      }while(line!=null);
      
    }
    catch (FileNotFoundException e)
    {
    	System.out.println("Inappropriate text file\n");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
  
  public static void generateSQL()
  {
    int vectorSize = sqlVector.size();
    SQLTable sqlTable = null;
    for (int i = 0; i < vectorSize; i++)
    {
      sqlTable = (SQLTable)sqlVector.elementAt(i);
      if (sqlTable != null) {
        if (sqlTable.primaryKeys.size() > 0)
        {
          //generatePrimaryKeys(sqlTable, "0");
          //generatePrimaryKeys(sqlTable, "1");
          //generateTargetTable(sqlTable, "0");
          //generateTargetTable(sqlTable, "1");
          //generateExpectedResults(sqlTable, "0");
          //generateExpectedResults(sqlTable, "1");
          if (sqlTable.attributes.size() > 0)
          {
            //generateAttributes(sqlTable, "0");
            //generateAttributes(sqlTable, "1");
          }
          else
          {
        	  System.out.println("Table " + 
              sqlTable.name + 
              " has no attributes and no attributes validation script is generated!\n");
          }
        }
        else
        {
        	System.out.println("Table " + 
            sqlTable.name + 
            " skipped due to lack of Primary Key(s)\n");
        }
      }
    }
  }
  
  public static void generatePrimaryKeys(SQLTable table, String pass)
  {
	  //add code here to compare primary keys
	  try
	    {
	      FileWriter fstream = new FileWriter(initials.toLowerCase() + "_" + 
	        subjectArea.toLowerCase() + "_exp_res_l" + pass + "." + table.name + 
	        ".sql");
	      BufferedWriter out = new BufferedWriter(fstream);
	      
	      out.write(".REMARK 'Functional Area: " + subjectArea + " //Target table: Q1EDWTbls." + table.name + " //Author: " + author + "'" + "\n");
	      out.write(".REMARK 'Populate table Q4EDWTbls." + table.name + "_L" + pass + " with expected results'" + "\n");
	      out.write("\n");
	      out.write("\n");
	      
	     
	      out.close();
	      System.out.println(
	        initials.toLowerCase() + "_" + subjectArea.toLowerCase() + 
	        "_exp_res_l" + pass + "." + table.name + ".sql" + 
	        " generated\n");
	    }
	    catch (Exception e)
	    {
	      System.err.println("Error: " + e.getMessage());
	    }
  }
  
  public static void generateAttributes(SQLTable table, String pass)
  {
	//add code here to compare non-PK attributes
  }
  
  public static void generateTargetTable(SQLTable table, String pass)
  {
  
  }
  
  public static void generateExpectedResults(SQLTable table, String pass)
  {
	  
  }
}