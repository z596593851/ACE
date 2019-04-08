package com.hxm.trade.common.constants;


import org.apache.commons.lang3.StringUtils;

public class TradeEnums {

    public enum RestServerEnum{
        USER("localhost","user",8090),
        ORDER("localhost","ORDER",8070);
        private int serverPort;
        private String serverHost;
        private String contextPath;

        RestServerEnum(String serverHost,String contextPath,int serverPort) {
            this.serverPort = serverPort;
            this.serverHost = serverHost;
            this.contextPath = contextPath;
        }

        public int getServerPort() {
            return serverPort;
        }

        public String getServerHost() {
            return serverHost;
        }

        public String getContextPath() {
            return contextPath;
        }
        public String getServerUrl(){
            return "http://"+serverHost+":"+serverPort+"/"+contextPath+"/";
        }
    }
    public enum RetEnum{
        SUCCESS("1","成功"),FAIL("-1","失败");
        private String code;
        private String desc;
        RetEnum(String code,String desc){
            this.code=code;
            this.desc=desc;

        }
        public String getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }

    public enum OrderStatusEnum{
        NO_CONFIRM("0","未确认"),CONFIRM("1","已确认"),CANCEL("2","已取消"),INVALID("3","无效"),RETURNED("4","退货");
        private String statusCode;
        private String desc;
        public String getStatusCode() {
            return statusCode;
        }

        public String getDesc() {
            return desc;
        }

        OrderStatusEnum(String statusCode, String desc) {
            this.statusCode = statusCode;
            this.desc = desc;
        }

    }

    public enum PayStatusEnum{
        NO_PAY("0","未付款"),PAYING("1","支付中"),PAID("2","已付款");
        private String statusCode;
        private String desc;

        public String getStatusCode() {
            return statusCode;
        }

        public String getDesc() {
            return desc;
        }

        PayStatusEnum(String statusCode, String desc) {
            this.statusCode = statusCode;
            this.desc = desc;
        }
    }

    public enum ShippingStatusEnum{
        NO_SHIP("0","未发货"),SHIPPED("1","已发货"),RECEVIED("2","已收货");
        private String statusCode;
        private String desc;

        public String getStatusCode() {
            return statusCode;
        }

        public String getDesc() {
            return desc;
        }

        ShippingStatusEnum(String statusCode, String desc) {
            this.statusCode = statusCode;
            this.desc = desc;
        }
    }

    public enum YesNoEnum{
        NO("0","否"),YES("1","是");
        private String code;
        private String desc;

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        YesNoEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    public enum UserMoneyLogTypeEnum{
        PAID("1","订单付款"),REFUND("2","订单退款");
        private String code;
        private String desc;

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        UserMoneyLogTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }
}
