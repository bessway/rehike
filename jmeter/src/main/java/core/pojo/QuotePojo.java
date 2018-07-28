package core.pojo;
public class QuotePojo{
    private Integer quoteId=null;
    private String operaStatus=null;
    private Integer quoteStatus=null;

    public void setQuoteId(Integer quoteId){
        this.quoteId=quoteId;
    }
    public Integer getQuoteId(){
        return this.quoteId;
    }
    public void setOperaStatus(String operaStatus){
        this.operaStatus=operaStatus;
    }
    public String getOperaStatus(){
        return this.operaStatus;
    }
    public void setQuoteStatus(Integer quoteStatus){
        this.quoteStatus=quoteStatus;
    }
    public Integer getQuoteStatus(){
        return this.quoteStatus;
    }
}