/**
 *     Copyright (C) 2019-2023 Ubiqube.
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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ubiqube.etsi.mano.dao.mano.v2.AbstractTask;
import com.ubiqube.etsi.mano.dao.mano.v2.nfvo.NsTask;
import com.ubiqube.etsi.mano.service.pkg.bean.AffinityRuleAdapater;
import com.ubiqube.etsi.mano.service.pkg.bean.InstantiationLevel;
import com.ubiqube.etsi.mano.service.pkg.bean.InstantiationLevels;
import com.ubiqube.etsi.mano.service.pkg.bean.NsInformations;
import com.ubiqube.etsi.mano.service.pkg.bean.ProviderData;
import com.ubiqube.etsi.mano.service.pkg.bean.ScaleInfo;
import com.ubiqube.etsi.mano.service.pkg.bean.SecurityGroupRule;
import com.ubiqube.etsi.mano.service.pkg.bean.VduInitialDelta;
import com.ubiqube.etsi.mano.service.pkg.bean.VduInstantiationLevels;
import com.ubiqube.etsi.mano.service.pkg.bean.VduLevel;
import com.ubiqube.etsi.mano.service.pkg.bean.VduScalingAspectDeltas;
import com.ubiqube.etsi.mano.service.pkg.bean.VnfProfile;
import com.ubiqube.etsi.mano.service.pkg.bean.nsscaling.NsScaling;
import com.ubiqube.etsi.mano.tf.entities.NetworkPolicyTask;
import com.ubiqube.etsi.mano.utils.ReflectionUtils;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.EqualsVerifierReport;
import nl.jqno.equalsverifier.Warning;

/**
 *
 * @author Olivier Vignaud
 *
 */
class ModelTest {
	private static final Logger LOG = LoggerFactory.getLogger(ModelTest.class);

	@ParameterizedTest
	@MethodSource("providerClass")
	void testName(final Class<?> param) throws Exception {
		realHandle(param.getName());
		assertTrue(true);
	}

	private static Stream<Arguments> providerClass() {
		return Stream.of(
				Arguments.of(VnfProfile.class),
				Arguments.of(AffinityRuleAdapater.class),
				Arguments.of(InstantiationLevel.class),
				Arguments.of(InstantiationLevels.class),
				Arguments.of(NsInformations.class),
				Arguments.of(ProviderData.class),
				Arguments.of(ScaleInfo.class),
				Arguments.of(VduInitialDelta.class),
				Arguments.of(VduInstantiationLevels.class),
				Arguments.of(VduLevel.class),
				Arguments.of(VduScalingAspectDeltas.class),
				Arguments.of(SecurityGroupRule.class),
				Arguments.of(VnfProfile.class),
				Arguments.of(NsScaling.class));
	}

	private static void realHandle(final String x) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IntrospectionException {
		if (x.startsWith("com.ubiqube.etsi.mano.utils") || x.endsWith("Builder") || x.endsWith("Test") || !x.startsWith("com.ubiqube.etsi.mano")) {
			return;
		}
		final Class<?> clazz = Class.forName(x);
		if (clazz.isEnum()) {
			handleEnum((Class<? extends Enum>) clazz);
			return;
		}
		if (clazz.isInterface()
				|| Modifier.isAbstract(clazz.getModifiers())
				|| Exception.class.isAssignableFrom(clazz)) {
			return;
		}
		final Object obj = clazz.getConstructor().newInstance();
		final BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
		final PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		testObject(obj, props);
	}

	private static void handleEnum(final Class<? extends Enum> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final Enum en = Enum.valueOf(clazz, clazz.getEnumConstants()[0].name());
		en.toString();
		final Method m = ReflectionUtils.getMethodOrNull(en, "fromValue", String.class);
		if (m != null) {
			final String n = null;
			ReflectionUtils.invoke(m, en, n);
			ReflectionUtils.invoke(m, en, "badString");
			ReflectionUtils.invoke(m, en, en.toString());
		}
	}

	private static void testObject(final Object obj, final PropertyDescriptor[] props) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalArgumentException {
		for (final PropertyDescriptor propertyDescriptor : props) {
			final Method mr = propertyDescriptor.getReadMethod();
			if (null != mr) {
				mr.invoke(obj);
			}
			final Method mw = propertyDescriptor.getWriteMethod();
			if ((null != mw) && (null != mr)) {
				final Class<?> ret = mr.getReturnType();
				if (Modifier.isAbstract(ret.getModifiers())) {
					// continue;
				}
				mw.invoke(obj, createType(ret));
			}
		}
		obj.hashCode();
		obj.toString();
		obj.equals(null);
		obj.equals(props);
		obj.equals(obj);
		final EqualsVerifierReport rep = EqualsVerifier
				.simple()
				.forClass(obj.getClass())
				.suppress(Warning.INHERITED_DIRECTLY_FROM_OBJECT, Warning.SURROGATE_KEY)
				.report();
		System.out.println("" + rep.getMessage());
		if (obj instanceof AbstractTask) {
			executeCopy(obj);
		}
	}

	private static void executeCopy(final Object obj) {
		final Method m = ReflectionUtils.getMethodOrNull(obj, "copy");
		ReflectionUtils.invoke(m, obj);
	}

	private static Object createType(final Class<?> ret) throws NoSuchMethodException, SecurityException, IllegalArgumentException {
		if ("boolean".equals(ret.getCanonicalName()) || ret.isAssignableFrom(Boolean.class)) {
			return Boolean.TRUE;
		}
		if ("int".equals(ret.getCanonicalName()) || ret.isAssignableFrom(Boolean.class)) {
			return 123;
		}
		if ("long".equals(ret.getCanonicalName()) || ret.isAssignableFrom(Boolean.class)) {
			return 123L;
		}
		if ("double".equals(ret.getCanonicalName()) || ret.isAssignableFrom(Boolean.class)) {
			return 123d;
		}
		if (ret.isAssignableFrom(Set.class)) {
			return Set.of();
		}
		if (ret.isAssignableFrom(List.class)) {
			return List.of();
		}
		if (ret.isAssignableFrom(Map.class)) {
			return Map.of();
		}
		if (ret.isAssignableFrom(UUID.class)) {
			return UUID.randomUUID();
		}
		if (ret.isAssignableFrom(LocalTime.class)) {
			return LocalTime.now();
		}
		if (ret.isAssignableFrom(LocalDateTime.class)) {
			return LocalDateTime.now();
		}
		if (ret.isAssignableFrom(OffsetDateTime.class)) {
			return OffsetDateTime.now();
		}
		if (ret.isAssignableFrom(ZonedDateTime.class)) {
			return ZonedDateTime.now();
		}
		if (ret.isAssignableFrom(Long.class)) {
			return Long.valueOf(123);
		}
		if (ret.isAssignableFrom(Integer.class)) {
			return Integer.valueOf(123);
		}
		if (ret.isAssignableFrom(Double.class)) {
			return Double.valueOf(123D);
		}
		if (ret.isAssignableFrom(BigDecimal.class)) {
			return BigDecimal.ONE;
		}
		if (ret.isAssignableFrom(URI.class)) {
			return URI.create("http://localhost/");
		}
		if (ret.isAssignableFrom(NsTask.class)) {
			return new NetworkPolicyTask();
		}
		return createComplex(ret);
	}

	private static Object createComplex(final Class<?> ret) throws NoSuchMethodException, SecurityException, IllegalArgumentException {
		if (ret.isEnum()) {
			final Object[] cst = ret.getEnumConstants();
			return cst[0];
		}
		try {
			final Constructor<?> ctor = ret.getConstructor();
			return ctor.newInstance();
		} catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
			LOG.trace("", e);
			return null;
		}
	}

}
