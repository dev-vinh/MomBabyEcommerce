package hcmuaf.fit.mombabyecommerce.request;

import java.util.List;

public class GHNCancelOrderRequest {
    List<String> order_codes;

    public GHNCancelOrderRequest(List<String> order_codes) {
        this.order_codes = order_codes;
    }

    public List<String> getOrder_codes() {
        return order_codes;
    }

    public void setOrder_codes(List<String> order_codes) {
        this.order_codes = order_codes;
    }
}
