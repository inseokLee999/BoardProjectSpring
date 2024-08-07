package org.choongang.tour.entities;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTourPlace is a Querydsl query type for TourPlace
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTourPlace extends EntityPathBase<TourPlace> {

    private static final long serialVersionUID = 1498186236L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTourPlace tourPlace = new QTourPlace("tourPlace");

    public final StringPath address = createString("address");

    public final QAreaCode areaCode;

    public final BooleanPath bookTour = createBoolean("bookTour");

    public final StringPath category1 = createString("category1");

    public final StringPath category2 = createString("category2");

    public final StringPath category3 = createString("category3");

    public final NumberPath<Long> contentId = createNumber("contentId", Long.class);

    public final NumberPath<Long> contentTypeId = createNumber("contentTypeId", Long.class);

    public final StringPath cpyrhtDivCd = createString("cpyrhtDivCd");

    public final NumberPath<Double> distance = createNumber("distance", Double.class);

    public final StringPath firstImage = createString("firstImage");

    public final StringPath firstImage2 = createString("firstImage2");

    public final NumberPath<Double> latitude = createNumber("latitude", Double.class);

    public final NumberPath<Double> longitude = createNumber("longitude", Double.class);

    public final NumberPath<Integer> mapLevel = createNumber("mapLevel", Integer.class);

    public final NumberPath<Integer> sigugunCode = createNumber("sigugunCode", Integer.class);

    public final StringPath tel = createString("tel");

    public final StringPath title = createString("title");

    public QTourPlace(String variable) {
        this(TourPlace.class, forVariable(variable), INITS);
    }

    public QTourPlace(Path<? extends TourPlace> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTourPlace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTourPlace(PathMetadata metadata, PathInits inits) {
        this(TourPlace.class, metadata, inits);
    }

    public QTourPlace(Class<? extends TourPlace> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.areaCode = inits.isInitialized("areaCode") ? new QAreaCode(forProperty("areaCode")) : null;
    }

}

