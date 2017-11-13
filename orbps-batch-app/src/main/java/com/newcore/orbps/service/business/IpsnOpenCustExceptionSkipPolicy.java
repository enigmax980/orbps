package com.newcore.orbps.service.business;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

/**
 * 定义开户跳过异常策略,指定异常直接跳过，没有次数限制
 * Created by liushuaifeng on 2017/3/20 0020.
 */
public class IpsnOpenCustExceptionSkipPolicy implements SkipPolicy {

    private Class<? extends Exception> exceptionClassToSkip;

    public IpsnOpenCustExceptionSkipPolicy(
            Class<? extends Exception> exceptionClassToSkip) {
        super();
        this.exceptionClassToSkip = exceptionClassToSkip;
    }

    @Override
    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
        return exceptionClassToSkip.isAssignableFrom(
                t.getClass()
        );
    }

}
