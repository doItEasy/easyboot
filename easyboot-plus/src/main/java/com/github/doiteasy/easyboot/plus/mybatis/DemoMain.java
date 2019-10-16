package com.github.doiteasy.easyboot.plus.mybatis;

import java.io.File;
import java.util.Scanner;

public class DemoMain {

    public static void main(String[] args) {
        String projectRoot = new File("ga-wisdom-policing-common/src/main").getAbsolutePath();
        System.setProperty("projectRoot", projectRoot);
        System.setProperty("dalPackage", "com.lvdoo.ga.common");
        String configFile = new File("ga-wisdom-policing-infrastructure/src/main/resources").getAbsoluteFile() + "/mybatis-generator-config.xml";
        System.out.println();
        System.out.println("q (quit) 退出");
        System.out.println("输入要生成的表名(%:全部)");

        String input;
        boolean quit = false;
        while (!quit) {
            Scanner in = new Scanner(System.in);
            input = in.nextLine();
            if ("q".equalsIgnoreCase(input)) {
                quit = true;
            } else {
                MybatisGenerator.write(configFile, input);
            }
        }
        System.out.println("结束");
    }
}
