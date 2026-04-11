package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.OrderDetailDao;
import hcmuaf.fit.mombabyecommerce.connection.DBConnection;
import hcmuaf.fit.mombabyecommerce.model.OptionVariant;
import hcmuaf.fit.mombabyecommerce.model.OrderDetail;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OrderDetailService {
    Jdbi jdbi;
    OrderDetailDao orderDetailDao;
    OptionService optionService = new OptionService(DBConnection.getJdbi());

    public OrderDetailService(Jdbi jdbi) {
        orderDetailDao = jdbi.onDemand(OrderDetailDao.class);
    }


    public Boolean addOrderDetail(OrderDetail orderDetail) {

        OptionVariant options = optionService.getOptionById(orderDetail.getOptionId());
        if (options == null || (options.getStock() < orderDetail.getQuantity())) {
            throw new RuntimeException("Bad request");
        } else {

            Boolean flag = orderDetailDao.addOrderDetail(
                    orderDetail.getOrderId(),
                    orderDetail.getProductId(),
                    orderDetail.getQuantity(),
                    orderDetail.getTotal(),
                    orderDetail.getOptionId()
            );

            if (flag) {
                Integer newStock = options.getStock() - orderDetail.getQuantity();
                // Increase Stock Quantity
                optionService.updateStock(orderDetail.getOptionId(), newStock);
            }

            return flag;
        }


    }

    public String getProductNameById(Integer productId) {
        return orderDetailDao.getProductNameById(productId);
    }

    public Integer getQuantityByOrderDetailId(Integer orderDetailId) {
        return orderDetailDao.getQuantityByOrderDetailId(orderDetailId);
    }

    public String getOrderStatusByOrderId(Integer orderId) {
        return orderDetailDao.getOrderStatusByOrderId(orderId);
    }

    public OrderDetail getOrderDetailsByOrderId(Integer orderId) {
        return orderDetailDao.getOrderDetailById(orderId);
    }


    public List<OrderDetail> getOrderDetailByOrderId(Integer orderId) {
        return orderDetailDao.getOrderDetailByOrderId(orderId);
    }
}
