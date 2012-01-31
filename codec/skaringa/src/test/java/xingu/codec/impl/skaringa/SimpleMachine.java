package xingu.codec.impl.skaringa;

import java.util.ArrayList;
import java.util.List;

public class SimpleMachine
{
    @SuppressWarnings("unused")
    private long beanId;
    
    private String id;
    
    private String os;
    
    private String name;
    
    private String kiduxHome;
    
    @SuppressWarnings("unused")
    private List<String> accounts = new ArrayList<String>();
    
    @SuppressWarnings("unused")
    private List<String> localAccounts;


    public String id(){return id;}
    public void id(String id){this.id = id;}
    public String os(){return os;}
    public void os(String os){this.os = os;}
    public String name(){return name;}
    public void name(String name){this.name = name;}
    public String kiduxHome() {return kiduxHome;}
    public void kiduxHome(String kiduxHome) {this.kiduxHome = kiduxHome;}

}