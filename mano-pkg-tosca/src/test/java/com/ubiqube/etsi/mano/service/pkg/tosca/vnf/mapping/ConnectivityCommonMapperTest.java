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

import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.CpProtocolData;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class ConnectivityCommonMapperTest {
	private final ConnectivityCommonMapper mapper = Mappers.getMapper(ConnectivityCommonMapper.class);
	private final PodamFactoryImpl podam;

	public ConnectivityCommonMapperTest() {
		podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
	}

	@Test
	void test() {
		assertNull(mapper.mapToVlProtocolData(null));
		CpProtocolData o = podam.manufacturePojo(com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.CpProtocolData.class);
		assertNotNull(mapper.mapToVlProtocolData(o));
	}

}
