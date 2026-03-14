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

    public int createOptions(Integer productId, Integer price, Integer stock) {
        return optionDao.createOption(productId, price, stock);
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

    public boolean updateOption(Integer id, Integer price, Integer stock) {
        return optionDao.updateOption(id, price, stock);
    }



    public static void main(String[] args) {
        OptionService  optionService = new OptionService(DBConnection.getJdbi());

        System.out.println(optionService.getOptionsByProductId(1));



    }
}

