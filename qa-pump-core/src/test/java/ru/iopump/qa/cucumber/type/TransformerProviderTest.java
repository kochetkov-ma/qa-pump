package ru.iopump.qa.cucumber.type;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.iopump.qa.cucumber.processor.GroovyScriptProcessor;
import ru.iopump.qa.cucumber.transformer.Transformer;

public class TransformerProviderTest {

    private ObjectMapper mapper;
    private TransformerProvider provider;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        provider = new TransformerProvider(transformerCollection(), mapper);
    }


    @Test
    public void getAll() {
        assertThat(provider.getAll()).hasSize(11);
    }

    @Test
    public void findByType() {
        assertThat(provider.findByType(Map.class)).hasSize(6);
        assertThat(provider.findByType(new TypeReference<Map<Object, Object>>() {
        }.getType())).hasSize(3);
        assertThat(provider.findByType(new TypeReference<Map<String, Object>>() {
        }.getType())).hasSize(2);
        assertThat(provider.findByType(Object.class)).hasSize(11);
    }

    @Test
    public void findByTypeSorted() {
        assertThat(provider.findByTypeSorted(Object.class))
            .hasSize(11)
            .first()
            .extracting(Transformer::priority)
            .isEqualTo(8);

        assertThat(provider.findByTypeSorted(Map.class))
            .first()
            .extracting(Transformer::targetType)
            .isEqualTo(new TypeReference<Map>() {
            }.getType());

        assertThat(provider.findByTypeSorted(new TypeReference<Map<Object, Object>>() {
        }.getType()))
            .first()
            .extracting(Transformer::targetType)
            .isEqualTo(new TypeReference<Map<Object, Object>>() {
            }.getType());

        assertThat(provider.findByTypeSorted(new TypeReference<Map<String, Object>>() {
        }.getType()))
            .first()
            .extracting(Transformer::priority)
            .isEqualTo(2);


        assertThat(provider.findByTypeSorted(Integer.class))
            .hasSize(1)
            .first()
            .extracting(Transformer::targetType)
            .isEqualTo(Integer.class);

        assertThat(provider.findByTypeSorted(String.class))
            .hasSize(1)
            .first()
            .extracting(Transformer::targetType)
            .isEqualTo(String.class);
    }

    @Test
    public void instanceOf() {
        JavaType left = mapper.constructType(new TypeReference<HashMap<?, ?>>() {
        }.getType());
        JavaType right = mapper.constructType(new TypeReference<Map<Object, Object>>() {
        }.getType());
        assertThat(provider.instanceOf(left, right)).isTrue();

        left = mapper.constructType(new TypeReference<Map<?, ?>>() {
        }.getType());
        right = mapper.constructType(new TypeReference<HashMap<Object, Object>>() {
        }.getType());
        assertThat(provider.instanceOf(left, right)).isFalse();

        left = mapper.constructType(new TypeReference<HashMap<Object, ?>>() {
        }.getType());
        right = mapper.constructType(new TypeReference<Map<String, Object>>() {
        }.getType());
        assertThat(provider.instanceOf(left, right)).isFalse();

        left = mapper.constructType(Object.class);
        right = mapper.constructType(String.class);
        assertThat(provider.instanceOf(left, right)).isFalse();

        left = mapper.constructType(String.class);
        right = mapper.constructType(CharSequence.class);
        assertThat(provider.instanceOf(left, right)).isTrue();
    }

    @Test
    public void isSameParameters() {
        JavaType left = mapper.constructType(new TypeReference<Map<String, Object>>() {
        }.getType());
        JavaType right = mapper.constructType(new TypeReference<Map<String, Object>>() {
        }.getType());
        assertThat(provider.isSameParameters(left, right)).isTrue();

        left = mapper.constructType(new TypeReference<Map<Object, Object>>() {
        }.getType());
        right = mapper.constructType(new TypeReference<Map<String, Object>>() {
        }.getType());
        assertThat(provider.isSameParameters(left, right)).isFalse();

        left = mapper.constructType(new TypeReference<Map>() {
        }.getType());
        right = mapper.constructType(new TypeReference<Map<Object, Object>>() {
        }.getType());
        assertThat(provider.isSameParameters(left, right)).isTrue();

        left = mapper.constructType(new TypeReference<Map>() {
        }.getType());
        right = mapper.constructType(new TypeReference<Map<?, ?>>() {
        }.getType());
        assertThat(provider.isSameParameters(left, right)).isTrue();

        left = mapper.constructType(new TypeReference<Map<String, ?>>() {
        }.getType());
        right = mapper.constructType(new TypeReference<Map<?, ?>>() {
        }.getType());
        assertThat(provider.isSameParameters(left, right)).isFalse();

        left = mapper.constructType(new TypeReference<Collection<String>>() {
        }.getType());
        right = mapper.constructType(new TypeReference<List<String>>() {
        }.getType());
        assertThat(provider.isSameParameters(left, right)).isTrue();

        left = mapper.constructType(new TypeReference<Collection>() {
        }.getType());
        right = mapper.constructType(new TypeReference<List<?>>() {
        }.getType());
        assertThat(provider.isSameParameters(left, right)).isTrue();
    }


    private Collection<Transformer> transformerCollection() {
        return ImmutableList.<Transformer>builder()
            .add(transformer(0, String.class))
            .add(transformer(0, Object.class))
            .add(transformer(8, Object.class))
            .add(transformer(-10, Object.class))
            .add(transformer(-1, Integer.class))
            .add(transformer(0, new TypeReference<Map<String, Object>>() {
            }.getType()))
            .add(transformer(2, new TypeReference<Map<String, Object>>() {
            }.getType()))
            .add(transformer(10, new TypeReference<Map<Object, String>>() {
            }.getType()))
            .add(transformer(7, new TypeReference<Map>() {
            }.getType()))
            .add(transformer(100, new TypeReference<Map<?, ?>>() {
            }.getType()))
            .add(transformer(1, new TypeReference<Map<Object, Object>>() {
            }.getType()))
            .build();
    }

    private Transformer transformer(int priority, Type type) {
        Transformer transformer = Mockito.mock(Transformer.class);
        Mockito.when(transformer.targetType()).thenReturn(type);
        Mockito.when(transformer.priority()).thenReturn(priority);
        Mockito.when(transformer.preProcessorClass()).thenReturn(GroovyScriptProcessor.class);
        Mockito.when(transformer.toString()).thenReturn(type.toString());
        return transformer;
    }

}