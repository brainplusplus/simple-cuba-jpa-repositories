package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.bitsolution.addons.simple.cuba.jpa.repositories.config.SimpleJpqlQuery;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.FluentValueLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;

import java.lang.reflect.Method;
import java.util.List;

public class SimpleCubaJpqlPageQuery extends SimpleCubaAbstractQuery {

    private static final Log log = LogFactory.getLog(SimpleCubaJpqlPageQuery.class.getName());
    private String countQuery = "";

    public SimpleCubaJpqlPageQuery(SimpleJpqlQuery jpqlQuery, Method method, RepositoryMetadata metadata, ProjectionFactory factory, String query) {
        super(method, metadata, factory);
        countQuery = jpqlQuery.countQuery();
        jpql = generateQueryMetadata(method, query);
    }

    @Override
    protected Object doExecute(FluentLoader.ByQuery query, Object[] parameters) {
        boolean hasPageable = false;
        Pageable pageable = null;
        for(Object param:parameters){
            if(param instanceof Pageable){
                hasPageable = true;
                pageable = (Pageable) param;
            }
        }


        FluentValueLoader<?> fluentValueLoader = getDataManager()
                .loadValue(countQuery, metadata.getDomainType());

        //Cannot set parameters in map because we need implicit conversion disabled.
        for (int i = 0; i < parameters.length; i++){
            if(!(parameters[i] instanceof Pageable)) {
                fluentValueLoader.parameter(jpql.getParameterNames().get(i), parameters[i], false);
            }
        }

        Long total = (Long) fluentValueLoader.one();

        List list = query
                .view(jpql.getView())
                .list();

        return new PageImpl<>(list, pageable, total);
    }
}
