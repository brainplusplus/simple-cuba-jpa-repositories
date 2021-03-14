package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.haulmont.cuba.core.global.FluentLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.parser.PartTree;

import java.lang.reflect.Method;

public class SimpleCubaOneQuery extends SimpleCubaAbstractQuery {

    private static final Log log = LogFactory.getLog(SimpleCubaOneQuery.class.getName());

    public SimpleCubaOneQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, PartTree qryTree) {
        super(method, metadata, factory);
        jpql = generateQueryMetadata(method, metadata, qryTree);
    }

    @Override
    protected Object doExecute(FluentLoader.ByQuery query, Object[] parameters) {
        return query
                .view(jpql.getView())
                .one();
    }
}
