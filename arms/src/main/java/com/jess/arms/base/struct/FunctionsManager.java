package com.jess.arms.base.struct;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/25 0025.
 */

public class FunctionsManager {
    private static final FunctionsManager ourInstance = new FunctionsManager();

    public static FunctionsManager getInstance() {
        return ourInstance;
    }

    private FunctionsManager() {
        mFunctionNoParamNoResult = new HashMap<>();
        mFunctionWithParamAndResult = new HashMap<>();
        mFunctionWithParamOnly = new HashMap<>();
        mFunctionWithResltOnly = new HashMap<>();
    }

    private HashMap<String, FunctionNoParamNoResult> mFunctionNoParamNoResult;
    private HashMap<String, FunctionWithParamAndResult> mFunctionWithParamAndResult;
    private HashMap<String, FunctionWithParamOnly> mFunctionWithParamOnly;
    private HashMap<String, FunctionWithResltOnly> mFunctionWithResltOnly;

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
