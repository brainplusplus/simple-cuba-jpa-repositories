package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.FluentValueLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SimpleCubaScalarQuery extends SimpleCubaAbstractQuery {

    private static final Log log = LogFactory.getLog(SimpleCubaScalarQuery.class.getName());

    public SimpleCubaScalarQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, PartTree qryTree) {
        super(method, metadata, factory);
        jpql = generateQueryMetadata(method, metadata, qryTree);
    }

    @Override
    public Object execute(Object[] parameters) {
        log.debug(String.format("Query: \"%s\" Parameters: \"%s\"", jpql, Arrays.toString(parameters)));
        Assert.isTrue(parameters.length == jpql.getParameterNames().size(),
                String.format("Parameters list sizes in JPQL and in method are not equal: Method: %s JPQL: %s", Arrays.toString(parameters), jpql.getParameterNames()));
        //Issues with generic types in DataManager, so need to cast it forcefully.
        if (!Entity.class.isAssignableFrom(metadata.getDomainType())){
            throw new IllegalStateException("CUBA can process only entities of class com.haulmont.cuba.core.entity.Entity");
        }
        DataManager dataManager = getDataManager();
        FluentValueLoader<?> fluentValueLoader = dataManager
                .loadValue(jpql.getJpql(), metadata.getDomainType());

        //Cannot set parameters in map because we need implicit conversion disabled.
        for (int i = 0; i < parameters.length; i++){
            fluentValueLoader.parameter(jpql.getParameterNames().get(i), parameters[i], false);
        }

        return fluentValueLoader.one();
    }
    @Override
    protected Object doExecute(FluentLoader.ByQuery query, Object[] parameters) {
        throw new IllegalStateException("Scalar Query executes in a different way");
    }
}
