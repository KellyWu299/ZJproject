package org.hnu.precomputation.service.service;

import org.hnu.precomputation.common.model.Nebula.ClassAutoMapping;
import org.hnu.precomputation.common.model.Nebula.nebulaEdge;
import org.hnu.precomputation.common.model.Nebula.FieldAutoMapping;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;
@Component
//工具类，用于生成合适的NGQL语句
public class SqlBuildUtils {



        private static final String methodPre = "get";

        private static final String insertTagSqlTemplate = "insert vertex %s(%s) values \"%s\":(%s);";

        private static final String insertEdgeSqlTemplate = "insert edge %s(%s) values \"%s\" -> \"%s\":(%s);";

        private static final String deleteTagSqlTemplate = "delete tag %s from \"%s\";";

        private static final String deleteEdgeSqlTemplate = "delete edge %s \"%s\" -> \"%s\"@0;";

        private static final String deleteVertexSqlTemplate = "delete vertex \"%s\" with edge;";

        private static final String updateVertexSqlTemplate = "update vertex on %s \"%s\" set %s;";

        private static final String updateEdgeSqlTemplate = "UPDATE EDGE \"%s\" -> \"%s\" OF %s SET %s = %s;";

        private static final String createTag = "create tag if not exists %s(id int64, name String,description String) comment = \"%s\";";

        private static final String insertDefaultVertex = "insert vertex if not exists %s(id,name,description) values \"%s\":(%s,\"%s\",\"%s\");";

        private static final String insertDefaultEdge = "insert edge %s(id,name) values \"%s\" -> \"%s\":(%s,\"%s\");";

        private static final String dropTagSqlTemplate = "drop tag if exists %s;";

        private static final String dropEdgeSqlTemplate = "drop edge if exists %s;";

        private static final String createEdgeSqlTemplate = "create edge if not exists %s(id int64,name String) comment = \"%s\"";

        private static final String fetchVertexSqlTemplate = "FETCH PROP ON %s \"%s\" YIELD properties(vertex);";

        private static final String fetchEdgeSqlTemplate = "FETCH PROP ON %s \"%s\" -> \"%s\" YIELD properties(edge);";

        private static final String createEdgeIndex = "CREATE EDGE INDEX nebulaEdge_index on %s();";

        private static final String useGraphTemplate = "USE %s;";

        private static final String createSpaceTemplate = "CREATE SPACE IF NOT EXISTS %s (vid_type=FIXED_STRING(30));";

//        private static final String lookupEdge = "LOOKUP ON %s YIELD edge AS e;";

        public static <T> String buildInsert(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            SqlBuild tag = parse(t);
            return String.format(insertTagSqlTemplate, tag.getName(), tag.getField(), tag.getId(), tag.getValues());
        }

        public static  String buildEdge(nebulaEdge t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            SqlBuild edge = parse(t);
            return String.format(insertEdgeSqlTemplate, edge.getName(), edge.getField(), t.getLeftVid(), t.getRightVid(), edge.getValues());
        }

        public static <T> String deleteTag(T t) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
            SqlBuild tag = parse(t);
            return String.format(deleteTagSqlTemplate,tag.getName(),tag.getId());
        }

        public static String deleteEdge(String edgeName, String leftid, String rightid){
            return String.format(deleteEdgeSqlTemplate,edgeName,leftid,rightid);
        }

        public static String chooseGraph(String s){
            return String.format(useGraphTemplate,s);
        }

        public static String createSpace(String s){
            return String.format(createSpaceTemplate,s);
        }


        public static <T> String createEIndex(String s){
            return String.format(createEdgeIndex,s);
        }

        public static <T> String deleteVertex(String id){
            return String.format(deleteVertexSqlTemplate,id);
        }

        public static <T> String updateVertex(T t) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
            Class<?> clazz = t.getClass();
            ClassAutoMapping annotation = clazz.getAnnotation(ClassAutoMapping.class);
            String tagName = annotation.value();
            Field[] declaredFields = clazz.getDeclaredFields();
            StringBuilder set = new StringBuilder();
            Long id = null;
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                // 获取属性名称
                String name = declaredField.getName();
                // 获取自定义注解 FieldAutoMapping
                FieldAutoMapping autoMapping = declaredField.getAnnotation(FieldAutoMapping.class);
                if(null == autoMapping){
                    continue;
                }
                String methodName = autoMapping.method();
                String type = autoMapping.type();
                Method getMethod = clazz.getDeclaredMethod(methodName);
                Object value = getMethod.invoke(t);
                Object valueFormat = format(value, type);
                if ("id".equals(name)) {
                    id = (Long) value;
                    continue;
                }
                set.append(name).append("=").append("'").append(valueFormat).append("'");
                if (i != declaredFields.length - 1) {
                    set.append(",");
                }
            }
            return String.format(updateVertexSqlTemplate,tagName,id,set.toString());
        }

        public static String updateEdge(nebulaEdge t) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException{
            Class<?> clazz = t.getClass();
            ClassAutoMapping annotation = clazz.getAnnotation(ClassAutoMapping.class);
            String tagName = annotation.value();
            StringBuilder filedString = new StringBuilder();
            StringBuilder valueString = new StringBuilder();
            Field[] declaredFields = clazz.getDeclaredFields();
            StringBuilder set = new StringBuilder();
            Long id = null;
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                // 获取属性名称
                String name = declaredField.getName();
                // 获取自定义注解 FieldAutoMapping
                FieldAutoMapping autoMapping = declaredField.getAnnotation(FieldAutoMapping.class);
                if(null == autoMapping){
                    continue;
                }
                String methodName = autoMapping.method();
                String type = autoMapping.type();
                Method getMethod = clazz.getDeclaredMethod(methodName);
                Object value = getMethod.invoke(t);
                Object valueFormat = format(value, type);
                filedString.append(name);
                valueString.append(valueFormat);



            }
            return String.format(updateEdgeSqlTemplate,t.getLeftVid(),t.getRightVid(),tagName,filedString.toString(),valueString.toString());
        }

        public static <T> String queryVertex(String name,String id) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException{
            return String.format(fetchVertexSqlTemplate,name,id);
        }

        public static <T> String queryEgde(String name,String lid,String rid) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException{
            return String.format(fetchEdgeSqlTemplate,name,lid,rid);
        }

//        public static String lookupE(String s){
//            return String.format(lookupEdge,s);
//        }
        public static String updateDefault(String tagName,Long vid,String name, String description){
            String setCall = "name="+format(name,"String")+",description="+format(description,"String");
            return String.format(updateVertexSqlTemplate,tagName,vid,setCall);
        }

        private static <T> SqlBuild parse(T t) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Class<?> clazz = t.getClass();
            ClassAutoMapping annotation = clazz.getAnnotation(ClassAutoMapping.class);
            String tagName = annotation.value();
            Field[] declaredFields = clazz.getDeclaredFields();
            StringBuilder filedString = new StringBuilder();
            StringBuilder valueString = new StringBuilder();
            String id = null;
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                // 获取属性名称
                String name = declaredField.getName();
                // 获取自定义注解 FieldAutoMapping
                FieldAutoMapping autoMapping = declaredField.getAnnotation(FieldAutoMapping.class);
                if(null == autoMapping){
                    continue;
                }
                String methodName = autoMapping.method();
                String type = autoMapping.type();
                Method getMethod = clazz.getDeclaredMethod(methodName);
                Object value = getMethod.invoke(t);
                filedString.append(name);
                Object valueFormat = format(value, type);
                if ("id".equals(name)) {
                    id = (String) value;
                }
                System.out.println(tagName);

                if("nebulaEdge".equals(tagName)){
                    if (i == 0) {
                        valueString.append("'");
                        valueString.append(valueFormat);
                        filedString.append(",");
                        valueString.append("'");
                        valueString.append(",");
                        valueString.append("'");
                    }
                    if (i == 1) {
                        valueString.append(valueFormat);
                        filedString.append(",");
                        valueString.append("'");
                        valueString.append(",");
                        valueString.append("'");
                    }
                    if (i == 2) {
                        valueString.append(valueFormat);
                        valueString.append("'");
                    }
                }
                else {
                    valueString.append(valueFormat);
                    if (i == 0) {
                        filedString.append(",");
                        valueString.append(",");
                        valueString.append("'");
                    }
                    if (i == 1) {
                        filedString.append(",");
                        valueString.append("'");
                        valueString.append(",");
                        valueString.append("'");
                    }
                    if (i == 2) {
                        valueString.append("'");
                    }
                }

//                if (i != declaredFields.length - 1) {
//                    filedString.append(",");
//                    valueString.append(",");
//                    valueString.append("'");
//                }
//                else {
//                    valueString.append("'");
//                }
            }
            return new SqlBuild(id,tagName,filedString.toString(),valueString.toString());
        }

        public static String createTag(String tagName,String comment){
            return String.format(createTag,tagName,comment);
        }

        public static String createEdge(String edgeName,String comment){
            return String.format(createEdgeSqlTemplate,edgeName,comment);
        }

        public static String insertDefaultVertex(String tagName,Long vid,String name,String description){
            return String.format(insertDefaultVertex,tagName,vid,vid,name,description);
        }

        public static String dropTag(String tagName){
            return String.format(dropTagSqlTemplate,tagName);
        }

        public static String dropEdge(String edgeName){
            return String.format(dropEdgeSqlTemplate,edgeName);
        }

        public static String insertDefaultEdge(String edgeCode,Long pid,Long id,Long preId,String edgeName){
            return String.format(insertDefaultEdge,edgeCode,pid,id,preId,edgeName);
        }

        /**
         * 首字母转大写
         *
         * @param name 字符串
         * @return 转大写
         */
        private static String firstCharacterToUppercase(String name) {
            String startName = name.substring(0, 1);
            String endName = name.substring(1);
            return startName.toUpperCase(Locale.ROOT) + endName;
        }

        private static Object format(Object value, String type) {
            if ("String".equals(type)) {
                String s = value + "";
                return "\"" + s + "\"";
            }
            return value;
        }

    }
