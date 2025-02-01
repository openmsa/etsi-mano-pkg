package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.MonitoringParams;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VnfMonitoringParameter;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VnfMonitoringCommonMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final VnfMonitoringParameter obj = podam.manufacturePojo(VnfMonitoringParameter.class);
		final VnfMonitoringCommonMapping mapper = Mappers.getMapper(VnfMonitoringCommonMapping.class);
		final MonitoringParams r = mapper.mapToMonitoringParams(obj, "toscaName");
		assertNotNull(r);
	}

}
