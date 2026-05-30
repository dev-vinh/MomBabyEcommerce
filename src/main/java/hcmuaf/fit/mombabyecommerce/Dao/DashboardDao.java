package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.DashboardOrder;
import hcmuaf.fit.mombabyecommerce.model.DashboardStats;
import hcmuaf.fit.mombabyecommerce.model.Product;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

@RegisterConstructorMapper(DashboardStats.class)
@RegisterConstructorMapper(DashboardOrder.class)
@RegisterBeanMapper(Product.class)
public interface DashboardDao {

    //danh thu hien tai
    @SqlQuery("""
    SELECT COALESCE(SUM(od.total),0)
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
      AND YEARWEEK(o.createAt,1)=YEARWEEK(CURDATE(),1)""")
    Integer getCurrentWeekRevenue();

    @SqlQuery("""
    SELECT COALESCE(SUM(od.total),0)
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
      AND YEAR(o.createAt)=YEAR(CURDATE())
      AND MONTH(o.createAt)=MONTH(CURDATE())""")
    Integer getCurrentMonthRevenue();

    @SqlQuery("""
    SELECT COALESCE(SUM(od.total),0)
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
    AND YEAR(o.createAt)=YEAR(CURDATE())
""")
    Integer getCurrentYearRevenue();


    //don hang hien tai
    @SqlQuery("""
    SELECT COUNT(*)
    FROM orders
    WHERE YEARWEEK(createAt,1)=YEARWEEK(CURDATE(),1)
    """)
    Integer getCurrentWeekOrders();


    @SqlQuery("""
    SELECT COUNT(*)
    FROM orders
    WHERE YEAR(createAt)=YEAR(CURDATE())
      AND MONTH(createAt)=MONTH(CURDATE())
    """)
    Integer getCurrentMonthOrders();

    @SqlQuery("""
    SELECT COUNT(*)
    FROM orders
    WHERE YEAR(createAt)=YEAR(CURDATE())
    """)
        Integer getCurrentYearOrders();


    //doanh thu truoc
    @SqlQuery("""
    SELECT COALESCE(SUM(od.total),0)
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
    AND YEARWEEK(o.createAt,1)= YEARWEEK(DATE_SUB(CURDATE(),INTERVAL 1 WEEK),1)""")
    Integer getLastWeekRevenue();

    @SqlQuery("""
    SELECT
    COALESCE(SUM(od.total), 0) as totalRevenue
    FROM orders o
    INNER JOIN order_detail od ON o.id = od.orderId
    WHERE YEAR(o.createAt) = YEAR(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH))
    AND MONTH(o.createAt) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH))
    AND o.paymentStatus = 'PAID'""")
    Integer getLastMonthRevenue();
    @SqlQuery("""
    SELECT COALESCE(SUM(od.total),0)
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
      AND YEAR(o.createAt)=YEAR(CURDATE())-1
""")
    Integer getLastYearRevenue();


    // don hang truoc
    @SqlQuery("""
    SELECT COUNT(*)
    FROM orders
    WHERE YEARWEEK(createAt,1)
          = YEARWEEK(DATE_SUB(CURDATE(),INTERVAL 1 WEEK),1)
    """)
    Integer getLastWeekOrders();
    @SqlQuery("""
    SELECT COUNT(*)
    FROM orders
    WHERE YEAR(createAt)=YEAR(CURDATE())-1
""")
    Integer getLastYearOrders();
    @SqlQuery("SELECT COUNT(*) FROM orders WHERE YEAR(createAt) = YEAR(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH)) AND MONTH(createAt) = MONTH(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH))")
    Integer getLastMonthOrders();


    //bieu do
    @SqlQuery("""
    SELECT
        DATE(o.createAt) as label,
        COALESCE(SUM(od.total),0) as revenue
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
      AND YEARWEEK(o.createAt,1)=YEARWEEK(CURDATE(),1)
    GROUP BY DATE(o.createAt)
    ORDER BY DATE(o.createAt)""")
    List<DashboardStats> getCurrentWeekRevenueChart();

    @SqlQuery("""
    SELECT
        DAY(o.createAt) as label,
        COALESCE(SUM(od.total),0) as revenue
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE YEAR(o.createAt)=YEAR(CURDATE())
      AND MONTH(o.createAt)=MONTH(CURDATE())
      AND o.paymentStatus='PAID'
    GROUP BY DAY(o.createAt)
    ORDER BY DAY(o.createAt)""")
    List<DashboardStats> getCurrentMonthRevenueChart();

    @SqlQuery("""
    SELECT
        MONTH(o.createAt) as label,
        COALESCE(SUM(od.total),0) as revenue
    FROM orders o
    JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
      AND YEAR(o.createAt)=YEAR(CURDATE())
    GROUP BY MONTH(o.createAt)
    ORDER BY MONTH(o.createAt)
    """)
    List<DashboardStats> getCurrentYearRevenueChart();


    //
    @SqlQuery("SELECT COUNT(*) FROM products WHERE isActive = 1")
    Integer getTotalProducts();

    @SqlQuery("SELECT COUNT(*) FROM users u INNER JOIN user_role ur ON u.id = ur.userId INNER JOIN roles r ON ur.roleId = r.id WHERE r.roleType = 'USER'")
    Integer getTotalCustomers();

    @SqlQuery("""
        SELECT
            MONTH(o.createAt) as month,
            COALESCE(SUM(od.total), 0) as revenue
        FROM orders o
        INNER JOIN order_detail od ON o.id = od.orderId
        WHERE YEAR(o.createAt) = YEAR(CURRENT_DATE())
          AND o.paymentStatus = 'PAID'
        GROUP BY MONTH(o.createAt)
        ORDER BY MONTH(o.createAt)
    """)
    List<DashboardStats> getMonthlyRevenue();

    @SqlQuery("""
        SELECT
            o.id, o.createAt, o.paymentStatus, o.orderStatus,
            o.userId, o.addressId, o.cardId, o.isCOD,
            SUM(od.total) + COALESCE(o.shippingFee, 0) as total,
            u.fullName as userName, o.shippingFee
        FROM orders o
        INNER JOIN order_detail od ON o.id = od.orderId
        INNER JOIN users u ON o.userId = u.id
        GROUP BY o.id, o.createAt, o.paymentStatus, o.orderStatus,
                 o.userId, o.addressId, o.cardId, o.isCOD, u.fullName, o.shippingFee
        ORDER BY o.createAt DESC
        LIMIT 5
    """)
    List<DashboardOrder> getRecentOrders();

    @SqlQuery("""
        SELECT
            COUNT(*) as lowStockCount
        FROM inventory i
        INNER JOIN option_variant ov ON i.optionVariantId = ov.id
        INNER JOIN products p ON ov.productId = p.id
        WHERE i.quantity > 0 AND i.quantity <= 20
    """)
    Integer getLowStockCount();

    @SqlQuery("""
        SELECT COUNT(*) as outOfStockCount
        FROM inventory i
        INNER JOIN option_variant ov ON i.optionVariantId = ov.id
        WHERE i.quantity = 0
    """)
    Integer getOutOfStockCount();

    @SqlQuery("""
        SELECT
            p.id, p.name, p.sku, p.description, p.isActive,
            p.categoryId, p.brandId, p.noOfViews, p.noOfSold,
            p.imageId, img.url as imageUrl,
            MIN(ops.price) as price,
            COALESCE(MIN(inv.quantity), 0) as stock,
            MIN(ops.id) as optionId
        FROM products p
        INNER JOIN option_variant ops ON ops.productId = p.id
        LEFT JOIN inventory inv ON inv.optionVariantId = ops.id
        INNER JOIN image img ON p.imageId = img.id
        WHERE p.isActive = 1
          AND COALESCE(inv.quantity, 0) <= 20
        GROUP BY p.id, p.name, p.sku, p.description, p.isActive,
                 p.categoryId, p.brandId, p.noOfViews, p.noOfSold, p.imageId, img.url
        ORDER BY stock ASC
        LIMIT 10
    """)
    List<Product> getLowStockProducts();

    @SqlQuery("""
    SELECT COALESCE(SUM(od.total),0)
    FROM orders o JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
      AND DATE(o.createAt) BETWEEN :from AND :to
""")
    Integer getRevenueByRange(@Bind("from") String from, @Bind("to") String to);

    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE DATE(createAt) BETWEEN :from AND :to
""")
    Integer getOrdersByRange(@Bind("from") String from, @Bind("to") String to);


    @SqlQuery("""
    SELECT DATE(o.createAt) as label, COALESCE(SUM(od.total),0) as revenue
    FROM orders o JOIN order_detail od ON o.id = od.orderId
    WHERE o.paymentStatus='PAID'
      AND DATE(o.createAt) BETWEEN :from AND :to
    GROUP BY DATE(o.createAt) ORDER BY DATE(o.createAt)
""")
    List<DashboardStats> getRevenueChartByRange(@Bind("from") String from, @Bind("to") String to);


    // Đơn hủy hiện tại
    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE orderStatus = 'CANCELLED'
      AND YEARWEEK(createAt,1) = YEARWEEK(CURDATE(),1)
""")
    Integer getCurrentWeekCancelled();

    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE orderStatus = 'CANCELLED'
      AND YEAR(createAt) = YEAR(CURDATE())
      AND MONTH(createAt) = MONTH(CURDATE())
""")
    Integer getCurrentMonthCancelled();

    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE orderStatus = 'CANCELLED'
      AND YEAR(createAt) = YEAR(CURDATE())
""")
    Integer getCurrentYearCancelled();

    // Đơn hủy kỳ trước
    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE orderStatus = 'CANCELLED'
      AND YEARWEEK(createAt,1) = YEARWEEK(DATE_SUB(CURDATE(), INTERVAL 1 WEEK),1)
""")
    Integer getLastWeekCancelled();

    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE orderStatus = 'CANCELLED'
      AND YEAR(createAt) = YEAR(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))
      AND MONTH(createAt) = MONTH(DATE_SUB(CURDATE(), INTERVAL 1 MONTH))
""")
    Integer getLastMonthCancelled();

    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE orderStatus = 'CANCELLED'
      AND YEAR(createAt) = YEAR(CURDATE()) - 1
""")
    Integer getLastYearCancelled();

    @SqlQuery("""
    SELECT COUNT(*) FROM orders
    WHERE orderStatus = 'CANCELLED'
      AND DATE(createAt) BETWEEN :from AND :to
""")
    Integer getCancelledByRange(@Bind("from") String from, @Bind("to") String to);
}