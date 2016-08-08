package Transceiver;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Transceiver {

    private static final Map<String, TransitItem> listMap = new HashMap<>();


    /*Отправка сообщения*/
    public static void send(String key, Object o) {
        innerHeader(key, null, o, Transaction.send);
    }

    /*  подписка на события*/
    public static void subscribe(String key, ITransceiver iTransceiver) {
        innerHeader(key, iTransceiver, null, Transaction.subscribe);
    }

    /*Отписка от события*/
    public static void cancelSubscribe(String key) {
        innerHeader(key, null, null, Transaction.cancel);
    }

    private static synchronized void innerHeader(String key, ITransceiver iTransceiver, Object o, Transaction transaction) {
        switch (transaction) {
            case send: {
                innerSend(key, o);
                break;
            }
            case subscribe: {
                innerSubscribe(key, iTransceiver);
                break;
            }
            case cancel: {
                innerCancelSubscribe(key);
                break;
            }
            case cancelAll: {
                listMap.clear();
                break;
            }
            default: {
                throw new RuntimeException("no selected Transaction");
            }
        }
    }

    private static void innerSubscribe(String key, ITransceiver iTransceiver) {
        TransitItem item = new TransitItem();
        item.iTransceiver = iTransceiver;
        listMap.remove(key);
        listMap.put(key, item);
    }

    private static void innerSend(String key, Object o) {
        TransitItem item = listMap.get(key);
        if (item != null) {
            item.iTransceiver.action(o);
        }
    }

    private static void innerCancelSubscribe(String key) {
        try {
            listMap.remove(key);
        } catch (Exception ex) {
            throw new RuntimeException("cancel transiver " + ex.getMessage());
        }
    }

    public static void cancelAll() {
        innerHeader(null, null, null, Transaction.cancelAll);
    }

    private enum Transaction {
        send,
        subscribe,
        cancel,
        cancelAll,
    }

    public interface ITransceiver {
        void action(Object o);
    }

    private static class TransitItem {
        public ITransceiver iTransceiver;
        public UUID uuid;
    }

}
