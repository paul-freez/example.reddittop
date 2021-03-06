package com.testsite.reddittop.utils.connectivity;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by paulf
 */
public class EnumRetrofitConverterFactory extends Converter.Factory {

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Converter<?, String> converter = null;
        if (type instanceof Class && ((Class<?>) type).isEnum()) {
            converter = new Converter<Object, String>() {
                @Override
                public String convert(Object value) {
                    Enum e = Enum.class.cast(value);
                    if (e != null) {
                        return getSerializedNameValue(e);
                    }
                    return "";
                }
            };
        }
        return converter;
    }

    @Nullable
    private static <E extends Enum> String getSerializedNameValue(@NonNull E e) {
        String value = null;
        try {
            value = e.getClass().getField(e.name()).getAnnotation(SerializedName.class).value();
        } catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return value;
    }
}
