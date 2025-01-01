/**
 * Copyright (C) 2019-2025 Ubiqube.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import java.net.URI;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ubiqube.etsi.mano.dao.mano.AdditionalArtifact;
import com.ubiqube.etsi.mano.tosca.ArtefactInformations;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ArtefactInformationsMapping {
	/** Logger. */
	Logger LOG = LoggerFactory.getLogger(ArtefactInformationsMapping.class);

	@Mapping(target = "artifactClassification", source = "classifier")
	@Mapping(target = "artifactPath", source = "path")
	@Mapping(target = "artifactURI", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "isEncrypted", source = "encrypted")
	@Mapping(target = "nonManoArtifactSetId", source = "nonManoSetIndentifier")
	@Mapping(target = "repository", ignore = true)
	@Mapping(source = "checksum", target = "checksum.hash")
	@Mapping(source = "algorithm", target = "checksum.algorithm")
	AdditionalArtifact map(ArtefactInformations aa);

	@AfterMapping
	default void afterMapping(@MappingTarget final AdditionalArtifact o) {
		final String p = o.getArtifactPath();
		if (null != p) {
			if (isRemote(p)) {
				o.setArtifactURI(p);
				o.setArtifactPath(null);
			} else {
				o.setArtifactPath(p);
				o.setArtifactURI(null);
			}
		}
	}

	default boolean isRemote(final String p) {
		try {
			final URI uri = URI.create(p);
			return !uri.getScheme().isEmpty();
		} catch (final RuntimeException e) {
			LOG.trace("", e);
		}
		return false;
	}

}
