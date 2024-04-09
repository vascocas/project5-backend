package paj.project5_vc.bean;
import paj.project5_vc.websocket.Notification;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.inject.Inject;
@Singleton
public class TimerBean {
    @Inject
    Notification notifier;


    @Schedule(second="*", minute="*/5", hour="*") // this automatic timer is set to expire every 5 minutes
    public void automaticTimer(){
        String msg = "This is just a test!";
        //System.out.println(msg);
        notifier.send("mytoken",msg);
    }
}
