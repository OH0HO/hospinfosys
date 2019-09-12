package tw.com.hismax.test.hospinfosys;

/**
 * Created by apple on 16/7/17.
 */
public class MessageItem {
    private String dateTime;
    private String msgContext;

    public MessageItem(String dd, String msg){
        this.dateTime = dd;
        this.msgContext = msg;
    }
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMsgContext() {
        return msgContext;
    }

    public void setMsgContext(String msgContext) {
        this.msgContext = msgContext;
    }
}
