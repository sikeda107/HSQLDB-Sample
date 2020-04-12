import com.sun.org.apache.bcel.internal.generic.ATHROW;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OperationDb {
    private String url;
    private String user;
    private String password;

    OperationDb(String url, String user, String password){
        setUrl(url);
        setUser(user);
        setPassword(password);
    }

    public String getUrl() {
        return url;
    }

    private void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    private void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public void testConnection() throws SQLException {
        System.out.println("Start DB Connection");
        // DB接続
        Connection connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
        System.out.println("Close DB Connection");
        //接続を閉じる
        connection.close();
        System.out.println("END");
    }

    public void printCatalogSchemaTableInfo() throws SQLException {
        System.out.println("PrintCatalogSchemaTableInfo----------------------");

        // DB接続
        Connection connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
        //データベースのメタデータを取得
        final DatabaseMetaData databaseMetaData = connection.getMetaData();
        //スキーマ情報の取得
        ResultSet resultSetSchemas = databaseMetaData.getSchemas();

        //スキーマごとに「カタログ名」「スキーマ名」を表示
        while(resultSetSchemas.next()){
            String catalog = resultSetSchemas.getString("TABLE_CATALOG");
            String schema = resultSetSchemas.getString("TABLE_SCHEM");
            System.out.print("カタログ名:" + catalog);
            System.out.println(" スキーマ名:" + schema);
            //「カタログ」と「スキーマ」で絞り込んで「テーブル」を取得
            ResultSet resultSetTables = databaseMetaData.getTables(catalog, schema, null, null);
            //スキーマごとに「テーブル名」を表示
            while (resultSetTables.next()){
                System.out.println("テーブル名:" + resultSetTables.getString("TABLE_NAME"));
            }
            System.out.println("next ----------------------");
        }
        connection.close();
        System.out.println("----------------------");
    }

    public void createTable(Statement statement, String schemaName, String tableName) throws SQLException{
        //スキーマネームがあればドットを付与
        String schema = schemaName.length() > 1 ? (schemaName + ".") : "";
        //SQLを生成
        String query = String.join("",
                "CREATE TABLE ",
                schema,
                tableName,
                "(id int, name varchar);"
        );
        System.out.println("Execute :" + query);
        //SQLの実行
        statement.executeUpdate(query);
    }

    public void insertData(Statement statement, String schemaName, String tableName, List data) throws SQLException {
        //スキーマネームがあればドットを付与
        String schema = schemaName.length() > 1 ? (schemaName + ".") : "";
        //VALUESの文字列を作成
        List<String> values = IntStream.range(0, data.size())
                .mapToObj(i -> "VALUES(" + String.valueOf(i) + ", '" + data.get(i) + "')")
                .collect(Collectors.toList());
        //データごとに挿入を実行
        values.forEach(v -> {
            //SQLを生成
            String query = String.join("",
                    "INSERT INTO ",
                    schema,
                    tableName,
                    "(id, name)",
                    v,
                    ";");
            System.out.println("Execute :" + query);
            //SQLの実行
            try {
                statement.executeUpdate(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void selectTable(Statement statement, String schemaName, String tableName) throws SQLException {
        //スキーマネームがあればドットを付与
        String schema = schemaName.length() > 1 ? (schemaName + ".") : "";
        //SQLを生成
        String query = "SELECT id, name FROM " + schema + tableName;
        //SQLの実行
        ResultSet resultSet = statement.executeQuery(query);
        //結果を表示
        while (resultSet.next()) {
            System.out.println(resultSet.getInt("id") + "," + resultSet.getString("name"));
        }
    }

    public void simulate() throws SQLException {
        //0.事前処理
        //DB接続
        Connection connection = DriverManager.getConnection(getUrl(), getUser(), getPassword());
        //ステートメント作成
        Statement statement = connection.createStatement();
        //スキーマの作成
        String query = String.join("","CREATE SCHEMA " + "artist" + ";");
        statement.executeUpdate(query);

        //1.テーブル作成
        createTable(statement,"","rad");
        createTable(statement,"artist","bump");

        //2.データ投入
        List<String> data = new ArrayList<String>(Arrays.asList("name1", "name2", "name3", "name4", "name5"));
        insertData(statement,"","rad", data);
        insertData(statement,"artist","bump", data);

        //3.データ取得
        selectTable(statement,"","rad");
        selectTable(statement,"artist","bump");

        //4.終了処理
        //ステートメントを閉じる
        statement.close();
        //接続を閉じる
        connection.close();
    }
}
