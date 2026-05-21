package hcmuaf.fit.mombabyecommerce.Dao;


import hcmuaf.fit.mombabyecommerce.contant.OrderStatus;
import hcmuaf.fit.mombabyecommerce.contant.PaymentStatus;
import hcmuaf.fit.mombabyecommerce.model.Order;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDate;
import java.util.List;

@RegisterConstructorMapper(Order.class)
public interface OrderDao {

    @SqlUpdate(value = "INSERT INTO orders(createAt, paymentStatus, orderStatus, userId, addressId, cardId, isCOD, shippingFee)" +
            "VALUE (" + "  :createAt , :paymentStatus, :orderStatus , :userId, :addressId, :cardId, :isCOD,:shippingFee)")
    @GetGeneratedKeys
    Integer createOrder(
            @Bind("createAt") LocalDate createAt,
            @Bind("paymentStatus") PaymentStatus paymentStatus,
            @Bind("orderStatus") OrderStatus orderStatus,
            @Bind("userId") Integer userId,
            @Bind("addressId") Integer addressId,
            @Bind("cardId") Integer cardId,
            @Bind("isCOD") Boolean isCOD,
            @Bind("shippingFee") Integer shippingFee
    );



    @SqlQuery(value = """
        SELECT
            o.id,
            o.createAt,
            o.paymentStatus,
            o.orderStatus,
            o.userId,
            o.addressId,
            o.cardId,
            o.isCOD,
            o.shippingFee,
            SUM(od.quantity) AS quantity,
            (SUM(od.total) + o.shippingFee) AS total,
            MIN(p.name) AS product_name,
            MIN(i.url) AS product_image
        FROM
            orders o
                INNER JOIN order_detail od ON o.id = od.orderId
                INNER JOIN products p ON p.id = od.productId
                INNER JOIN image i ON i.id = p.imageId
        WHERE
            o.userId = :userId
        GROUP BY
            o.id,
            o.createAt,
            o.paymentStatus,
            o.orderStatus,
            o.userId,
            o.addressId,
            o.cardId,
            o.isCOD,
            o.shippingFee
        ORDER BY
            o.createAt DESC
""")
    List<Order> getOrdersByUserId(@Bind("userId") Integer userId);


    @SqlQuery(value = "select\n" +
            "    o.id as id, o.createAt, o.paymentStatus, o.orderStatus,\n" +
            "    o.userId, o.addressId, o.cardId, o.isCOD,o.shippingFee as  shippingFee,\n " +
            " o.shippingId as shippingId , " +
            "    sum(od.total) as total\n" +
            "from orders as o inner join order_detail as od\n" +
            "                            on o.id = od.orderId\n" +
            "where o.userId = :userId and o.id = :orderId\n" +
            "group by\n" +
            "    o.id, o.createAt, o.paymentStatus, o.orderStatus,\n" +
            "    o.userId, o.addressId, o.cardId, o.isCOD, o.shippingFee " +
            " order by o.createAt DESC ")
    Order getOrderByIdAndUserId(@Bind("orderId") Integer orderId, @Bind("userId") Integer userId);


    @SqlQuery(value = "select\n" +
            "    o.id as id, o.createAt, o.paymentStatus, o.orderStatus,\n" +
            "    o.userId, o.addressId, o.cardId, o.isCOD, o.shippingFee as  shippingFee,\n" +
            "     o.shippingId as shippingId ," +
            "    sum(od.total) as total\n" +
            "from orders as o inner join order_detail as od\n" +
            "                            on o.id = od.orderId\n" +
            "where o.id = :orderId\n" +
            "group by\n" +
            "    o.id, o.createAt, o.paymentStatus, o.orderStatus,\n" +
            "    o.userId, o.addressId, o.cardId, o.isCOD\n")
    Order getOrderById (@Bind("orderId") Integer orderId );






    @SqlQuery(value ="select \n" +
            "\to.id, o.createAt, o.paymentStatus, o.orderStatus, \n" +
            "o.userId, o.addressId,\n" +
            "    o.cardId, o.isCOD,\n" +
            "  u.fullName as userName ,o.shippingFee as  shippingFee,\n" +
            "  sum(od.total) as total\n" +
            "from orders as o\n" +
            "     inner join order_detail as od\n" +
            "           on o.id = od.orderId\n" +
            "     inner join users as u\n" +
            "           on u.id = o.userId \n" +
            "group by\n" +
            "   o.id, o.createAt, o.paymentStatus,\n" +
            "   o.orderStatus,o.userId, o.addressId,\n" +
            "   o.cardId, o.isCOD,\n" +
            "   u.fullName \n" +
            "order by o.createAt desc")
    List<Order> getAllOrders();


    @SqlUpdate("UPDATE orders " +
            "SET orderStatus = :orderStatus "+
            "WHERE id = :orderId ")

    void updateOrderStatus(@Bind("orderId") Integer orderId, @Bind("orderStatus") OrderStatus orderStatus);

    @SqlUpdate("UPDATE orders " +
            "SET orderStatus = :orderStatus "+
            "WHERE shippingId = :shippingId ")

    boolean updateOrderStatusByShippingId(@Bind("shippingId") String shippingId, @Bind("orderStatus") OrderStatus orderStatus);


    @SqlUpdate("UPDATE orders " +
            "SET shippingId = :shippingId "+
            "WHERE id = :orderId ")

    void updateOrderShippingId(@Bind("orderId") Integer orderId, @Bind("shippingId") String shippingId);

    @SqlQuery(value = """
    SELECT
        o.id,
        o.createAt,
        o.paymentStatus,
        o.orderStatus,
        o.userId,
        o.addressId,
        o.cardId,
        o.isCOD,
        o.shippingFee,
        SUM(od.quantity) AS quantity,
        (SUM(od.total) + o.shippingFee) AS total,
        MIN(p.name) AS product_name,
        MIN(i.url) AS product_image
    FROM
        orders o
            INNER JOIN order_detail od ON o.id = od.orderId
            INNER JOIN products p ON p.id = od.productId
            INNER JOIN image i ON i.id = p.imageId
    WHERE
        o.userId = :userId
        AND o.orderStatus = :status
    GROUP BY
        o.id,
        o.createAt,
        o.paymentStatus,
        o.orderStatus,
        o.userId,
        o.addressId,
        o.cardId,
        o.isCOD,
        o.shippingFee
    ORDER BY
        o.createAt DESC
""")
    List<Order> getOrdersByUserIdAndStatus(
            @Bind("userId") Integer userId,
            @Bind("status") OrderStatus status
    );


    @SqlUpdate("""
    UPDATE orders
    SET shippingId = :shippingId
    WHERE id = :orderId
    """)
    boolean updateShippingId(@Bind("orderId") Integer orderId,
                             @Bind("shippingId") String shippingId);

    @SqlUpdate("""
    UPDATE orders
    SET paymentStatus = :paymentStatus
    WHERE id = :orderId
""")
    boolean updatePaymentStatus(
            @Bind("orderId")
            Integer orderId,
            @Bind("paymentStatus")
            PaymentStatus paymentStatus
    );
}
