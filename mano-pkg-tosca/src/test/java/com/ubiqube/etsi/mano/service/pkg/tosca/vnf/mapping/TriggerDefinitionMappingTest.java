package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.parser.tosca.TriggerDefinition;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class TriggerDefinitionMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final TriggerDefinition obj = podam.manufacturePojo(TriggerDefinition.class);
		final TriggerDefinitionMapping mapper = Mappers.getMapper(TriggerDefinitionMapping.class);
		final com.ubiqube.etsi.mano.dao.mano.TriggerDefinition r = mapper.mapToTriggerDefinition(obj);
		assertNotNull(r);
	}

}
