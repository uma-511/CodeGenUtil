#!python3
import log


# 数据表字段相关信息
class TableMeta:
    # 数据表字段信息 包含字段名、数据类型、是否是主键
    __fields = []
    # 需要导入的包名
    __imports = []
    # 主键
    __primary_key = ''
    # 主键类型
    __primary_key_type = ''
    # 查询条件
    __query = []
    # MySQL数据类型与JAVA数据类型对应表
    __field_types = {
        'int': 'Integer',
        'tinyint': 'Boolean',
        'smallint': 'Integer',
        'mediumint': 'Integer',
        'bigint': 'Long',
        'float': 'Float',
        'double': 'Double',
        'decimal': 'Double',
        'char': 'String',
        'varchar': 'String',
        'tinytext': 'String',
        'text': 'String',
        'mediumtext': 'String',
        'longtext': 'String',
        'date': 'Date',
        'time': 'Date',
        'datetime': 'Date',
        'timestamp': 'Date',
        'bit': 'Boolean'
    }

    def __init__(self, table_name: str, prefix=None):
        self.prefix = prefix
        if prefix is not None:
            table_name = table_name.replace(prefix, '')
            table_name = table_name[:1].upper() + table_name[1:]
        self.__table_name = table_name

    @property
    def get_prefix(self):
        return self.prefix

    @property
    def get_table_name(self):
        return self.__table_name

    @get_table_name.setter
    def set_table_name(self, table_name: str, prefix=None):
        if prefix is not None:
            table_name = table_name.replace(prefix, '')
            temp = table_name.split('_')
            if len(temp) > 1:
                for i in range(0, len(temp), 1):
                    temp[i] = temp[i][:1].upper() + temp[i][1:]
                table_name = ''.join(temp)
            else:
                table_name = table_name[:1].upper() + table_name[1:]
        self.__table_name = table_name

    @property
    def get_primary_key(self):
        return self.__primary_key

    @property
    def get_primary_key_type(self):
        return self.__primary_key_type

    @property
    def get_imports(self):
        return self.__imports

    @property
    def get_query(self):
        return self.__query

    @property
    def get_fields(self):
        return self.__fields

    def add_fields(self, fields: dict, querys: list = None, noshow: list = None, noedit: list = None):
        if fields is None or len(fields) == 0:
            log.logging.error('字段信息为空')
            raise Exception('字段信息为空！')
        is_upper = True if len(fields) > 0 and 'COLUMN_NAME' in fields[0] else False
        self.__fields.clear()
        for field in fields:
            temp_query = None
            is_show = True
            is_edit = True
            if querys is not None and len(querys) > 0:
                for query in querys:
                    if query['name'] == field['COLUMN_NAME' if is_upper else 'column_name']:
                        temp_query = query
                        break
            if noshow is not None and len(noshow) > 0 and field['COLUMN_NAME' if is_upper else 'column_name'] in noshow:
                is_show = False
            if noedit is not None and len(noedit) > 0 and field['COLUMN_NAME' if is_upper else 'column_name'] in noedit:
                is_edit = False
            self.__add_field(field['COLUMN_NAME' if is_upper else 'column_name'],
                             field['DATA_TYPE' if is_upper else 'data_type'],
                             field['COLUMN_COMMENT' if is_upper else 'column_comment'],
                             field['COLUMN_KEY' if is_upper else 'column_key'],
                             temp_query, is_show, is_edit)

    def __add_field(self, column_name: str, data_type: str, column_comment: str, column_key: str, query: dict = None,
                    is_show: bool = True, is_edit: bool = True):
        org_column = column_name
        if '\n' in column_comment:
            column_comment = column_comment[:column_comment.find('\n')]
        column_name = column_name.replace("is_", "")
        temp = column_name.split('_')
        if len(temp) > 1:
            for i in range(1, len(temp), 1):
                temp[i] = temp[i][:1].upper() + temp[i][1:]
            column_name = ''.join(temp)
        temp_field = {'org_column': org_column, 'name': column_name, 'type': self.__get_type(data_type),
                      'comment': column_comment,
                      'show': is_show, 'edit': is_edit}
        self.__fields.append(temp_field)
        if query is not None:
            query['fieldName'] = query['name']
            query['name'] = column_name
            query['type'] = temp_field['type']
            self.__query.append(query)
        if column_key.lower() == 'pri':
            self.__primary_key = column_name
            self.__primary_key_type = temp_field['type']

    def __get_type(self, data_type):
        type_name = self.__field_types[data_type]
        if type_name == 'Date' and 'java.util.Date' not in self.__imports:
            self.__imports.append('java.util.Date')
        return type_name
