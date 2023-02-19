package com.robertg.linkedin.posts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImmutableCollectionsTest {

    @Test
    @DisplayName("Test unmodifiable* methods from Collections class available in Java 8.")
    void testUnmodifiableCollectionsJava8(){
        final var unmodifiableList = Collections.unmodifiableList(new ArrayList<>());
        assertThatThrownBy(() -> unmodifiableList.add("Robert")).isExactlyInstanceOf(UnsupportedOperationException.class);

        final var unmodifiableSet = Collections.unmodifiableSet(new HashSet<>());
        assertThatThrownBy(() -> unmodifiableSet.add("Robert")).isExactlyInstanceOf(UnsupportedOperationException.class);

        final var unmodifiableMap = Collections.unmodifiableMap(new HashMap<>());
        assertThatThrownBy(() -> unmodifiableMap.put("Robert", Integer.MAX_VALUE)).isExactlyInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("Test of() static factory methods from collection interfaces available in Java 9.")
    void testCollectionsFactoryMethodsJava9(){
        final var unmodifiableList = List.of();
        assertThatThrownBy(() -> unmodifiableList.add("Robert")).isExactlyInstanceOf(UnsupportedOperationException.class);

        final var unmodifiableSet = Set.of();
        assertThatThrownBy(() -> unmodifiableSet.add("Robert")).isExactlyInstanceOf(UnsupportedOperationException.class);

        final var unmodifiableMap = Map.of();
        assertThatThrownBy(() -> unmodifiableMap.put("Robert", Integer.MAX_VALUE)).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
}
