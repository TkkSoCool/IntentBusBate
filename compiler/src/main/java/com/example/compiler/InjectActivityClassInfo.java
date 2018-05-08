package com.example.compiler;

import com.example.api.Utils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Created  on 2018/4/13
 * @author 唐开阔
 * @describe 生成类信息
 * 问题1：不能再注解处理区判断数据class是否序列化
 * 问题2：使用值传递的时，内存泄漏问题
 * 问题3：父类也有注解时的一系列问题
 */
public class InjectActivityClassInfo {
    public TypeElement mClassElement; //使用注解的类元素
    private Elements mElementUtils; //元素相关的辅助类
    public List<ParamInfo> paramInfos = new LinkedList<>();//方法

    public InjectActivityClassInfo(TypeElement mClassElement, Elements mElementUtils) {
        this.mClassElement = mClassElement;
        this.mElementUtils = mElementUtils;
    }

    public JavaFile getJavaFile() {
        //标记类信息
        String packageName = getPackageName(mClassElement);
        String className = getClassName(mClassElement, packageName);
        ClassName bindingClassName = ClassName.get(packageName, className);
        //生成类名
        String creatClassName = bindingClassName.simpleName() + "_SUBSCRIBE_INFO";
        /**
         * Builder数据内部类
         */
        TypeSpec.Builder builderInnerTypeBuilder = TypeSpec.classBuilder("Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addField(FieldSpec.builder(TypeNameUtils.CONTEXT, "context", Modifier.PRIVATE).build())
                .addField(FieldSpec.builder(TypeName.BOOLEAN, "isDataByIntent", Modifier.PRIVATE).build());
        /**
         * Builder构造方法
         */
        MethodSpec.Builder constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(TypeNameUtils.CONTEXT, "from", Modifier.FINAL)
                .addStatement("this.context = from;");
        /**
         * 暴露给外部的builder的方法用于生成对象
         */
        MethodSpec.Builder builderBuider = MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(TypeNameUtils.CONTEXT, "from", Modifier.FINAL)
                .returns(ClassName.get("", "Builder"))
                .addStatement("return new Builder(from)");
        /**
         * Builder的create方法，用于创建生成类
         */
        MethodSpec.Builder creatBuider = MethodSpec.methodBuilder("create")
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get("", creatClassName))
                .addStatement("instance = new $L(this)", creatClassName)
                .addStatement("return instance");


        /**
         * 生成类构成方法
         */
        MethodSpec.Builder creatClassConstructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PRIVATE)
                .addParameter(TypeNameUtils.BUILDER, "builder", Modifier.FINAL)
                .addStatement(" this.builder = builder")
                .addStatement("mContent = builder.context");
        /**
         * 跳转方法
         */
        MethodSpec.Builder goBuilder = MethodSpec.methodBuilder("go")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$T intent = new $T(mContent, $T.class)", TypeNameUtils.INTENT, TypeNameUtils.INTENT, TypeName.get(mClassElement.asType()))
                .addStatement("setIntent(intent)")
                .addStatement(" mContent.startActivity(intent)");
        /**
         * 给Intent赋值方法
         */
        MethodSpec.Builder setIntentBuilder = MethodSpec.methodBuilder("setIntent")
                .addModifiers(Modifier.PRIVATE)
                .addParameter(TypeNameUtils.INTENT, "intent");
        /**
         * 从intent获取字段值
         */
        MethodSpec.Builder setValueByIntentBuilder = MethodSpec.methodBuilder("setValueByIntent")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "target", Modifier.FINAL);
        /**
         * 从引用获取字段值
         */
        MethodSpec.Builder setValueByMemoryBuilder = MethodSpec.methodBuilder("setValueByMemory")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "target", Modifier.FINAL);
        /**
         * 从intent获取字段值
         */
        MethodSpec.Builder bindBuilder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(TypeName.get(mClassElement.asType()), "target", Modifier.FINAL)
                .addStatement("if (instance == null)" +
                        "\nreturn")
                .addStatement("if (instance.builder.isDataByIntent)" +
                        "\ninstance.setValueByIntent(target)")
                .addStatement("  else" +
                        "\ninstance.setValueByMemory(target)");

        for (int i = 0; i < paramInfos.size(); i++) {
            ParamInfo paramInfo = paramInfos.get(i);
            TypeName typeName = paramInfo.getTypeName();
            String name = paramInfo.getName();
            //builder类字段
            builderInnerTypeBuilder.addField(FieldSpec.builder(typeName, name, Modifier.PRIVATE).build());
            //builder类set方法
            MethodSpec.Builder setValueMethodBuilder = MethodSpec.methodBuilder(getSetName(name))
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(typeName, name)
                    .returns(ClassName.get("", builderInnerTypeBuilder.build().name))
                    .addStatement("this.$L = $L", name, name)
                    .addStatement("return this");
            builderInnerTypeBuilder.addMethod(setValueMethodBuilder.build());
            //通过引用直接赋值
            setValueByMemoryBuilder.addStatement("target.$L = builder.$L", name, name);
            /**
             * 通过intent携带数据
             */
            if (typeName.isPrimitive()) {
                String getIntExtraName = getOneUppercaseStr(typeName.toString());
                setValueByIntentBuilder.addStatement("target.$L =  target.getIntent().get$LExtra($S,target.$L)", name, getIntExtraName, name, name);
                setIntentBuilder.addStatement("intent.putExtra($S,builder.$L)", name, name);
            } else if (typeName instanceof ArrayTypeName && ((ArrayTypeName) typeName).componentType.isPrimitive()) {
                String getIntExtraName = getOneUppercaseStr(((ArrayTypeName) typeName).componentType.toString());
                setValueByIntentBuilder.addStatement("target.$L =  target.getIntent().get$LExtra($S)", name, getIntExtraName + "Array", name);
                setIntentBuilder.addStatement("intent.putExtra($S,builder.$L)", name, name);
            } else if (typeName instanceof ParameterizedTypeName && TypeNameUtils.ARRAYLIST.equals(((ParameterizedTypeName) typeName).rawType)) {
                ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
                TypeName paraTypeName = parameterizedTypeName.typeArguments.get(0);
                if (paraTypeName.toString().equals(TypeNameUtils.INTEGER.toString())) {
                    setValueByIntentBuilder.addStatement("target.$L =  target.getIntent().getIntegerArrayListExtra($S)", name, name);
                    setIntentBuilder.addStatement("intent.putIntegerArrayListExtra($S,builder.$L)", name, name);
                } else if (paraTypeName.toString().equals(TypeNameUtils.STRING.toString())) {
                    setValueByIntentBuilder.addStatement("target.$L =  target.getIntent().getStringArrayListExtra($S)", name, name);
                    setIntentBuilder.addStatement("intent.putStringArrayListExtra($S,builder.$L)", name, name);
                } else if (paraTypeName.toString().equals(TypeNameUtils.CHAR_SEQUENCE.toString())) {
                    setValueByIntentBuilder.addStatement("target.$L =  target.getIntent().getCharSequenceArrayListExtra($S)", name, name);
                    setIntentBuilder.addStatement("intent.putCharSequenceArrayListExtra($S,builder.$L)", name, name);
                } else if (paraTypeName.toString().equals(TypeNameUtils.PARCELABLE.toString())) {
                    setValueByIntentBuilder.addStatement("target.$L =  target.getIntent().getParcelableArrayListExtra($S)", name, name);
                    setIntentBuilder.addStatement("intent.putParcelableArrayListExtra($S,builder.$L)", name, name);
                }
            } else {
//                Class nowClass = getTypeClass(typeName.toString());
//                setIntentBuilder.addStatement("//$L",typeName.toString());
//                    setIntentBuilder.beginControlFlow("if($T.typeIsisAssignableFromParcelable(builder.$L.getClass(),$T.class))",TypeNameUtils.UTILS,name,TypeNameUtils.PARCELABLE)
//                            .addStatement("intent.putExtra($S, ($T)builder.$L)",name,TypeNameUtils.PARCELABLE,name)
//                            .endControlFlow();
//                    setIntentBuilder.beginControlFlow("else if($T.typeIsisAssignableFromSerializable(builder.$L.getClass()))",TypeNameUtils.UTILS,name)
//                            .addStatement("intent.putExtra($S, ($T)builder.$L)",name,Serializable.class,name)
//                            .endControlFlow();


            }

        }
        builderInnerTypeBuilder.addMethod(constructor.build());
        builderInnerTypeBuilder.addMethod(creatBuider.build());
        //生成类
        TypeSpec finderClass = TypeSpec.classBuilder(creatClassName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get("com.example.api", "ISetParamValue"), TypeName.get(mClassElement.asType())))
                .addMethod(setValueByIntentBuilder.build())
                //context对象
                .addField(FieldSpec.builder(TypeNameUtils.CONTEXT, "mContent", Modifier.PRIVATE).build())
                //Builder对象
                .addField(FieldSpec.builder(TypeNameUtils.BUILDER, "builder", Modifier.PRIVATE).build())
                //单例对象
                .addField(FieldSpec.builder(TypeNameUtils.THIS(creatClassName), "instance", Modifier.PRIVATE, Modifier.STATIC).build())
                //构造方法
                .addMethod(creatClassConstructor.build())
                .addMethod(setValueByMemoryBuilder.build())
                //跳转方法
                .addMethod(goBuilder.build())
                //builder方法生成对象
                .addMethod(builderBuider.build())
                //使用intent携带数据
                .addMethod(setIntentBuilder.build())
                // Builder内部类
                .addType(builderInnerTypeBuilder.build())
                //目标类赋值方法
                .addMethod(bindBuilder.build())
                .build();
        return JavaFile.builder(packageName, finderClass).build();

    }

    /**
     * 一个使用注解的字段所有信息
     */
    static class ParamInfo {
        public String name;
        public TypeName typeName;

        public ParamInfo(VariableElement variableElement) {
            name = variableElement.getSimpleName().toString();
            typeName = TypeName.get(variableElement.asType());
        }

        public TypeName getTypeName() {
            return typeName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    private String getPackageName(TypeElement type) {
        return mElementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private String getSetName(String fileName) {
        return "set" + getOneUppercaseStr(fileName);
    }

    private String getOneUppercaseStr(String str) {
        if (!Character.isUpperCase(str.charAt(0)))
            str = (new StringBuilder()).append(Character.toUpperCase(str.charAt(0))).append(str.substring(1)).toString();
        return str;
    }

    private Class getTypeClass(String strName){
        Class clz = null;
        try {
            clz = Class.forName(strName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clz;
    }

}
