package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.haulmont.cuba.core.global.View;

import java.util.Collections;
import java.util.List;

public class SimpleJpqlMetadata {

    private String jpql;
    private List<String> parameterNames;
    private String view = View.LOCAL;

    public SimpleJpqlMetadata(String jpql, List<String> parameterNames) {
        this.jpql = jpql;
        this.parameterNames = parameterNames;
    }

    public String getJpql() {
        return jpql;
    }

    public List<String> getParameterNames() {
        return Collections.unmodifiableList(parameterNames);
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    @Override
    public String toString() {
        return "SimpleJpqlMetadata{" +
                "jpql='" + jpql + '\'' +
                ", parameterNames=" + parameterNames +
                ", view='" + view + '\'' +
                '}';
    }
}
