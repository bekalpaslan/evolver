package com.evolver.context.collectors;

import com.evolver.context.*;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Collects existing documentation and comments
 */
public class DocumentationCollector implements ContextCollector {
    private static final Logger logger = Logger.getLogger(DocumentationCollector.class.getName());

    private static final int MAX_CONTENT_SIZE = 50 * 1024; // 50KB limit
    private static final int MAX_FILES_TO_READ = 20;
    private static final Set<String> DOCUMENTATION_EXTENSIONS = Set.of(
        ".md", ".txt", ".rst", ".adoc", ".html"
    );
    private static final Set<String> DOCUMENTATION_FILES = Set.of(
        "README", "readme", "README.md", "readme.md",
        "CHANGELOG", "changelog", "CHANGELOG.md", "changelog.md",
        "CONTRIBUTING", "contributing", "CONTRIBUTING.md", "contributing.md",
        "docs", "DOCS", "documentation", "DOCUMENTATION"
    );

    @Override
    public boolean isApplicable(ContextRequest request) {
        return request.getTaskType() == TaskType.DOCUMENTATION ||
               request.getTaskType() == TaskType.EXPLANATION ||
               request.getTaskType() == TaskType.CODE_GENERATION;
    }

    @Override
    public ContextFragment collect(ContextRequest request) {
        String projectPath = (String) request.getParameter("project_path");
        if (projectPath == null) {
            logger.warning("No project path provided for documentation collection");
            return null;
        }

        Path projectDir = Paths.get(projectPath);
        if (!Files.exists(projectDir) || !Files.isDirectory(projectDir)) {
            logger.warning("Project path does not exist or is not a directory: " + projectPath);
            return null;
        }

        try {
            String documentation = collectDocumentation(projectDir);

            if (documentation.trim().isEmpty()) {
                logger.info("No documentation found in project: " + projectPath);
                return null;
            }

            return ContextFragment.builder()
                .source("DocumentationCollector")
                .type(ContextType.PROJECT_DOCUMENTATION)
                .content(documentation)
                .addAspect("documentation")
                .addAspect("comments")
                .addAspect("readme")
                .relevanceScore(0.6)
                .addMetadata("source_path", projectPath)
                .addMetadata("files_scanned", getScannedFiles(projectDir))
                .estimatedTokens(estimateTokenCount(documentation))
                .build();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to collect documentation from: " + projectPath, e);
            return null;
        }
    }

    private String collectDocumentation(Path projectDir) {
        StringBuilder content = new StringBuilder();
        List<Path> docFiles = findDocumentationFiles(projectDir);

        for (Path docFile : docFiles) {
            if (content.length() >= MAX_CONTENT_SIZE) {
                content.append("\n\n[Content truncated due to size limit]");
                break;
            }

            try {
                String fileContent = readFileSafely(docFile);
                if (!fileContent.trim().isEmpty()) {
                    content.append("## ").append(projectDir.relativize(docFile)).append("\n\n");
                    content.append(fileContent).append("\n\n---\n\n");
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Failed to read documentation file: " + docFile, e);
            }
        }

        return content.toString();
    }

    private List<Path> findDocumentationFiles(Path projectDir) {
        List<Path> docFiles = new ArrayList<>();

        try {
            // Find documentation files in root directory
            try (Stream<Path> files = Files.list(projectDir)) {
                files.filter(this::isDocumentationFile)
                     .limit(MAX_FILES_TO_READ)
                     .forEach(docFiles::add);
            }

            // Find docs/ directory and its contents
            Path docsDir = projectDir.resolve("docs");
            if (Files.exists(docsDir) && Files.isDirectory(docsDir)) {
                try (Stream<Path> docsFiles = Files.walk(docsDir, 2)) { // Max depth 2
                    docsFiles.filter(Files::isRegularFile)
                            .filter(this::isDocumentationFile)
                            .limit(MAX_FILES_TO_READ - docFiles.size())
                            .forEach(docFiles::add);
                }
            }

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error finding documentation files", e);
        }

        return docFiles;
    }

    private boolean isDocumentationFile(Path path) {
        if (!Files.isRegularFile(path)) {
            return false;
        }

        String fileName = path.getFileName().toString().toLowerCase();

        // Check for documentation file names
        for (String docFile : DOCUMENTATION_FILES) {
            if (fileName.startsWith(docFile.toLowerCase())) {
                return true;
            }
        }

        // Check for documentation extensions
        for (String ext : DOCUMENTATION_EXTENSIONS) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }

        return false;
    }

    private String readFileSafely(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file, StandardCharsets.UTF_8)) {
            return lines.limit(500) // Limit lines to prevent excessive reading
                       .collect(StringBuilder::new,
                               (sb, line) -> sb.append(line).append('\n'),
                               StringBuilder::append)
                       .toString();
        }
    }

    private List<String> getScannedFiles(Path projectDir) {
        try {
            return findDocumentationFiles(projectDir).stream()
                .map(projectDir::relativize)
                .map(Path::toString)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private int estimateTokenCount(String content) {
        if (content == null) return 0;
        // Rough estimation: 1 token per 4 characters
        return content.length() / 4;
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public int getEstimatedCost() {
        return 200; // Higher cost due to file I/O
    }

    @Override
    public CollectorMetadata getMetadata() {
        return new CollectorMetadata(
            "DocumentationCollector",
            "Collects existing documentation, README files, and code comments with size limits and error handling",
            "2.0.0",
            CollectorMetadata.CollectorType.STATIC
        );
    }
}
