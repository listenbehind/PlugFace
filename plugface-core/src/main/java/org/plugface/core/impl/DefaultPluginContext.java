package org.plugface.core.impl;

/*-
 * #%L
 * PlugFace :: Core
 * %%
 * Copyright (C) 2017 - 2018 PlugFace
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 * #L%
 */

import org.plugface.core.PluginContext;
import org.plugface.core.internal.AnnotationProcessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class DefaultPluginContext implements PluginContext {

    private final Map<String, Object> registry;

    public DefaultPluginContext() {
        registry = new HashMap<>();
    }

    public DefaultPluginContext(Map<String, Object> registry) {
        this.registry = registry;
    }

    @Override
    @Nullable
    public <T> T getPlugin(String pluginName) {
        if (registry.containsKey(pluginName)) {
            return (T) registry.get(pluginName);
        }
        return null;
    }

    @Override
    @Nullable
    public <T> T getPlugin(Class<T> pluginClass) {
        for (Object plugin : registry.values()) {
            if (pluginClass.equals(plugin.getClass())) {
                return (T) plugin;
            }
        }
        return null;
    }

    @Override
    public <T> void addPlugin(T plugin) {
        final String name = AnnotationProcessor.getPluginName(plugin);
        if (registry.containsKey(name) || registry.containsValue(plugin)) {
            throw new IllegalArgumentException("Plugin already registered");
        }
        registry.put(name, plugin);
    }

    @Override
    @Nullable
    public <T> T removePlugin(T plugin) {
        final String name = AnnotationProcessor.getPluginName(plugin);
        return removePlugin(name);
    }

    @Override
    @Nullable
    public <T> T removePlugin(String pluginName) {
        if (registry.containsKey(pluginName)) {
            return (T) registry.remove(pluginName);
        }
        return null;
    }

    @Override
    public boolean hasPlugin(String pluginName) {
        return registry.containsKey(pluginName);
    }

    @Override
    public <T> boolean hasPlugin(Class<T> pluginClass) {
        return getPlugin(pluginClass) != null;
    }


}