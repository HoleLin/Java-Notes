package com.holelin.sundry.test.jvm;

class S<T> {
    private T value;
    public T getValue() {
        return value;
    }
    public void setValue(T value) {
        this.value = value;
    }
}
public class BridgeMethodsTest extends S<String> {

    @Override
    public void setValue(String value) {
        super.setValue(value);
    }
}
