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

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.vim.VnfStorage;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.VirtualObjectStorage;
import com.ubiqube.parser.tosca.scalar.Size;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VirtualObjectStorageMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final VirtualObjectStorage obj = podam.manufacturePojo(VirtualObjectStorage.class);
		final VirtualObjectStorageMapping mapper = Mappers.getMapper(VirtualObjectStorageMapping.class);
		obj.getVirtualObjectStorageData().setMaxSizeOfStorage(new Size("1gib"));
		final VnfStorage r = mapper.map(obj);
		assertNotNull(r);
	}

}
