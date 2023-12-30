<#assign className = data.className>
<#assign packageName = data.packageName>
<#assign properties = data.properties>

package ${packageName}.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "${className?uncap_first}s")
public class ${className} {

@Id
private String id;

<#list properties as property>
private String ${property};
</#list>

// Constructors, getters, setters, and other methods

<#list properties as property>
public String get${property?cap_first}() {
return ${property};
}

public void set${property?cap_first}(String ${property}) {
this.${property} = ${property};
    }
    </#list>
}
