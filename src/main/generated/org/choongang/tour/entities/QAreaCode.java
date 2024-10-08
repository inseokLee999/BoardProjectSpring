package org.choongang.tour.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAreaCode is a Querydsl query type for AreaCode
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAreaCode extends EntityPathBase<AreaCode> {

    private static final long serialVersionUID = -811936243L;

    public static final QAreaCode areaCode = new QAreaCode("areaCode");

    public final StringPath code = createString("code");

    public final StringPath name = createString("name");

    public QAreaCode(String variable) {
        super(AreaCode.class, forVariable(variable));
    }

    public QAreaCode(Path<? extends AreaCode> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAreaCode(PathMetadata metadata) {
        super(AreaCode.class, metadata);
    }

}

