package com.base.service.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseEntityWithDeleted is a Querydsl query type for BaseEntityWithDeleted
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseEntityWithDeleted extends EntityPathBase<BaseEntityWithDeleted> {

    private static final long serialVersionUID = -1189093571L;

    public static final QBaseEntityWithDeleted baseEntityWithDeleted = new QBaseEntityWithDeleted("baseEntityWithDeleted");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final BooleanPath deleted = createBoolean("deleted");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QBaseEntityWithDeleted(String variable) {
        super(BaseEntityWithDeleted.class, forVariable(variable));
    }

    public QBaseEntityWithDeleted(Path<? extends BaseEntityWithDeleted> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntityWithDeleted(PathMetadata metadata) {
        super(BaseEntityWithDeleted.class, metadata);
    }

}

