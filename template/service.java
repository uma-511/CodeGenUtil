package {{packageName}}.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
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
public interface {{className}} extends IService<{{entityName}}DO>{

    Page<{{entityName}}DO> getPageList(Page<{{entityName}}DO> page{% for arg in args %}{% if arg.type == 'Date' %},{{arg.type}} {{arg.name}}_start,{{arg.type}} {{arg.name}}_end{% else %},{{arg.type}} {{arg.name}}{% endif %}{% endfor %});

}
