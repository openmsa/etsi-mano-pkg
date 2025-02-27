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

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScaleInfo;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class ScaleCommonMappingTest {
	private final ScaleCommonMapping mapper = Mappers.getMapper(ScaleCommonMapping.class);
	private final PodamFactoryImpl podam;

	public ScaleCommonMappingTest() {
		podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
	}

	@Test
	void test() {
		assertNull(mapper.mapToScaleInfo(null));
		ScaleInfo o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScaleInfo.class);
		assertNotNull(mapper.mapToScaleInfo(o));
	}

}
