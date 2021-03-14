package com.bitsolution.addons.simple.cuba.jpa.repositories.config;

import com.bitsolution.addons.simple.cuba.jpa.repositories.support.SimpleCubaRepositoryConfigurationExtension;
import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

public class SimpleCubaRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableSimpleCubaRepositories.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new SimpleCubaRepositoryConfigurationExtension();
    }
}
