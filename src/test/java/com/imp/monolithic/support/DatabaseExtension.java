package com.imp.monolithic.support;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.stereotype.Component;

@Component
public class DatabaseExtension implements AfterEachCallback {
    private final DatabaseCleaner databaseCleaner;

    public DatabaseExtension(final DatabaseCleaner databaseCleaner) {
        this.databaseCleaner = databaseCleaner;
    }

    @Override
    public void afterEach(final ExtensionContext context) throws Exception {
        databaseCleaner.execute();
    }
}
