package com.jess.arms.base.struct;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public abstract class FunctionWithParamAndResult<Result, Param> extends BaseFunction {

    public FunctionWithParamAndResult(String funNmae) {
        super(funNmae);
    }

    public abstract Result function(Param param);
}
