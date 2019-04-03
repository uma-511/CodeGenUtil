#! python3
from database.dbhelper import DBHelper
from model.table_meta import TableMeta
import os
import time
from jinja2 import Environment
from jinja2 import FileSystemLoader


class CodeGen:
    # 配置信息
    __config = {}
    __dbhelper = None
    __evn = None

    def __init__(self, config=None):
        self.set_config(config)

    def set_config(self, config):
        self.__config = config
        self.__dbhelper = DBHelper()
        path = os.path.join('../', 'template')
        path = os.path.abspath(path)
        self.__evn = Environment(loader=FileSystemLoader(path), trim_blocks=True)

    def gen_code(self):
        if len(self.__config) == 0:
            raise Exception('配置信息为空！')
        self.__dbhelper.get_connection()
        for table in self.__config:
            table_meta = TableMeta(table['tableName'], table['prefix'])
            fields = self.__dbhelper.get_table_meta(table['tableName'])
            table_meta.add_fields(fields, table['query'], table['noShow'].split(','), table['noEdit'].split(','))
            if ('genCode' not in table.keys()) or table['genCode']:
                self.__gen__dao(table, table_meta)
                self.__gen_service(table, table_meta)
                self.__gen_entity(table, table_meta)
                self.__gen_controller(table, table_meta)
            if ('genView' not in table.keys()) or table['genView']:
                self.__gen_view(table, table_meta)
        self.__dbhelper.close_connection()

    def __gen_entity(self, table: dict, table_meta: TableMeta):
        template = self.__evn.get_template('entity.java')
        imports = table_meta.get_imports
        class_name = CodeGen.get_class_name(table['tableName'], table['prefix']) + 'DO'
        data = {
            'packageName': table['packageName'],
            'imports': imports,
            'swagger': table['swagger'],
            'className': class_name,
            'remark': table['remark'],
            'tableName': table['tableName'],
            'attrs': table_meta.get_fields,
            'primaryKey': table_meta.get_primary_key,
            'date': CodeGen.get_date()
        }
        target = template.render(data)
        path = (table['codePath'] + '/', table['codePath'])[table['codePath'].endswith('/')]
        path = path + (table['packageName'] + '.entity').replace('.', '/')
        self.write_to_file(path, target, class_name + '.java')

    def __gen__dao(self, table: dict, table_meta: TableMeta):
        template = self.__evn.get_template('mapper.xml')
        class_name = CodeGen.get_class_name(table['tableName'], table['prefix'])
        data = {
            'packageName': table['packageName'],
            'entityName': class_name,
            'className': class_name+'Dao',
            'hasQuery': (False, True)[len(table_meta.get_query) > 0],
            'date': CodeGen.get_date()
        }
        target = template.render(data)
        basepath = (table['codePath'] + '/', table['codePath'])[table['codePath'].endswith('/')]
        path = basepath + (table['packageName'] + '.mapper').replace('.', '/')
        self.write_to_file(path, target, class_name + '.mapper.xml')
        template = self.__evn.get_template('dao.java')
        target = template.render(data)
        path = basepath + (table['packageName'] + '.dao').replace('.', '/')
        self.write_to_file(path, target, class_name + 'Dao.java')

    def __gen_service(self, table: dict, table_meta: TableMeta):
        template = self.__evn.get_template('service.java')
        class_name = CodeGen.get_class_name(table['tableName'], table['prefix'])
        data = {
            'packageName': table['packageName'],
            'entityName': class_name,
            'className': class_name + 'Service',
            'args': table_meta.get_query,
            'imports': table_meta.get_imports,
            'date': CodeGen.get_date()
        }
        target = template.render(data)
        basepath = (table['codePath'] + '/', table['codePath'])[table['codePath'].endswith('/')]
        path = basepath + (table['packageName'] + '.service').replace('.', '/')
        self.write_to_file(path, target, class_name + 'Service.java')
        data['className'] = class_name + 'ServiceImpl'
        template = self.__evn.get_template('serviceImpl.java')
        target = template.render(data)
        path = basepath + (table['packageName'] + '.service.impl').replace('.', '/')
        self.write_to_file(path, target, class_name + 'ServiceImpl.java')

    def __gen_controller(self, table: dict, table_meta: TableMeta):
        template = self.__evn.get_template('controller.java')
        class_name = CodeGen.get_class_name(table['tableName'], table['prefix'])
        data = {
            'packageName': table['packageName'],
            'entityName': class_name,
            'className': class_name + 'Controller',
            'name': class_name[:1].lower() + class_name[1:],
            'swagger': table['swagger'],
            'remark': table['remark'],
            'args': table_meta.get_query,
            'imports': table_meta.get_imports,
            'primaryKey': table_meta.get_primary_key,
            'primaryKeyType': table_meta.get_primary_key_type,
            'date': CodeGen.get_date()
        }
        target = template.render(data)
        path = (table['codePath'] + '/', table['codePath'])[table['codePath'].endswith('/')]
        path = path + (table['packageName'] + '.controller').replace('.', '/')
        self.write_to_file(path, target, class_name + 'Controller.java')

    def __gen_view(self, table: dict, table_meta: TableMeta):
        template = self.__evn.get_template('view.vue')
        data = {
            'primaryKey': table_meta.get_primary_key,
            'name': table_meta.get_table_name[:1].lower() + table_meta.get_table_name[1:],
            'remark': table['remark'],
            'agrs': table_meta.get_query,
            'attrs': table_meta.get_fields,
            'date': CodeGen.get_date()
        }
        target = template.render(data)
        path = (table['viewPath'] + '/', table['viewPath'])[table['viewPath'].endswith('/')]
        path = path + table_meta.get_table_name[:1].lower() + table_meta.get_table_name[1:]
        self.write_to_file(path, target, 'index.vue')

    @staticmethod
    def write_to_file(path: str, content: str, file_name: str):
        if not os.path.exists(path):
            os.makedirs(path)
        path = os.path.join(path, file_name)
        target_file = open(path, 'wb')
        target_file.write(content.encode())
        target_file.close()

    @staticmethod
    def get_class_name(table_name: str, prefix: str):
        if prefix is not None:
            table_name = table_name.replace(prefix, '')
        temp = table_name.split('_')
        if len(temp) > 1:
            for i in range(0, len(temp), 1):
                temp[i] = temp[i][:1].upper() + temp[i][1:]
            new_table_name = ''.join(temp)
        else:
            new_table_name = table_name[:1].upper() + table_name[1:]
        return new_table_name

    @staticmethod
    def get_date():
        return time.strftime("%Y-%m-%d %H:%M", time.localtime())
