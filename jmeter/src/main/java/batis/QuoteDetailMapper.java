package batis;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import core.pojo.QuoteDetailPojo;

public interface QuoteDetailMapper{
    @Select("select reqmproname,reqmprobrand,reqmpromodels,reqmprounit,reqmquantity,quoteid,skuno,createdon from quotedetail where skuno is not null and skuno!='' and createdon<#{create} and reqmproname!='' order by createdon desc limit #{limit}")
    public List<QuoteDetailPojo> getInnerQuoteDetail(Integer limit,String create);

    @Select("select reqmproname,reqmprobrand,reqmpromodels,reqmprounit,reqmquantity,quoteid,skuno,createdon from quotedetail where (skuno is null or skuno='') and createdon<#{create} and reqmproname!='' order by createdon desc limit #{limit}")
    public List<QuoteDetailPojo> getOutterQuoteDetail(Integer limit,String create);
}