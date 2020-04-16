package com.zhangqiang.instancerestore.annotationprocessor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.zhangqiang.instancerestore.annotations.Instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

@AutoService(Processor.class)
public class AnnoptationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_7;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> hashSet = new LinkedHashSet<>();
        hashSet.add(Instance.class.getName());
        return hashSet;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        Map<Element, List<Element>> elementListMap = new HashMap<>();
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Instance.class);
        for (Element element : elements) {


            Element enclosingElement = element.getEnclosingElement();

            List<Element> instanceElements = elementListMap.get(enclosingElement);
            if (instanceElements == null) {
                instanceElements = new ArrayList<>();
                elementListMap.put(enclosingElement, instanceElements);
            }
            instanceElements.add(element);
        }

        for (Map.Entry<Element, List<Element>> entry : elementListMap.entrySet()) {
            TypeElement typeElement = (TypeElement) entry.getKey();

            List<Element> instanceElementList = entry.getValue();


            MethodSpec restoreInstanceMethod = MethodSpec.methodBuilder("restoreInstance")
                    .addParameter(ParameterSpec.builder(ClassName.get(typeElement), "target").build())
                    .addParameter(ParameterSpec.builder(getBundleTypeName(), "bundle").build())
                    .addCode(makeRestoreInstanceCodeBlock(instanceElementList))
                    .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                            .addMember("value", "\"unchecked\"")
                            .build())
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build();

            MethodSpec saveInstanceMethod = MethodSpec.methodBuilder("saveInstance")
                    .addParameter(ParameterSpec.builder(ClassName.get(typeElement), "target").build())
                    .addParameter(ParameterSpec.builder(getBundleTypeName(), "bundle").build())
                    .addCode(makeSaveInstanceCodeBlock(instanceElementList))
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .build();

            TypeSpec typeSpec = TypeSpec.classBuilder("InstanceRestore_" + typeElement.getSimpleName())
                    .addMethod(restoreInstanceMethod)
                    .addMethod(saveInstanceMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .build();

            JavaFile javaFile = JavaFile.builder(ElementUtils.getPackageName(typeElement), typeSpec).build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return !elementListMap.isEmpty();
    }

    private CodeBlock makeSaveInstanceCodeBlock(List<Element> instanceElementList) {
        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        for (Element instanceElement : instanceElementList) {

            VariableElement variableElement = (VariableElement) instanceElement;

            TypeMirror typeMirror = variableElement.asType();

            System.out.println("===============" + typeMirror + "$$" + typeMirror.getClass());

            String fieldType = typeMirror.toString();
            String fieldName = instanceElement.getSimpleName().toString();
            String keyName;
            String key = variableElement.getAnnotation(Instance.class).value();
            if (key.length() <= 0) {
                keyName = fieldName;
            } else {
                keyName = key;
            }
            if ("int".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putInt($S,target.$N);\n", keyName, fieldName);
            } else if ("int[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putIntArray($S,target.$N);\n", keyName, fieldName);
            } else if ("java.util.ArrayList<java.lang.Integer>".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putIntegerArrayList($S,target.$N);\n", keyName, fieldName);
            } else if ("long".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putLong($S,target.$N);\n", keyName, fieldName);
            } else if ("long[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putLongArray($S,target.$N);\n", keyName, fieldName);
            } else if ("java.lang.String".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putString($S,target.$N);\n", keyName, fieldName);
            } else if ("java.lang.String[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putStringArray($S,target.$N);\n", keyName, fieldName);
            } else if ("java.util.ArrayList<java.lang.String>".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putStringArrayList($S,target.$N);\n", keyName, fieldName);
            } else if ("android.os.Bundle".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putBundle($S,target.$N);\n", keyName, fieldName);
            } else if ("float".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putFloat($S,target.$N);\n", keyName, fieldName);
            } else if ("float[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putFloatArray($S,target.$N);\n", keyName, fieldName);
            } else if ("double".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putDouble($S,target.$N);\n", keyName, fieldName);
            } else if ("double[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putDoubleArray($S,target.$N);\n", keyName, fieldName);
            } else if ("boolean".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putBoolean($S,target.$N);\n", keyName, fieldName);
            } else if ("boolean[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putBooleanArray($S,target.$N);\n", keyName, fieldName);
            } else if ("char".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putChar($S,target.$N);\n", keyName, fieldName);
            } else if ("char[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putCharArray($S,target.$N);\n", keyName, fieldName);
            } else if ("byte".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putByte($S,target.$N);\n", keyName, fieldName);
            } else if ("byte[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putByteArray($S,target.$N);\n", keyName, fieldName);
            } else if ("android.util.Size".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putSize($S,target.$N);\n", keyName, fieldName);
            } else if ("java.lang.CharSequence".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putCharSequence($S,target.$N);\n", keyName, fieldName);
            } else if ("java.lang.CharSequence[]".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putCharSequenceArray($S,target.$N);\n", keyName, fieldName);
            } else if ("java.util.ArrayList<java.lang.CharSequence>".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putCharSequenceArrayList($S,target.$N);\n", keyName, fieldName);
            } else if ("android.util.SizeF".equals(fieldType)) {
                codeBlockBuilder.add("bundle.putSizeF($S,target.$N);\n", keyName, fieldName);
            } else {

                boolean handed = false;
                if (typeMirror instanceof DeclaredType) {

                    DeclaredType declaredType = (DeclaredType) typeMirror;
                    if (isParcelable(declaredType)) {
                        codeBlockBuilder.add("bundle.putParcelable($S,target.$N);\n", keyName, fieldName);
                        handed = true;
                    } else if (isParcelableArrayList(declaredType)) {

                        codeBlockBuilder.add("bundle.putParcelableArrayList($S,target.$N);\n", keyName, fieldName);
                        handed = true;
                        System.out.println("&&&&&&&&&&&&&&&");
                    } else if (isSerializable(declaredType)) {
                        codeBlockBuilder.add("bundle.putSerializable($S,target.$N);\n", keyName, fieldName);
                        handed = true;
                    }
                } else if (typeMirror instanceof ArrayType) {

                    ArrayType arrayType = (ArrayType) typeMirror;
                    if (isParcelableArray(arrayType)) {

                        codeBlockBuilder.add("bundle.putParcelableArray($S,target.$N);\n", keyName, fieldName);
                        handed = true;
                    }
                }
                if (!handed) {
                    throwNotSupportException(variableElement);
                }
            }
        }
        return codeBlockBuilder.build();
    }

    private void throwNotSupportException(VariableElement variableElement) {
        Element enclosingElement = variableElement.getEnclosingElement();
        TypeMirror typeMirror = variableElement.asType();
        throw new IllegalArgumentException("not support "
                + typeMirror + " "
                + variableElement + " in "
                + enclosingElement + " yet!!");
    }

    private CodeBlock makeRestoreInstanceCodeBlock(List<Element> instanceElementList) {

        CodeBlock.Builder codeBlockBuilder = CodeBlock.builder();
        for (Element instanceElement : instanceElementList) {

            VariableElement variableElement = (VariableElement) instanceElement;

            TypeMirror typeMirror = variableElement.asType();

            System.out.println("===============" + typeMirror + "$$" + typeMirror.getClass());

            String fieldType = typeMirror.toString();
            String fieldName = instanceElement.getSimpleName().toString();
            String keyName;
            String key = variableElement.getAnnotation(Instance.class).value();
            if (key.length() <= 0) {
                keyName = fieldName;
            } else {
                keyName = key;
            }
            if ("int".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getInt($S);\n", fieldName, keyName);
            } else if ("int[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getIntArray($S);\n", fieldName, keyName);
            } else if ("java.util.ArrayList<java.lang.Integer>".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getIntegerArrayList($S);\n", fieldName, keyName);
            } else if ("long".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getLong($S);\n", fieldName, keyName);
            } else if ("long[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getLongArray($S);\n", fieldName, keyName);
            } else if ("java.lang.String".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getString($S);\n", fieldName, keyName);
            } else if ("java.lang.String[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getStringArray($S);\n", fieldName, keyName);
            } else if ("java.util.ArrayList<java.lang.String>".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getStringArrayList($S);\n", fieldName, keyName);
            } else if ("android.os.Bundle".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getBundle($S);\n", fieldName, keyName);
            } else if ("float".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getFloat($S);\n", fieldName, keyName);
            } else if ("float[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getFloatArray($S);\n", fieldName, keyName);
            } else if ("double".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getDouble($S);\n", fieldName, keyName);
            } else if ("double[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getDoubleArray($S);\n", fieldName, keyName);
            } else if ("boolean".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getBoolean($S);\n", fieldName, keyName);
            } else if ("boolean[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getBooleanArray($S);\n", fieldName, keyName);
            } else if ("char".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getChar($S);\n", fieldName, keyName);
            } else if ("char[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getCharArray($S);\n", fieldName, keyName);
            } else if ("byte".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getByte($S);\n", fieldName, keyName);
            } else if ("byte[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getByteArray($S);\n", fieldName, keyName);
            } else if ("android.util.Size".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getSize($S);\n", fieldName, keyName);
            } else if ("java.lang.CharSequence".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getCharSequence($S);\n", fieldName, keyName);
            } else if ("java.lang.CharSequence[]".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getCharSequenceArray($S);\n", fieldName, keyName);
            } else if ("java.util.ArrayList<java.lang.CharSequence>".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getCharSequenceArrayList($S);\n", fieldName, keyName);
            } else if ("android.util.SizeF".equals(fieldType)) {
                codeBlockBuilder.add("target.$N=bundle.getSizeF($S);\n", fieldName, keyName);
            } else {

                boolean handed = false;
                if (typeMirror instanceof DeclaredType) {

                    DeclaredType declaredType = (DeclaredType) typeMirror;
                    if (isParcelable(declaredType)) {
                        codeBlockBuilder.add("target.$N=bundle.getParcelable($S);\n", fieldName, keyName);
                        handed = true;
                    } else if (isParcelableArrayList(declaredType)) {

                        codeBlockBuilder.add("target.$N=bundle.getParcelableArrayList($S);\n", fieldName, keyName);
                        handed = true;
                        System.out.println("&&&&&&&&&&&&&&&");
                    } else if (isSerializable(declaredType)) {
                        Element element = declaredType.asElement();
                        if (element instanceof TypeElement) {
                            codeBlockBuilder.add("target.$N=($T)bundle.getSerializable($S);\n", fieldName, declaredType, keyName);
                            handed = true;
                        }
                    }
                } else if (typeMirror instanceof ArrayType) {
                    ArrayType arrayType = (ArrayType) typeMirror;
                    if (isParcelableArray(arrayType)) {

                        TypeMirror type = arrayType.getComponentType();
                        if (type instanceof DeclaredType) {
                            codeBlockBuilder.add("target.$N=($T[])bundle.getParcelableArray($S);\n", fieldName, type, keyName);
                            handed = true;
                        }
                    }
                }
                if (!handed) {
                    throwNotSupportException(variableElement);
                }
            }

        }
        return codeBlockBuilder.build();
    }

    private boolean isParcelable(DeclaredType declaredType) {
        Element element = declaredType.asElement();
        if (element instanceof TypeElement) {
            List<? extends TypeMirror> interfaces = ((TypeElement) element).getInterfaces();
            for (TypeMirror interfaceTypeMirror : interfaces) {
                String interfaceType = interfaceTypeMirror.toString();
                if ("android.os.Parcelable".equals(interfaceType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParcelableArrayList(DeclaredType declaredType) {

        Element element = declaredType.asElement();
        if (element instanceof TypeElement) {
            TypeElement typeElement = (TypeElement) element;
            String typeName = typeElement.getQualifiedName().toString();
            if ("java.util.ArrayList".equals(typeName)) {
                System.out.println("%%%%%%%%%%%%%" + typeElement.toString());
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                for (TypeMirror typeArgument : typeArguments) {
                    if (typeArgument instanceof DeclaredType) {
                        Element typeArgumentElement = ((DeclaredType) typeArgument).asElement();
                        if (typeArgumentElement instanceof TypeElement) {
                            List<? extends TypeMirror> interfaces = ((TypeElement) typeArgumentElement).getInterfaces();
                            for (TypeMirror interfaceTypeMirror : interfaces) {
                                String interfaceType = interfaceTypeMirror.toString();
                                if ("android.os.Parcelable".equals(interfaceType)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private String getTypeArgumentSimpleName(DeclaredType declaredType) {

        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        for (TypeMirror typeArgument : typeArguments) {
            if (typeArgument instanceof DeclaredType) {
                Element element = ((DeclaredType) typeArgument).asElement();
                if (element instanceof TypeElement) {
                    return element.getSimpleName().toString();
                }
            }
        }
        return null;
    }

    private boolean isSerializable(DeclaredType declaredType) {
        Element element = declaredType.asElement();
        if (element instanceof TypeElement) {
            List<? extends TypeMirror> interfaces = ((TypeElement) element).getInterfaces();
            for (TypeMirror interfaceTypeMirror : interfaces) {
                String interfaceType = interfaceTypeMirror.toString();
                if ("java.io.Serializable".equals(interfaceType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isParcelableArray(ArrayType arrayType) {

        TypeMirror componentType = arrayType.getComponentType();
        if (componentType instanceof DeclaredType) {
            return isParcelable(((DeclaredType) componentType));
        }
        return false;
    }

//    private ParameterSpec createBundleParameter(){
//
//        return ParameterSpec.builder().build();
//    }


    private TypeName getBundleTypeName() {
        return ClassName.get("android.os", "Bundle");
    }


//    private TypeName getBundleTypeName(){
//        return ClassName.get("android.os","Bundle");
//    }
}
