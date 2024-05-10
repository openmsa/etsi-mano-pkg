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
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.OsContainerMapping;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainer;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.OsContainerDeployableUnit;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class OsContainerMappingTest {

	@Test
	void testName() throws Exception {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final OsContainer obj = podam.manufacturePojo(OsContainer.class);
		final OsContainerMapping mapper = Mappers.getMapper(OsContainerMapping.class);
		final com.ubiqube.etsi.mano.dao.mano.pkg.OsContainer r = mapper.mapToOsContainer(obj);
		assertNotNull(r);
	}

	@Test
	void testOsContainerDeployableUnit() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final OsContainerDeployableUnit obj = podam.manufacturePojo(OsContainerDeployableUnit.class);
		final OsContainerMapping mapper = Mappers.getMapper(OsContainerMapping.class);
		obj.setNfviConstraints(Map.of("NODE_POOL", "NODE_POOL"));
		obj.setMcioConstraintParams(List.of("NODE_POOL"));
		final com.ubiqube.etsi.mano.dao.mano.pkg.OsContainerDeployableUnit r = mapper.mapToOsContainerDeployableUnit(obj);
		assertNotNull(r);
	}
}