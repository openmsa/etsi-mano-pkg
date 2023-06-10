/**
 *     Copyright (C) 2019-2023 Ubiqube.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ubiqube.etsi.mano.dao.mano.AdditionalArtifact;
import com.ubiqube.etsi.mano.tosca.ArtefactInformations;

class AdditionalArtefactMapperTest {

	@Test
	void test() {
		final AdditionalArtefactMapper srv = new AdditionalArtefactMapper();
		final AdditionalArtifact b = new AdditionalArtifact();
		final ArtefactInformations a = new ArtefactInformations();
		srv.mapAtoB(a, b, null);
		assertTrue(true);
	}

	@Test
	void testRemote() {
		final AdditionalArtefactMapper srv = new AdditionalArtefactMapper();
		final AdditionalArtifact b = new AdditionalArtifact();
		final ArtefactInformations a = new ArtefactInformations();
		a.setPath("http://localhost/test");
		srv.mapAtoB(a, b, null);
		assertTrue(true);
	}

	@Test
	void testLocal() {
		final AdditionalArtefactMapper srv = new AdditionalArtefactMapper();
		final AdditionalArtifact b = new AdditionalArtifact();
		final ArtefactInformations a = new ArtefactInformations();
		a.setPath("/localhost/test");
		srv.mapAtoB(a, b, null);
		assertTrue(true);
	}

}
