package xingu.codec.impl.skaringa;

import java.util.ArrayList;
import java.util.List;

public class SimpleMachine
{
	long beanId;
    
    String id;
    
    String os;
    
    String name;
    
    String kiduxHome;
    
    List<String> accounts = new ArrayList<String>();
    
    List<String> localAccounts;

    public String id(){return id;}
    public void id(String id){this.id = id;}
    public String os(){return os;}
    public void os(String os){this.os = os;}
    public String name(){return name;}
    public void name(String name){this.name = name;}
    public String kiduxHome() {return kiduxHome;}
    public void kiduxHome(String kiduxHome) {this.kiduxHome = kiduxHome;}

}