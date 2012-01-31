package xingu.email;

import java.util.Date;

import xingu.store.PersistentBean;

public interface Email
    extends PersistentBean
{
    void setSent(Date sent);
    
    Date getSent();
    
    String getType();
    
    String getCode(); /* Tracking Code */
    
    String getToAddress();
    
    String getToName();
    
    String getFromAddress();
    
    String getFromName();
    
    String getSubject();

    String getHtmlTemplate();
    String getHtmlLayoutTemplate();
    
    String getTextTemplate();
    String getTextLayoutTemplate();

	String getBounceTo();
}
