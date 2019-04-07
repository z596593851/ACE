package com.hxm.trade.common.client;

import com.hxm.trade.common.constants.TradeEnums;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RestClientProxyFactoryBean implements FactoryBean {
    private RestTemplate restTemplate=new RestTemplate();
    private Class serviceInterface;
    private TradeEnums.RestServerEnum serverEnum;

    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{serviceInterface},new ClientProxy());
    }

    public Class<?> getObjectType() {
        return serviceInterface;
    }

    public boolean isSingleton() {
        return true;
    }
    private class ClientProxy implements InvocationHandler{
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return restTemplate.postForObject(serverEnum.getServerUrl()+method.getName(),args[0],method.getReturnType());
        }
    }

    public void setServiceInterface(Class serviceInterface) {
        this.serviceInterface = serviceInterface;
    }

    public void setServerEnum(TradeEnums.RestServerEnum serverEnum) {
        this.serverEnum = serverEnum;
    }
}
