package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.model.Product;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelImportService {
    public List<Product> readProductsFromExcel(InputStream inputStream) throws IOException {
        List<Product> products = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Product product = new Product();
                if (row.getCell(0) != null) {
                    product.setName(row.getCell(0).getStringCellValue().trim());
                }
                if (row.getCell(1) != null) {
                    product.setSku(row.getCell(1).getStringCellValue().trim());
                }
                if (row.getCell(2) != null) {
                    product.setDescription(row.getCell(2).getStringCellValue().trim());
                }
                if (row.getCell(3) != null && row.getCell(3).getCellType() == CellType.NUMERIC) {
                    product.setCategoryId((int) row.getCell(3).getNumericCellValue());
                }
                if (row.getCell(4) != null && row.getCell(4).getCellType() == CellType.NUMERIC) {
                    product.setBrandId((int) row.getCell(4).getNumericCellValue());
                }
                if (row.getCell(5) != null) {
                    product.setImageUrl(row.getCell(5).getStringCellValue().trim());
                }
                product.setActive(true);
                if (product.getName() != null && !product.getName().isEmpty() && product.getSku() != null) {
                    products.add(product);
                }
            }
        }
        return products;
    }
}
