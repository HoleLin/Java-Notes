package ${classPath};
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ${className} {

<#list attributes as attribute>
    private ${attribute.type} ${attribute.name};
</#list>

}