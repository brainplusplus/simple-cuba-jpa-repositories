package com.bitsolution.addons.simple.cuba.jpa.repositories.support;

import com.bitsolution.addons.simple.cuba.jpa.repositories.query.SimpleCubaQueryLookupStrategy;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;

import java.util.Optional;

public class SimpleCubaRepositoryFactory extends RepositoryFactorySupport {

    @Override
    public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return new SimpleCubaEntityInformation(domainClass);
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {
        Object domainClass = metadata.getDomainType();
        return getTargetRepositoryViaReflection(metadata, domainClass);
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return SimpleCubaJpaRepositoryImpl.class;
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        return Optional.of(new SimpleCubaQueryLookupStrategy());
    }
}
