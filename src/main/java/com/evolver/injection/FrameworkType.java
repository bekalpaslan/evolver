package com.evolver.injection;

/**
 * Detected existing frameworks
 */
public enum FrameworkType {
    // Java
    SPRING, SPRING_BOOT, HIBERNATE, STRUTS,

    // JavaScript/TypeScript
    REACT, VUE, ANGULAR, EXPRESS, NEXTJS, NUXTJS,

    // Python
    DJANGO, FLASK, FASTAPI, PYRAMID,

    // .NET
    ASPNET, ASPNET_CORE, ENTITY_FRAMEWORK,

    // Other
    RAILS, LARAVEL, SYMFONY,

    // None detected
    NONE
}