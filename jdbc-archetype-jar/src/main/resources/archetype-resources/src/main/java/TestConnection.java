#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestConnection {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        System.out.println("start testing....");
        for (int i = 0; i < 20; i++) {
            Thread thread = new Thread(()->{
                TestConnection testConnection = new TestConnection();
                try {
                    testConnection.testing();
                } catch (SQLException e) {
                    System.out.println("ERROR："+e.toString());
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    System.out.println("ERROR："+e.toString());
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    System.out.println("ERROR："+e.toString());
                    throw new RuntimeException(e);
                }
            });
            thread.start();
        }
    }

    public void testing() throws SQLException, ClassNotFoundException, InterruptedException {
        HashMap query = null;
        for (int i = 0; i < 100000; i++) {
            Thread.sleep(1000);
            String sql = "select count(*) from sys_enterprise_base limit 1";
            query = this.getQuery(sql);

            System.out.println(i+"ent count: " + query.get("data").toString());
        }
    }


    public HashMap getQuery(String sql) throws SQLException, ClassNotFoundException {
        Connection conn = this.getMysqlConnection();
        Statement stmt = conn.createStatement();
        HashMap<Object, Object> resultMap = new HashMap();
        ResultSet resultSet = stmt.executeQuery(sql);
        if (resultSet != null) {
            ResultSetMetaData columnName = resultSet.getMetaData();
            int columnCount = columnName.getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = columnName.getColumnLabel(i + 1);
            }
            // 将数据存储到数据中
            ArrayList<Object> list = new ArrayList<Object>();
            while (resultSet.next()) {
                Map<String, Object> perMap = new HashMap<String, Object>();
                for (int i = 0; i < columnCount; i++) {
                    // 获取列名
                    String columnN = columnNames[i];
                    // 获取该列对应的值
                    Object value = resultSet.getObject(columnN);
                    perMap.put(columnN, value);
                }
                list.add(perMap);
            }
            // 计算数据的总数
            int total = list.size();
            resultMap.put("data", list);
            resultMap.put("total", total);
        }
        conn.close();
        return resultMap;
    }


    public Connection getMysqlConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        //return DriverManager.getConnection("jdbc:mysql://172.168.0.106:3306/users?user=master&password=pZdxysqJX4bK4VrB&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        //return DriverManager.getConnection("jdbc:mysql://10.4.5.35:3306/master?user=master&password=pZdxysqJX4bK4VrB&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        return DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/master?user=master&password=pZdxysqJX4bK4VrB&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    }
}
