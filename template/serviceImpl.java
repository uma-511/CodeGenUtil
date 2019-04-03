package {{packageName}}.service.impl;
{% if args|length > 0 %}
import com.baomidou.mybatisplus.mapper.EntityWrapper;
{% endif %}
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import {{packageName}}.service.{{entityName}}Service;
import {{packageName}}.dao.{{entityName}}Dao;
import {{packageName}}.entity.{{entityName}}DO;
{% for imp in imports %}
import {{imp}};
{% endfor %}

/**
 * @author Rookie
 * @ClassName: {{className}}
 * @Description: {{remark}}
 * @date {{date}}
 * @Version 1.0
 */
@Service
public class {{className}} extends BaseServiceImpl<{{entityName}}Dao, {{entityName}}DO> implements {{entityName}}Service {

    @Override
    public Page<{{entityName}}DO> getPageList(Page<{{entityName}}DO> page{% for arg in args %}{% if arg.type == "Date" %},{{arg.type}} {{arg.name}}_start,{{arg.type}} {{arg.name}}_end{% else %},{{arg.type}} {{arg.name}}{% endif %}{% endfor %}) {
{% if args|length > 0 %}
        EntityWrapper<{{entityName}}> ew = new EntityWrapper<>();
{% for arg in args %}{% if arg.type == "Date" %}
        if({{arg.name}}_start != null){
            ew.ge("{{arg.fieldName}}",{{arg.name}}_start);
        }
        if({{arg.name}}_end != null){
            ew.le("{{arg.fieldName}}",{{arg.name}}_end);
        }
{% elif arg.type == "String" %}
        if(!StringUtils.isBlank({{arg.name}})){
            ew.eq("{{arg.fieldName}}",{{arg.name}});
        }
{% else %}
        if({{arg.name}} != {{arg.defaultValue}}){
            ew.eq("{{arg.fieldName}}",{{arg.name}});
        }
{% endif %}
{% endfor %}
        page.setRecords(baseMapper.getPageList(page, ew));
{% else %}
        page.setRecords(baseMapper.getPageList(page));
{% endif %}
        return page;
   }

}