package com.fullwall.maps.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fullwall.maps.utils.Messaging;
import com.google.common.collect.Maps;

public class SettingsTree {
	private final Map<String, String> tree = new ConcurrentHashMap<String, String>(
			100);

	public void copy(String from, String to) {
		Map<String, String> toCopy = Maps.newHashMap();
		for (String key : tree.keySet()) {
			if (tree.get(key).isEmpty() || !key.startsWith(from))
				continue;
			toCopy.put(to + key.substring(from.length(), key.length()),
					tree.get(key));
		}
		tree.putAll(toCopy);
	}

	public String get(String path) {
		return tree.get(path);
	}

	public Map<String, String> getTree() {
		return tree;
	}

	public void populate(String path) {
		StringBuilder progressive = new StringBuilder();
		int index = 0;
		String[] branches = path.split("\\.");
		for (String branch : branches) {
			progressive.append(branch);
			if (tree.get(progressive.toString()) == null) {
				tree.put(progressive.toString(), "");
			}
			if (index != branches.length - 1)
				progressive.append(".");
			++index;
		}
	}

	public void remove(String path) {
		for (String key : tree.keySet()) {
			if (!key.startsWith(path))
				continue;
			tree.remove(key);
		}
	}

	public void set(String path, String value) {
		if (!path.equals(value)) {
			tree.put(path, value);
		} else {
			Messaging.debug(path, "was set to an illegal value of", value);
		}
		populate(path);
	}
}