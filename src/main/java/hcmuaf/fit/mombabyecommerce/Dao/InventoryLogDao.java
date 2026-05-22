package hcmuaf.fit.mombabyecommerce.Dao;

import hcmuaf.fit.mombabyecommerce.model.InventoryLog;
import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

@RegisterConstructorMapper(InventoryLog.class)
public interface InventoryLogDao {

    @SqlUpdate("""
        INSERT INTO inventory_logs
            (option_variant_id, product_id, action_type, quantity_change,
             stock_before, stock_after, reason, user_id)
        VALUES (:optionVariantId, :productId, :actionType, :quantityChange,
                :stockBefore, :stockAfter, :reason, :userId)
        """)
    void insertLog(@Bind("optionVariantId") Integer optionVariantId,
                   @Bind("productId") Integer productId,
                   @Bind("actionType") String actionType,
                   @Bind("quantityChange") Integer quantityChange,
                   @Bind("stockBefore") Integer stockBefore,
                   @Bind("stockAfter") Integer stockAfter,
                   @Bind("reason") String reason,
                   @Bind("userId") Integer userId);

    @SqlQuery("""
        SELECT
            il.id,
            il.option_variant_id,
            il.product_id,
            il.action_type,
            il.quantity_change,
            il.stock_before,
            il.stock_after,
            il.reason,
            il.user_id,
            u.fullName as user_name,
            DATE_FORMAT(il.created_at, '%d/%m/%Y %H:%i') as created_at,
            p.name as product_name,
            CONCAT(COALESCE(v.name,''), ': ', COALESCE(v.value,'')) as variant_label
        FROM inventory_logs il
        LEFT JOIN users u ON il.user_id = u.id
        LEFT JOIN option_variant ov ON il.option_variant_id = ov.id
        LEFT JOIN products p ON ov.productId = p.id
        LEFT JOIN variant v ON v.optionId = ov.id
        WHERE 1=1
        AND (:hasProductId = 0 OR il.product_id = :productId)
        AND (:hasActionType = 0 OR il.action_type = :actionType)
        AND (:hasFromDate = 0 OR DATE(il.created_at) >= :fromDate)
        AND (:hasToDate = 0 OR DATE(il.created_at) <= :toDate)
        ORDER BY il.created_at DESC
        LIMIT :limit OFFSET :offset
        """)
    List<InventoryLog> getLogsPaged(@Bind("productId") Integer productId,
                                   @Bind("hasProductId") int hasProductId,
                                   @Bind("actionType") String actionType,
                                   @Bind("hasActionType") int hasActionType,
                                   @Bind("fromDate") String fromDate,
                                   @Bind("hasFromDate") int hasFromDate,
                                   @Bind("toDate") String toDate,
                                   @Bind("hasToDate") int hasToDate,
                                   @Bind("limit") int limit,
                                   @Bind("offset") int offset);

    @SqlQuery("""
        SELECT COUNT(*)
        FROM inventory_logs il
        WHERE 1=1
        AND (:hasProductId = 0 OR il.product_id = :productId)
        AND (:hasActionType = 0 OR il.action_type = :actionType)
        AND (:hasFromDate = 0 OR DATE(il.created_at) >= :fromDate)
        AND (:hasToDate = 0 OR DATE(il.created_at) <= :toDate)
        """)
    int countLogs(@Bind("productId") Integer productId,
                  @Bind("hasProductId") int hasProductId,
                  @Bind("actionType") String actionType,
                  @Bind("hasActionType") int hasActionType,
                  @Bind("fromDate") String fromDate,
                  @Bind("hasFromDate") int hasFromDate,
                  @Bind("toDate") String toDate,
                  @Bind("hasToDate") int hasToDate);
}
