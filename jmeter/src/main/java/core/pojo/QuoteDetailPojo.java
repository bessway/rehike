package core.pojo;

import java.sql.Timestamp;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class QuoteDetailPojo{
    @Excel(name="询价单号",orderNum="0")
    private Integer quoteId=null;
    @Excel(name="名称",orderNum="1")
    private String reqmProName=null;
    @Excel(name="品牌",orderNum="2")
    private String reqmBrand=null;
    @Excel(name="规格",orderNum="3")
    private String reqmProModels=null;
    @Excel(name="数量",orderNum="4")
    private Integer reqmQuantity=null;
    @Excel(name="单位",orderNum="5")
    private String reqmProUnit=null;
    @Excel(name="sku",orderNum="6")
    private String skuNo=null;
    @Excel(name="创建时间",orderNum="7",format="yyyy-MM-dd")
    private Timestamp createdon=null;

    public void setCreatedon(Timestamp createdon){
        this.createdon=createdon;
    }
    public Timestamp getCreatedon(){
        return this.createdon;
    }
    public void setReqmProModels(String reqmProModels){
        this.reqmProModels=reqmProModels;
    }
    public String getReqmProModels(){
        return this.reqmProModels;
    }
    public void setQuoteId(Integer quoteId){
        this.quoteId=quoteId;
    }
    public Integer getQuoteId(){
        return this.quoteId;
    }
    public void setReqmProUnit(String reqmProUnit){
        this.reqmProUnit=reqmProUnit;
    }
    public String getReqmProUnit(){
        return this.reqmProUnit;
    }
    public void setSkuNo(String skuNo){
        this.skuNo=skuNo;
    }
    public String getSkuNo(){
        return this.skuNo;
    }
    public void setReqmBrand(String reqmBrand){
        this.reqmBrand=reqmBrand;
    }
    public String getReqmBrand(){
        return this.reqmBrand;
    }
    public void setReqmQuantity(Integer reqmQuantity){
        this.reqmQuantity=reqmQuantity;
    }
    public Integer getReqmQuantity(){
        return this.reqmQuantity;
    }
    public void setReqmProName(String reqmProName){
        this.reqmProName=reqmProName;
    }
    public String getReqmProName(){
        return this.reqmProName;
    }
}