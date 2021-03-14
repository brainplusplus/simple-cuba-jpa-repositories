package com.bitsolution.addons.simple.cuba.jpa.repositories.config;

import com.bitsolution.addons.simple.cuba.jpa.repositories.support.SimpleCubaJpaRepositoryImpl;
import com.bitsolution.addons.simple.cuba.jpa.repositories.support.SimpleCubaRepositoryFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.repository.query.QueryLookupStrategy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SimpleCubaRepositoriesRegistrar.class)
public @interface EnableSimpleCubaRepositories {

    /**
     * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
     * {@code @EnableSimpleCubaRepositories("org.my.pkg")} instead of
     * {@code @EnableSimpleCubaRepositories(basePackages="org.my.pkg")}.
     * @return list of packages that should be scanned
     */
    String[] value() default {};

    /**
     * Base packages to scan for annotated components. {@link #value()} is an alias for (and mutually exclusive with)
     * this attribute. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
     * @return list of packages that should be scanned
     */
    String[] basePackages() default {};

    /**
     * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components.
     * The package of each class specified will be scanned. Consider creating a special no-op marker class or interface
     * in each package that serves no purpose other than being referenced by this attribute.
     * @return list of packages that should be scanned
     */
    Class<?>[] basePackageClasses() default {};

    /**
     * Specifies which types are not eligible for component scanning.
     * @return filters that exclude some classes from scanning
     */
    Filter[] excludeFilters() default {};

    /**
     * Specifies which types are eligible for component scanning. Further narrows the set of candidate components from
     * everything in {@link #basePackages()} to everything in the base packages that matches the given filter or
     * filters.
     * @return filters that include classes into scanning
     */
    Filter[] includeFilters() default {};

    /**
     * Returns the postfix to be used when looking up custom repository implementations. Defaults to {@literal Impl}. So
     * for a repository named {@code PersonRepository} the corresponding implementation class will be looked up scanning
     * for {@code PersonRepositoryImpl}.
     *
     * @return Postfix to be used when looking up custom repository implementations.
     */
    String repositoryImplementationPostfix() default "Impl";

    /**
     * Configures the location of where to find the Spring Data named queries properties file.
     *
     * @return Location of where to find the Spring Data named queries properties file.
     */
    String namedQueriesLocation() default "";

    /**
     * Returns the key of the {@link QueryLookupStrategy} to be used for lookup queries for com.haulmont.addons.cuba.jpa..repository.query methods. Defaults to
     * {@link QueryLookupStrategy.Key#CREATE_IF_NOT_FOUND}.
     *
     * @return Key of the {@link QueryLookupStrategy} to be used for lookup queries for com.haulmont.addons.cuba.jpa..repository.query methods.
     */
    QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

    /**
     * Returns the {@link FactoryBean} class to be used for each repository instance. Defaults to
     * {@link SimpleCubaRepositoryFactoryBean}.
     *
     * @return {@link FactoryBean} class to be used for each repository instance.
     */
    Class<?> repositoryFactoryBeanClass() default SimpleCubaRepositoryFactoryBean.class;

    /**
     * Configure the repository base class to be used to create repository proxies for this particular configuration.
     *
     * @return Repository base class to be used to create repository proxies for this particular configuration.
     */
    Class<?> repositoryBaseClass() default SimpleCubaJpaRepositoryImpl.class;

    /**
     * Configures whether nested repository-interfaces (e.g. defined as inner classes) should be discovered by the
     * repositories infrastructure.
     * @return Flag that indicates if we should consider nested repositories during scan
     */
    boolean considerNestedRepositories() default false;


}
