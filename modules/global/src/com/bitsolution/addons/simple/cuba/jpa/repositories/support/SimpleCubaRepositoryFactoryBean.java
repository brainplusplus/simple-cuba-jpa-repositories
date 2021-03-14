package com.bitsolution.addons.simple.cuba.jpa.repositories.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

public class SimpleCubaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends RepositoryFactoryBeanSupport<T, S, ID> implements ApplicationContextAware {

    private ApplicationContext ctx;

    public SimpleCubaRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    @Override
    protected RepositoryFactorySupport createRepositoryFactory() {
        return new SimpleCubaRepositoryFactory();
    }
}
