package com.bitsolution.addons.simple.cuba.jpa.repositories.support;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.FluentLoader;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.bitsolution.addons.simple.cuba.jpa.repositories.config.SimpleCubaJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public class SimpleCubaJpaRepositoryImpl<T extends Entity<ID>, ID extends Serializable> implements SimpleCubaJpaRepository<T, ID> {

    private String defaultViewName = View.LOCAL;

    private Class<T> domainClass;

    public SimpleCubaJpaRepositoryImpl(Class<T> domainClass) {
        this.domainClass = domainClass;
    }

    protected DataManager getDataManager() {
        return AppBeans.get(DataManager.class);
    }

    @Override
    public T findOne(ID id, String view) {
        return getDataManager().load(domainClass).id(id).view(defaultViewName).one();
    }

    @Override
    public Iterable<T> findAll(String view) {
        return getDataManager().load(domainClass).view(view).list();
    }

    @Override
    public Iterable<T> findAll(Iterable<ID> ids, String view) {//TODO implement find by ID list
        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        }
        FluentLoader<T, ID> loader = getDataManager().load(domainClass).view(view);
        List<T> results = new ArrayList<T>();
        for (ID id : ids) {
            loader.id(id).optional().ifPresent(results::add);
        }
        return results;
    }

    @Override
    public <S extends T> S save(S entity) {
        return getDataManager().commit(entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();
        for (S entity : entities) {
            savedEntities.add(save(entity));
        }
        return savedEntities;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(getDataManager().load(domainClass).id(id).view(defaultViewName).one());
    }

    @Override
    public boolean existsById(ID id) {
        return getDataManager().load(domainClass).id(id).optional().isPresent();
    }

    @Override
    public Iterable<T> findAll() {
        return getDataManager().load(domainClass).view(defaultViewName).list();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return findAll(ids, defaultViewName);
    }

    @Override
    public long count() {
        return getDataManager().getCount(LoadContext.create(domainClass));
    }

    @Override
    public void deleteById(ID id) { //TODO Need to add removal by entity ID to DataManager
        DataManager dataManager = getDataManager();
        T entity = dataManager.load(domainClass).id(id).view(defaultViewName).one();
        dataManager.remove(entity);
    }

    @Override
    public void delete(T entity) {
        getDataManager().remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        entities.forEach(getDataManager()::remove);
    }

    @Override
    public void deleteAll() {//TODO implement total delete by entity class in DataManager
        Iterable<T> entities = getDataManager().load(domainClass).list();
        entities.forEach(getDataManager()::remove);
    }

    @Override
    public String getDefaultViewName() {
        return defaultViewName;
    }

    @Override
    public void setDefaultViewName(String defaultViewName) {
        this.defaultViewName = defaultViewName;
    }

    public Class<T> getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(Class<T> domainClass) {
        this.domainClass = domainClass;
    }
}
