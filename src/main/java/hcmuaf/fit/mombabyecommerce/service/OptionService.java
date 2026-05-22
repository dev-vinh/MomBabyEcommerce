package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.InventoryLogDao;
import hcmuaf.fit.mombabyecommerce.Dao.OptionVariantDao;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OptionService {
    private final OptionVariantDao optionDao;
    private final InventoryLogDao logDao;

    public OptionService(Jdbi jdbi) {
        this.optionDao = jdbi.onDemand(OptionVariantDao.class);
        this.logDao = jdbi.onDemand(InventoryLogDao.class);
    }

    public int createOptions(Integer productId, Integer price) {
        return optionDao.createOption(productId, price);
    }

    public OptionVariant getOptionById(Integer id) {
        return optionDao.getOptionById(id);
    }

    public Boolean updateStock(Integer id, Integer stock) {
        return optionDao.updateStock(id, stock);
    }

    public List<OptionVariant> getVariantByOptionId(List<Integer> optionIds) {
        return optionDao.getVariantByOptionId(optionIds);
    }

    public List<OptionVariant> getOptionsByProductId(Integer productId) {
        return optionDao.getOptionsByProductId(productId);
    }

    public boolean updateOption(Integer id, Integer price) {
        return optionDao.updateOption(id, price);
    }

    public List<OptionVariant> getOptionDetailsByProductId(Integer productId) {
        return optionDao.getOptionDetailsByProductId(productId);
    }

    public Integer getStockByOptionId(Integer optionVariantId) {
        Integer stock = optionDao.getStockByOptionId(optionVariantId);
        return stock == null ? 0 : stock;
    }

    public boolean decreaseStockIfEnough(Integer optionVariantId, Integer quantity) {
        return optionDao.decreaseStockIfEnough(optionVariantId, quantity);
    }

    public boolean decreaseStockWithLog(Integer optionVariantId, Integer quantity,
                                        Integer userId, String orderId, Integer productId) {
        Integer stockBefore = getStockByOptionId(optionVariantId);
        boolean ok = optionDao.decreaseStockIfEnough(optionVariantId, quantity);
        if (ok) {
            Integer stockAfter = getStockByOptionId(optionVariantId);
            logDao.insertLog(optionVariantId, productId, "EXPORT",
                    -quantity, stockBefore, stockAfter,
                    "Xuất kho đơn hàng #" + orderId, userId);
        }
        return ok;
    }

    public void createInventory(Integer optionVariantId, Integer quantity) {
        optionDao.createInventory(optionVariantId, quantity);
    }

    public List<OptionVariant> getAllOptionsWithStock() {
        return optionDao.getAllOptionsWithStock();
    }

    public boolean updateStockWithLocation(Integer optionVariantId, Integer quantity,
                                           String location, Integer userId, String reason) {
        Integer stockBefore = getStockByOptionId(optionVariantId);
        boolean updated = optionDao.updateStockWithLocation(optionVariantId, quantity, location);

        if (updated) {
            OptionVariant opt = getOptionById(optionVariantId);
            int productId = opt != null ? opt.getProductId() : null;
            int change = quantity - stockBefore;
            if (change != 0) {
                String actionType = change > 0 ? "IMPORT" : "EXPORT";
                String logReason = (reason != null && !reason.isBlank()) ? reason : "Cập nhật tồn kho thủ công";
                logDao.insertLog(optionVariantId, productId, actionType,
                        change, stockBefore, quantity, logReason, userId);
            }
        }
        return updated;
    }

    public List<OptionVariant> getOptionsWithStockByProductId(Integer productId) {
        return optionDao.getOptionsWithStockByProductId(productId);
    }

    // --- Inventory Log methods ---

    public List<hcmuaf.fit.mombabyecommerce.model.InventoryLog> getLogsPaged(
            Integer productId, String actionType, String fromDate, String toDate,
            int page, int size) {
        int offset = (page - 1) * size;
        return logDao.getLogsPaged(productId, productId != null ? 1 : 0,
                actionType, (actionType != null && !actionType.isBlank()) ? 1 : 0,
                fromDate, (fromDate != null && !fromDate.isBlank()) ? 1 : 0,
                toDate, (toDate != null && !toDate.isBlank()) ? 1 : 0,
                size, offset);
    }

    public int countLogs(Integer productId, String actionType, String fromDate, String toDate) {
        return logDao.countLogs(productId, productId != null ? 1 : 0,
                actionType, (actionType != null && !actionType.isBlank()) ? 1 : 0,
                fromDate, (fromDate != null && !fromDate.isBlank()) ? 1 : 0,
                toDate, (toDate != null && !toDate.isBlank()) ? 1 : 0);
    }
}
