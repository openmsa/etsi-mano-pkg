package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.HelmChart;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.Compute;
import com.ubiqube.parser.tosca.scalar.Frequency;
import com.ubiqube.parser.tosca.scalar.Size;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VnfComputeMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final Compute obj = podam.manufacturePojo(Compute.class);
		obj.setArtifacts(Map.of("img", new SwImage()));
		obj.getVirtualCompute().getVirtualCpu().setVirtualCpuClock(new Frequency("1ghz"));
		obj.getVduProfile().getNfviMaintenanceInfo().setSupportedMigrationType(List.of("NO_MIGRATION"));
		obj.getVirtualCompute().getVirtualMemory().setVirtualMemSize(new Size("1gib"));
		final VnfComputeMapping mapper = Mappers.getMapper(VnfComputeMapping.class);
		final VnfCompute r = mapper.map(obj);
		assertNotNull(r);
		assertNull(mapper.map((Compute) null));
	}

	@Test
	void testHelmCahrt() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final HelmChart obj = podam.manufacturePojo(HelmChart.class);
		final VnfComputeMapping mapper = Mappers.getMapper(VnfComputeMapping.class);
		SoftwareImage r = mapper.map(obj);
		assertNotNull(r);
		assertNull(mapper.map((HelmChart) null));
	}

}
