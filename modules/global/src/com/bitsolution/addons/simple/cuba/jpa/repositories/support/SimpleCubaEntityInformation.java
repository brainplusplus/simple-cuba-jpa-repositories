package com.bitsolution.addons.simple.cuba.jpa.repositories.support;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityStates;
import org.springframework.data.repository.core.support.AbstractEntityInformation;

import java.io.Serializable;

public class SimpleCubaEntityInformation<T, ID extends Serializable> extends AbstractEntityInformation<T, ID> {

    public SimpleCubaEntityInformation(Class<T> domainClass) {
        super(domainClass);
    }

    @Override
    public ID getId(T object) {
        if (object == null) {
            return null;
        }
        if (Entity.class.isAssignableFrom(getJavaType())) {
            Entity entity = (Entity) object;
            return (ID) entity.getId();
        } else {
            throw new IllegalStateException("Wrong entity type");
        }
    }

    @Override
    public Class<ID> getIdType() {
        try {
            return (Class<ID>) getJavaType().getMethod("getId").getReturnType();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Wrong entity type");
        }
    }

    @Override
    public boolean isNew(T entity) {
        EntityStates entityStates = AppBeans.get(EntityStates.class);
        return entityStates.isNew(entity);
    }
}
