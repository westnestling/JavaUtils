
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


public class ExcelUtils {

    //写入excel
    public static HSSFWorkbook exportExcel(List<String> headers, List<List<String>> values) throws Exception {
        HSSFWorkbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet();
        Row row = sheet.createRow(0);
        sheet.autoSizeColumn(0, true);
        if (CollectionUtils.isNotEmpty(headers)) {
            int index = 0;
            for (String header : headers) {
                createCell(wb, row, index++, header);
            }
        }

        Row rowRecord;
        if (CollectionUtils.isNotEmpty(values)) {
            int count = 1;
            for (List<String> value : values) {
                rowRecord = sheet.createRow(count++);
                int index = 0;
                for (String v : value) {
                    createCell(wb, rowRecord, index++, v);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(headers)) {
            for (int index = 0, size = headers.size(); index < size; index++) {
                sheet.autoSizeColumn(index, true);
            }
        }
        return wb;
    }

    //填充cell
    private static void createCell(HSSFWorkbook wb, Row row, int column, String value) {
        HSSFCellStyle cellStyle=wb.createCellStyle();
        HSSFDataFormat format = wb.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        if (StringUtils.isNotBlank(value)) {
            Cell cell = row.createCell(column);
            cell.setCellStyle(cellStyle);
            cell.setCellValue(value);
        } else {
            Cell cell = row.createCell(column, Cell.CELL_TYPE_BLANK);
            cell.setCellStyle(cellStyle);
            cell.setCellValue("");
        }
    }

}
