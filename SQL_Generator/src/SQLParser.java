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


public class SQLParser
{
  public static Vector<SQLTable> sqlVector;
  //public static sqlGUI gui;
  public static String subjectArea;
  public static String initials;
  public static String descriptionFilename;
  public static String author;
  
  /*
  public SQLParser(sqlGUI gui)
  {
    gui = gui;
    sqlVector = new Vector();
  }
  
  */
  
  public static void parseTextFile(String filename)
  {
    try
    {
      boolean attributeDoubled = false;
      String line = null;
      BufferedReader input = new BufferedReader(new FileReader(filename));
      StringTokenizer strToken;
      for (; (line = input.readLine()) != null; strToken.hasMoreTokens())
      {
        int localLoop = 0;
        String tableName = null;
        String attributeName = null;
        strToken = new StringTokenizer(line);
        
        
       //continue;
       
        String item = strToken.nextToken();
        
        System.out.println(item);
        
        /*
        if (localLoop == 0)
        {
          if ((!vectorHasElement(item)) || 
            (sqlVector.size() == 0))
          {
            SQLTable sqlTable = new SQLTable(item);
            sqlVector.add(sqlTable);
          }
          tableName = item;
        }
        else if (localLoop == 1)
        {
          attributeName = item.toUpperCase();
          attributeDoubled = attributeIsDoubled(tableName, 
            attributeName);
        }
        else if (localLoop == 2)
        {
          if (!attributeDoubled)
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
       
        */
        localLoop++;
       
      }
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
  
  public static boolean vectorHasElement(String elementName)
  {
    boolean vectorContainsElement = false;
    SQLTable sqlElement = null;
    
    int vectorSize = sqlVector.size();
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
          generatePrimaryKeys(sqlTable, "0");
          generatePrimaryKeys(sqlTable, "1");
          generateTargetTable(sqlTable, "0");
          generateTargetTable(sqlTable, "1");
          generateExpectedResults(sqlTable, "0");
          generateExpectedResults(sqlTable, "1");
          if (sqlTable.attributes.size() > 0)
          {
            generateAttributes(sqlTable, "0");
            generateAttributes(sqlTable, "1");
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
    try
    {
      FileWriter fstream = new FileWriter(initials.toLowerCase() + "_" + 
        subjectArea.toLowerCase() + "_val_id_l" + pass + "." + table.name + 
        ".sql");
      BufferedWriter out = new BufferedWriter(fstream);
      
      out.write(".REMARK 'Functional Area: " + subjectArea + " //Target table: Q1EDWTbls." + table.name + "//Author: " + author + "'" + "\n");
      out.write(".REMARK 'Compare expected results populated in Q4EDWTbls." + table.name + "_L" + pass + " with actual results copied to Q5EDWTst." + table.name + "_L" + pass + ": Primary Keys'" + "\n");
      out.write("\n");
      out.write(".REMARK 'TC001.TS001: Comparing the number of records in Q4EDWTbls." + table.name + "_L" + pass + " (expected results) with the number of records in Q5EDWTbls." + table.name + "_L" + pass + " (actual results)'" + "\n");
      
      out.write("SELECT\n");
      out.write("\t'TC001.TS001' AS TEST_CASE,\n");
      out.write("\tETL_CNT.CNT AS ETL_COUNT,\n");
      out.write("\tEXP_CNT.CNT AS EXP_COUNT,\n");
      out.write("\tCASE\n");
      out.write("\tWHEN ETL_CNT.CNT=EXP_CNT.CNT THEN 'OK!'\n");
      out.write("\tELSE 'ERROR!'\n");
      out.write("\tEND\t\t\tAS TEST_RESULT\n");
      out.write("FROM\n");
      out.write("\t(SELECT COUNT(*) AS CNT Q5EDWTST." + 
        table.name.toUpperCase() + "_L" + pass + ") ETL_CNT," + "\n");
      out.write("\t(SELECT COUNT(*) AS CNT Q4EDWTBLS." + 
        table.name.toUpperCase() + "_L" + pass + ") EXP_CNT;" + "\n");
      out.write("\n");
      out.write("--=======================================================================================\n");
      
      out.write("\n");
      out.write(".REMARK 'TC001.TS002: Matching the PK values from Q4EDWTbls." + table.name + "_L" + pass + " and Q5EDWTst." + table.name + "_L" + pass + "'" + "\n");
      out.write("\n");
      out.write("WITH\n");
      
      int loop = table.primaryKeys.size();
      
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < loop; i++)
      {
        sb.append("ETL_ID");
        sb.append(i + 1);
        sb.append(", ");
      }
      String part1 = sb.toString();
      
      StringBuilder sb2 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1)
        {
          sb2.append("EXP_ID");
          sb2.append(i + 1);
        }
        else
        {
          sb2.append("EXP_ID");
          sb2.append(i + 1);
          sb2.append(", ");
        }
      }
      String part2 = sb2.toString();
      out.write("QA_MATCH(" + part1 + part2 + " ) AS" + "\n");
      out.write("(\n");
      out.write("\tSELECT\n");
      for (int i = 0; i < loop; i++) {
        out.write("\t\tETL." + 
          ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
          " AS ETL_ID" + (i + 1) + "," + "\n");
      }
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          out.write("\t\tEXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AS EXP_ID" + (i + 1) + "\n");
        } else {
          out.write("\t\tEXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AS EXP_ID" + (i + 1) + "," + "\n");
        }
      }
      out.write("\tFROM \tQ5EDWTST." + table.name.toUpperCase() + 
        "_L" + pass + " ETL FULL OUTER JOIN Q4EDWTBLS." + 
        table.name.toUpperCase() + "_L" + pass + " EXPTD" + "\n");
      out.write("\tON\n");
      out.write("\t\t(\n");
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          out.write("\t\tETL." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            "=EXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            "\n");
        } else {
          out.write("\t\tETL." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            "=EXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AND" + "\n");
        }
      }
      out.write("\t\t)\n");
      out.write(")\n");
      out.write("SELECT\t'TC001.TS002'\tAS TEST_CASE,\n");
      out.write("\tETL_CNT.CNT \tAS ETL_COUNT,\n");
      out.write("\tEXP_CNT.CNT\tAS EXP_COUNT,\n");
      out.write("\tMATCH_CNT.CNT\tAS MATCH_COUNT,\n");
      out.write("\tQA_L.CNT\tAS NOT_FOUND_IN_EXP,\n");
      out.write("\tQA_R.CNT\tAS NOT_FOUND_IN_ETL,\n");
      out.write("\tCASE\n");
      out.write("\tWHEN ETL_CNT.CNT=EXP_CNT.CNT AND ETL_CNT.CNT=MATCH_CNT.CNT THEN 'OK!'\n");
      
      out.write("\tELSE 'ERROR!'\n");
      out.write("\tEND\t\t\tAS TEST_RESULT\n");
      out.write("FROM\n");
      out.write("\t(SELECT COUNT(*) AS CNT FROM Q5EDWTST." + 
        table.name.toString().toUpperCase() + "_L" + pass + ") ETL_CNT," + 
        "\n");
      out.write("\t(SELECT COUNT(*) AS CNT FROM Q4EDWTBLS." + 
        table.name.toString().toUpperCase() + "_L" + pass + ") EXP_CNT," + 
        "\n");
      out.write("\t(SELECT COUNT(*) AS CNT FROM QA_MATCH WHERE\n");
      
      StringBuilder sb3 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        sb3.append("ETL_ID" + (i + 1) + " IS NOT NULL AND ");
      }
      out.write("\t\t\t" + sb3.toString() + "\n");
      
      StringBuilder sb4 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          sb4.append("EXP_ID" + (i + 1) + " IS NOT NULL ");
        } else {
          sb4.append("EXP_ID" + (i + 1) + " IS NOT NULL AND ");
        }
      }
      out.write("\t\t\t" + sb4.toString() + " ) MATCH_CNT," + "\n");
      
      out.write("\t(SELECT COUNT(*) AS CNT FROM QA_MATCH WHERE\n");
      
      StringBuilder sb5 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        sb5.append("ETL_ID" + (i + 1) + " IS NOT NULL AND ");
      }
      out.write("\t\t\t" + sb5.toString() + "\n");
      
      StringBuilder sb6 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          sb6.append("EXP_ID" + (i + 1) + " IS NULL ");
        } else {
          sb6.append("EXP_ID" + (i + 1) + " IS NULL AND ");
        }
      }
      out.write("\t\t\t" + sb6.toString() + " ) QA_L," + "\n");
      
      out.write("\t(SELECT COUNT(*) AS CNT FROM QA_MATCH WHERE\n");
      
      StringBuilder sb7 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        sb7.append("ETL_ID" + (i + 1) + " IS NULL AND ");
      }
      out.write("\t\t\t" + sb7.toString() + "\n");
      
      StringBuilder sb8 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          sb8.append("EXP_ID" + (i + 1) + " IS NOT NULL ");
        } else {
          sb8.append("EXP_ID" + (i + 1) + " IS NOT NULL AND ");
        }
      }
      out.write("\t\t\t" + sb8.toString() + " ) QA_R;" + "\n");
      out.write("\n");
      out.write("--=======================================================================================\n");
      
      out.write("\n");
      out.write(".REMARK 'TC001.TS003: Outputting a sample of unmatched PKs from Q4EDWTbls." + table.name + "_L" + pass + " and Q5EDWTst." + table.name + "_L" + pass + "'" + "\n");
      out.write("\n");
      
      out.write("WITH\n");
      
      StringBuilder sb9 = new StringBuilder();
      for (int i = 0; i < loop; i++)
      {
        sb9.append("ETL_ID");
        sb9.append(i + 1);
        sb9.append(", ");
      }
      String part3 = sb9.toString();
      
      StringBuilder sb10 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1)
        {
          sb10.append("EXP_ID");
          sb10.append(i + 1);
        }
        else
        {
          sb10.append("EXP_ID");
          sb10.append(i + 1);
          sb10.append(", ");
        }
      }
      String part4 = sb10.toString();
      out.write("QA_MATCH(" + part3 + part4 + " ) AS" + "\n");
      out.write("(\n");
      out.write("\tSELECT\n");
      for (int i = 0; i < loop; i++) {
        out.write("\t\tETL." + 
          ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
          " AS ETL_ID" + (i + 1) + "," + "\n");
      }
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          out.write("\t\tEXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AS EXP_ID" + (i + 1) + "\n");
        } else {
          out.write("\t\tEXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AS EXP_ID" + (i + 1) + "," + "\n");
        }
      }
      out.write("\tFROM \tQ5EDWTST." + table.name.toUpperCase() + 
        "_L" + pass + " ETL FULL OUTER JOIN Q4EDWTBLS." + 
        table.name.toUpperCase() + "_L" + pass + " EXPTD" + "\n");
      out.write("\tON\n");
      out.write("\t\t(\n");
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          out.write("\t\tETL." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            "=EXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            "\n");
        } else {
          out.write("\t\tETL." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            "=EXPTD." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AND" + "\n");
        }
      }
      out.write("\t\t)\n");
      out.write(")\n");
      
      out.write("SELECT TOP 10\n");
      out.write("\t'TC001.TS003'\tAS TEST_CASE,\n");
      for (int i = 0; i < loop; i++) {
        out.write("\t\t\tETL_ID" + (i + 1) + " AS ETL_ID" + (i + 1) + 
          ", " + "\n");
      }
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          out.write("\t\t\tEXP_ID" + (i + 1) + " AS EXP_ID" + (i + 1) + 
            "\n");
        } else {
          out.write("\t\t\tEXP_ID" + (i + 1) + " AS EXP_ID" + (i + 1) + 
            ", " + "\n");
        }
      }
      out.write("\tFROM QA_MATCH\n");
      out.write("WHERE ( \n");
      
      StringBuilder sb11 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          sb11.append("ETL_ID" + (i + 1) + " IS NULL ");
        } else {
          sb11.append("ETL_ID" + (i + 1) + " IS NULL AND ");
        }
      }
      out.write("\t\t\t( " + sb11.toString() + ")" + "\n");
      
      out.write("OR\n");
      
      StringBuilder sb12 = new StringBuilder();
      for (int i = 0; i < loop; i++) {
        if (i == loop - 1) {
          sb12.append("EXP_ID" + (i + 1) + " IS NULL ");
        } else {
          sb12.append("EXP_ID" + (i + 1) + " IS NULL AND ");
        }
      }
      out.write("\t\t\t( " + sb12.toString() + ")" + "\n");
      
      out.write(");\n");
      out.write("\n");
      out.write(".QUIT 0\n");
      out.close();
      System.out.println(
        initials.toLowerCase() + "_" + subjectArea.toLowerCase() + 
        "_val_id_l" + pass + "." + table.name + ".sql" + 
        " generated\n");
    }
    catch (Exception e)
    {
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  public static void generateAttributes(SQLTable table, String pass)
  {
    try
    {
      FileWriter fstream = new FileWriter(initials.toLowerCase() + "_" + 
        subjectArea.toLowerCase() + "_val_attr_l" + pass + "." + table.name + 
        ".sql");
      BufferedWriter out = new BufferedWriter(fstream);
      
      out.write(".REMARK 'Functional Area: " + subjectArea + " //Target table: Q1EDWTbls." + table.name + "//Author: " + author + "'" + "\n");
      out.write(".REMARK 'Compare expected results populated in Q4EDWTbls." + table.name + "_L" + pass + " with actual results copied to Q5EDWTst." + table.name + "_L" + pass + ": Attributes'" + "\n");
      out.write("\n");
      out.write("\n");
      for (int elInd = 0; elInd < table.attributes.size(); elInd++)
      {
        out.write(".REMARK 'TC00" + (elInd + 2) + ".TS001: Counting the number of equal and unequal values of attribute " + (String)table.attributes.get(elInd) + " in tables Q4EDWTbls." + table.name + "_L" + pass + " and Q5EDWTst." + table.name + "_L" + pass + "'" + "\n");
        out.write("\n");
        out.write("WITH\n");
        
        int loop = table.primaryKeys.size();
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < loop; i++) {
          sb.append("ID" + (i + 1) + ", ");
        }
        out.write("QA_DATA(" + sb.toString().toUpperCase() + 
          "ETL_VAL, EXP_VAL) AS" + "\n");
        out.write("\t(\n");
        
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < loop; i++) {
          sb2.append("ETL." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AS ID" + (i + 1) + ", ");
        }
        out.write("\tSELECT " + sb2.toString().toUpperCase() + " ETL." + 
          ((String)table.attributes.get(elInd)).toString().toUpperCase() + 
          " AS ETL_VAL, EXPTD." + 
          ((String)table.attributes.get(elInd)).toString().toUpperCase() + 
          " AS EXP_VAL" + "\n");
        out.write("\tFROM Q5EDWTST." + table.name + 
          "_L" + pass + " ETL INNER JOIN Q4EDWTBLS." + table.name + 
          "_L" + pass + " EXPTD " + "\n");
        out.write("\tON\n");
        out.write("\t(\n");
        for (int i = 0; i < loop; i++) {
          if (i == loop - 1) {
            out.write("ETL." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + 
              "=EXPTD." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + "\n");
          } else {
            out.write("ETL." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + 
              "=EXPTD." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + ", " + "\n");
          }
        }
        out.write("\t)\n");
        out.write(")\n");
        out.write("SELECT 'TC00" + (elInd + 2) + 
          ".TS001'\tAS TEST_CASE," + "\n");
        out.write("\tETL_CNT.CNT\tAS ETL_COUNT,\n");
        out.write("\tEXP_CNT.CNT\tAS EXP_COUNT,\n");
        out.write("\tMATCH_CNT.CNT + NULL_CNT.CNT\tAS MATCH_COUNT,\n");
        out.write("\tERROR_CNT.CNT + ERROR_ETL_NULL_CNT.CNT + ERROR_EXP_NULL_CNT.CNT\tAS ERROR_COUNT,\n");
        out.write("\tCASE\n");
        out.write("\tWHEN ETL_CNT.CNT=EXP_CNT.CNT AND ETL_CNT.CNT=MATCH_CNT.CNT THEN 'OK!'\n");
        
        out.write("\tELSE 'ERROR!'\n");
        out.write("\tEND\t\tAS TEST_RESULT\n");
        out.write("FROM\n");
        out.write("\t(SELECT COUNT(*) AS CNT FROM Q5EDWTST." + 
          table.name.toUpperCase() + "_L" + pass + ") ETL_CNT," + "\n");
        out.write("\t(SELECT COUNT(*) AS CNT FROM Q4EDWTBLS." + 
          table.name.toUpperCase() + "_L" + pass + ") EXP_CNT," + "\n");
        out.write("\t(SELECT COUNT(*) AS CNT FROM QA_DATA WHERE ETL_VAL=EXP_VAL) MATCH_CNT,\n");
        
        out.write("\t(SELECT COUNT(*) AS CNT FROM QA_DATA WHERE ETL_VAL IS NULL AND EXP_VAL IS NULL) NULL_CNT,\n");
        
        out.write("\t(SELECT COUNT(*) AS CNT FROM QA_DATA WHERE ETL_VAL<>EXP_VAL) ERROR_CNT ;\n");
        
        out.write("\t(SELECT COUNT(*) AS CNT FROM QA_DATA WHERE ETL_VAL IS NULL AND EXP_VAL IS NOT NULL) ERROR_ETL_NULL_CNT ;\n");
        
        out.write("\t(SELECT COUNT(*) AS CNT FROM QA_DATA WHERE ETL_VAL IS NOT NULL AND EXP_VAL IS NULL) ERROR_EXP_NULL_CNT ;\n");
        
        out.write("\n");
        out.write("--=======================================================================================\n");
        
        out.write("\n");
        out.write(".REMARK 'TC00" + (elInd + 2) + ".TS002: Outputting a sample of unequal values of attribute " + (String)table.attributes.get(elInd) + " in tables Q4EDWTbls." + table.name + "_L" + pass + " and Q5EDWTst." + table.name + "_L" + pass + "'" + "\n");
        out.write("\n");
        out.write("WITH\n");
        
        StringBuilder sb3 = new StringBuilder();
        for (int i = 0; i < loop; i++) {
          sb3.append("ID" + (i + 1) + ", ");
        }
        out.write("QA_DATA(" + sb3.toString().toUpperCase() + 
          "ETL_VAL, EXP_VAL) AS" + "\n");
        out.write("\t(\n");
        
        StringBuilder sb4 = new StringBuilder();
        for (int i = 0; i < loop; i++) {
          sb4.append("ETL." + 
            ((String)table.primaryKeys.get(i)).toString().toUpperCase() + 
            " AS ID" + (i + 1) + ", ");
        }
        out.write("\tSELECT " + sb4.toString().toUpperCase() + " ETL." + 
          ((String)table.attributes.get(elInd)).toString().toUpperCase() + 
          " AS ETL_VAL, EXPTD." + 
          ((String)table.attributes.get(elInd)).toString().toUpperCase() + 
          " AS EXP_VAL" + "\n");
        out.write("\tFROM Q5EDWTST." + table.name + 
          "_L" + pass + " ETL INNER JOIN Q4EDWTBLS." + table.name + 
          "_L" + pass + " EXPTD " + "\n");
        out.write("\tON\n");
        out.write("\t(\n");
        for (int i = 0; i < loop; i++) {
          if (i == loop - 1) {
            out.write("ETL." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + 
              "=EXPTD." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + "\n");
          } else {
            out.write("ETL." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + 
              "=EXPTD." + 
              ((String)table.primaryKeys.get(i)).toString()
              .toUpperCase() + ", " + "\n");
          }
        }
        out.write("\t)\n");
        out.write(")\n");
        out.write("SELECT TOP 10\n");
        out.write("\t'TC00" + (elInd + 2) + ".TS002'\tAS TEST_CASE," + 
          "\n");
        for (int i = 0; i < loop; i++) {
          out.write("\tID" + (i + 1) + " AS ID" + (i + 1) + "," + 
            "\n");
        }
        out.write("\tETL_VAL\t\tAS ETL_VAL,\n");
        out.write("\tEXP_VAL\t\tAS EXP_VAL\n");
        out.write("\tFROM\tQA_DATA\n");
        out.write("\tWHERE\t(ETL_VAL <> EXP_VAL)OR (ETL_VAL IS NULL AND EXP_VAL IS NOT NULL) OR (ETL_VAL IS NOT NULL AND EXP_VAL IS NULL) ;\n");
        out.write("\t\n");
        out.write("--=======================================================================================\n");
        
        out.write("\t\n");
      }
      out.write("\n");
      out.write(".QUIT 0\n");
      
      out.close();
      System.out.println(
        initials.toLowerCase() + "_" + subjectArea.toLowerCase() + 
        "_val_attr_l" + pass + "." + table.name + ".sql" + 
        " generated\n");
    }
    catch (Exception e)
    {
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  public static void generateTargetTable(SQLTable table, String pass)
  {
    try
    {
      FileWriter fstream = new FileWriter(initials.toLowerCase() + "_" + 
        subjectArea.toLowerCase() + "_cp_trg_tbls_l" + pass + "." + table.name + 
        ".sql");
      BufferedWriter out = new BufferedWriter(fstream);
      
      out.write(".REMARK 'Functional Area: " + subjectArea + " //Target table: Q1EDWTbls." + table.name + " //Author: " + author + "'" + "\n");
      out.write(".REMARK 'Copy target table containing results populated by the ETL code to Q5EDWTst." + table.name + "_L" + pass + "'" + "\n");
      out.write("\n");
      out.write("\n");
      
      out.write("DROP TABLE Q5EDWTST." + table.name + "_L" + pass + ";" + "\n");
      out.write("CREATE TABLE Q5EDWTST." + table.name + "_L" + pass + " AS (SELECT * FROM Q1EDWTBLS." + table.name + ") WITH DATA;" + "\n");
      
      out.write("\n");
      out.write(".QUIT 0\n");
      out.close();
      System.out.println(
        initials.toLowerCase() + "_" + subjectArea.toLowerCase() + 
        "_cp_trg_tbls_l" + pass + "." + table.name + ".sql" + 
        " generated\n");
    }
    catch (Exception e)
    {
      System.err.println("Error: " + e.getMessage());
    }
  }
  
  public static void generateExpectedResults(SQLTable table, String pass)
  {
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
      
      out.write("DROP TABLE Q4EDWTBLS." + table.name + "_L" + pass + ";" + "\n");
      out.write("CREATE TABLE Q4EDWTBLS." + table.name + "_L" + pass + " AS (SELECT * FROM Q1EDWTBLS." + table.name + ") WITH NO DATA;" + "\n");
      out.write("\n");
      out.write("INSERT INTO Q4EDWTBLS." + table.name + "_L" + pass + "\n");
      out.write("\n");
      if (pass.equalsIgnoreCase("1"))
      {
        out.write("SELECT * FROM Q4EDWTBLS." + table.name + "_L0;" + "\n");
        out.write("\n");
        out.write("INSERT INTO Q4EDWTBLS." + table.name + "_L" + pass + "\n");
      }
      out.write("#add mapping rule\n");
      out.write("\n");
      out.write(".QUIT 0\n");
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
  
  public static void setSubjectArea(String subjectArea)
  {
    subjectArea = subjectArea;
  }
  
  public static void setInitials(String initials)
  {
    initials = initials;
  }
  
  public static void setDescriptionFilename(String descriptionFilename)
  {
    descriptionFilename = descriptionFilename;
  }
  
  public static void setAuthor(String author)
  {
    author = author;
  }
}
