package ru.iopump.qa.cucumber.type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.iopump.qa.cucumber.transformer.LastResortTransformer;
import ru.iopump.qa.cucumber.transformer.Transformer;

@SuppressWarnings("rawtypes")
@Slf4j
@RequiredArgsConstructor
public class TransformerProvider {

    private final Collection<Transformer> transformerCollection;
    private final ObjectMapper objectMapper;
    private final LastResortTransformer lastResortTransformer;

    public Collection<Transformer> getAll() {
        return Collections.unmodifiableCollection(transformerCollection);
    }

    public Collection<Transformer> findByType(@NonNull Type baseTargetType) {
        return getAll().stream()
            .filter(t -> instanceOf(t.targetType(), baseTargetType))
            .collect(Collectors.toList());
    }

    public Collection<Transformer> findByTypeSorted(@NonNull Type baseTargetType) {
        return sort(findByType(baseTargetType), baseTargetType);
    }

    public LastResortTransformer getLastResortTransformer() {
        return lastResortTransformer;
    }

    //// PACKAGE

    Collection<Transformer> sort(@NonNull Collection<Transformer> notSortedCollection, @NonNull Type baseTargetType) {

        return notSortedCollection.stream().sorted((left, right) -> {
            if (left.targetType().equals(baseTargetType) && !right.targetType().equals(baseTargetType)) {
                return Integer.MIN_VALUE;
            } else if (!left.targetType().equals(baseTargetType) && right.targetType().equals(baseTargetType)) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.compare(right.priority(), left.priority());
            }
        }).collect(Collectors.toList());
    }

    boolean instanceOf(Type checkedLeftType, Type baseRightType) {
        JavaType checkedLeftJavaType = objectMapper.constructType(checkedLeftType);
        JavaType baseRightJavaType = objectMapper.constructType(baseRightType);
        boolean isMainClassMatches = checkedLeftJavaType.isTypeOrSubTypeOf(baseRightJavaType.getRawClass());
        return isMainClassMatches
            && (baseRightJavaType.containedTypeCount() == 0 || isSameParameters(checkedLeftJavaType, baseRightJavaType));
    }

    boolean isSameParameters(JavaType checkedLeftJavaType, JavaType baseRightJavaType) {
        final Collection<JavaType> checkedLeftJavaTypeParameters = Lists.newArrayList();
        for (int i = 0; i < checkedLeftJavaType.containedTypeCount(); i++) {
            checkedLeftJavaTypeParameters.add(checkedLeftJavaType.containedType(i));
        }
        final Collection<JavaType> baseRightJavaTypeParameters = Lists.newArrayList();
        for (int i = 0; i < baseRightJavaType.containedTypeCount(); i++) {
            baseRightJavaTypeParameters.add(baseRightJavaType.containedType(i));
        }
        if (checkedLeftJavaTypeParameters.equals(baseRightJavaTypeParameters)) {
            return true;
        }
        boolean leftAllObject = checkedLeftJavaTypeParameters.stream().allMatch(i -> i.getRawClass() == Object.class);
        boolean rightAllObject = baseRightJavaTypeParameters.stream().allMatch(i -> i.getRawClass() == Object.class);
        return leftAllObject && rightAllObject;
    }
}
