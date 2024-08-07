/**
 *     Copyright (C) 2019-2024 Ubiqube.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see https://www.gnu.org/licenses/.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

class FindDuplicatesTest {
	/** Logger. */
	private static final Logger LOG = LoggerFactory.getLogger(FindDuplicatesTest.class);

	private final Reflections reflections;
	private final MultiValueMap<Key, Method> cache = new LinkedMultiValueMap<>();

	public FindDuplicatesTest() {
		reflections = new Reflections("com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping", new SubTypesScanner(false));
	}

	@Test
	void test() {
		final Set<Class<? extends Object>> set = reflections.getSubTypesOf(Object.class);
		final Map<String, Set<String>> subtype = reflections.getStore().get("SubTypes");
		subtype.forEach((x, y) -> {
			handle(x);
		});
		renderResult();
	}

	private void renderResult() {
		cache.entrySet().forEach(x -> {
			if (x.getValue().size() <= 1) {
				return;
			}
			renderError(x);
		});
	}

	private void renderError(final Entry<Key, List<Method>> x) {
		LOG.error("- " + x.getKey());
		x.getValue().forEach(y -> {
			LOG.error("    - {}", y);
		});
		LOG.error("---------------------------------------");
	}

	private void handle(final String x) {
		if (x.startsWith("java.lang")) {
			return;
		}
		try {
			realHandle(x);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void realHandle(final String x) throws ClassNotFoundException {
		final Class<?> clazz = Class.forName(x);
		final Method[] meths = clazz.getDeclaredMethods();
		for (final Method method : meths) {
			final Key k = new Key(method.getReturnType().getCanonicalName(), method.getParameters()[0].getType().getCanonicalName());
			cache.add(k, method);
		}
	}

}

record Key(String returnType, String param) {
	//
}