package {{packageName}}.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
{% for imp in imports %}
import {{imp}};
{% endfor %}

{% if swagger %}
@ApiModel(value = "{{className}}",description = "{{remark}}")
{% endif %}
@TableName(value = "{{tableName}}")
public class {{className}} implements Serializable{

{% for attr in attrs %}
{% if swagger %}
   @ApiModelProperty(value = "{{attr.comment}}")
{% endif %}
{% if attr.name == primaryKey %}
   @TableId
{% endif %}
   @Setter @Getter
   private {{attr.type}} {{attr.name}};

{% endfor %}
}