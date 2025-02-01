package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.vim.VnfStorage;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.VirtualObjectStorage;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VirtualObjectStorageMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final VirtualObjectStorage obj = podam.manufacturePojo(VirtualObjectStorage.class);
		final VirtualObjectStorageMapping mapper = Mappers.getMapper(VirtualObjectStorageMapping.class);
		final VnfStorage r = mapper.map(obj);
		assertNotNull(r);
	}

}
