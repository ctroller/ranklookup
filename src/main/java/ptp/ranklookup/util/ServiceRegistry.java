package ptp.ranklookup.util;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings( "unchecked" )
public class ServiceRegistry {
    private static final Map<Class<?>, List<?>> REFERENCE_MAP = new ConcurrentHashMap<>(10);

    private ServiceRegistry ()
    {
    }

    public static <T> void register( Class<T> clazz, T service )
    {
        ((List<T>) REFERENCE_MAP.computeIfAbsent(clazz, k -> new CopyOnWriteArrayList<>())).add(service);
    }

    /**
     * Helper function to retrieve a service from the service loader.
     *
     * @param clazz the service class to look for
     * @param <T>   the type of service to look for
     * @return the found service
     * @throws java.util.NoSuchElementException if there is no service registered for given class
     */
    public static <T> T getService(Class<T> clazz) {
        return (T) REFERENCE_MAP.get( clazz ).get(0);
    }

    /**
     * Helper function to retrieve all services for given class from the service loader.
     *
     * @param clazz the service class to look for
     * @param <T>   the type of service to look for
     * @return the found services as an iterator
     */
    @NotNull
    public static <T> List<T> getServices(Class<T> clazz) {
        return (List<T>) REFERENCE_MAP.get(clazz);
    }
}
