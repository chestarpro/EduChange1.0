package kg.itacademy.converter;

import java.util.function.Function;

public class BaseConverter<Model, Entity> {
    private final Function<Model, Entity> fromModel;
    private final Function<Entity, Model> fromEntity;

    public BaseConverter(final Function<Model, Entity> fromModel, final Function<Entity, Model> fromEntity) {
        this.fromModel = fromModel;
        this.fromEntity = fromEntity;
    }

    public final Entity convertFromModel(final Model modelToConvert) {
        return fromModel.apply(modelToConvert);
    }

    public final Model convertFromEntity(final Entity entityToConvert) {
        return fromEntity.apply(entityToConvert);
    }
}