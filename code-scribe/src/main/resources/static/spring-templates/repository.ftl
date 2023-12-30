<#assign className = data.className>
<#assign packageName = data.packageName>
<#assign idType = "String">

package ${packageName}.repository;
import ${packageName}.model.${className};
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ${className}Repository extends MongoRepository<${className}, ${idType}> {

// Additional custom queries can be defined here if needed
}
