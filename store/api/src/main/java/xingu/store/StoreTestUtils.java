package xingu.store;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class StoreTestUtils
{
	private static long id = 1;
	
	public static Answer<Void> beanIdSetter = new Answer<Void>(){
		@Override
		public Void answer(InvocationOnMock invocation)
			throws Throwable
		{
			Object[] args = invocation.getArguments();
			PersistentBean bean = (PersistentBean) args[1];
			if(bean.getId() <= 0)
			{
				bean.setId(id++);
			}
			return null;
		}
	};
}