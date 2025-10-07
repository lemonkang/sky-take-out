package org.example.config;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;

public class NoticeRowWriteHandler implements RowWriteHandler {
    // 注意事项内容（可根据业务动态修改）
    private final List<String> notices;

    public NoticeRowWriteHandler(List<String> notices) {
        this.notices = notices;
    }

    @Override
    public void beforeRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                Integer rowIndex, Integer relativeRowIndex, Boolean isHead) {
        // 只在表头行（relativeRowIndex=0）创建前插入注意事项
        if (isHead && relativeRowIndex == 0) {
            Sheet sheet = writeSheetHolder.getSheet();
            
            // 创建样式
            CellStyle noticeStyle = sheet.getWorkbook().createCellStyle();
            Font font = sheet.getWorkbook().createFont();
            font.setBold(true);
            noticeStyle.setFont(font);
            
            // 插入注意事项行（从第0行开始）
            for (int i = 0; i < notices.size(); i++) {
                Row noticeRow = sheet.createRow(i);
                Cell cell = noticeRow.createCell(0);
                cell.setCellValue(notices.get(i));
                cell.setCellStyle(noticeStyle);
                
                // 合并所有列（根据实际表头列数调整，ExcelUserOrdersDto有5列：0-4）
                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 4));
            }
        }
    }

    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, 
                              Row row, Integer relativeRowIndex, Boolean isHead) {
        // 如果是表头行，需要调整其位置到注意事项之后
        if (isHead && relativeRowIndex == 0) {
            Sheet sheet = writeSheetHolder.getSheet();
            // 将表头行移动到注意事项行之后（第notices.size()行）
            int targetRowIndex = notices.size();
            
            // 如果当前行不是目标位置，需要移动
            if (row.getRowNum() != targetRowIndex) {
                // 创建新行在正确位置
                Row newHeaderRow = sheet.createRow(targetRowIndex);
                // 复制表头内容
                for (int i = 0; i < row.getLastCellNum(); i++) {
                    Cell oldCell = row.getCell(i);
                    if (oldCell != null) {
                        Cell newCell = newHeaderRow.createCell(i);
                        newCell.setCellValue(oldCell.getStringCellValue());
                        newCell.setCellStyle(oldCell.getCellStyle());
                    }
                }
                // 删除原来的行
                sheet.removeRow(row);
            }
        }
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, 
                               Row row, Integer relativeRowIndex, Boolean isHead) {}
}
