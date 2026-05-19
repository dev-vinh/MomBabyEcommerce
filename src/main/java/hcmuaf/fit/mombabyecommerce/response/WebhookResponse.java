package hcmuaf.fit.mombabyecommerce.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookResponse {
    @JsonProperty("CODAmount")
    private int codAmount;

    @JsonProperty("CODTransferDate")
    private String codTransferDate;

    @JsonProperty("ClientOrderCode")
    private String clientOrderCode;

    @JsonProperty("ConvertedWeight")
    private int convertedWeight;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Fee")
    private Fee fee;

    @JsonProperty("Height")
    private int height;

    @JsonProperty("IsPartialReturn")
    private boolean isPartialReturn;

    @JsonProperty("Length")
    private int length;

    @JsonProperty("OrderCode")
    private String orderCode;

    @JsonProperty("PartialReturnCode")
    private String partialReturnCode;

    @JsonProperty("PaymentType")
    private int paymentType;

    @JsonProperty("Reason")
    private String reason;

    @JsonProperty("ReasonCode")
    private String reasonCode;

    @JsonProperty("ShopID")
    private int shopId;

    @JsonProperty("Status")
    private String status;

//    @JsonProperty("Time")
//    private LocalDate time;

    @JsonProperty("TotalFee")
    private int totalFee;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Warehouse")
    private String warehouse;

    @JsonProperty("Weight")
    private int weight;

    @JsonProperty("Width")
    private int width;

    public int getCodAmount() {
        return codAmount;
    }

    public void setCodAmount(int codAmount) {
        this.codAmount = codAmount;
    }

    public String getCodTransferDate() {
        return codTransferDate;
    }

    public void setCodTransferDate(String codTransferDate) {
        this.codTransferDate = codTransferDate;
    }

    public String getClientOrderCode() {
        return clientOrderCode;
    }

    public void setClientOrderCode(String clientOrderCode) {
        this.clientOrderCode = clientOrderCode;
    }

    public int getConvertedWeight() {
        return convertedWeight;
    }

    public void setConvertedWeight(int convertedWeight) {
        this.convertedWeight = convertedWeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Fee getFee() {
        return fee;
    }

    public void setFee(Fee fee) {
        this.fee = fee;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isPartialReturn() {
        return isPartialReturn;
    }

    public void setPartialReturn(boolean partialReturn) {
        isPartialReturn = partialReturn;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPartialReturnCode() {
        return partialReturnCode;
    }

    public void setPartialReturnCode(String partialReturnCode) {
        this.partialReturnCode = partialReturnCode;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public LocalDate getTime() {
//        return time;
//    }
//
//    public void setTime(LocalDate time) {
//        this.time = time;
//    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static class Fee {
        @JsonProperty("CODFailedFee")
        private int codFailedFee;

        @JsonProperty("CODFee")
        private int codFee;

        @JsonProperty("Coupon")
        private int coupon;

        @JsonProperty("DeliverRemoteAreasFee")
        private int deliverRemoteAreasFee;

        @JsonProperty("DocumentReturn")
        private int documentReturn;

        @JsonProperty("DoubleCheck")
        private int doubleCheck;

        @JsonProperty("Insurance")
        private int insurance;

        @JsonProperty("MainService")
        private int mainService;

        @JsonProperty("PickRemoteAreasFee")
        private int pickRemoteAreasFee;

        @JsonProperty("R2S")
        private int r2s;

        @JsonProperty("Return")
        private int returnFee;

        @JsonProperty("StationDO")
        private int stationDO;

        @JsonProperty("StationPU")
        private int stationPU;

        @JsonProperty("Total")
        private int total;

        public int getCodFailedFee() {
            return codFailedFee;
        }

        public void setCodFailedFee(int codFailedFee) {
            this.codFailedFee = codFailedFee;
        }

        public int getCodFee() {
            return codFee;
        }

        public void setCodFee(int codFee) {
            this.codFee = codFee;
        }

        public int getCoupon() {
            return coupon;
        }

        public void setCoupon(int coupon) {
            this.coupon = coupon;
        }

        public int getDeliverRemoteAreasFee() {
            return deliverRemoteAreasFee;
        }

        public void setDeliverRemoteAreasFee(int deliverRemoteAreasFee) {
            this.deliverRemoteAreasFee = deliverRemoteAreasFee;
        }

        public int getDocumentReturn() {
            return documentReturn;
        }

        public void setDocumentReturn(int documentReturn) {
            this.documentReturn = documentReturn;
        }

        public int getDoubleCheck() {
            return doubleCheck;
        }

        public void setDoubleCheck(int doubleCheck) {
            this.doubleCheck = doubleCheck;
        }

        public int getInsurance() {
            return insurance;
        }

        public void setInsurance(int insurance) {
            this.insurance = insurance;
        }

        public int getMainService() {
            return mainService;
        }

        public void setMainService(int mainService) {
            this.mainService = mainService;
        }

        public int getPickRemoteAreasFee() {
            return pickRemoteAreasFee;
        }

        public void setPickRemoteAreasFee(int pickRemoteAreasFee) {
            this.pickRemoteAreasFee = pickRemoteAreasFee;
        }

        public int getR2s() {
            return r2s;
        }

        public void setR2s(int r2s) {
            this.r2s = r2s;
        }

        public int getReturnFee() {
            return returnFee;
        }

        public void setReturnFee(int returnFee) {
            this.returnFee = returnFee;
        }

        public int getStationDO() {
            return stationDO;
        }

        public void setStationDO(int stationDO) {
            this.stationDO = stationDO;
        }

        public int getStationPU() {
            return stationPU;
        }

        public void setStationPU(int stationPU) {
            this.stationPU = stationPU;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
