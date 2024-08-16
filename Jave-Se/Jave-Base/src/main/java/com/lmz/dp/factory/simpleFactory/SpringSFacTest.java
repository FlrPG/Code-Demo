package com.lmz.dp.factory.simpleFactory;

public class SpringSFacTest {
}


/*
public interface IChannelService {

    boolean match(Integer channel);

    Order getChannelOrder(String orderNo);

}

@Component
public class ChannelServiceFactory {

    // 创建对象的工作全部交给Spring
    // Spring会把所有实现了IChannelService接口的对象注入channelServiceList
    @Resource
    private List<IChannelService> channelServiceList;

    public IChannelService select(Integer channel) {
        for (IChannelService channelService : channelServiceList) {
            if(channelService.match(channel)){
                return channelService;
            }
        }
        throw new RuntimeException("渠道类型错误");
    }
}

public void test() {
    Integer channel = ChannelEnum.TAO_BAO.getCode();
    String orderNo = "123456";
    // 最终，简单工厂模式只剩下“选择对象”的逻辑
    ChannelOrderTO order = channelServiceFactory.select(channel).getChannelOrder(orderNo);
}*/
