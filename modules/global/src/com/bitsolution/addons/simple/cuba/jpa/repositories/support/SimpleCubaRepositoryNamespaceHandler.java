package com.bitsolution.addons.simple.cuba.jpa.repositories.support;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.data.repository.config.RepositoryBeanDefinitionParser;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

public class SimpleCubaRepositoryNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        RepositoryConfigurationExtension extension = new SimpleCubaRepositoryConfigurationExtension();
        RepositoryBeanDefinitionParser repositoryBeanDefinitionParser = new RepositoryBeanDefinitionParser(extension);
        registerBeanDefinitionParser("repositories", repositoryBeanDefinitionParser);
    }
}
