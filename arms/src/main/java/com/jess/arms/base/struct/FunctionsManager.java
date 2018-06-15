package com.jess.arms.base.struct;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by wangzhiyuan on 2017/12/25
 */

public class FunctionsManager {
    private static FunctionsManager ourInstance;

    public static FunctionsManager getInstance() {
        if(null == ourInstance){
            return ourInstance = new FunctionsManager();
        }else {
            return ourInstance;

        }
    }

    private FunctionsManager() {
        mFunctionNoParamNoResult = new HashMap<>();
        mFunctionWithParamAndResult = new HashMap<>();
        mFunctionWithParamOnly = new HashMap<>();
        mFunctionWithResltOnly = new HashMap<>();
    }

    private static HashMap<String, FunctionNoParamNoResult> mFunctionNoParamNoResult;
    private static HashMap<String, FunctionWithParamAndResult> mFunctionWithParamAndResult;
    private static HashMap<String, FunctionWithParamOnly> mFunctionWithParamOnly;
    private static HashMap<String, FunctionWithResltOnly> mFunctionWithResltOnly;

    public FunctionsManager addFunction(FunctionNoParamNoResult function){
        mFunctionNoParamNoResult.put(function.mFuctionNmae, function);
        return this;
    }

    public void invokeFunc(String funcName) throws FunctionException {
        if(!TextUtils.isEmpty(funcName) && mFunctionNoParamNoResult != null){
            FunctionNoParamNoResult f = mFunctionNoParamNoResult.get(funcName);
            if(f != null){
                f.function();
            }else {
                throw new FunctionException("Has no this function:"+funcName);
            }
        }
    }

    public FunctionsManager addFunction(FunctionWithParamAndResult function){
        mFunctionWithParamAndResult.put(function.mFuctionNmae, function);
        return this;
    }

    public <Result,Param> Result invokeFunc(String funcName, Param param, Class<Result> c) throws FunctionException {
        if(TextUtils.isEmpty(funcName)){
            return null;
        }
        if(mFunctionWithParamAndResult != null){
            FunctionWithParamAndResult f = mFunctionWithParamAndResult.get(funcName);
            if(f != null){
                if(c != null){
                    return c.cast(f.function(param));
                }else {
                    return (Result)f.function(param);
                }

            }else {
                throw new FunctionException("Has no this function:"+funcName);
            }

        }
        return  null;
    }

    public FunctionsManager addFunction(FunctionWithParamOnly function){
        mFunctionWithParamOnly.put(function.mFuctionNmae, function);
        return this;
    }

    public <Pararm> void invokeFunc(String funcName, Pararm data) throws FunctionException {
        if(TextUtils.isEmpty(funcName)){
            return;
        }
        if(mFunctionWithParamOnly != null){
            FunctionWithParamOnly f = mFunctionWithParamOnly.get(funcName);
            if(f != null){
                f.function(data);

            }else {
                throw new FunctionException("Has no this function:"+funcName);
            }

        }
    }

    public FunctionsManager addFunction(FunctionWithResltOnly function){
        mFunctionWithResltOnly.put(function.mFuctionNmae, function);
        return this;
    }

    public <Result> Result invokeFunc(String funcName, Class<Result> c) throws FunctionException {
        if(TextUtils.isEmpty(funcName)){
            return null;
        }
        if(mFunctionWithResltOnly != null){
            FunctionWithResltOnly f = mFunctionWithResltOnly.get(funcName);
            if(f != null){
                if(c != null){
                    return c.cast(f.function());
                }else {
                    return (Result)f.function();
                }

            }else {
                throw new FunctionException("Has no this function:"+funcName);
            }

        }
        return  null;
    }
}
