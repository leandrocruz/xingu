package br.com.ibnetwork.xingu.attributes.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

import br.com.ibnetwork.xingu.attributes.Attribute;
import br.com.ibnetwork.xingu.attributes.AttributeException;
import br.com.ibnetwork.xingu.attributes.AttributeList;
import br.com.ibnetwork.xingu.container.Dependency;
import br.com.ibnetwork.xingu.ibatis.Ibatis;

public class IbatisAttributeListManager
    extends AttributeListManagerSupport
{
    @Dependency
    private Ibatis iBatis;
    
    private static final String NAME_SPACE = "attributes.";
    
    public AttributeList getListById(String groupName, String repoName, long id)
        throws AttributeException
    {
        if(id <= 0)
        {
            return null;
        }
        
        SqlMapClient sqlMap = iBatis.getSqlMap();
        Long listId = new Long(id);
        try
        {
            Map map = (Map) sqlMap.queryForObject(NAME_SPACE+"getById", listId);
            if(map == null || map.isEmpty())
            {
                throw new AttributeException("can't find list ["+id+"]");
            }
            String nameSpace = (String) map.get("NAMESPACE");
            List list = sqlMap.queryForList(NAME_SPACE+"getAttributes", listId);
            AttributeList result = create(id, list);
            result.setNameSpace(nameSpace);
            return result;
        }
        catch (SQLException e)
        {
            throw new AttributeException("Error retrieving list["+id+"] from database",e);
        }
    }

    public AttributeList[] queryList(String groupName, String repoName, Attribute attribute) 
        throws AttributeException
    {
        throw new UnsupportedOperationException();
    }

    public void remove(String groupName, String repoName, long id)
        throws AttributeException
    {
        SqlMapClient sqlMap = iBatis.getSqlMap();
        try
        {
            sqlMap.delete(NAME_SPACE+"delete", id);
            sqlMap.delete(NAME_SPACE+"deleteAttributes", id);
        }            
        catch (SQLException e)
        {
            throw new AttributeException("Error deleting list["+id+"] on database",e);
        }
    }

    public long store(String groupName, String repoName, AttributeList list)
        throws AttributeException
    {
        if(list == null)
        {
            return defaultId;
        }
        long id = list.getId();
        boolean newList = false;
        if(id <= 0)
        {
            //set id if list is new
            id = setListId(list);
            newList = true;
        }
        
        Long listId = new Long(id);
        SqlMapClient sqlMap = iBatis.getSqlMap();
        List<Attribute> attributes = list.getAttributes();
        try
        {
            if(newList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", listId);
                map.put("namespace", getNameSpace(groupName, repoName));
                sqlMap.insert(NAME_SPACE+"insert", map);
            }
            
            sqlMap.delete(NAME_SPACE+"deleteAttributes", id);
            for (Attribute attribute : attributes)
            {
                sqlMap.insert(NAME_SPACE+"insertAttribute", attribute);    
            }
        }
        catch (SQLException e)
        {
            throw new AttributeException("Error storing list["+id+"] on database",e);
        }
        return listId.longValue();
    }

}
