package com.fullwall.maps.storage;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import javax.annotation.Nullable;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Bytes;

public class MemoryDataKey extends DataKey {
    private String name;
    private final String path;

    private final ConfigurationSection root;

    public MemoryDataKey() {
        root = new MemoryConfiguration();
        path = "";
    }

    private MemoryDataKey(ConfigurationSection root, String path) {
        this.root = root;
        this.path = path;
    }

    @Override
    public boolean getBoolean(String key) {
        return root.getBoolean(getKeyFor(key), false);
    }

    @Override
    public byte[] getByteArray(String key) {
        return Bytes.toArray(root.getByteList(getKeyFor(key)));
    }

    @Override
    public double getDouble(String key) {
        return root.getDouble(getKeyFor(key), 0D);
    }

    @Override
    public int getInt(String key) {
        return root.getInt(getKeyFor(key), 0);
    }

    private String getKeyFor(String key) {
        if (key.isEmpty())
            return path;
        if (key.charAt(0) == '.')
            return path.isEmpty() ? key.substring(1, key.length()) : path + key;
        return path.isEmpty() ? key : path + "." + key;
    }

    @Override
    public long getLong(String key) {
        return root.getLong(getKeyFor(key), 0L);
    }

    @Override
    public Object getRaw(String key) {
        return root.get(getKeyFor(key));
    }

    @Override
    public MemoryDataKey getRelative(String relative) {
        String key = getKeyFor(relative);
        return new MemoryDataKey(root, key);
    }

    @Override
    public String getString(String key) {
        return root.getString(getKeyFor(key), "");
    }

    @Override
    public Iterable<DataKey> getSubKeys() {
        ConfigurationSection head = root.getConfigurationSection(path);
        if (head == null)
            return Collections.emptyList();
        Set<String> keys = head.getKeys(false);
        return Iterables.transform(keys, new Function<String, DataKey>() {
            @Override
            public DataKey apply(@Nullable String input) {
                return new MemoryDataKey(root, getKeyFor(input));
            }
        });
    }

    @Override
    public boolean keyExists(String key) {
        return root.isSet(getKeyFor(key));
    }

    @Override
    public String name() {
        if (name == null) {
            int idx = path.lastIndexOf('.');
            name = idx == -1 ? path : path.substring(idx + 1, path.length());
        }
        return name;
    }

    @Override
    public void removeKey(String key) {
        set(key, null);
    }

    private void set(String key, Object value) {
        root.set(getKeyFor(key), value);
    }

    @Override
    public void setBoolean(String key, boolean value) {
        set(key, value);
    }

    @Override
    public void setByteArray(String key, byte[] array) {
        set(key, Arrays.asList(array));
    }

    @Override
    public void setDouble(String key, double value) {
        set(key, value);
    }

    @Override
    public void setInt(String key, int value) {
        set(key, value);
    }

    @Override
    public void setLong(String key, long value) {
        set(key, value);
    }

    @Override
    public void setRaw(String key, Object value) {
        set(key, value);
    }

    @Override
    public void setString(String key, String value) {
        set(key, value);
    }
}
