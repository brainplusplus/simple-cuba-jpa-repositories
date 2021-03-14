# Spring Data JPA Usage

This component adds Spring Data JPA repositories to CUBA platform. This is an experimental feature.

Before usage, install it to the local repository:
* Create database and build the artifact `gradlew startDb createDb build` - you need database for tests.
* Install artifact to a local repository `gradlew publishToMavenLocal`

Add the module to build file as an additional component in your `build.gradle`:
add maven url from addon's bintray
```
repositories {
        .......
        maven {
            url 'https://dl.bintray.com/bitsolution/main'
        }
        .......        
    }
```

then, add depedencies

```
dependencies {
    appComponent("com.haulmont.cuba:cuba-global:$cubaVersion")
    appComponent("com.bitsolution.addons.simple.cuba.jpa.repositories:simple-cuba-jpa-repositories-global:0.1.1")
}
```

Then, XML configuration to enable query interfaces:

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:beans="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:repositories="http://www.bitsolution.co.id/schema/data/jpa"
       xsi:schemaLocation="
   http://www.springframework.org/schema/beans
   http://www.springframework.org/schema/beans/spring-beans.xsd
   http://www.springframework.org/schema/context
   http://www.springframework.org/schema/context/spring-context-4.3.xsd
   http://www.bitsolution.co.id/schema/data/jpa
   http://www.bisolution.co.id/schema/data/jpa/simple-cuba-repositories.xsd">

    <!-- Annotation-based beans -->
    <context:component-scan base-package="com.bitsolution.cubaproject"/>
    
    <repositories:repositories base-package="com.bitsolution.cubaproject.repository"/>

</beans>

```
Voila, now it's working

Example :

Bank.java

```
@Table(name = "CUBAPROJECT_BANK")
@Entity(name = "cubaproject_Bank")
@NamePattern("%s|label")
public class Bank extends BaseGenericIdEntity<String> {
    private static final long serialVersionUID = -6460255337403979003L;

    public Bank() {
        id = UUID.randomUUID().toString();
    }

    @Id
    @Column(name = "ID", nullable = false)
    protected String id;

    @Column(name = "CODE", length = 100)
    private String code;

    @NotNull
    @Column(name = "LABEL", nullable = false, length = 100)
    private String label;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }    
}
```

BankRepository.java
```
public interface BankRepository extends SimpleCubaJpaRepository<Bank, String> {

    @SimpleJpqlQuery("SELECT Count(Bank) FROM cubaproject_Bank Bank")
    Long countAll();

    @SimpleCubaView("_local")
    @SimpleJpqlQuery("SELECT  Bank FROM cubaproject_Bank Bank ORDER BY Bank.label ASC")
    Bank findFirstByOrderByLabelDesc();

    @SimpleCubaView("_local")
    @SimpleJpqlQuery("SELECT  Bank FROM cubaproject_Bank Bank ORDER BY Bank.label ASC")
    List<Bank> findAllByOrderByLabelAsc(Pageable pageable);

    @SimpleCubaView("_local")
    @SimpleJpqlQuery(value = "SELECT  Bank FROM cubaproject_Bank Bank ORDER BY Bank.label ASC",
                     countQuery = "SELECT Count(Bank) FROM cubaproject_Bank Bank")
    Page<Bank> findPageByOrderByLabelAsc(Pageable pageable);

}
```

Modify from [blog post](https://www.cuba-platform.com/blog/spring-query-interfaces-in-cuba) about using this component in more details.

Limitations:
* There might be some issues if you use entity names that may clash with SQL reserved words: order, like, select, etc.
* Custom queries has limited abilities, we do not match method parameter names and custom query parameter names.
* If you want to specify like clause with wildcards and parameters in query annotation, please use concat function and named parameters like in the example: `select ... where ... like concat('?', :name, '%')`
