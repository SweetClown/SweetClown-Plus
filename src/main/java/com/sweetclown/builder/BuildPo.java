package com.sweetclown.builder;

import com.sweetclown.bean.Constants;
import com.sweetclown.bean.FieldInfo;
import com.sweetclown.bean.TableInfo;
import com.sweetclown.utils.DateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class BuildPo {

    private static final Logger logger = LoggerFactory.getLogger(BuildPo.class);

    public static void execute(TableInfo tableInfo) {

        File folder = new File(Constants.PATH_PO);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File pofile = new File(folder, tableInfo.getBeanName() + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(pofile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_PO + ";");
            bw.newLine();
            bw.newLine();
            Boolean haveIgnoreBean = false;

            for (FieldInfo field : tableInfo.getFieldList()) {
                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","), field.getPropertyName())) {
                    haveIgnoreBean = true;
                    break;
                }
            }

            if (haveIgnoreBean) {
                bw.write(Constants.IGNORE_BEAN_TOJSON_CLASS + ";");
            }

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
                bw.write("import java.io.Serializable;");
                bw.newLine();
            }

            if (tableInfo.getHaveBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }

            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment());

            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();

            for (FieldInfo field : tableInfo.getFieldList()) {

                if (!StringUtils.isBlank(field.getComment().trim())) {
                    BuildComment.createFieldComment(bw, field.getComment());
                }

                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                }

                if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
                    bw.write("\t" + String.format(Constants.BEAN_DATE_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                    bw.write("\t" + String.format(Constants.BEAN_DATE_UNFORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }

                if (ArrayUtils.contains(Constants.IGNORE_BEAN_TOJSON_FILED.split(","), field.getPropertyName())) {
                    bw.write("\t" + Constants.IGNORE_BEAN_TOJSON_EXPRESSION);
                    bw.newLine();
                }


                bw.write("\tprivate " + field.getJavaType() + " " + field.getPropertyName() + ";");
                bw.newLine();
                bw.newLine();
            }

            for (FieldInfo field : tableInfo.getFieldList()) {
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

            StringBuffer toString = new StringBuffer();
            for (FieldInfo field : tableInfo.getFieldList()) {
                if (Constants.IGNORE_BEAN_TOSTRING_COLUMN != null) {
                    Boolean ignore = false;
                    for (String column : Constants.IGNORE_BEAN_TOSTRING_COLUMN) {
                        if (field.getFieldName().equalsIgnoreCase(column)) {
                            ignore = true;
                        }
                    }
                    if (ignore) {
                        continue;
                    }
                }
                String properName = field.getComment();
                properName = StringUtils.isEmpty(properName) ? field.getPropertyName() : properName;
                String column = field.getPropertyName();
                if (ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
                    column = "(" + column + " == null ? \"空\" : DateUtil.format(" + column + ", " +
                            "DateTimePatternEnum.YYYY_MM_DD_HH_MM_SS" +
                            ".getPattern()))";
                } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType())) {
                    column = "(" + column + " == null ? \"空\" : DateUtil.format(" + column + ", " +
                            "DateTimePatternEnum.YYYY_MM_DD" +
                            ".getPattern()))";
                } else {
                    column = "(" + column + " == null ? \"空\" : " + column + ")";
                }

                toString.append("\"" + properName + ":\"+" + column + "+");
            }

            String toStringResult = "\"" + toString.substring(1, toString.length() - 1) + ";";

            bw.write("\t@Override");
            bw.newLine();
            bw.write("\tpublic String toString () {");
            bw.newLine();
            bw.write("\t\treturn " + toStringResult);
            bw.newLine();
            bw.write("\t}");
            bw.newLine();

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

}
