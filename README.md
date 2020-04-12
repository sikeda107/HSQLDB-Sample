# H2DB
# GUI編

```Shell
// 起動
$ java -jar h2*.jar
```

```SQL
-- スキーマの作成
CREATE SCHEMA artist
;
-- artist スキーマへテーブル作成
CREATE TABLE artist.bump(id int, name varchar)
;
-- PUBLIC スキーマへテーブル作成
CREATE TABLE rad(id int, name varchar)
;
-- スキーマ情報からテーブルを確認
SELECT * FROM INFORMATION_SCHEMA.TABLES
;
-- テストデータ挿入
INSERT INTO rad(id,name) VALUES(1,'name1');
INSERT INTO rad(id,name) VALUES(2,'name2');
INSERT INTO rad(id,name) VALUES(3,'name3');
INSERT INTO rad(id,name) VALUES(4,'name4');
INSERT INTO rad(id,name) VALUES(5,'name5');
-- テストデータ挿入
INSERT INTO artist.bump(id,name) VALUES(1,'name1');
INSERT INTO artist.bump(id,name) VALUES(2,'name2');
INSERT INTO artist.bump(id,name) VALUES(3,'name3');
INSERT INTO artist.bump(id,name) VALUES(4,'name4');
INSERT INTO artist.bump(id,name) VALUES(5,'name5');

-- スキーマ情報からテーブルを確認
SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'TABLE'
;
```