package com.testsite.reddittop.utils.connectivity;

import com.testsite.reddittop.App;
import com.testsite.reddittop.R;
import com.testsite.reddittop.utils.exceptions.NetworkErrorException;
import com.testsite.reddittop.utils.exceptions.NoConnectivityException;
import com.testsite.reddittop.utils.exceptions.UnauthorizedException;
import com.testsite.reddittop.utils.exceptions.UnexpectedErrorException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by paulf
 */
public class ErrorHandler {

    private final Throwable exception;

    public ErrorHandler(Throwable exception) {
        this.exception = exception;
    }

    public String getMessage() {
        return exception.getMessage();
    }

    public Throwable getException() {
        return exception;
    }

    public static class ErrorHandlingCallAdapterFactory extends CallAdapter.Factory {

        @Override
        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, final Retrofit retrofit) {
            if (getRawType(returnType) != Call.class) {
                return null;
            }
            final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
            return new ErrorHandlingCallAdapter<>(responseType, retrofit.callbackExecutor());
        }
    }

    private static class ErrorHandlingCallAdapter<R> implements CallAdapter<R, Call<R>> {
        private final Type responseType;
        private final Executor callbackExecutor;

        ErrorHandlingCallAdapter(Type responseType, Executor callbackExecutor) {
            this.responseType = responseType;
            this.callbackExecutor = callbackExecutor;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public Call<R> adapt(Call<R> call) {
            return new ErrorHandlingCall<>(call, callbackExecutor);
        }
    }

    static class ErrorHandlingCall<T> implements Call<T> {

        private final Call<T> delegate;
        private final Executor executor;

        ErrorHandlingCall(Call<T> delegate, Executor executor) {
            this.delegate = delegate;
            this.executor = executor;
        }

        @Override
        public void enqueue(final Callback<T> callback) {
            delegate.enqueue(new Callback<T>() {
                @Override
                public void onResponse(final Call<T> call, final Response<T> response) {
                    if (executor != null) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                int code = response.code();
                                if (code >= 200 && code < 300) {
                                    callback.onResponse(ErrorHandlingCall.this, response);

                                    // Exceptions
                                } else  if (code == 403) {
                                    callback.onFailure(ErrorHandlingCall.this,
                                            new UnauthorizedException());
                                } else if (code >= 500 && code < 600) {
                                    callback.onFailure(ErrorHandlingCall.this,
                                            new RuntimeException(App.getContext().getString(R.string.error_servererror)));
                                } else {
                                    callback.onFailure(ErrorHandlingCall.this,
                                            new UnexpectedErrorException(response.message()));
                                }
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<T> call, final Throwable t) {
                    if (executor != null) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if (t instanceof IOException && !(t instanceof NoConnectivityException)) {
                                    callback.onFailure(ErrorHandlingCall.this, new NetworkErrorException((IOException) t));
                                } else {
                                    callback.onFailure(ErrorHandlingCall.this, t);
                                }
                            }
                        });
                    }
                }
            });
        }

        @Override
        public Response<T> execute() throws IOException {
            return delegate.execute();
        }

        @Override
        public boolean isExecuted() {
            return delegate.isExecuted();
        }

        @Override
        public void cancel() {
            delegate.cancel();
        }

        @Override
        public boolean isCanceled() {
            return delegate.isCanceled();
        }

        @Override
        public Call<T> clone() {
            return delegate.clone();
        }

        @Override
        public Request request() {
            return delegate.request();
        }
    }

}
