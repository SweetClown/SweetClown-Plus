package com.sweetclown.builder;

import com.sweetclown.bean.Constants;
import com.sweetclown.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuildComment {

    public static void createClassComment(BufferedWriter bw,String classComment) throws IOException {
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Description:" + classComment);
        bw.write(" *");
        bw.newLine();
        bw.write(" * @author:" + Constants.AUTHOR);
        bw.newLine();
        bw.write(" * @date:" + DateUtils.format(new Date(),DateUtils.YYYY_MM_DD));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
    };

    public static void createFieldComment(BufferedWriter bw,String fieldComment) throws IOException {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + fieldComment);
        bw.newLine();
        bw.write("\t */");
        bw.newLine();
    };

    public static void createMethodComment() {

    }

}
