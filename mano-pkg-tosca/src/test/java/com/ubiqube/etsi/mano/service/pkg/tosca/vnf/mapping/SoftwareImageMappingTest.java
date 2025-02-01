package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class SoftwareImageMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final SwImage obj = podam.manufacturePojo(SwImage.class);
		obj.setContainerFormat("ARI");
		obj.setDiskFormat("QCOW2");
		final SoftwareImageMapping mapper = Mappers.getMapper(SoftwareImageMapping.class);
		final SoftwareImage r = mapper.map(obj);
		assertNotNull(r);
	}

}
