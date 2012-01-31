package br.com.ibnetwork.xingu.flow.adapters;

//import javax.servlet.http.HttpSession;

//import org.apache.struts2.ServletActionContext;

//import br.com.ibnetwork.xingu.flow.BusinessFacade;
//import br.com.ibnetwork.xingu.flow.PersistenceAgent;
//import br.com.ibnetwork.xingu.flow.Workflow;
//import br.com.ibnetwork.xingu.flow.engine.StateMachine;

//import com.opensymphony.xwork2.ActionSupport;
//import com.opensymphony.xwork2.Preparable;

@SuppressWarnings("serial")
public abstract class StrutsFacadeSuport{//<FACADE extends BusinessFacade> extends ActionSupport implements Preparable, PersistenceAgent {

//	protected HttpSession session;
//	protected FACADE facade;
//	protected Workflow workflow;
//
//	protected String back;
//	protected String backTo;
//	protected String start;
//	
//	@SuppressWarnings("unchecked")
//	public void prepare() throws Exception{
//		session =  ServletActionContext.getRequest().getSession();
//		facade = instantiateBusinessFacade();
//		workflow = instantiateWorkflow();
//	}
//	
//	protected abstract FACADE instantiateBusinessFacade();
//	protected abstract Workflow instantiateWorkflow();
//
//	@Override
//	public String execute() throws Exception {
//		String back = ServletActionContext.getRequest().getParameter("back");
//		String backTo = ServletActionContext.getRequest().getParameter("backTo");
//		String start = ServletActionContext.getRequest().getParameter("start");
//		
//		if(back!=null)
//			StateMachine.back(facade, this);
//		else if(backTo!=null)
//			StateMachine.back(facade, this, backTo);
//		else if(start!=null){
//			StateMachine.clearData(this);
//			StateMachine.setState(this, null);
//			StateMachine.execute(workflow, facade, this);
//		}else
//			StateMachine.execute(workflow, facade, this);
//		
//		String submitTo =  StateMachine.getState(this);
//		StateMachine.log("--> submit to:" +submitTo);
//		return submitTo;
//	}
//
//	public Object load(String key) {
//		return session.getAttribute(key) ;
//	}
//
//	public void persist(String key, Object toPersist) {
//		session.setAttribute(key, toPersist);
//	}
//
//	//Getters e Setters
//	public String getStart() {return start;}
//	public void setStart(String start) {this.start = start;}
//	public String getBack() {return back;}
//	public void setBack(String back) {this.back = back;}
//	public String getBackTo() {return backTo;}
//	public void setBackTo(String backTo) {this.backTo = backTo;}
//	public FACADE getFacade() {return facade;}
//	public void setFacade(FACADE facade) {this.facade = facade;}	
}