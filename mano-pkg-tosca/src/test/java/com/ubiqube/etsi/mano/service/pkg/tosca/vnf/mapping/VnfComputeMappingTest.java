package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.VnfCompute;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage;
import com.ubiqube.parser.tosca.objects.tosca.nodes.nfv.vdu.Compute;
import com.ubiqube.parser.tosca.scalar.Frequency;

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
		final VnfComputeMapping mapper = Mappers.getMapper(VnfComputeMapping.class);
		final VnfCompute r = mapper.map(obj);
		assertNotNull(r);
	}

}
