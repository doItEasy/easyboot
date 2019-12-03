package com.github.doiteasy.easyboot.plus.generate;

import com.github.doiteasy.easyboot.plus.generate.plugin.MybatisGeneratorPlugin;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.Comment;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class MybatisGenerator {


    public static void write (String configFile, String table) {
        if (StringUtils.isBlank(table)) {
            return;
        }
        if ("%".equals(table)) {
            System.setProperty("GEN_TABLE_NAMES", "%");
        } else {
            String prefix = "ga_";
            if (!table.startsWith(prefix)) {
                table = prefix + table;
            }
            System.setProperty("GEN_TABLE_NAMES", table.toLowerCase());
        }
        List<String> warnings = new ArrayList<>();
        try {
            ConfigurationParser cp = new ConfigurationParser(warnings);
            Configuration config = cp.parseConfiguration(new File(configFile));

            DefaultShellCallback shellCallback = new DefaultShellCallback(false) {
                @Override
                public boolean isMergeSupported() {
                    return true;
                }

                @Override
                public String mergeJavaFile(String newFileSource, File existingFile, String[] javadocTags, String fileEncoding) {
                    CompilationUnit newCompilationUnit = JavaParser.parse(newFileSource);
                    CompilationUnit existingCompilationUnit;
                    try {
                        existingCompilationUnit = JavaParser.parse(existingFile);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Set<String> importNames = new LinkedHashSet<>();
                    NodeList<ImportDeclaration> imports = new NodeList<>();

                    for (ImportDeclaration im : newCompilationUnit.getImports()) {
                        if (!importNames.contains(im.getNameAsString())) {
                            imports.add(im);
                            importNames.add(im.getNameAsString());
                        }
                    }
                    for (ImportDeclaration im : existingCompilationUnit.getImports()) {
                        if (!importNames.contains(im.getNameAsString())) {
                            imports.add(im);
                            importNames.add(im.getNameAsString());
                        }
                    }
                    newCompilationUnit.setImports(imports);
                    NodeList<TypeDeclaration<?>> oldType = existingCompilationUnit.getTypes();
                    for (int i = 0; i < oldType.size(); i++) {
                        for (FieldDeclaration fieldDeclaration : oldType.get(i).getFields()) {
                            final Optional<Comment> comment = fieldDeclaration.getComment();
                            if (!comment.isPresent() || (comment.isPresent() && !comment.get().getContent().contains(MergeConstants.NEW_ELEMENT_TAG))) {
                                newCompilationUnit.getType(i).getMembers().add(fieldDeclaration);
                            }
                        }
                    }
                    for (int i = 0; i < oldType.size(); i++) {
                        for (MethodDeclaration methodDeclaration : oldType.get(i).getMethods()) {
                            final Optional<Comment> comment = methodDeclaration.getComment();
                            if (!comment.isPresent() || (comment.isPresent() && !comment.get().getContent().contains(MergeConstants.NEW_ELEMENT_TAG))) {
                                newCompilationUnit.getType(i).getMembers().add(methodDeclaration);
                            }
                        }
                    }

                    return newCompilationUnit.toString();
                }
            };

            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, warnings);
            myBatisGenerator.generate(new MybatisGeneratorPlugin.VerboseProgressCallback(), new HashSet<String>(), new HashSet<String>());

        } catch (XMLParserException e) {
            writeLine(getString("Progress.3"));
            writeLine();
            for (String error : e.getErrors()) {
                writeLine(error);
            }

            return;
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } catch (InvalidConfigurationException e) {
            writeLine(getString("Progress.16"));
            for (String error : e.getErrors()) {
                writeLine(error);
            }
            return;
        } catch (InterruptedException e) {

        }

        for (String warning : warnings) {
            writeLine(warning);
        }

        if (warnings.size() == 0) {
            writeLine(getString("Progress.4"));
        } else {
            writeLine();
            writeLine(getString("Progress.5"));
        }
    }

    private static void writeLine(String message) {
        System.out.println(message);
    }

    private static void writeLine() {
        System.out.println();
    }

}
