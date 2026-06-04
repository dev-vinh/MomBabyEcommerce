package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Voucher;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
@RegisterConstructorMapper(Voucher.class)
public interface VoucherDao {

    @SqlQuery("""
SELECT *
FROM vouchers
WHERE code = :code
""")
    @RegisterBeanMapper(Voucher.class)
    Voucher findByCode(@Bind("code") String code);

    @SqlUpdate("""
UPDATE vouchers
SET quantity = quantity - 1
WHERE id = :id
""")
    int decreaseQuantity(@Bind("id") Integer id);

    @SqlQuery("""
    SELECT *
    FROM vouchers
    ORDER BY id DESC
""")
    List<Voucher> getAllVouchers();


    @SqlUpdate("""
INSERT INTO vouchers(
    code,
    discount_percent,
    min_order_amount,
    max_discount,
    quantity,
    start_date,
    end_date,
    active,
    description
)
VALUES(
    :code,
    :discountPercent,
    :minOrderAmount,
    :maxDiscount,
    :quantity,
    :startDate,
    :endDate,
    :active,
    :description
)
""")
    void insertVoucher(@BindBean Voucher voucher);

    @SqlUpdate("""
    UPDATE vouchers
    SET active = 0
    WHERE id = :id
    """)
    void disableVoucher(@Bind("id") Integer id);

    @SqlQuery("""
    select *
    from vouchers
    where id = :id
    """)
    Voucher findById(@Bind("id") Integer id);

    @SqlUpdate("""
UPDATE vouchers
SET code = :code,
    discount_percent = :discountPercent,
    min_order_amount = :minOrderAmount,
    max_discount = :maxDiscount,
    quantity = :quantity,
    start_date = :startDate,
    end_date = :endDate,
    active = :active,
    description = :description
WHERE id = :id
""")
    void updateVoucher(@BindBean Voucher voucher);
}
