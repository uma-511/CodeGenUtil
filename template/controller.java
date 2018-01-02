package {{packageName}}.controller;

import com.warrior.common.JSONMsg;
import com.warrior.common.annotation.SysLog;
import {{packageName}}.entity.{{entityName}};
import {{packageName}}.service.{{entityName}}Service;
import com.warrior.common.web.WarriorBaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.plugins.Page;
{% if swagger %}
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
{% else %}
import springfox.documentation.annotations.ApiIgnore; 
{% endif %}
{% for imp in imports %}
import {{ imp }};
{% endfor %}

{% if swagger %}
@Api(value="{{className}}",tags = "{{remark}}",description = "{{remark}}")
{% else %}
@ApiIgnore
{% endif %}
@RestController
@RequestMapping("/{{name}}")
public class {{className}} extends WarriorBaseController {

    @Autowired
    private {{entityName}}Service ${name}Service;

    /**
    * 根据id获取{{remark}}
    *
    * @param {{primaryKey}}
    * @return
    */
    @RequiresPermissions("admin:{{name}}:view")
    @RequestMapping(value = "{{"/{%s}"|format(primaryKey)}}", method = {RequestMethod.GET})
{% if swagger %}
    @ApiOperation(value = "获取{{remark}}",httpMethod = "GET",response = JSONMsg.class)
{% endif %}
    public JSONMsg query{{entityName}}(
{% if swagger %}
        @ApiParam(name="{{primaryKey}}",value = "{{primaryKey}}",required = true)
{% endif %}
        @PathVariable(value = "{{primaryKey}}") {{primaryKeyType}} {{primaryKey}}) {
        return buildMsg({{name}}Service.selectById({{primaryKey}}));
    }

    /**
    * 新增{{remark}}
    *
    * @param {{name}}
    * @return
    */
    @SysLog("新增{{remark}}")
    @RequiresPermissions("admin:{{name}}:add")
    @RequestMapping(value = {""}, method = {RequestMethod.POST})
{% if swagger %}
    @ApiOperation(value = "新增{{remark}}",httpMethod = "POST",response = JSONMsg.class)
{% endif %}
    public JSONMsg add{{entityName}}({% if swagger %}@ModelAttribute{% endif %} {{entityName}} {{name}}) {
        return buildMsg({{name}}Service.insert({{name}}));
    }

    /**
    * 删除{{remark}}
    *
    * @param {{primaryKey}}
    * @return
    */
    @SysLog("删除{{remark}}")
    @RequiresPermissions("admin:{{name}}:del")
    @RequestMapping(value = "{{"/{%s}"|format(primaryKey)}}", method = {RequestMethod.DELETE})
{% if swagger %}
    @ApiOperation(value = "删除{{name}}",httpMethod = "DELETE",response = JSONMsg.class)
{% endif %}
    public JSONMsg del{{entityName}}(
{% if swagger %}
        @ApiParam(name="{{primaryKey}}",value = "{{primaryKey}}",required = true)
{% endif %}
        @PathVariable(value = "{{primaryKey}}") {{primaryKeyType}} {{primaryKey}}) {
        return buildMsg({{name}}Service.deleteById({{primaryKey}}));
    }

    /**
    * 修改{{remark}}
    *
    * @param {{name}}
    * @return
    */
    @SysLog("修改{{remark}}")
    @RequiresPermissions("admin:{{name}}:update")
    @RequestMapping(value = "", method = {RequestMethod.PUT})
{% if swagger %}
    @ApiOperation(value = "修改{{remark}}",httpMethod = "PUT",response = JSONMsg.class)
{% endif %}
    public JSONMsg modified{{entityName}}({% if swagger %}@ModelAttribute {% endif %} {{entityName}} {{name}}) {
        return buildMsg({{name}}Service.insertOrUpdate({{name}}));
    }

    /**
    * 查询{{remark}}列表
    *
    */
    @RequiresPermissions("admin:{{name}}:view")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
{% if swagger %}
    @ApiOperation(value = "获取{{remark}}列表",httpMethod = "GET",response = JSONMsg.class)
{% endif %}
    public JSONMsg get{{entityName}}List(
{% for arg in args %}
{% if arg.type == "Date" %}
{% if swagger %}
        @ApiParam(name="{{arg.name}}_start",value = "{{arg.remark}}")
{% endif %}
        @RequestParam(name = "{{arg.name}}_start", defaultValue = "") {{arg.type}} {{arg.name}}_start,
{% if swagger %}
        @ApiParam(name="{{arg.name}}_end",value = "{{arg.remark}}")
{% endif %}
        @RequestParam(name = "{{arg.name}}_end", defaultValue = "") {{arg.type}} {{arg.name}}_end,
{% else %}
{% if swagger %}
        @ApiParam(name="{{arg.name}}",value = "{{arg.remark}}")
{% endif %}
        @RequestParam(name = "{{arg.name}}", defaultValue = "") {{arg.type}} {{arg.name}},
{% endif %}
{% endfor %}
{% if swagger%}
        @ApiParam(name="page",value = "页码")
{% endif %}
        @RequestParam(name="page",defaultValue = "1")int page,
{% if swagger %}
        @ApiParam(name="rows",value = "分页大小")
{% endif %}
        @RequestParam(name="rows",defaultValue = "10")int rows) {

        return buildMsg({{name}}Service.getPageList(new Page(page,rows){% for arg in args %}{% if arg.type == "Date" %},{{arg.name}}_start,{{arg.name}}_end {% else %},{{arg.name}}{% endif %}{% endfor %}));
    }
}