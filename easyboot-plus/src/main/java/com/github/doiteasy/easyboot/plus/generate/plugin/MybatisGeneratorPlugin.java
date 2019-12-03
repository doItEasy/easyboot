package com.github.doiteasy.easyboot.plus.generate.plugin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.NullProgressCallback;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class MybatisGeneratorPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        super.initialized(introspectedTable);
//        introspectedTable.setBaseRecordType(introspectedTable.getBaseRecordType() + "DO");
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addJavaDocLine("/**");
        interfaze.addJavaDocLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
        interfaze.addJavaDocLine(" * 表名: " + introspectedTable.getFullyQualifiedTableNameAtRuntime());
        interfaze.addJavaDocLine(" * @date " + new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        interfaze.addJavaDocLine(" */");
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType(Data.class.getCanonicalName()));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(Accessors.class.getCanonicalName()));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(Builder.class.getCanonicalName()));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(AllArgsConstructor.class.getCanonicalName()));
        topLevelClass.addImportedType(new FullyQualifiedJavaType(NoArgsConstructor.class.getCanonicalName()));


        topLevelClass.addAnnotation("@Data");
        topLevelClass.addAnnotation("@Accessors(chain = true)");
        topLevelClass.addAnnotation("@Builder");
        topLevelClass.addAnnotation("@NoArgsConstructor");
        topLevelClass.addAnnotation("@AllArgsConstructor");
        return true;
    }

    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return false;
    }

    public static class LocalDateTimeTypeResolver extends JavaTypeResolverDefaultImpl {
        public LocalDateTimeTypeResolver() {
        }

        @Override
        public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
            FullyQualifiedJavaType fullyQualifiedJavaType = super.calculateJavaType(introspectedColumn);
            if (Date.class.getCanonicalName().equals(fullyQualifiedJavaType.getFullyQualifiedName())) {
                if ("DATE".equals(typeMap.get(introspectedColumn.getJdbcType()).getJdbcTypeName())) {
                    return new FullyQualifiedJavaType(LocalDate.class.getCanonicalName());
                } else {
                    return new FullyQualifiedJavaType(LocalDateTime.class.getCanonicalName());
                }
            }
//            if (StringUtils.contains(introspectedColumn.getRemarks(), "#加密敏感字段#")) {
//                System.out.println("转换敏感字段类型:" + introspectedColumn.getActualColumnName());
//                return new FullyQualifiedJavaType(Sensitive.class.getCanonicalName());
//            }
            return fullyQualifiedJavaType;
        }
    }

    public static class CommentGenerator extends DefaultCommentGenerator {
        @Override
        public void addComment(XmlElement xmlElement) {
            xmlElement.addElement(new TextElement("<!--" + MergeConstants.NEW_ELEMENT_TAG + "-->"));
        }

        @Override
        public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
            topLevelClass.addJavaDocLine("/**");
            topLevelClass.addJavaDocLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
            topLevelClass.addJavaDocLine(" * 表名: " + introspectedTable.getFullyQualifiedTableNameAtRuntime());
            topLevelClass.addJavaDocLine(" * @date " + new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
            topLevelClass.addJavaDocLine(" */");
        }

        @Override
        public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
            innerEnum.addJavaDocLine("/**");
            innerEnum.addJavaDocLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
            innerEnum.addJavaDocLine(" * 枚举:" + innerEnum.toString());
            innerEnum.addJavaDocLine(" */");
        }

        @Override
        public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
            if (introspectedColumn != null && introspectedColumn.getRemarks() != null) {
                field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
            }
            if (introspectedColumn != null && introspectedColumn.getActualColumnName() != null) {
                field.addJavaDocLine(" * 表字段: " + introspectedColumn.getActualColumnName());
            }
            field.addJavaDocLine(" */");
            if(introspectedColumn.getActualColumnName().equals("id")){
                field.addAnnotation("@Id");
            }
        }

        @Override
        public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
            addFieldComment(field, introspectedTable, null);
        }

        @Override
        public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
            method.addJavaDocLine("/**");
            method.addJavaDocLine(" * " + MergeConstants.NEW_ELEMENT_TAG);
            method.addJavaDocLine(" */");
        }

        @Override
        public void addRootComment(XmlElement rootElement) {
        }

        @Override
        public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        }

        @Override
        public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        }

        @Override
        public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        }

        @Override
        public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        }

        @Override
        public void addJavaFileComment(CompilationUnit compilationUnit) {
        }
    }

    private static final ThreadLocal<String> currentTable = new ThreadLocal<>();

    public static class VerboseProgressCallback extends NullProgressCallback {

        public VerboseProgressCallback() {
            super();
        }

        @Override
        public void startTask(String taskName) {
            System.out.println("日志:" + taskName);
            if (StringUtils.startsWith(taskName, "Introspecting table")) {
                currentTable.set(taskName.replace("Introspecting table ", ""));
            }
        }
    }

}
