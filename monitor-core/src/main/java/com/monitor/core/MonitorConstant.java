package com.monitor.core;

/**
 * @author WuQinglong
 * @since 2023/2/24 16:27
 */
public interface MonitorConstant {

    String JVM_NAME_PREFIX = "jvm";

    String GC_NAME_PREFIX = JVM_NAME_PREFIX + ".gc";

    String MEMORY_NAME_PREFIX = JVM_NAME_PREFIX + ".memory";

    String HEAP_MEMORY_NAME_PREFIX = JVM_NAME_PREFIX + ".memory_pool.heap";

    String NO_HEAP_MEMORY_NAME_PREFIX = JVM_NAME_PREFIX + ".memory_pool.no_heap";

    String THREAD_NAME_PREFIX = JVM_NAME_PREFIX + ".thread";

    String HIKARI_NAME_PREFIX = JVM_NAME_PREFIX + ".hikari";

    String DRUID_NAME_PREFIX = JVM_NAME_PREFIX + ".druid";

    String TOMCAT_NAME_PREFIX = JVM_NAME_PREFIX + ".tomcat";

}
