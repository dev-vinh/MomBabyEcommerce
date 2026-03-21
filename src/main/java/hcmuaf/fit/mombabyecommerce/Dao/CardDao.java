package hcmuaf.fit.mombabyecommerce.Dao;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import javax.smartcardio.Card;
import java.time.LocalDate;
import java.util.List;
@RegisterConstructorMapper(Card.class)
public interface CardDao {
    @SqlQuery(value = "SELECT *\n" +
            "FROM card\n" +
            "WHERE userId = :userId;")
    List<Card> getCardByUserId(@Bind("userId") Integer userId);

    @SqlUpdate(value = "insert into card( userId, duration, type, isDefault, last4) VALUE (\n" +
            "  :userId, :duration , :type , :isDefault , :last4 \n" +
            " );")
    Boolean addCard(
            @Bind("userId") Integer userId,
            @Bind("duration") LocalDate duration,
            @Bind("type") String type,
            @Bind("isDefault") Boolean isDefault,
            @Bind("last4") Integer last4
    );
}
