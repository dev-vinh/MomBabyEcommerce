package hcmuaf.fit.mombabyecommerce.response;

public class GHNCancelOrderResponse {
    String order_code;

    public GHNCancelOrderResponse(String order_code) {
        this.order_code = order_code;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }
}
