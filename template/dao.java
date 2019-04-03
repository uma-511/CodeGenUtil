package {{packageName}}.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
{% if hasQuery %}
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.apache.ibatis.annotations.Param;
{% endif %}
import java.util.List;
import {{packageName}}.entity.{{entityName}}DO;

/**
 * @author Rookie
 * @ClassName: {{className}}
 * @Description: {{remark}}
 * @date {{date}}
 * @Version 1.0
 */
public interface {{className}} extends BaseMapper<{{entityName}}DO> {

    /**
     * 分页查询
     *
     * @param page 分页参数
     * @return 数据集合
     */
    List<{{entityName}}DO> getPageList(Page page{% if hasQuery %},@Param("ew")Wrapper<{{entityName}}DO> wrapper{% endif %});

}