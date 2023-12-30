<#assign className = data.className>
<#assign packageName = data.packageName>
<#assign idType = "String">

package ${packageName}.service;

import ${packageName}.model.${className};

import java.util.List;

public interface ${className}Service {

List<${className}> getAll${className}s();
${className} get${className}ById(${idType} id);
${className} save${className}(${className} ${className?uncap_first});
void delete${className}(${idType} id);
}
