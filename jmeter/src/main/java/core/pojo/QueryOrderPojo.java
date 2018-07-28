package core.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class QueryOrderPojo{
    @Excel(name="序号",orderNum="0")
    private Integer index=null;
    @Excel(name="名称",orderNum="1")
    private String name=null;
    @Excel(name="规格/品牌",orderNum="2")
    private String type=null;
    @Excel(name="单位",orderNum="3")
    private String unit=null;
    @Excel(name="数量",orderNum="4")
    private Integer quantity=null;
    @Excel(name="结果",orderNum="5")
    private String comment="";
    @Excel(name="预期",orderNum="6")
    private String sku="";

    public QueryOrderPojo(){
        
    }
    public QueryOrderPojo(Integer index,String name,String type,String unit){
        this.index=index;
        this.name=name;
        this.type=type;
        this.unit=unit;
    }
    public String getSku(){
        return this.sku;
    }
    public void setSku(String sku){
        this.sku=sku;
    }
    public String getComment(){
        return this.comment;
    }
    public void setComment(String comment){
        this.comment=comment;
    }
    public Integer getQuantity(){
        return this.quantity;
    }
    public void setQuantity(Integer quantity){
        this.quantity=quantity;
    }
    public Integer getIndex(){
        return this.index;
    }
    public void setIndex(Integer index){
        this.index=index;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public void setType(String type){
        this.type=type;
    }
    public String getType(){
        return this.type;
    }
    public void setUnit(String unit){
        this.unit=unit;
    }
    public String getUnit(){
        return this.unit;
    }
}