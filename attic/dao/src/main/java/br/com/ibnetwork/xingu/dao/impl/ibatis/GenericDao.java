package br.com.ibnetwork.xingu.dao.impl.ibatis;

import org.apache.commons.lang.StringUtils;

public class GenericDao<T>
    extends IbatisDaoSupport<Long, T>
{
	private Class<T> targetClass; 
	
	public GenericDao(Class<T> clazz)
	{
		this.targetClass = clazz;
	}

	@Override
    public String getNameSpace()
    {
		return StringUtils.uncapitalize(targetClass.getSimpleName()); 
    }
}
