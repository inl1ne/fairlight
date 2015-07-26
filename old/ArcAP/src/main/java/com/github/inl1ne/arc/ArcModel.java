package com.github.inl1ne.arc;

public class ArcModel {
    public final String packagename;
    public final String classname;
    public final String baseclass;
    public final String path;
    public final String entityName;
    public final String idfield;
    public final String idtype;

    public ArcModel(String packagename, String classname, String baseclass, String path, String entityName, String idfield, String idtype) {
        this.packagename = packagename;
        this.classname = classname;
        this.baseclass = baseclass;
        this.path = path;
        this.entityName = entityName;
        this.idfield = idfield;
        this.idtype = idtype;
    }

    public String getPackagename() {
        return packagename;
    }

    public String getClassname() {
        return classname;
    }

    public String getBaseclass() {
        return baseclass;
    }

    public String getPath() {
        return path;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getIdfield() {
        return idfield;
    }

    public String getIdtype() {
        return idtype;
    }
}
