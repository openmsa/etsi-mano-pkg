/**
 * Copyright (C) 2019-2025 Ubiqube.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScaleInfo;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.VNF;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VnfMappingTest {
	private final VnfMapping mapper = Mappers.getMapper(VnfMapping.class);
	private final PodamFactoryImpl podam;

	VnfMappingTest() {
		podam = new PodamFactoryImpl();
	}

	@Test
	void test() {
		assertNotNull(mapper.mapSi(null));
		ScaleInfo o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScaleInfo.class);
		assertNotNull(mapper.mapSi(Map.of("key", o)));
	}

	@Test
	void testMap() {
		assertNotNull(mapper.map((Map) null));
	}

	@Test
	void testVNF() {
		assertNull(mapper.map((VNF) null));
		VNF o = podam.manufacturePojo(VNF.class);
		o.setLocalizationLanguages(null);
		o.setOverloadedAttributes(null);
		o.setOverloadedInterfaces(null);
		o.setVnfmInfo(null);
		assertNotNull(mapper.map(o));
	}

	@Test
	void testVnfMonitoringParameter() {
		assertNull(mapper.map(null, null));
	}
}
