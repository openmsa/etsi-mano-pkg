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
