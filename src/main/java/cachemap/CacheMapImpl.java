package cachemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CacheMapImpl<KeyType, ValueType> implements CacheMap<KeyType, ValueType> {

    private long timeToLive;
    private Map<KeyType, Node<ValueType>> map;

    public CacheMapImpl() {
        map = new HashMap<>();
        timeToLive = 5 * 1000;
    }

    @Override
    public void setTimeToLive(long timeToLive) {
        if (timeToLive < 0) {
            throw new IllegalArgumentException("timeToLive can not be < 0");
        }
        this.timeToLive = timeToLive;
    }

    @Override
    public long getTimeToLive() {
        return timeToLive;
    }

    @Override
    public ValueType put(KeyType key, ValueType value) {
        Objects.requireNonNull(key, "key cannot be null");
        Node<ValueType> node = new Node<>(value, Clock.getTime(), timeToLive);
        Node<ValueType> privies = map.put(key, node);
        return privies == null || !privies.isLive() ? null : privies.getValue();
    }

    @Override
    public void clearExpired() {
        map.entrySet().removeIf(entry -> !entry.getValue().isLive());
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(KeyType key) {
        Objects.requireNonNull(key, "key cannot be null");
        Node<ValueType> node = map.get(key);
        if (node == null || !node.isLive()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean containsValue(ValueType value) {
        return map.values().stream().anyMatch(node -> node.isLive() && nodeEqualsValue(node, value));
    }

    @Override
    public ValueType get(KeyType key) {
        Objects.requireNonNull(key, "key cannot be null");
        Node<ValueType> node = map.get(key);
        return node != null && node.isLive() ? node.getValue() : null;
    }

    @Override
    public boolean isEmpty() {
        return map.values().stream().noneMatch(node -> node.isLive());
    }

    @Override
    public ValueType remove(KeyType key) {
        Objects.requireNonNull(key, "key cannot be null");
        Node<ValueType> node = map.get(key);
        if (node != null && node.isLive()) {
            Node<ValueType> privies = map.remove(key);
            return privies == null ? null : privies.getValue();
        }
        return null;
    }

    @Override
    public int size() {
        return (int) map.values().stream().filter(node -> node.isLive()).count();
    }

    private boolean nodeEqualsValue(Node<ValueType> node, ValueType value) {
        ValueType nodeValue = node.getValue();
        return (nodeValue == null && value == null) || (nodeValue != null && nodeValue.equals(value));
    }

    private static class Node<ValueType> {
        private ValueType value;
        private long timeToLive;
        private long timeOfCreation;

        public Node(ValueType value, long timeOfCreation, long timeToLive) {

            this.value = value;
            this.timeToLive = timeToLive;
            this.timeOfCreation = timeOfCreation;
        }

        public boolean isLive() {
            return (Clock.getTime() - getTimeOfCreation()) < timeToLive;
        }

        public ValueType getValue() {

            return value;
        }

        public void setValue(ValueType value) {
            this.value = value;
        }

        public long getTimeToLive() {
            return timeToLive;
        }

        public void setTimeToLive(long timeToLive) {
            this.timeToLive = timeToLive;
        }

        public long getTimeOfCreation() {
            return timeOfCreation;
        }

        public void setTimeOfCreation(long timeOfCreation) {
            this.timeOfCreation = timeOfCreation;
        }
    }
}
