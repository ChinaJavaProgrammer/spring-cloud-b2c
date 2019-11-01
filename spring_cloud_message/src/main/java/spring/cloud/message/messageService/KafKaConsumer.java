package spring.cloud.message.messageService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import spring.cloud.message.util.JmailUtil;

/**
 * @ClassName: KafKaConsumer
 * @Description: TODO kafka消费者
 * @author: dh
 * @date: 2019/11/1 9:03
 * Version   1.0
 **/
@Component
public class KafKaConsumer {

    Log log = LogFactory.getLog(this.getClass());
    @KafkaListener(topics = {"sendMobileMessage"})
    public void handle(String message){
        String email=message.substring(0,message.indexOf(":"));
        String content="您的验证码是:"+message.substring(message.indexOf(":")+1);
        JmailUtil jmailUtil = new JmailUtil();
        jmailUtil.sendEmail(content,"用户验证验证码",email);
    log.info("接收到消息:"+message);
}
    @KafkaListener(topics = {"sendMobileMessage"})
    public void handle(ConsumerRecord<String,String> record){
        log.info("topic:"+record.topic());
        log.info("key:"+record.key());
        log.info("value:"+record.value());

}
}
