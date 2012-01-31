package br.com.ibnetwork.xingu.flow.engine;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Stack;

import br.com.ibnetwork.xingu.flow.PersistenceAgent;

public class DataManager extends HashMap<String,Object> {

	private static final long serialVersionUID = 1L;
	private Stack<String> history;

	private void saveFieldsInDataManager(Object instance) {
		Field fields[] = instance.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(!Modifier.isTransient(field.getModifiers()) 
			   && !Modifier.isFinal(field.getModifiers())){
				Object tmp = getFieldValue(field, instance);
				if(tmp!=null)
					this.put(field.getName(), tmp);	
			}
		}
	}

	private void restoreFieldsFromDataManager(Object instance) {
		Field fields[] = instance.getClass().getDeclaredFields();
		for (Field field : fields) {
			if(!Modifier.isTransient(field.getModifiers()) 
			&& !Modifier.isFinal(field.getModifiers())){
				if(this.containsKey(field.getName())){
					Object value = getFieldValue(field, instance);
					if(value==null)
						setFieldValue(field, instance, this.get(field.getName()));
				}
			}
		}
	}

	void synchronizeData(Object instance) {	
		saveFieldsInDataManager(instance);
		restoreFieldsFromDataManager(instance);
	}	
		
	private Object getFieldValue(Field field, Object target) {
		Method getter;
		String mName = "get" + first2Up(field.getName());
		try {
			getter = target.getClass().getMethod(mName, new Class[0]);
		} catch (Exception e) {
			try {
				getter = target.getClass().getMethod("is" + first2Up(field.getName()), new Class[0]);
			} catch (Exception e1) {
				return null;
			}
		}
		try {
			return getter.invoke(target, new Object[0]);
		} catch (Exception e1) {
			return null;
		}
	}

	private void setFieldValue(Field field, Object target, Object fieldValue) {
		Method setter;
		String mName = "set" + first2Up(field.getName());
		try {
			setter = target.getClass().getMethod(mName, new Class[]{field.getType()});
			setter.invoke(target, new Object[]{fieldValue});
		} catch (Exception e) {
			//ignore
		}
	}

	@SuppressWarnings("unused")
	void persist(PersistenceAgent session, Object instance) {
		saveFieldsInDataManager(instance.getClass());
		session.persist("dataManager", this);
	}

	@SuppressWarnings("unchecked")
	void clear(PersistenceAgent session) {
		this.clear();
		session.persist("dataManager", this);
	}

	private static String first2Up(String src) {
		src = src.substring(0, 1).toUpperCase() 
			+ src.substring(1, src.length());
		return src;
	}

	@SuppressWarnings("unchecked")
	Stack<String> getHistory() {
		history = (Stack<String>)this.get("history");
		if(history==null){
			history = new Stack<String>();
			this.put("history", history);
		}
		return history;
	}
	
	String getState() {
		return (String)this.get("state");
	}

	void setState(String state) {
		this.put("state", state);
	}

	void setHistory(Stack<String> newHistory) {
		history = newHistory;
		this.put("history", history);	
	}
}