package top.kwseeker.classloader.grpcServerProvider;

import java.util.*;

final class ServiceProviders {

    private ServiceProviders() {
    }

    /**
     * 返回加载的Service实现中优先级最高的实现
     * 如果没有则返回null
     */
    public static <T> T load(Class<T> klass,
                             ClassLoader cl,
                             ServiceProviders.PriorityAccessor<T> priorityAccessor) {

        List<T> candidates = loadAll(klass, cl, priorityAccessor);
        return candidates.isEmpty() ? null : candidates.get(0);
    }

    /**
     * 加载Service所有的实现
     * @param klass             要加载的服务类型
     * @param cl                使用的类加载器
     * @param priorityAccessor  从多个Service实现选择一个的判定标准接口
     * @param <T>               优先级判定接口针对哪种类型的服务的判定实现
     * @return
     */
    public static <T> List<T> loadAll(Class<T> klass,
                                      ClassLoader cl,
                                      final ServiceProviders.PriorityAccessor<T> priorityAccessor) {

        Iterable<T> candidates = getCandidatesViaServiceLoader(klass, cl);

        List<T> list = new ArrayList<>();
        Iterator<T> var6 = candidates.iterator();
        while(var6.hasNext()) {
            T current = var6.next();
            if (priorityAccessor.isAvailable(current)) {
                list.add(current);
            }
        }

        //将list中的Service实现，按照优先级排序
        Collections.sort(list, Collections.reverseOrder(new Comparator<T>() {
            public int compare(T f1, T f2) {
                return priorityAccessor.getPriority(f1) - priorityAccessor.getPriority(f2);
            }
        }));
        return Collections.unmodifiableList(list);
    }

    public static <T> Iterable<T> getCandidatesViaServiceLoader(Class<T> klass, ClassLoader cl) {
        Iterable<T> i = ServiceLoader.load(klass, cl);
        if (!i.iterator().hasNext()) {
            i = ServiceLoader.load(klass);
        }
        return i;
    }

    static <T> T create(Class<T> klass, Class<?> rawClass) {
        try {
            return rawClass.asSubclass(klass).getConstructor().newInstance();
        } catch (Throwable var3) {
            throw new ServiceConfigurationError(String.format("Provider %s could not be instantiated %s", rawClass.getName(), var3), var3);
        }
    }

    /**
     * Service优先级控制，当有多个Service实现的时候，通过这个接口，选择一个实现
     * @param <T>
     */
    public interface PriorityAccessor<T> {
        boolean isAvailable(T var1);
        int getPriority(T var1);
    }
}
