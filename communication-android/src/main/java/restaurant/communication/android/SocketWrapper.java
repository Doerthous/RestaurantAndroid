package restaurant.communication.android;

import android.net.wifi.WifiManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import restaurant.communication.core.impl.ISocketWrapper;

/**
 * Created by Doerthous on 2017/12/17.
 */

public class SocketWrapper implements ISocketWrapper {
    private WifiManager.MulticastLock lock;
    private Map<String, Boolean> listeners;
    public SocketWrapper(WifiManager.MulticastLock lock){
        this.lock = lock;
        listeners = new HashMap<>();
    }

    public static class Address implements ISocketWrapper.IAddress {
        private String ip;
        private Integer port;

        public Address(String ip, Integer port) {
            this.ip = ip;
            this.port = port;
        }

        @Override
        public String getIp() {
            return ip;
        }

        @Override
        public Integer getPort() {
            return port;
        }
    }
    public static class Data implements ISocketWrapper.IData {
        private Address source;
        private Address target;
        private Serializable data;

        public Data(Address source, Address target, Serializable data) {
            this.source = source;
            this.target = target;
            this.data = data;
        }

        @Override
        public IAddress getSourceAddress() {
            return source;
        }

        @Override
        public IAddress getTargetAddress() {
            return target;
        }

        @Override
        public Serializable getData() {
            return data;
        }
    }

    @Override
    public void sendByTcp(ISocketWrapper.IData data) throws ConnectException {
        Socket other = null;
        ObjectOutputStream out = null;
        try {
            other = new Socket(data.getTargetAddress().getIp(), data.getTargetAddress().getPort());
            out = new ObjectOutputStream(other.getOutputStream());
            out.writeObject(data);
            out.writeObject(null);
        } catch(ConnectException e){
            throw e;
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(out != null) {
                    out.close();
                } if(other != null) {
                    other.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void sendByUdp(ISocketWrapper.IData data) {
        MulticastSocket other = null;
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
            oos.writeObject(null);
            byte[] buf = baos.toByteArray();
            other = new MulticastSocket();
            other.setBroadcast(true); //有没有没啥不同
            //other.setLoopbackMode(true);
            DatagramPacket packet = new DatagramPacket(buf, buf.length,
                    InetAddress.getByName(data.getTargetAddress().getIp()), data.getTargetAddress().getPort());
            other.send(packet);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if(baos != null) {
                    baos.close();
                }
                if(oos != null){
                    oos.close();
                }
                if(other != null) {
                    other.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public String listenByTcp(IAddress address, ISocketWrapper.IAction action) {
        String uuid = UUID.randomUUID().toString();
        listeners.put(uuid, true);
        new Thread(() -> {
            ServerSocket server = null;
            try {
                server = new ServerSocket();
                server.bind(new InetSocketAddress(address.getIp(), address.getPort()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (listeners.get(uuid)) {
                Socket client;
                try {
                    client = server.accept();
                    ObjectInputStream in = new ObjectInputStream(client.getInputStream());
                    ISocketWrapper.IData data = (ISocketWrapper.IData) in.readObject();
                    in.close();
                    String sourceIp = client.getInetAddress().toString().substring(1);
                    ISocketWrapper.IData data2 = new Data(new Address(sourceIp, client.getPort()),
                            new Address(data.getTargetAddress().getIp(), data.getTargetAddress().getPort()),
                            data.getData());
                    client.close();
                    new Thread(()->action.receive(data2)).start();
                } catch (SocketException e){
                    if(!e.getMessage().equalsIgnoreCase("socket closed")){
                        e.printStackTrace();
                    }
                } catch(EOFException e){

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return uuid;
    }
    @Override
    public String listenByUdp(ISocketWrapper.IAddress address, ISocketWrapper.IAction action){
        String uuid = UUID.randomUUID().toString();
        listeners.put(uuid, true);
        new Thread(() -> {
            while (listeners.get(uuid)) {
                MulticastSocket server = null;
                try {
                    lock.acquire();
                    try {
                        server = new MulticastSocket(null);
                        server.setReuseAddress(true);
                        server.bind(new InetSocketAddress(address.getPort()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byte[] buffer = new byte[65535];
                    DatagramPacket packet = new DatagramPacket(buffer , buffer.length);
                    while(true){
                        server.receive(packet);
                        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        ISocketWrapper.IData data = (ISocketWrapper.IData) ois.readObject();
                        String sourceIp = packet.getAddress().toString().substring(1);
                        ISocketWrapper.IData data2 = new Data(new Address(sourceIp, packet.getPort()),
                                new Address(data.getTargetAddress().getIp(), data.getTargetAddress().getPort()),
                                data.getData());
                        new Thread(()->action.receive(data2)).start();
                    }
                } catch (IOException e) {
                    if(!e.getMessage().equals("socket closed")) {
                        e.printStackTrace();
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if(server != null){
                        server.close();
                        lock.release();
                    }
                }
            }
        }).start();
        return uuid;
    }
    @Override
    public void killListener(String listenId) {
        if(listeners.keySet().contains(listenId)){
            listeners.put(listenId, false);
        }
    }


}
