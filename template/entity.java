package {{packageName}}.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
{% for imp in imports %}
import {{imp}};
{% endfor %}
{% if swagger %}
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
{% endif %}


/**
 * @author Rookie
 * @ClassName: {{className}}
 * @Description: {{remark}}
 * @date {{date}}
 * @Version 1.0
 */
{% if swagger %}
@ApiModel(value = "{{className}}",description = "{{remark}}")
{% endif %}
@TableName(value = "{{tableName}}")
@Data
public class {{className}} implements Serializable{

{% for attr in attrs %}
{% if swagger %}
   @ApiModelProperty(value = "{{attr.comment}}")
{% endif %}
{% if attr.name == primaryKey %}
   @TableId
   private {{attr.type}} {{attr.name}};
{% else %}
   @TableField("{{attr.org_column}}")
   private {{attr.type}} {{attr.name}};
{% endif %}


{% endfor %}

   @Override
   public String toString(){
      return "{{className}}: "{%- for attr in attrs -%} + "{{attr.name}}=" + this.{{attr.name}}{%-if not loop.last-%}+","{%-endif-%}{%-endfor-%};
   }
}