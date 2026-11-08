package com.sweetclown.bean;

import com.sweetclown.utils.YamlConfigUtils;

public class Constants {

    public static String AUTHOR;

    public static Boolean IGNORE_TABLE_PERFIX;

    public static String SUFFIX_MAPPER;
    public static String SUFFIX_BEAN_QUERY;
    public static String SUFFIX_BEAN_QUERY_START;
    public static String SUFFIX_BEAN_QUERY_END;
    public static String SUFFIX_BEAN_QUERY_FUZZY;

    public static String[] IGNORE_BEAN_TOSTRING_COLUMN;
    public static String IGNORE_BEAN_TOJSON_FILED;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;

    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;

    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;


    public static String PATH_BASE;

    public static String PACKAGE_BASE;

    public static String PATH_JAVA = "java";

    public static String PATH_RESOURCE = "resources";

    public static String BASE_PATH;

    public static String BASE_RESOURCE;

    public static String PACKAGE_PO;

    public static String PACKAGE_QUERY;

    public static String PACKAGE_UTILS;

    public static String PACKAGE_ENUMS;

    public static String PACKAGE_MAPPERS;

    public static String PACKAGE_SERVICE;

    public static String PACKAGE_SERVICE_IMPL;

    public static String PATH_PO;

    public static String PATH_QUERY;

    public static String PATH_UTILS;

    public static String PATH_ENUMS;

    public static String PATH_MAPPERS;

    public static String PATH_MAPPERS_XMLS;

    public static String PATH_SERVICE;

    public static String PATH_SERVICE_IMPL;

    static {
        AUTHOR = YamlConfigUtils.getString("author.name");
        IGNORE_BEAN_TOJSON_FILED = YamlConfigUtils.getString("ignore.bean.tojson.filed");
        IGNORE_BEAN_TOJSON_EXPRESSION = YamlConfigUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = YamlConfigUtils.getString("ignore.bean.tojson.class");

        BEAN_DATE_UNFORMAT_EXPRESSION = YamlConfigUtils.getString("bean.date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = YamlConfigUtils.getString("bean.date.unformat.class");

        BEAN_DATE_FORMAT_EXPRESSION = YamlConfigUtils.getString("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = YamlConfigUtils.getString("bean.date.format.class");

        IGNORE_TABLE_PERFIX = Boolean.valueOf(YamlConfigUtils.getString("ignore.table.prefix"));
        SUFFIX_MAPPER = YamlConfigUtils.getString("suffix.mappers");
        SUFFIX_BEAN_QUERY = YamlConfigUtils.getString("suffix.bean.query");
        SUFFIX_BEAN_QUERY_FUZZY = YamlConfigUtils.getString("suffix.bean.query.fuzzy");
        SUFFIX_BEAN_QUERY_START = YamlConfigUtils.getString("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_END = YamlConfigUtils.getString("suffix.bean.query.time.end");

        PATH_BASE = YamlConfigUtils.getString("path.base");
        PACKAGE_BASE = YamlConfigUtils.getString("package.base");

        BASE_PATH = PATH_BASE + "/" + PATH_JAVA + "/" + PACKAGE_BASE;
        BASE_PATH = BASE_PATH.replace(".", "/");
        BASE_RESOURCE = PATH_BASE+ "/" + PATH_RESOURCE + "/" + PACKAGE_BASE;
        BASE_RESOURCE = BASE_RESOURCE.replace(".", "/");

        PACKAGE_PO = PACKAGE_BASE + "." + YamlConfigUtils.getString("package.po");
        PACKAGE_QUERY = PACKAGE_BASE + "." + YamlConfigUtils.getString("package.query");
        PACKAGE_UTILS = PACKAGE_BASE + "." + YamlConfigUtils.getString("package.utils");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + YamlConfigUtils.getString("package.enums");
        PACKAGE_MAPPERS = PACKAGE_BASE + "." + YamlConfigUtils.getString("package.mappers");
        PACKAGE_SERVICE = PACKAGE_BASE + "." + YamlConfigUtils.getString("package.service");
        PACKAGE_SERVICE_IMPL = PACKAGE_BASE + "." + YamlConfigUtils.getString("package.service.impl");

        PATH_PO = BASE_PATH + "/" + YamlConfigUtils.getString("package.po").replace(".", "/");
        PATH_QUERY= BASE_PATH + "/" + YamlConfigUtils.getString("package.query").replace(".","/");
        PATH_UTILS = BASE_PATH + "/" +YamlConfigUtils.getString("package.utils").replace(".","/");
        PATH_ENUMS = BASE_PATH + "/" + YamlConfigUtils.getString("package.enums").replace(".","/");
        PATH_MAPPERS = BASE_PATH + "/" + YamlConfigUtils.getString("package.mappers").replace(".","/");
        PATH_MAPPERS_XMLS = BASE_RESOURCE + "/" + YamlConfigUtils.getString("package.mappers").replace(".","/");
        PATH_SERVICE = BASE_PATH + "/" + YamlConfigUtils.getString("package.service").replace(".","/");
        PATH_SERVICE_IMPL = BASE_PATH + "/" + YamlConfigUtils.getString("package.serviceImpl").replace(".","/");

        String ignore_bean_tostring_columnstr = YamlConfigUtils.getString("ignore.bean.tostring.column");
        if (null != ignore_bean_tostring_columnstr) {
            IGNORE_BEAN_TOSTRING_COLUMN = ignore_bean_tostring_columnstr.split(",");
        }

    }

    //Date time
    public static final String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};

    //Date
    public static final String[] SQL_DATE_TYPES = new String[]{"date"};

    //Decimal
    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float"};

    //String
    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};

    //Integer
    public static final String[] SQL_INTEGER_TYPES = new String[]{"int", "tinyint"};

    //Long
    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};

    public static void main(String args[]){
        System.out.println(BASE_RESOURCE);
        System.out.println(PATH_MAPPERS_XMLS);
        System.out.println(PACKAGE_PO);
    }

}
