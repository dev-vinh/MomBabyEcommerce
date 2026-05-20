package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.model.Order;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExportService  {
    public byte[] exportOrdersToExcel(List<Order> orders) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Orders");
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Order ID", "Create Date", "User Name", "Payment Status",
                    "Order Status", "Total Amount", "Shipping Fee", "Is COD"
            };

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowNum = 1;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getCreateAt().format(formatter));
                row.createCell(2).setCellValue(order.getUserName() != null ? order.getUserName() : "");
                row.createCell(3).setCellValue(order.getPaymentStatus() != null ? order.getPaymentStatus().toString() : "");
                row.createCell(4).setCellValue(order.getOrderStatus() != null ? order.getOrderStatus().toString() : "");
                row.createCell(5).setCellValue(order.getTotal() != null ? order.getTotal() : 0);
                row.createCell(6).setCellValue(order.getShippingFee() != null ? order.getShippingFee() : 0);
                row.createCell(7).setCellValue(order.getCOD() != null ? (order.getCOD() ? "Yes" : "No") : "No");
            }
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
