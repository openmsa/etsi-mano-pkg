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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ScaleInfo;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.InstantiationLevels;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduInitialDelta;
import com.ubiqube.parser.tosca.objects.tosca.policies.nfv.VduScalingAspectDeltas;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class ScalingMappingTest {

	@Test
	void testVduInitialDelta() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final VduInitialDelta obj = podam.manufacturePojo(VduInitialDelta.class);
		final ScalingMapping mapper = Mappers.getMapper(ScalingMapping.class);
		final com.ubiqube.etsi.mano.service.pkg.bean.VduInitialDelta r = mapper.mapToVduInitialDelta(obj);
		assertNotNull(r);
	}

	@Test
	void testVduScalingAspectDeltas() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final VduScalingAspectDeltas obj = podam.manufacturePojo(VduScalingAspectDeltas.class);
		final ScalingMapping mapper = Mappers.getMapper(ScalingMapping.class);
		final com.ubiqube.etsi.mano.service.pkg.bean.VduScalingAspectDeltas r = mapper.mapToVduScalingAspectDeltas(obj);
		assertNotNull(r);
	}

	@Test
	void testScaleInfo() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final ScaleInfo obj = podam.manufacturePojo(ScaleInfo.class);
		final ScalingMapping mapper = Mappers.getMapper(ScalingMapping.class);
		final com.ubiqube.etsi.mano.service.pkg.bean.ScaleInfo r = mapper.mapToScaleInfo(obj);
		assertNotNull(r);
	}

	//
	@Test
	void testInstantiationLevels() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final InstantiationLevels obj = podam.manufacturePojo(InstantiationLevels.class);
		final ScalingMapping mapper = Mappers.getMapper(ScalingMapping.class);
		final com.ubiqube.etsi.mano.service.pkg.bean.InstantiationLevels r = mapper.mapToInstantiationLevels(obj);
		assertNotNull(r);
	}
}
