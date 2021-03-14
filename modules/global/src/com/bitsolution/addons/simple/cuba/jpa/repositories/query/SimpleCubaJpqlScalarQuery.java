package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.FluentValueLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import java.lang.reflect.Method;

public class SimpleCubaJpqlScalarQuery extends SimpleCubaAbstractQuery {

    private static final Log log = LogFactory.getLog(SimpleCubaJpqlScalarQuery.class.getName());

    public SimpleCubaJpqlScalarQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, String query) {
        super(method, metadata, factory);
        jpql = generateQueryMetadata(method, query);
    }

    @Override
    protected Object doExecute(FluentLoader.ByQuery query, Object[] parameters) {
        FluentValueLoader<?> fluentValueLoader = getDataManager()
                .loadValue(jpql.getJpql(), metadata.getDomainType());

        //Cannot set parameters in map because we need implicit conversion disabled.
        for (int i = 0; i < parameters.length; i++){
            fluentValueLoader.parameter(jpql.getParameterNames().get(i), parameters[i], false);
        }

        return fluentValueLoader.one();
//        return query
////                .view(jpql.getView())
//                .one();
    }
}
