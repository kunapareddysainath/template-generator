<#assign className = data.className>
<#assign packageName = data.packageName>
<#assign idType = "String">

package ${packageName}.controller;

import ${packageName}.model.${className};
import ${packageName}.service.${className}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${className?uncap_first}s")
public class ${className}Controller {

private final ${className}Service ${className?uncap_first}Service;

@Autowired
public ${className}Controller(${className}Service ${className?uncap_first}Service) {
this.${className?uncap_first}Service = ${className?uncap_first}Service;
    }

    @GetMapping
    public List<${className}> getAll${className}s() {
return ${className?uncap_first}Service.getAll${className}s();
    }

    @GetMapping("/{id}")
    public ${className} get${className}ById(@PathVariable ${idType} id) {
return ${className?uncap_first}Service.get${className}ById(id);
    }

    @PostMapping
    public ${className} create${className}(@RequestBody ${className} ${className?uncap_first}) {
return ${className?uncap_first}Service.save${className}(${className?uncap_first});
    }

    @PutMapping("/{id}")
    public ${className} update${className}(@PathVariable ${idType} id, @RequestBody ${className} ${className?uncap_first}) {
${className?uncap_first}.setId(id);
        return ${className?uncap_first}Service.save${className}(${className?uncap_first});
    }

    @DeleteMapping("/{id}")
    public void delete${className}(@PathVariable ${idType} id) {
${className?uncap_first}Service.delete${className}(id);
    }
}
