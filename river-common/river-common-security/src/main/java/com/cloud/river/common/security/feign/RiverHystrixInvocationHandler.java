package com.cloud.river.common.security.feign;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommand.Setter;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader;
import feign.InvocationHandlerFactory.MethodHandler;
import com.sun.istack.Nullable;
import feign.InvocationHandlerFactory;
import feign.Target;
import feign.hystrix.FallbackFactory;
import feign.hystrix.SetterFactory;
import rx.Completable;
import rx.Observable;
import rx.Single;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Watchable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static feign.Util.checkNotNull;
import static feign.Util.removeValues;

/**
 * @program: RiverCloud
 * @description: 降级注入
 * @author: River
 * @create: 2019-03-29 14:24
 **/
public final class RiverHystrixInvocationHandler implements InvocationHandler {
    private static final String EQUALS="equals";
    private static final String HASH_CODE="hashCode";
    private static final String TO_STRING="toString";
    private final Target<?> target;
    private final Map<Method, MethodHandler> dispatch;
    @Nullable
    private final FallbackFactory<?> fallbackFactory;
    private final Map<Method, Method> fallbackMethodMap;
    private final Map<Method, Setter> setterMethodMap;

    public RiverHystrixInvocationHandler(Target<?> target,
                                         Map<Method, MethodHandler> dispatch,
                                         SetterFactory setterFactory,
                                         FallbackFactory<?> fallbackFactory){
        this.target = checkNotNull(target, "target");
        this.dispatch = checkNotNull(dispatch, "dispatch");
        this.fallbackFactory = fallbackFactory;
        this.fallbackMethodMap = toFallbackMethod(dispatch);
        this.setterMethodMap = toSetter(setterFactory, target, dispatch.keySet());
    }

    private static Map<Method, Method> toFallbackMethod(Map<Method, MethodHandler> dispatch){
        Map<Method, Method> result= new LinkedHashMap<>(dispatch.size());
        for(Method method: dispatch.keySet()){
            method.setAccessible(true);
            result.put(method,method);
        }
        return result;
    }

    private static Map<Method, Setter> toSetter(SetterFactory setterFactory,
                                                Target<?> target, Set<Method> methods){
        Map<Method, Setter> result = new LinkedHashMap<>(methods.size());

        for(Method method: methods){
            method.setAccessible(true);
            Setter setter = setterFactory.create(target, method);
            result.put(method, setter);
        }
        return result;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable{
        if(EQUALS.equals(method.getName())){
            try {
                Object otherHander = args.length>0 && args[0]!=null
                        ? Proxy.getInvocationHandler(args[0]) : null;

                return equals(otherHander);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        else if(HASH_CODE.equals(method.getName())){
            return hashCode();
        }
        else if(TO_STRING.equals(method.getName())){
            return toString();
        }

        HystrixCommand<Object> hystrixCommand = new HystrixCommand<Object>(setterMethodMap.get(method)){
            @Override
            protected Object run() throws Exception{
                try {
                    return dispatch.get(method).invoke(args);
                } catch (Exception e){
                    throw e;
                } catch (Throwable t) {
                    throw (Error) t;
                }
            }

            @Override
            @Nullable
            protected Object getFallback(){
                Object fallback;

                try {
                    if(fallbackFactory == null){
                        fallback = RiverFeignFallbackFactory.INSTANCE.create(target.type(), getExecutionException());
                    }
                    else{
                        fallback = fallbackFactory.create(getExecutionException());
                    }
                    Object result = fallbackMethodMap.get(method).invoke(fallback, args);
                    if (isReturnsHystrixCommand(method)) {
                        return ((HystrixCommand)result).execute();
                    } else if (isReturnsObservable(method)) {
                        // Create a cold Observable
                        return ((Observable) result).toBlocking().first();
                    } else if (isReturnsSinglable(method)) {
                        // Create a cold Observable as a Single
                        return ((Single)result).toObservable().toBlocking().first();
                    } else if (isReturnsCompletable(method)) {
                        ((Completable)result).await();
                        return null;
                    } else {
                        return result;
                    }
                } catch (IllegalAccessException e) {
                    throw new AssertionError(e);
                } catch (InvocationTargetException e){
                    throw new AssertionError(e.getCause());
                }
            }
        };

        if (isReturnsHystrixCommand(method)) {
            return hystrixCommand;
        } else if (isReturnsObservable(method)) {
            // Create a cold Observable
            return hystrixCommand.toObservable();
        } else if (isReturnsSinglable(method)) {
            // Create a cold Observable as a Single
            return hystrixCommand.toObservable().toSingle();
        } else if (isReturnsCompletable(method)) {
            return hystrixCommand.toObservable().toCompletable();
        }
        return hystrixCommand.execute();
    }

    private boolean isReturnsCompletable(Method method){
        return Comparable.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsHystrixCommand(Method method){
        return HystrixCommand.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsObservable(Method method){
        return Observable.class.isAssignableFrom(method.getReturnType());
    }

    private boolean isReturnsSinglable(Method method){
        return Single.class.isAssignableFrom(method.getReturnType());
    }

    @Override
    public int hashCode() {
        return target.hashCode();
    }

    @Override
    public String toString() {
        return target.toString();
    }
}
