import java.util.ArrayList;
import java.util.List;

public class SQLTable
{
  public String name;
  public List<String> primaryKeys;
  public List<String> attributes;
  
  public SQLTable()
  {
    this.primaryKeys = new ArrayList();
    this.attributes = new ArrayList();
  }
  
  public SQLTable(String name)
  {
    this.name = name;
    this.primaryKeys = new ArrayList();
    this.attributes = new ArrayList();
  }
  
  public List<String> getPrimaryKeys()
  {
    return this.primaryKeys;
  }
  
  public void setPrimaryKeys(List<String> primaryKeys)
  {
    this.primaryKeys = primaryKeys;
  }
  
  public List<String> getAttributes()
  {
    return this.attributes;
  }
  
  public void setAttributes(List<String> attributes)
  {
    this.attributes = attributes;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
}
