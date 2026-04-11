package hcmuaf.fit.mombabyecommerce.service;

import hcmuaf.fit.mombabyecommerce.Dao.CardDao;
import org.jdbi.v3.core.Jdbi;
import hcmuaf.fit.mombabyecommerce.model.Card;
import java.util.List;

public class CardService {
    CardDao cardDao;

    public CardService(Jdbi jdbi) {
        this.cardDao = jdbi.onDemand(CardDao.class);
    }

    public List<Card> getCartByUserId(Integer user_id) {
        return cardDao.getCardByUserId(user_id);
    }

    public Card getCardById(Integer card_id) {
        return cardDao.getCardById(card_id);
    }

    public Boolean addCard(Card card) {
        return cardDao.addCard(
                card.getUserId(),
                card.getDuration(),
                card.getType(),
                card.getDefault()

        );
    }

}
