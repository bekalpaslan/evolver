package com.evolver.injection;

/**
 * Types of projects that can be detected
 */
public enum ProjectType {
    // Java
    MAVEN_JAVA, GRADLE_JAVA, GRADLE_KOTLIN, JAVA_GENERIC,

    // JavaScript/TypeScript
    NODE_JS, REACT, VUE, ANGULAR, NODE_EXPRESS, JAVASCRIPT_GENERIC,

    // Python
    PYTHON_PIP, PYTHON_POETRY, PYTHON_SETUPTOOLS, PYTHON_GENERIC,

    // .NET
    DOTNET_CSHARP, DOTNET_FSHARP, DOTNET_SOLUTION,

    // Other languages
    GO_MODULE, RUST_CARGO,

    // Unknown
    UNKNOWN
}