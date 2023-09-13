package site.achun.git.social.local;
@FunctionalInterface
public interface Setter<T,U> {
    void set(T obj,U value);
}
