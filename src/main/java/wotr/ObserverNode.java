package wotr;

import java.net.InetAddress;

/* renamed from: ObserverNode */
public interface ObserverNode extends Runnable {
    void enqueue(String str);

    void giveUpHosting();

    void setAddress(InetAddress inetAddress);

    void setPort(int i);
}
