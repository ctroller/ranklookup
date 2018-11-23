package ptp.ranklookup.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceRegistry {
    private static final Map<Class<?>, WeakReference<ServiceLoader<?>>> REFERENCE_MAP = new ConcurrentHashMap<>(10);

    private ServiceRegistry ()
    {
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
        return getServiceLoader(clazz).findFirst()
                .orElseThrow();
    }

    /**
     * Helper function to retrieve all services for given class from the service loader.
     *
     * @param clazz the service class to look for
     * @param <T>   the type of service to look for
     * @return the found services as an iterator
     */
    @NotNull
    public static <T> Iterator<T> getServices(Class<T> clazz) {
        return getServiceLoader(clazz).iterator();
    }

    @NotNull
    @SuppressWarnings( "unchecked" )
    private static <T> ServiceLoader<T> getServiceLoader(Class<T> clazz) {
        return (ServiceLoader<T>) REFERENCE_MAP.computeIfAbsent(clazz, k -> new WeakReference<>(ServiceLoader.load(clazz)))
                .get();
    }
}
