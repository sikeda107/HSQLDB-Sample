import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        String dbPath = "./DB/testDb";
        String url = "jdbc:h2:file:" + dbPath;
        String user = "sa";
        String password = "";
        OperationDb operationDb = new OperationDb(url, user, password);
        try {
            //サンプルを実行
            operationDb.simulate();
            //「カタログ」「スキーマ」「テーブル」名を表示
//            operationDb.printCatalogSchemaTableInfo();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
