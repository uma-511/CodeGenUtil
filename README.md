# CodeGenUtil
代码生成工具

##python模块
- pip install jinja2  模板工具
- pip install pymysql mysql连接模块

- 完整配置信息
    ~~~
    {
      "tables": [
        {
          "tableName": "sys_setting",
          "prefix": "sys_",
          "codePath": "/Users/rookie/Desktop/com.warrior.base/src/main/java",
          "viewPath": "/Users/rookie/Desktop/com.warrior.view/views/src/views",
          "packageName": "com.warrior.base",
          "remark": "系统设置",
          "query": [
            {
              "name": "sys_key",
              "remark": "参数值",
              "defaultVaule": ""
            },
            {
              "name": "create_time",
              "remark": "新增时间",
              "defaultVaule": ""
            }
          ],
          "noShow": "id,update_time",
          "noEdit": "id,update_time,create_time",
          "genCode": true,
          "genView": true,
          "swagger": true
        }
      ]
    }
    ~~~