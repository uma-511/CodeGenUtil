package {{packageName}}.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
{% if hasQuery %}
import com.baomidou.mybatisplus.mapper.Wrapper;
import org.apache.ibatis.annotations.Param;
{% endif %}
import java.util.List;
import {{packageName}}.entity.{{entityName}};

public interface {{className}} extends BaseMapper<{{entityName}}> {

    List<{{entityName}}> getPageList(Pagination page{% if hasQuery %},@Param("ew")Wrapper<{{entityName}}> wrapper{% endif %});

}