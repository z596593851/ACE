package com.hxm.trade.common.constants;

public class MQEnums {
    public enum TopicEnum{
        ORDER_CONFIRM("orderTopic","confirm"),ORDER_CANCEL("orderTopic","cancel"),PAY_PAID("orderTopic","paid");
        private String topic;
        private String tag;
        TopicEnum(String tag,String topic){
            this.tag=tag;
            this.topic=topic;
        }

        public String getTopic() {
            return topic;
        }

        public String getTag() {
            return tag;
        }
    }

}
