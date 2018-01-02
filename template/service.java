package {{packageName}}.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import {{packageName}}.entity.{{entityName}};
{% for imp in imports %}
import {{imp}};
{% endfor %}

public interface {{className}} extends IService<{{entityName}}>{

    Page<{{entityName}}> getPageList(Page<{{entityName}}> page{% for arg in args %}{% if arg.type == 'Date' %},{{arg.type}} {{arg.name}}_start,{{arg.type}} {{arg.name}}_end{% else %},{{arg.type}} {{arg.name}}{% endif %}{% endfor %});

}
