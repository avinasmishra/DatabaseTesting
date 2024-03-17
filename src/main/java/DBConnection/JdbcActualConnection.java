package DBConnection;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.*;

public class JdbcActualConnection {
    public static void main(String[] args) throws SQLException {

      String query =  "select * from credentials where scenario = 'zerobalancecard'";
        String query1 =  "select * from credentials";
      executeQuery("root","root",query1);

    }

    private static Connection getConnection(String username, String password) throws SQLException {
        String host = "localhost";
        String port = "3306";
        String dbUrl = "jdbc:mysql://" + host + ":" +  port + "/demo";

        Connection connection = DriverManager.getConnection(dbUrl,username, password);
        return connection;
    }

    public static List<Map<String, Object>> executeQuery(String username, String password, String queryToExecute) throws SQLException {
        System.out.println("DB Query - \n" + queryToExecute);
        try(Connection connection = getConnection(username,password))
        {
            List<Map<String, Object>> list = new ArrayList<>();
            ResultSet resultSet = connection.createStatement().executeQuery(queryToExecute);
            ResultSetMetaData md = resultSet.getMetaData();
            while(resultSet.next())
            {
                LinkedHashMap<String,Object> row = new LinkedHashMap<>();
                for (int i=1;i<= md.getColumnCount();i++)
                {
                    row.put(md.getColumnLabel(i),resultSet.getObject(i));
                }
                list.add(row);
            }
            printDatabaseResults(list);
            return list;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static void printDatabaseResults(List<Map<String,Object>> resultSet)
    {
        Map<String,Integer> maxColSizeMap = new HashMap<>();
        boolean initMaxColSizeMap = true;
        for (Map<String, Object> map: resultSet)
        {
            for (String key: map.keySet())
            {
                String colValue = (map.get(key)==null)? "" : map.get(key).toString();
                Integer whoIsBig = Math.max(colValue.length(),key.length());
                if(!initMaxColSizeMap)
                {
                    whoIsBig = Math.max(maxColSizeMap.get(key),whoIsBig);
                }
                maxColSizeMap.put(key,whoIsBig);
            }
            initMaxColSizeMap = false;
        }
        StringBuilder underLine = new StringBuilder();
        for(Map<String,Object> map:resultSet)
        {
            System.out.println("");
            StringBuilder colName = new StringBuilder();
            for(String key:map.keySet())
            {
                colName.append("| ");
                colName.append(StringUtils.rightPad(key,maxColSizeMap.get(key)+1));
                underLine.append(StringUtils.repeat("_",maxColSizeMap.get(key)+3));
            }
            colName.append(" |");
            underLine.append("_");
            System.out.println(underLine);
            System.out.println(colName);
            System.out.println(underLine);
            break;
        }
        for(Map<String,Object> map:resultSet)
        {
            StringBuilder row = new StringBuilder();
            for (String key:map.keySet())
            {
                String str = map.get(key)==null? "NULL" : map.get(key).toString();
                if (str.equalsIgnoreCase("true"))
                {
                    str = "1";
                } else if (str.equalsIgnoreCase("false")) {
                    str = "0";
                }
                row.append("| ");
                row.append(StringUtils.rightPad(str,maxColSizeMap.get(key)+1));
            }
            row.append(" |");
            System.out.println(row);
        }
        System.out.println(underLine);
    }

    public static String printDatabaseResultHTML(List<Map<String, Object>> resultSet)
    {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<table border='1'>");
        for (Map<String,Object>map:resultSet)
        {
            htmlContent.append("<tr>");
            for (String key : map.keySet())
            {
                htmlContent.append("<th align='left'>");
                htmlContent.append(key);
                htmlContent.append("</th align='left'>");
            }
            htmlContent.append("</tr>");
            break;
        }
        for (Map<String, Object> map: resultSet)
        {
            htmlContent.append("<tr>");
            for (String key : map.keySet())
            {
                String str = map.get(key)== null ? "NULL": map.get(key).toString();
                if (str.equalsIgnoreCase("true"))
                {
                    str = "1";
                } else if (str.equalsIgnoreCase("false")) {
                    str = "0";
                }
                htmlContent.append("<td align='left'>");
                htmlContent.append(str);
                htmlContent.append("</td align='left'>");
            }
            htmlContent.append("</tr>");
        }
        htmlContent.append("</table>");
        return htmlContent.toString();
    }

}
















