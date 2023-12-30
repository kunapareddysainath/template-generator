<#assign className = data.className>
<#assign packageName = data.packageName>
<#assign idType = "String">

package ${packageName}.service.impl;
import ${packageName}.model.${className};
import ${packageName}.repository.${className}Repository;
import ${packageName}.service.${className}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;


@Service
public class ${className}ServiceImpl implements ${className}Service {

private final ${className}Repository ${className?uncap_first}Repository;
@Autowired
public ${className}ServiceImpl(${className}Repository ${className?uncap_first}Repository) {
this.${className?uncap_first}Repository = ${className?uncap_first}Repository;
    }

    @Override
    public List<${className}> getAll${className}s() {
        return ${className?uncap_first}Repository.findAll();
    }

    @Override
    public ${className} get${className}ById(${idType} id) {
    Optional<${className}> optional${className} = ${className?uncap_first}Repository.findById(id);
        return optional${className}.orElse(null);
    }

    @Override
    public ${className} save${className}(${className} ${className?uncap_first}) {
        return ${className?uncap_first}Repository.save(${className?uncap_first});
    }

    @Override
    public void delete${className}(${idType} id) {
        ${className?uncap_first}Repository.deleteById(id);
    }
}
