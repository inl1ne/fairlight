package com.github.inl1ne.arc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that ARC (Automatic Restful CRUD) should inject a RESTful API
 * with CRUD operations for this class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface InjectCrud {
    public Class ApiClass();
    public String EntityPath();
}
