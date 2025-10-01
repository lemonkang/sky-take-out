package org.example.config;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
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
            // 插入注意事项行（从第0行开始）
            for (int i = 0; i < notices.size(); i++) {
                Row noticeRow = sheet.createRow(i);
                noticeRow.createCell(0).setCellValue(notices.get(i));
                // 合并所有列（根据实际表头列数调整，示例为4列：0-3）
                sheet.addMergedRegion(new CellRangeAddress(i, i, 0, 3));
            }
        }
    }

    // 其他默认方法可不实现
    @Override
    public void afterRowCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {}

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {}
}
