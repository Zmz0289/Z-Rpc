package github.zmz.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 查找指定路径下面实现指定接口的全部类
 */
@Slf4j
public class ClassUtil {


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<Class<?>> getAllClassByInterface(Class<?> clazz) {
        ArrayList<Class<?>> list = new ArrayList<>();
        // 判断是否是一个接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class<?>> allClass = getAllClass(clazz.getPackage().getName());
                // 循环判断路径下的所有类是否实现了指定的接口
                for (int i = 0; i < allClass.size(); i++) {
                    if (clazz.isAssignableFrom(allClass.get(i))) {
                        // 排除自身
                        if (!clazz.equals(allClass.get(i))) {
                            list.add(allClass.get(i));
                        }
                    }
                }
            } catch (Exception e) {
                log.error("ClassUtil #getAllClassByInterface() has error occurred, msg = {}", e.getMessage(), e);
            }
        }
        return list;
    }

    /**
     * 从一个指定路径下查找所有的类
     */
    @SuppressWarnings("rawtypes")
    private static ArrayList<Class<?>> getAllClass(String packageName) {
        ArrayList<Class<?>> list = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        try {
            ArrayList<File> fileList = new ArrayList<>();
            Enumeration<URL> enumeration = classLoader.getResources(path);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                fileList.add(new File(url.getFile()));
            }
            for (File file : fileList) {
                list.addAll(findClass(file, packageName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 如果file是文件夹，则递归调用findClass方法，或者文件夹下的类
     * 如果file本身是类文件，则加入list中进行保存，并返回
     *
     * @param file
     * @param packageName
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static ArrayList<Class<?>> findClass(File file, String packageName) {
        ArrayList<Class<?>> list = new ArrayList<>();
        if (!file.exists()) {
            return list;
        }
        File[] files = file.listFiles();
        for (File file2 : files) {
            if (file2.isDirectory()) {
                assert !file2.getName().contains(".");//添加断言用于判断
                ArrayList<Class<?>> arrayList = findClass(file2, packageName + "." + file2.getName());
                list.addAll(arrayList);
            } else if (file2.getName().endsWith(".class")) {
                try {
                    // 保存的类文件不需要后缀.class
                    list.add(Class.forName(packageName + '.' + file2.getName().substring(0,
                            file2.getName().length() - 6)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
}