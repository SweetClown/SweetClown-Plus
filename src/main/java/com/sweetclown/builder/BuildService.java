package com.sweetclown.builder;

import com.sweetclown.bean.Constants;
import com.sweetclown.bean.FieldInfo;
import com.sweetclown.bean.TableInfo;
import com.sweetclown.utils.StringTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;

public class BuildService {

    private static final Logger logger = LoggerFactory.getLogger(BuildService.class);

    public static void execute(TableInfo tableInfo) {

        File folder = new File(Constants.PATH_SERVICE);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String className = tableInfo.getBeanName() + "Service";
        File serviceFile = new File(folder, className + ".java");

        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(serviceFile);
            outw = new OutputStreamWriter(out, "utf-8");
            bw = new BufferedWriter(outw);
            bw.write("package " + Constants.PACKAGE_SERVICE + ";");
            bw.newLine();
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_PO + "." + tableInfo.getBeanName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_QUERY + "." + tableInfo.getBeanParamName() + ";");
            bw.newLine();
            bw.write("import " + Constants.PACKAGE_VO + ".PaginationResultVO;");
            bw.newLine();
            bw.write("import java.util.List;");
            bw.newLine();

            BuildComment.createClassComment(bw, tableInfo.getComment() + "Service");;
            bw.write("public interface " + className + " {" );
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw, "find the list based on param");
            bw.write("\tList<" + tableInfo.getBeanName() + "> findListByParam(" + tableInfo.getBeanParamName() +" param);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw, "find the Count based on param");
            bw.write("\tInteger findCountByParam(" + tableInfo.getBeanParamName() +" param);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw, "find the list based on page");
            bw.write("\tPaginationResultVO<" + tableInfo.getBeanName() + "> findListByPage(" + tableInfo.getBeanParamName() +" param);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw, "Add");
            bw.write("\tInteger add(" + tableInfo.getBeanName() +" bean);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw, "Add batch");
            bw.write("\tInteger addBatch(List<" + tableInfo.getBeanName() +"> listBean);");
            bw.newLine();
            bw.newLine();
            BuildComment.createFieldComment(bw, "Add or Update batch");
            bw.write("\tInteger addOrUpdateBatch(List<" + tableInfo.getBeanName() +"> listBean);");
            bw.newLine();
            bw.newLine();

            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> keyFieldInfoList = entry.getValue();
                Integer index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParams = new StringBuilder();
                for (FieldInfo fieldInfo : keyFieldInfoList) {
                    index++;
                    methodName.append(StringTools.upperCaseFirstLetter(fieldInfo.getPropertyName()));
                    if (index < keyFieldInfoList.size()) {
                        methodName.append("And");
                    }
                    methodParams.append(fieldInfo.getJavaType()+" " +fieldInfo.getPropertyName());
                    if (index < keyFieldInfoList.size()) {
                        methodParams.append(", ");
                    }
                }
                BuildComment.createFieldComment(bw, "Update Object By" + methodName );
                bw.write("\tInteger update"+ tableInfo.getBeanName() + "By" + methodName +"("+ tableInfo.getBeanName() + " bean, " + methodParams + ");");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "Delete Object By" + methodName );
                bw.write("\tInteger delete" + tableInfo.getBeanName() + "By" + methodName +"(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
                BuildComment.createFieldComment(bw, "Select Object By" + methodName );
                bw.write("\t" + tableInfo.getBeanName() + " get" + tableInfo.getBeanName() + "By" + methodName + "(" + methodParams + ");");
                bw.newLine();
                bw.newLine();
            }

            bw.newLine();
            bw.write("}");



            bw.flush();
        } catch (Exception e) {
            logger.error("Create Service failed", e);
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
