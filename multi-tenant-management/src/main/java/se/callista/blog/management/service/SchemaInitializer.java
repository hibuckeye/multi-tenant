package se.callista.blog.management.service;

import se.callista.blog.management.domain.entity.Shard;

public interface SchemaInitializer {

    void initializeDBSchema(Shard shard);

}
