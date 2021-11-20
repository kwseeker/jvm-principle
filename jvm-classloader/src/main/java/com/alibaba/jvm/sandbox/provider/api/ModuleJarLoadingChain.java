package com.alibaba.jvm.sandbox.provider.api;

import java.io.File;

public interface ModuleJarLoadingChain {

    void loading(File moduleJarFile) throws Throwable;
}