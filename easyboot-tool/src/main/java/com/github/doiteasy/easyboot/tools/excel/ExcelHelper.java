package com.github.doiteasy.easyboot.tools.excel;

import com.alibaba.fastjson.util.TypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author feixm
 */
@Slf4j
public class ExcelHelper {
    private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static void exportExcel(HttpServletResponse response, Class<?> c, List<?> dataList,String fileName){
        exportExcel(response,c,dataList,fileName,null);
    }

    public static void exportExcel(HttpServletResponse response, Class<?> c, List<?> dataList,String fileName,Map<Integer,Map<String,String>> selectListMap){
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        generateTitles(c, sheet);
        generateContent(sheet, c, dataList,selectListMap);

        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename="+ java.net.URLEncoder.encode(fileName, "UTF-8"));
            wb.write(response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<?> importExcel(InputStream inputStream,Class clazz) throws Exception {
        return importExcel(inputStream,clazz,null);
    }

    public static List<?> importExcel(InputStream inputStream,Class clazz,Map<Integer,Map<String,String>> selectListMap) throws Exception{
        List<Object> res = new ArrayList<Object>();
        Workbook workbook = WorkbookFactory.create(inputStream);
        //默认只获取第一个工作表
        Sheet sheet = workbook.getSheetAt(0);
        if (sheet != null) {

            List<String> values = new ArrayList<String>();
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();
                if(row.getRowNum()==0) continue;

                int cellNum = row.getPhysicalNumberOfCells();
                for (int i = 0; i <= cellNum; i++) {
                    Cell cell =   row.getCell(i);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING );  //设置单元格内容类型
                        values.add(cell.getStringCellValue());
                    }else{
                        values.add(null);
                    }
                }

                Object obj = clazz.newInstance();

                for(Field f : clazz.getDeclaredFields()){
                    if(f.isAnnotationPresent(ExcelResources.class)){
                        ExcelResources annotation = f.getAnnotation(ExcelResources.class);
                        String value = values.get(annotation.index()-1);
                        if(annotation.enableSelect()){
                            value = getKeyByValue(selectListMap.get(annotation.index()),String.valueOf(value ) );
                        }
                        f.setAccessible(true);
                        Object val = TypeUtils.cast(value,f.getType(),null);
                        f.set(obj,val);
                    }
                }
                res.add(obj);
            }
        }
        return res;
    }

    private static void generateTitles(Class<?> c, XSSFSheet sheet) {
        Row row = sheet.createRow(0);

        List<String> titles = Stream.of(c.getDeclaredFields())
                .filter(field -> field.getAnnotation(ExcelResources.class)!=null)
                .map(field -> {
                    int index = field.getAnnotation(ExcelResources.class).index();
                    int width = field.getAnnotation(ExcelResources.class).width()*20;
                    sheet.setColumnWidth(index,width);
                    return field;
                })
                .map(field -> field.getAnnotation(ExcelResources.class))
                .sorted(Comparator.comparing(ExcelResources::index))
                .map(ExcelResources::title)
                .collect(Collectors.toList());
        for (int col = 0; col < titles.size(); col++) {
            row.createCell(col);
            row.getCell(col).setCellValue(titles.get(col));
        }
    }


    private static void generateContent(XSSFSheet sheet, Class<?> c, List<?> dataList,Map<Integer,Map<String,String>> selectListMap) {
        if(dataList!=null){
            List<Field> fields = getSortedExcelFields(c);
            for (int i = 0; i < dataList.size(); i++) {
                Row newRow = sheet.createRow(i + 1);
                for (int j = 0; j < fields.size(); j++){
                    newRow.createCell(j);
                    String value = getFieldValue(fields.get(j), dataList.get(i));
                    ExcelResources annotation = fields.get(j).getAnnotation(ExcelResources.class);
                    if(annotation.enableSelect()){
                        value =selectListMap.get(j+1).get(value);
                    }
                    newRow.getCell(j).setCellValue(value);
                }
            }
        }
        int lastRow = dataList!=null?dataList.size():1000;
        //创建下拉列表
        createDataValidation(sheet,selectListMap,lastRow);

    }


    private static List<Field> getSortedExcelFields(Class<?> c){
        return Stream.of(c.getDeclaredFields())
                .filter(field -> field.getAnnotation(ExcelResources.class)!=null)
                .sorted(Comparator.comparing(field -> {
                    ExcelResources excelResources = field.getAnnotation(ExcelResources.class);
                    return excelResources.index();
                }))
                .collect(Collectors.toList());
    }


    private static String getFieldValue( Field field , Object o) {
        try {
            field.setAccessible(true);
            return Optional.ofNullable(field.get(o))
                    .map(obj -> {
                        if (obj instanceof LocalDateTime) {
                            return ((LocalDateTime) obj).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
                        }else if (obj instanceof Date) {
                            return dateFormatYYYYMMDDHHMMSS((Date) obj);
                        } else {
                            return String.valueOf(obj);
                        }
                    }).orElse(StringUtils.EMPTY);
        } catch (Exception e) {
            log.error("获取字段值出错");
        }
        return StringUtils.EMPTY;
    }


    /**
     * excel添加下拉数据校验
     */
    public static void createDataValidation(Sheet sheet,Map<Integer,Map<String,String>> selectListMap,int lastRow) {
        if(selectListMap.size()>0) {
            for(Map.Entry<Integer,Map<String,String>> entry:selectListMap.entrySet()  ){
                Integer key = entry.getKey()-1;
                Map<String,String> value = entry.getValue();
                // 第几列校验（0开始）key 数据源数组value
                if(value.size()>0) {
                    int i=0;
                    String[] valueArr = new String[value.size()];
                    for(Map.Entry<String,String> ent :value.entrySet()){
                        valueArr[i] = ent.getValue();
                        i++;
                    }
                    CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, lastRow, key, key);
                    DataValidationHelper helper = sheet.getDataValidationHelper();
                    DataValidationConstraint constraint = helper.createExplicitListConstraint(valueArr);
                    DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
                    //处理Excel兼容性问题
                    if (dataValidation instanceof XSSFDataValidation) {
                        dataValidation.setSuppressDropDownArrow(true);
                        dataValidation.setShowErrorBox(true);
                    } else {
                        dataValidation.setSuppressDropDownArrow(false);
                    }
                    dataValidation.setEmptyCellAllowed(true);
                    dataValidation.setShowPromptBox(true);
                    dataValidation.createPromptBox("提示", "只能选择下拉框里面的数据");
                    sheet.addValidationData(dataValidation);
                }
            }
        }
    }



    /**
     *通过value获取key值
     */
    private static String getKeyByValue(Map<String,String> selectMap,String value){
        if(selectMap!=null){
            for(Map.Entry<String,String> ent :selectMap.entrySet()){
                if(value!=null&&value.equals(ent.getValue()))
                    return ent.getKey();
            }
        }else{
            return value;
        }
        return null;
    }


    private static String dateFormatYYYYMMDDHHMMSS(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(date);
    }

}
