package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.OrderDao;
import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.model.Order;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class OrderService {
    OrderDao orderDao;


    public OrderService(Jdbi jdbi) {
        orderDao = jdbi.onDemand(OrderDao.class);
    }

    public Integer addOrder(Order order) {
        return orderDao.createOrder(
                order.getCreateAt(),
                order.getPaymentStatus(),
                order.getOrderStatus(),
                order.getUserId(),
                order.getAddressId(),
                order.getCardId(),
                order.getCOD(),
                order.getShippingFee()
        );
    }

    public List<Order> getOrdersByUserId(Integer userId) {
        return orderDao.getOrdersByUserId(userId);
    }


    public  Order getOrderByIdAndUserId(Integer orderId ,Integer userId) {
        return orderDao.getOrderByIdAndUserId(orderId, userId);
    }

    public  Order getOrderById(Integer orderId  ) {
        return orderDao.getOrderById(orderId);
    }



    public List<Order> getAllOrders( ) {
        return orderDao.getAllOrders();
    }
    public void updateStatus(Integer orderId, OrderStatus orderStatus) {
        orderDao.updateOrderStatus(orderId, orderStatus);
    }

    public List<Order> getOrdersByUserIdAndStatus(Integer userId, String status) {
        return orderDao.getOrdersByUserIdAndStatus(
                userId,
                OrderStatus.valueOf(status)
        );
    }

}
