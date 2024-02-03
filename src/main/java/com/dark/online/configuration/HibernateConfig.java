//package com.dark.online.configuration;
//
//import org.hibernate.dialect.Dialect;
//import org.hibernate.dialect.identity.IdentityColumnSupport;
//import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class HibernateConfig {
//
//    @Bean
//    public MutableIdentifierGeneratorFactoryStandardImpl identifierGeneratorFactory() {
//        MutableIdentifierGeneratorFactoryStandardImpl factory = new MutableIdentifierGeneratorFactoryStandardImpl();
//        factory.register("custom-id", CustomIdGenerator.class);
//        return factory;
//    }
//
//    @Bean
//    public MutableIdentifierGeneratorFactoryResolver identifierGeneratorFactoryResolver() {
//        return new MutableIdentifierGeneratorFactoryResolver() {
//            @Override
//            public MutableIdentifierGeneratorFactory resolve() {
//                return identifierGeneratorFactory();
//            }
//        };
//    }
//
//    @Bean
//    public Dialect dialect() {
//        return new Dialect() {
//            @Override
//            public IdentityColumnSupport getIdentityColumnSupport() {
//                return new IdentityColumnSupportImpl();
//            }
//        };
//    }
//}
