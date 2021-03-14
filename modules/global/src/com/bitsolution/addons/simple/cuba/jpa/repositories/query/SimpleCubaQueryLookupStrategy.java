package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.bitsolution.addons.simple.cuba.jpa.repositories.config.SimpleJpqlQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.PartTree;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class SimpleCubaQueryLookupStrategy implements QueryLookupStrategy {

    private static final Log log = LogFactory.getLog(SimpleCubaQueryLookupStrategy.class.getName());

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {

        log.debug(String.format("Resolving query for %s", method));

        SimpleJpqlQuery simpleJpqlQuery = method.getDeclaredAnnotation(SimpleJpqlQuery.class);
        if (simpleJpqlQuery != null) {
            //System.out.println("SimpleJpqlQuery");
            String qryString = simpleJpqlQuery.value();
            log.debug(String.format("Query string is: %s", qryString));
            if(method.getReturnType().getName().equals(List.class.getTypeName())){
                //System.out.println("SimpleCubaJpqlListQuery");
                return new SimpleCubaJpqlListQuery(method, metadata, factory, qryString);
            }else if(method.getReturnType().getName().equals(Page.class.getTypeName())){
                //System.out.println("SimpleCubaJpqlPageQuery");
                return new SimpleCubaJpqlPageQuery(simpleJpqlQuery,method, metadata, factory, qryString);
            }else if(method.getReturnType().getName().equals(Long.class.getTypeName())){
                //System.out.println("SimpleCubaJpqlScalarQuery");
                return new SimpleCubaJpqlScalarQuery(method, metadata, factory, qryString);
            }else{
                //System.out.println("SimpleCubaJpqlOneQuery");
                return new SimpleCubaJpqlOneQuery(method, metadata, factory, qryString);
            }
        } else {
            //System.out.println("PartTree");
            PartTree qryTree = new PartTree(method.getName(), metadata.getDomainType());
            if (qryTree.isDelete()) {
                //System.out.println("SimpleCubaDeleteQuery");
                return new SimpleCubaDeleteQuery(method, metadata, factory, qryTree);
            } else if (qryTree.isCountProjection()){
                //System.out.println("SimpleCubaScalarQuery");
                return new SimpleCubaScalarQuery(method, metadata, factory,  qryTree);
            } else {
                if(method.getReturnType().getName().equals(List.class.getTypeName())) {
                    //System.out.println("SimpleCubaListQuery");
                    return new SimpleCubaListQuery(method, metadata, factory, qryTree);
                }else{
                    //System.out.println("SimpleCubaListQuery");
                    return new SimpleCubaOneQuery(method, metadata, factory, qryTree);
                }
            }
        }
    }
}
