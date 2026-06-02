package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.Voucher;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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


}
