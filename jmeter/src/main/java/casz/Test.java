package casz;

import java.io.Serializable;
import java.util.Random;

import org.apache.jmeter.assertions.AssertionResult;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class Test extends AbstractJavaSamplerClient implements Serializable{
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult result=new SampleResult();
        AssertionResult aresult=new AssertionResult();
        Integer x=0;
        result.sampleStart();
        Random rand=new Random();
        x=rand.nextInt(1000);
        try{Thread.sleep(200);}catch(Exception e){}
        result.sampleEnd();
        if(x%3==0){
            result.setSuccessful(false);
            aresult.setFailure(true);
            if(x>500){
                aresult.setFailureMessage("bigger");
            }else{
                aresult.setFailureMessage("smaller");
            }
        }else{
            result.setSuccessful(true);
        }
        result.addAssertionResult(aresult);
        return result;
    }
}