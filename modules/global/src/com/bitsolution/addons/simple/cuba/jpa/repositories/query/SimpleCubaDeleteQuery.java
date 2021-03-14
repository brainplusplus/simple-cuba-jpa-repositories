package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.haulmont.cuba.core.global.FluentLoader;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.parser.PartTree;

import java.lang.reflect.Method;
import java.util.Arrays;

public class SimpleCubaDeleteQuery extends SimpleCubaAbstractQuery {

    private static final Log log = LogFactory.getLog(SimpleCubaDeleteQuery.class.getName());

    //TODO need to implement batch delete in dataManager
    public SimpleCubaDeleteQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, PartTree qryTree) {
        super(method, metadata, factory);
        jpql = generateQueryMetadata(method, metadata, qryTree);
    }

    @Override
    public Object execute(Object[] parameters) {
        log.debug(String.format("Query: \"%s\" Parameters: \"%s\"", jpql, Arrays.toString(parameters)));
        throw new NotImplementedException("Batch delete is not supported yet");
    }

    @Override
    protected Object doExecute(FluentLoader.ByQuery query, Object[] parameters) {
        throw new NotImplementedException("Batch delete is not supported yet");
    }
}
