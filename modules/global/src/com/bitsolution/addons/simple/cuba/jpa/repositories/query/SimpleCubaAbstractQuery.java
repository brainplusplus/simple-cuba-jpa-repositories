package com.bitsolution.addons.simple.cuba.jpa.repositories.query;

import com.bitsolution.addons.simple.cuba.jpa.repositories.config.SimpleCubaView;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Query implementation for CUBA platform. If you need different types of queries, you can either extend this class or implement parent interface.
 * @see SimpleCubaQueryLookupStrategy is responsible for generating Query implementations based on interface method names that will be executed by the CUBA platform.
 * @see RepositoryQuery
 */
public abstract class SimpleCubaAbstractQuery implements RepositoryQuery {

    private static final Log log = LogFactory.getLog(SimpleCubaAbstractQuery.class.getName());

    protected final Method method;
    protected final RepositoryMetadata metadata;
    protected final ProjectionFactory factory;
    protected SimpleJpqlMetadata jpql;

    public SimpleCubaAbstractQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory) {
        this.method = method;
        this.metadata = metadata;
        this.factory = factory;
    }

    @Override
    public QueryMethod getQueryMethod() {
        return new QueryMethod(method, metadata, factory);
    }

    public DataManager getDataManager(){
        return AppBeans.get(DataManager.class);
    }

    @Override
    public Object execute(Object[] parameters) {
        log.debug(String.format("Query: \"%s\" Parameters: \"%s\"", jpql, Arrays.toString(parameters)));
        //System.out.println(String.format("Query: \"%s\" Parameters: \"%s\"", jpql, Arrays.toString(parameters)));
        boolean hasPageable = false;
        Pageable pageable = null;
        for(Object param:parameters){
            if(param instanceof Pageable){
                hasPageable = true;
                pageable = (Pageable) param;
            }
        }
        Assert.isTrue(parameters.length == jpql.getParameterNames().size() || (hasPageable && (parameters.length-1 == jpql.getParameterNames().size())),
                String.format("Parameters list sizes in JPQL and in method are not equal: Method: %s JPQL: %s", Arrays.toString(parameters), jpql.getParameterNames()));
        //Issues with generic types in DataManager, so need to cast it forcefully.
        if (!Entity.class.isAssignableFrom(metadata.getDomainType())){
            throw new IllegalStateException("CUBA can process only entities of class com.haulmont.cuba.core.entity.Entity");
        }
        DataManager dataManager = getDataManager();
        FluentLoader.ByQuery query = dataManager
                .load((Class<? extends Entity>) metadata.getDomainType())
                .query(jpql.getJpql());
        //Cannot set parameters in map because we need implicit conversion disabled.
        for (int i = 0; i < parameters.length; i++){
            if(!(parameters[i] instanceof Pageable)) {
                query.parameter(jpql.getParameterNames().get(i), parameters[i], false);
            }
        }
        if(hasPageable)
            query.firstResult((int)pageable.getOffset()).maxResults(pageable.getPageSize());
        return doExecute(query, parameters);
    }

    /**
     * Method to generate JPQL Metadata based on com.haulmont.addons.cuba.jpa.repositories.query string.
     * @param method interface com.haulmont.addons.cuba.jpa.repositories.query method metadata.
     * @param query com.haulmont.addons.cuba.jpa.repositories.query string.
     * @return Metadata that will be used for com.haulmont.addons.cuba.jpa.repositories.query execution.
     */
    protected SimpleJpqlMetadata generateQueryMetadata(Method method, String query) {
        log.debug(String.format("Generating query metadata for %s", method));
        List<String> parameters = new ArrayList<>();
        Matcher m = Pattern.compile("(:[a-zA-Z]+)").matcher(query);
        while (m.find()){
            //extracting name from com.haulmont.addons.cuba.jpa.repositories.query removing colon
            String name = query.substring(m.start(), m.end()).replaceAll(":", "");
            parameters.add(name);
        }
        SimpleJpqlMetadata simpleJpqlMetadata = new SimpleJpqlMetadata(query, parameters);
        setView(method, simpleJpqlMetadata);
        return simpleJpqlMetadata;
    }

    /**
     * Method to generate JPQL metadata based on com.haulmont.addons.cuba.jpa.repositories.query method reflection metadata.
     * @param method interface com.haulmont.addons.cuba.jpa.repositories.query method metadata.
     * @param metadata JPA repository metadata.
     * @param qryTree parsed com.haulmont.addons.cuba.jpa.repositories.query method name that contains all information for JPQL generation.
     * @return Metadata that will be used for com.haulmont.addons.cuba.jpa.repositories.query execution.
     */
    protected SimpleJpqlMetadata generateQueryMetadata(Method method, RepositoryMetadata metadata, PartTree qryTree) {
        log.debug(String.format("Generating query metadata for %s", method));
        SimpleJpqlMetadata simpleJpqlMetadata = SimpleJpqlQueryGenerator.generateJpqlMetadata(metadata, qryTree);
        setView(method, simpleJpqlMetadata);
        return simpleJpqlMetadata;
    }

    private void setView(Method method, SimpleJpqlMetadata simpleJpqlMetadata) {
        SimpleCubaView viewAnnotation = method.getDeclaredAnnotation(SimpleCubaView.class);
        if (viewAnnotation != null) {
            simpleJpqlMetadata.setView(viewAnnotation.value());
        }
    }

    /**
     * Method that performes com.haulmont.addons.cuba.jpa.repositories.query execution.
     * @param query JPQL CUBA com.haulmont.addons.cuba.jpa.repositories.query that should be executed.
     * @param parameters parameters values.
     * @return com.haulmont.addons.cuba.jpa.repositories.query execution result.
     */
    protected abstract Object doExecute(FluentLoader.ByQuery query, Object[] parameters);
}
