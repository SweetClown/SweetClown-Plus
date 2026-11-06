package com.sweetclown.builder;

import com.alibaba.fastjson.JSON;
import com.sweetclown.bean.Constants;
import com.sweetclown.bean.FieldInfo;
import com.sweetclown.bean.TableInfo;
import com.sweetclown.utils.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildMapperXML {

    private static final Logger logger = LoggerFactory.getLogger(BuildMapperXML.class);

    static String base_result_map = "base_result_map";
    static String base_Column_List = "base_Column_List";
    static String base_condition_filed = "base_condition_filed";
    static String query_condition = "query_condition";
    static String selectList = "selectList";
    static String selectNumber = "selectNumber";

    public static void execute(TableInfo tableInfo) {

        File folder = new File(Constants.PATH_MAPPERS_XMLS);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String className = tableInfo.getBeanName() + com.sweetclown.utils.StringUtils.upperCaseFirstLetter(Constants.SUFFIX_MAPPER);

        File mapperFile = new File(folder, className + ".xml");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(mapperFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);

            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            bw.newLine();
            bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"");
            bw.newLine();
            bw.write("\t\t\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
            bw.newLine();
            bw.write("<mapper namespace=\"" + Constants.PACKAGE_MAPPERS + "." + className + "\">");
            bw.newLine();

            bw.write("\t<!--Entity Mapping-->");
            bw.newLine();

            String poClass = Constants.PACKAGE_PO + "." + tableInfo.getBeanName();
            bw.write("\t<resultMap id=\"" + base_result_map + "\" type=\"" + poClass + "\">");
            bw.newLine();

            FieldInfo idField = null;
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                if ("PRIMARY".equals(entry.getKey())) {
                    List<FieldInfo> fieldInfoList = entry.getValue();
                    if (fieldInfoList.size() == 1) {
                        idField = fieldInfoList.get(0);
                        break;
                    }
                }
            }


            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                bw.write("\t\t<!--" + fieldInfo.getComment() + "-->");
                bw.newLine();
                String key = "";
                if (idField != null & fieldInfo.getPropertyName().equals(idField.getPropertyName())) {
                    key = "id";
                } else {
                    key = "result";
                }
                bw.write("\t\t<" + key + " column=\"" + fieldInfo.getFieldName() + "\" property=\"" + fieldInfo.getPropertyName() + "\"/>");
                bw.newLine();
            }

            bw.write("\t</resultMap>");
            bw.newLine();
            baseColumnListGenerator(bw, tableInfo);
            baseConditionFiledGenerator(bw, tableInfo);
            queryCondition(bw, tableInfo);
            selectList(bw, tableInfo);
            selectNumber(bw, tableInfo);
            bw.write("</mapper>");
            bw.newLine();

            bw.flush();
        } catch (Exception e) {
            logger.error("Created Mappers XML Failed", e);
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

    private static void baseColumnListGenerator(BufferedWriter bw, TableInfo tableInfo) throws IOException {
        //base_Column_List Generation
        bw.newLine();
        bw.write("\t<!--" + base_Column_List + "-->");
        bw.newLine();
        bw.write("\t<sql id=\"" + base_Column_List + "\">");
        bw.newLine();
        StringBuilder columnBuilder = new StringBuilder();
        for (FieldInfo field : tableInfo.getFieldList()) {
            columnBuilder.append(field.getFieldName()).append(",");
        }
        String columnBuilderStr = columnBuilder.substring(0, columnBuilder.lastIndexOf(","));
        bw.write("\t\t" + columnBuilderStr);
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
    }

    private static void baseConditionFiledGenerator(BufferedWriter bw, TableInfo tableInfo) throws IOException {
        //base_Column_List Generation
        bw.write("\t<!--" + base_condition_filed + "-->");
        bw.newLine();
        bw.write("\t<sql id=\"" + base_condition_filed + "\">");
        bw.newLine();
        for (FieldInfo field : tableInfo.getFieldList()) {
            String stringQuery = "";
            if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, field.getSqlType())) {
                stringQuery = " and query." + field.getPropertyName() + " != '' ";
            }
            bw.write("\t\t<if test=\"query." + field.getPropertyName() + " != null" + stringQuery + "\">");
            bw.newLine();
            bw.write("\t\t\tand " + field.getFieldName() + " = #{query." + field.getPropertyName() + "}");
            bw.newLine();
            bw.write("\t\t</if>");
            bw.newLine();
        }
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
    }

    private static void queryCondition(BufferedWriter bw, TableInfo tableInfo) throws IOException {
        //queryCondition Generation
        bw.write("\t<!--" + query_condition + "-->");
        bw.newLine();
        bw.write("\t<sql id=\"" + query_condition + "\">");
        bw.newLine();
        bw.write("\t\t<where>");
        bw.newLine();
        bw.write("\t\t\t<include refid=\"" + base_condition_filed + "\" />");
        bw.newLine();
        for (FieldInfo field : tableInfo.getFieldExtendList()) {
            String andWhere = "";
            if (ArrayUtils.contains(Constants.SQL_STRING_TYPES, field.getSqlType())) {
                andWhere = "and  " + field.getFieldName() + " like concat('%', #{query." + field.getPropertyName() + "}, \'%\')";
            } else if (ArrayUtils.contains(Constants.SQL_DATE_TYPES, field.getSqlType()) || ArrayUtils.contains(Constants.SQL_DATE_TIME_TYPES, field.getSqlType())) {
                if (field.getPropertyName().endsWith(StringUtils.upperCaseFirstLetter(Constants.SUFFIX_BEAN_QUERY_START))) {
                    andWhere = "<![CDATA[ and " + field.getFieldName() + ">=str_to_date(#{query." + field.getPropertyName() + "}, '%Y-%m-%d') ]]>";
                } else if (field.getPropertyName().endsWith(StringUtils.upperCaseFirstLetter(Constants.SUFFIX_BEAN_QUERY_END))) {
                    andWhere = "<![CDATA[ and " + field.getFieldName() + "< date_sub(str_to_date(#{query." + field.getPropertyName() + "},'%Y-%m-%d'),interval -1 day) ]]>";
                }
            }
            bw.write("\t\t\t<if test=\"query." + field.getPropertyName() + " != null and query." + field.getPropertyName() + "!=\'\'\">");
            bw.newLine();
            bw.write("\t\t\t\t" + andWhere);
            bw.newLine();
            bw.write("\t\t\t</if>");
            bw.newLine();
        }
        bw.write("\t\t</where>");
        bw.newLine();
        bw.write("\t</sql>");
        bw.newLine();
        bw.newLine();
    }

    private static void selectList(BufferedWriter bw, TableInfo tableInfo) throws IOException {
        //selectList Generation
        bw.write("\t<!--" + selectList + "-->");
        bw.newLine();
        bw.write("\t<select id=\"" + selectList + "\"  resultMap=\"" + base_result_map + "\" >");
        bw.newLine();
        bw.write("\t\tSELECT <include refid=\""+ base_Column_List + "\" /> FROM " + tableInfo.getTableName() + " <include refid=\"" + query_condition + "\" />");
        bw.newLine();
        bw.write("\t\t<if test=\"query.orderBy!=null\">\n \t\t\torder by ${query.orderBy}\n \t\t</if>");
        bw.newLine();
        bw.write("\t\t<if test=\"query.simplePage!=null\">\n\t\t\t limit #{query.simplePage.start},#{query.simplePage.end}\n\t\t</if>");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
    }

    private static void selectNumber(BufferedWriter bw, TableInfo tableInfo) throws IOException {
        //selectNumber Generation
        bw.write("\t<!--" + selectNumber + "-->");
        bw.newLine();
        bw.write("\t<select id=\"" + selectNumber + "\" resultType=\"java.lang.Long\" >");
        bw.newLine();
        bw.write("\t\tSELECT count(1) FROM "+ tableInfo.getTableName() + " <include refid=\""+ query_condition + "\" />");
        bw.newLine();
        bw.write("\t</select>");
        bw.newLine();
        bw.newLine();
    }



}
