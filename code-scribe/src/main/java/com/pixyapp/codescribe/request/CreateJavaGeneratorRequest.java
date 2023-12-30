package com.pixyapp.codescribe.request;

import java.util.List;

public class CreateJavaGeneratorRequest {

    private String packageName;
    private String className ;
    private List<String> properties;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }
}
