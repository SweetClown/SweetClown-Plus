package com.sweetclown.builder;

import com.alibaba.fastjson.JSON;
import com.sweetclown.bean.Constants;
import com.sweetclown.bean.FieldInfo;
import com.sweetclown.bean.TableInfo;
import com.sweetclown.utils.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.synth.SynthScrollBarUI;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BuildQuery {

    private static final Logger logger = LoggerFactory.getLogger(BuildQuery.class);

    public static void execute(TableInfo tableInfo) {

        File folder = new File(Constants.PATH_QUERY);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() +  com.sweetclown.utils.StringUtils.upperCaseFirstLetter(Constants.SUFFIX_BEAN_QUERY);
        File queryfile = new File(folder, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(queryfile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            bw.write("package " +  Constants.PACKAGE_QUERY + ";");
            bw.newLine();
            bw.newLine();

            if (tableInfo.getHaveDateTime() || tableInfo.getHaveDate()) {
                bw.newLine();
                bw.write(Constants.BEAN_DATE_FORMAT_CLASS + ";");
                bw.newLine();
                bw.write(Constants.BEAN_DATE_UNFORMAT_CLASS + ";");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_ENUMS + ".DateTimePatternEnum;");
                bw.newLine();
                bw.write("import " + Constants.PACKAGE_UTILS + ".DateUtil;");
                bw.newLine();
                bw.write("import java.util.Date;");
                bw.newLine();
            }

            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }

            bw.newLine();
            BuildComment.createClassComment(bw, tableInfo.getComment()+ "查詢對象");
            bw.write("public class " + className + " extends BaseQuery {");
            bw.newLine();

            List<FieldInfo> extendList = new ArrayList();
            for (FieldInfo field : tableInfo.getFieldList()) {

                if (!StringUtils.isBlank(field.getComment().trim())) {
                    BuildComment.createFieldComment(bw, field.getComment());
                }

                bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();

                if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, field.getSqlType())) {
                    String propertyName = field.getPropertyName() + com.sweetclown.utils.StringUtils.upperCaseFirstLetter(Constants.SUFFIX_BEAN_QUERY_FUZZY);
                    bw.write("\tprivate " + field.getJavaType() + " " + propertyName  + ";");
                    bw.newLine();
                    bw.newLine();

                    FieldInfo fuzzyField = new FieldInfo();
                    fuzzyField.setJavaType(field.getJavaType());
                    fuzzyField.setPropertyName(propertyName);
                    extendList.add(fuzzyField);

                }

                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
                    bw.write("\tprivate String " + field.getPropertyName() + com.sweetclown.utils.StringUtils.upperCaseFirstLetter(Constants.SUFFIX_BEAN_QUERY_START) + ";");
                    bw.newLine();
                    bw.newLine();

                    bw.write("\tprivate String " + field.getPropertyName() + com.sweetclown.utils.StringUtils.upperCaseFirstLetter(Constants.SUFFIX_BEAN_QUERY_END) + ";");
                    bw.newLine();
                    bw.newLine();
                }
            }

            buildGetSet(bw, tableInfo.getFieldList());
            buildGetSet(bw, tableInfo.getFieldExtendList());
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("Create po failed", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void buildGetSet(BufferedWriter bw,List<FieldInfo> fieldInfoList) throws Exception {
        for (FieldInfo field : fieldInfoList) {
            String tempField = com.sweetclown.utils.StringUtils.upperCaseFirstLetter(field.getPropertyName());
            bw.write("\tpublic void set" + tempField + "(" + field.getJavaType() + " " + field.getPropertyName() + ") {");
            bw.newLine();
            bw.write("\t\tthis." + field.getPropertyName() + " = " + field.getPropertyName() + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
            bw.write("\tpublic " + field.getJavaType() + " get" + tempField + "() {");
            bw.newLine();
            bw.write("\t\treturn " + field.getPropertyName() + ";");
            bw.newLine();
            bw.write("\t}");
            bw.newLine();
            bw.newLine();
        }
    }

}
