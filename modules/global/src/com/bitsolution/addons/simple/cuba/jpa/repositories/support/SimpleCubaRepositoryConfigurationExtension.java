package com.bitsolution.addons.simple.cuba.jpa.repositories.support;

import com.bitsolution.addons.simple.cuba.jpa.repositories.config.SimpleCubaJpaRepository;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

import java.util.Collection;
import java.util.Collections;

public class SimpleCubaRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getModuleName() {
        return "CUBA Platform";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getModulePrefix() {
        return "CUBA";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRepositoryFactoryBeanClassName() {
        return SimpleCubaRepositoryFactoryBean.class.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.singleton(SimpleCubaJpaRepository.class);
    }
}
