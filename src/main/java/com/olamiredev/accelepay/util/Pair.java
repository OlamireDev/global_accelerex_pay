package com.olamiredev.accelepay.util;

import lombok.AllArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
public class Pair<T, V> {

    @Nullable
    T first;

    @Nullable
    V second;

    public T first(){
        return this.first;
    }

    public V second(){
        return this.second;
    }

    public boolean hasFirst() { return this.first != null; }

    public boolean hasSecond(){ return this.second != null; }

    public static <T, V> Pair<T, V> of(T t, V v){
        return new Pair<>(t, v);
    }

    public static <T, V> Pair<T, V> ofSecond(V v){
        return Pair.of(null, v);
    }

    public static <T, V> Pair<T, V> ofFirst(T t){
        return Pair.of(t, null);
    }

}
