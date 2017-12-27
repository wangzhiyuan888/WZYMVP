package com.jess.arms.base.struct;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public abstract class FunctionWithParamOnly<Param> extends  BaseFunction {

    public FunctionWithParamOnly(String funNmae) {
        super(funNmae);
    }

    public abstract void function(Param param);
}
