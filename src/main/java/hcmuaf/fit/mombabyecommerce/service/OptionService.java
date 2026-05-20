package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.OptionVariantDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OptionService {
    private final OptionVariantDao optionDao;

    public OptionService(Jdbi jdbi) {
        this.optionDao = jdbi.onDemand(OptionVariantDao.class);
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
    public void createInventory(Integer optionVariantId, Integer quantity) {
        optionDao.createInventory(optionVariantId, quantity);
    }
    public List<OptionVariant> getAllOptionsWithStock() {
        return optionDao.getAllOptionsWithStock();
    }

    public boolean updateStockWithLocation(Integer optionVariantId, Integer quantity, String location) {
        return optionDao.updateStockWithLocation(optionVariantId, quantity, location);
    }

    public List<OptionVariant> getOptionsWithStockByProductId(Integer productId) {
        return optionDao.getOptionsWithStockByProductId(productId);
    }
    public static void main(String[] args) {
        OptionService  optionService = new OptionService(DBConnection.getJdbi());

        System.out.println(optionService.getOptionsByProductId(1));



    }
}

