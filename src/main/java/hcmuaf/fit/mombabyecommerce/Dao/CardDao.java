package hcmuaf.fit.mombabyecommerce.Dao;

import org.jdbi.v3.sqlobject.config.RegisterConstructorMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import javax.smartcardio.Card;
import java.util.List;
@RegisterConstructorMapper(Card.class)
public interface CardDao {
    @SqlQuery(value = "SELECT *\n" +
            "FROM card\n" +
            "WHERE userId = :userId;")
    List<Card> getCardByUserId(@Bind("userId") Integer userId);

}
