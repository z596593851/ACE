package com.hxm.trade.common.constants;


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
}
