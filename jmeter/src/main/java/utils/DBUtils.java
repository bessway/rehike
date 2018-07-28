package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import core.pojo.QuoteDetailPojo;
import core.pojo.QuotePojo;
import batis.QuoteDetailMapper;
import batis.QuoteMapper;

public class DBUtils{
    private static SqlSession session=null;
    public static SqlSession getSession() throws Exception{
        if(session==null){
            File f=Utils.readFile("mybatis-configuration.xml",true);
            SqlSessionFactory sqlFacy=new SqlSessionFactoryBuilder().build(new FileInputStream(f));
            session=sqlFacy.openSession();
        }
        return session;
    }
    public static List<QuotePojo> getQuoteByStatus(Integer statusId,Integer limit) throws Exception{
        QuoteMapper map=getSession().getMapper(QuoteMapper.class);
        List<QuotePojo> quotes=map.getQuotes(statusId, limit);
        return quotes;
    }

    public static List<QuoteDetailPojo> getQuoteDetailInner(Integer limit,String create) throws Exception{
        QuoteDetailMapper map=getSession().getMapper(QuoteDetailMapper.class);
        List<QuoteDetailPojo> quoteDetails=map.getInnerQuoteDetail(limit,create);
        return quoteDetails;
    }
    public static List<QuoteDetailPojo> getQuoteDetailOutter(Integer limit,String create) throws Exception{
        QuoteDetailMapper map=getSession().getMapper(QuoteDetailMapper.class);
        List<QuoteDetailPojo> quoteDetails=map.getOutterQuoteDetail(limit,create);
        return quoteDetails;
    }
}