package com.dxfx.netty.client;

import com.dxfx.netty.util.MyResponse;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zzj
 * @version 1.0
 * @date 2021/1/12 21:49
 * @description ----------封装request 以及服务端返回的response---------
 */
public class DefaultFuture {

    /**
     * 并发场景下的HashMap 以requestID->response存放键值对
     */
    public final static ConcurrentHashMap<Long, DefaultFuture> allDefaultFuture = new ConcurrentHashMap<>();

    final Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private MyResponse response;

    private Long dId;

    public DefaultFuture(ClientRequest request) {
        dId = request.getId();
        allDefaultFuture.put(dId, this);
    }

    public static void recieve(MyResponse response) {
        System.out.println(Thread.currentThread());
        if (response == null) {
            //服务端不能返回null
            throw new NullPointerException();
        }
        DefaultFuture df = allDefaultFuture.get(response.getId());
        if (df != null) {
            //获得df对应的重入锁
            Lock lock = df.lock;
            lock.lock();
            try {
                df.setResponse(response);
                df.condition.signal();
                //移除当前df
                allDefaultFuture.remove(df.dId);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * thread-1获取response 此时要判断response是否存在 如果不存在那么阻塞
     *
     * @return
     */
    public MyResponse get() {
        System.out.println(Thread.currentThread());
        lock.lock();
        try {
            //异步的等待response数据的到来
            while (!done()) {
                //使用condition前提是当前线程已经获得了该condition（条件对象）绑定的重入锁
                condition.await(5, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return this.response;
    }

    private boolean done() {
        return this.response != null;
    }

    public MyResponse getResponse() {
        return response;
    }

    public void setResponse(MyResponse response) {
        this.response = response;
    }

}
