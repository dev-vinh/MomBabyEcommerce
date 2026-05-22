package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.OptionVariantDao;
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
        this.jdbi = jdbi;
        orderDetailDao = jdbi.onDemand(OrderDetailDao.class);
    }


    public Boolean addOrderDetail(OrderDetail orderDetail) {
        if (orderDetail == null || orderDetail.getOptionId() == null || orderDetail.getQuantity() == null
                || orderDetail.getQuantity() <= 0) {
            throw new RuntimeException("Số lượng đặt hàng không hợp lệ");
        }

        return jdbi.inTransaction(handle -> {
            OptionVariantDao optionDao = handle.attach(OptionVariantDao.class);
            OrderDetailDao detailDao = handle.attach(OrderDetailDao.class);

            OptionVariant option = optionDao.getOptionById(orderDetail.getOptionId());
            int currentStock = option == null || option.getStock() == null ? 0 : option.getStock();

            if (option == null || currentStock < orderDetail.getQuantity()) {
                throw new RuntimeException("Sản phẩm không đủ tồn kho");
            }

            Boolean inserted = detailDao.addOrderDetail(
                    orderDetail.getOrderId(),
                    orderDetail.getProductId(),
                    orderDetail.getQuantity(),
                    orderDetail.getTotal(),
                    orderDetail.getOptionId()
            );

            if (!Boolean.TRUE.equals(inserted)) {
                throw new RuntimeException("Không thể tạo chi tiết đơn hàng");
            }

            boolean stockUpdated = optionService.decreaseStockWithLog(
                    orderDetail.getOptionId(),
                    orderDetail.getQuantity(),
                    null,
                    String.valueOf(orderDetail.getOrderId()),
                    orderDetail.getProductId()
            );

            if (!stockUpdated) {
                throw new RuntimeException("Sản phẩm không đủ tồn kho");
            }

            return true;
        });
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
