package executor;

import java.util.Hashtable;

public interface Executor<T,D>{
    public String execute(Hashtable<String,String> sPara,Hashtable<String,String> gPara) throws Exception;
    public Executor<T,D> getSuccessor(T test,D data);
}