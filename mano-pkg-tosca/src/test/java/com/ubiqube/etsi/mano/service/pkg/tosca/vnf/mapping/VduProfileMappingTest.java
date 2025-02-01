package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.VduProfile;

import uk.co.jemos.podam.api.PodamFactoryImpl;

class VduProfileMappingTest {

	@Test
	void test() {
		final PodamFactoryImpl podam = new PodamFactoryImpl();
		podam.getStrategy().setDefaultNumberOfCollectionElements(1);
		final VduProfile obj = podam.manufacturePojo(VduProfile.class);
		obj.getNfviMaintenanceInfo().setSupportedMigrationType(List.of("NO_MIGRATION"));
		final VduProfileMapping mapper = Mappers.getMapper(VduProfileMapping.class);
		final com.ubiqube.etsi.mano.dao.mano.VduProfile r = mapper.mapToVduProfile(obj);
		assertNotNull(r);
	}

}
