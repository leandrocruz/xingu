package br.com.ibnetwork.xingu.ibatis;

import com.ibatis.sqlmap.client.SqlMapClient;

public interface Ibatis
{
    String ROLE = Ibatis.class.getName();
    
    SqlMapClient getSqlMap();
    
}
