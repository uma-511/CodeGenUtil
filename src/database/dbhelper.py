#!python3
import pymysql.cursors


class DBHelper:
    __ip = '10.211.55.5'
    __userName = 'root'
    __password = '123456'
    __databaseName = 'lifepay'
    __connection = None

    def get_connection(self):
        self.__connection = pymysql.connect(
            host=self.__ip,
            user=self.__userName,
            password=self.__password,
            db=self.__databaseName,
            charset='utf8',
            cursorclass=pymysql.cursors.DictCursor)

    def get_table_meta(self, table_name):
        with self.__connection.cursor() as cursor:
            cursor.execute(
                "select column_name,data_type,column_key,column_comment from information_schema.columns where table_name = %s AND table_schema = %s",
                (table_name, self.__databaseName))
            result = cursor.fetchall()
        return result

    def close_connection(self):
        self.__connection.close()
