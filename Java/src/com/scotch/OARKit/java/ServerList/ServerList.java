package com.scotch.OARKit.java.ServerList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Campbell Millar on 29/03/2016.
 */
public class ServerList {
    public enum IndexType{
        IP(0),
        PORT(1),
        OBJECT(2);
        public int index;
        IndexType(int index){
            this.index = index;
        }
    }
    public static Map<String, ArrayList<Object>> array = new LinkedHashMap<>();
    final String name;
    private ArrayList<String> data;
    public ServerList(String name, String[] data){
        this.data = new ArrayList<String>(Arrays.asList(data));
        this.name = name;
        addToArray(this);
    }
    public void changeArray(IndexType index, String data) {
        if(index.index < 2){
            this.data.set(index.index,data);
        } else{
            System.out.println("WARNING THIS FUNCTION DOES NOT SUPPORT THE CHANGE OF "+index.name());
        }
    }
    public String getName() {
        return name;
    }
    public String[] getArray() {
        return Arrays.copyOf(data.toArray(),data.toArray().length,String[].class);
    }
    public static void addToArray(ServerList object){
        ArrayList<Object>objectArray = object.data.stream().collect(Collectors.toCollection(ArrayList::new));
        objectArray.add(object);
        array.put(object.name,objectArray);
    }
    public static String[] getIPAndPort(String key){
        ArrayList<String> ipPortArray = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ipPortArray.add((String)array.get(key).get(i));
        }
        return ipPortArray.toArray(new String[ipPortArray.size()]);
    }
    public static String[] getKeys(){
        return Arrays.copyOf(array.keySet().toArray(),array.keySet().toArray().length,String[].class);
    }

    //CHANGING/MODIFYING DATA
    @Deprecated //DO NOT USE THIS METHOD (ONLY FOR BACK END)
    public static void updateKey(String key, Object[] data){
        array.put(key,new ArrayList<Object>(Arrays.asList(data)));
        updateFile();
    }
    public static void changeData(String key, IndexType index, Object data){
        ArrayList<Object> x = array.get(key);
        x.set(index.index,data);
        array.put(key,x);
        updateFile();
    }
    public static void removeKey(String key){
        array.remove(key);
        updateFile();
    }
    public static void addKey(String key, String[] data){
        new ServerList(key, data);
        updateFile();
    }
    private static void updateFile(){
        GetServerList.updateList();
    }


}
