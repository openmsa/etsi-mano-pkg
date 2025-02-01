package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.etsi.mano.dao.mano.AdditionalArtifact;
import com.ubiqube.etsi.mano.tosca.ArtefactInformations;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class ArtefactInformationsMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final ArtefactInformations obj = podam.manufacturePojo(ArtefactInformations.class);
		obj.setClassifier("LICENSE");
		final ArtefactInformationsMapping mapper = Mappers.getMapper(ArtefactInformationsMapping.class);
		final AdditionalArtifact r = mapper.map(obj);
		assertNotNull(r);
	}

}
