package com.zhangqiang.instancerestore.annotationprocessor;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

public class ElementUtils {

    public static String getPackageName(TypeElement typeElement) {

        Name qualifiedName = typeElement.getQualifiedName();
        Name simpleName = typeElement.getSimpleName();
        return qualifiedName.toString().replace("." + simpleName, "");
    }
}
