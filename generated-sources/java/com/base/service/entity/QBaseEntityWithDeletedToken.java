package com.base.service.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseEntityWithDeletedToken is a Querydsl query type for BaseEntityWithDeletedToken
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseEntityWithDeletedToken extends EntityPathBase<BaseEntityWithDeletedToken> {

    private static final long serialVersionUID = 400484540L;

    public static final QBaseEntityWithDeletedToken baseEntityWithDeletedToken = new QBaseEntityWithDeletedToken("baseEntityWithDeletedToken");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final StringPath createdBy = _super.createdBy;

    public final StringPath deletedToken = createString("deletedToken");

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    //inherited
    public final StringPath updatedBy = _super.updatedBy;

    //inherited
    public final NumberPath<Long> version = _super.version;

    public QBaseEntityWithDeletedToken(String variable) {
        super(BaseEntityWithDeletedToken.class, forVariable(variable));
    }

    public QBaseEntityWithDeletedToken(Path<? extends BaseEntityWithDeletedToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseEntityWithDeletedToken(PathMetadata metadata) {
        super(BaseEntityWithDeletedToken.class, metadata);
    }

}

