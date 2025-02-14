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

import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.HelmChart;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage;
import com.ubiqube.parser.tosca.scalar.Size;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class SoftwareImageMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final SwImage obj = podam.manufacturePojo(SwImage.class);
		obj.setContainerFormat("ARI");
		obj.setDiskFormat("QCOW2");
		obj.setMinDisk(new Size("1gib"));
		obj.setMinRam(new Size("1gib"));
		obj.setSize(new Size("1gib"));
		final SoftwareImageMapping mapper = Mappers.getMapper(SoftwareImageMapping.class);
		final SoftwareImage r = mapper.map(obj);
		assertNotNull(r);
		assertNull(mapper.map((SwImage) null));
	}

	@Test
	void testHelmChart() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final HelmChart obj = podam.manufacturePojo(HelmChart.class);
		final SoftwareImageMapping mapper = Mappers.getMapper(SoftwareImageMapping.class);
		SoftwareImage r = mapper.map(obj);
		assertNotNull(r);
		assertNull(mapper.map((HelmChart) null));
	}

	@Test
	void testOther() {
		assertNull(Mappers.getMapper(SoftwareImageMapping.class).mapToChecksum(null));
	}
}
