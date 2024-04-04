package com.confusedparrotfish.difusement.util;

import java.util.ArrayList;
import java.util.List;

public class util {
    public static class dual<A,B> {
        public A a;
        public B b;
        public dual(A a, B b) {
            this.a = a;
            this.b = b;
        }
        public dual<A,B> a(A a) {
            this.a = a;
            return this;
        }
        public dual<A,B> b(B b) {
            this.b = b;
            return this;
        }
    }

    public static class option<T> {
        @SuppressWarnings("rawtypes")
        public static option none() {
            return new option<>(null);
        }
        public static <E> option<E> some(E t) {
            return new option<E>(t);
        }
        private T internal;
        private option(T p) {
            internal = p;
        }
        public boolean present() {
            return internal != null;
        }
        public T get() {
            return internal;
        }
    }

    public static class or<A,B> {
        public static <E> or<?,E> a(E t) {
            return new or<Object,E>(null,t);
        }
        public static <E> or<E,?> b(E t) {
            return new or<E,Object>(t,null);
        }
        private final A internal_a;
        private final B internal_b; 
        private or(A a, B b) {
            internal_a = a;
            internal_b = b;
        }
        // public or(A a) {
        //     internal_a = a;
        //     internal_b = null;
        // }
        // public or(B b) {
        //     internal_a = null;
        //     internal_b = b;
        // }
        public boolean present_a() {
            return internal_a != null;
        }
        public boolean present_b() {
            return internal_b != null;
        }
        public A get_a() {
            return internal_a;
        }
        public B get_b() {
            return internal_b;
        }
    }

    public static <E> dual<List<E>, List<E>> sublist(List<E> list, int num) {
        ArrayList<E> a = new ArrayList<>();
        ArrayList<E> b = new ArrayList<>();
        if (list.size() < num) throw new Error("not enought elements in list");
        for (int i = 0; i < list.size(); i++) {
            if (i < num) {
                a.add(list.get(i));
            } else {
                b.add(list.get(i));
            }
        }
        return new dual<List<E>,List<E>>(a, b);
    }
}
