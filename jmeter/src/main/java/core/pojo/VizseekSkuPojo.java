package core.pojo;
public class VizseekSkuPojo{
    private String sku=null;
    private Attribute attributes=null;
    private String score=null;
    
    public void setAttribute(Attribute attributes){
        this.attributes=attributes;
    }
    public Attribute getAttribute(){
        return this.attributes;
    }

    public void setSku(String sku){
        this.sku=sku;
    }
    public String getSku(){
        return this.sku;
    }

    public void setScore(String score){
        this.score=score;
    }
    public String getScore(){
        return this.score;
    }
}
class Attribute{
    private String manufacturerMaterialNo=null;
    private String brand=null;
    private String productName=null;

    public void setManufacturerMaterialNo(String manufactorerMaterialNo){
        this.manufacturerMaterialNo=manufactorerMaterialNo;
    }
    public String getManufacturerMaterialNo(){
        return this.manufacturerMaterialNo;
    }
    public void setBrand(String brand){
        this.brand=brand;
    }
    public String getBrand(){
        return this.brand;
    }
    public void setProductName(String productName){
        this.productName=productName;
    }
    public String getProductName(){
        return this.productName;
    }
}