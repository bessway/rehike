package core.pojo;
public class DaguanSkuPojo{
    private String sku=null;
    private String manufacturerMaterialNo=null;
    private String brand=null;
    private String productName=null;
    private String score=null;
    private String coreSpecification=null;

    public void setSku(String sku){
        this.sku=sku;
    }
    public String getSku(){
        return this.sku;
    }
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
    public void setScore(String score){
        this.score=score;
    }
    public String getScore(){
        return this.score;
    }
    public void setCoreSpecification(String coreSpecification){
        this.coreSpecification=coreSpecification;
    }
    public String getCoreSpecification(){
        return this.coreSpecification;
    }
}