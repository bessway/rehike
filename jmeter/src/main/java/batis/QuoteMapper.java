package batis;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import core.pojo.QuotePojo;

public interface QuoteMapper{
    @Select("select * from quote where quotestatus=#{statusId} order by createdon limit #{limit}")
    public List<QuotePojo> getQuotes(Integer statusId,Integer limit);
}