/**
 *     Copyright (C) 2019-2024 Ubiqube.
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
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping;

import java.util.Map;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import com.ubiqube.etsi.mano.dao.mano.vim.Checksum;
import com.ubiqube.etsi.mano.dao.mano.vim.SoftwareImage;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.parser.tosca.Artifact;
import com.ubiqube.parser.tosca.objects.tosca.artifacts.nfv.SwImage;
import com.ubiqube.parser.tosca.objects.tosca.datatypes.nfv.ChecksumData;
import com.ubiqube.parser.tosca.scalar.Size;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SoftwareImageMapping {

	@Named("softwareImage")
	default SoftwareImage mapTosoftwareImage(final Map<String, Artifact> o) {
		if ((null == o) || o.isEmpty()) {
			return null;
		}
		final Artifact obj = o.entrySet().iterator().next().getValue();
		if (obj instanceof final SwImage swi) {
			return map(swi);
		}
		throw new GenericException("Unknown instance type of artifact: " + o.getClass().getName());
	}

	@Mapping(target = "architecture", ignore = true)
	@Mapping(target = "audit", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "imagePath", ignore = true)
	@Mapping(target = "imageUri", ignore = true)
	@Mapping(target = "isEncrypted", ignore = true)
	@Mapping(target = "nfvoPath", ignore = true)
	@Mapping(target = "userMetadata", ignore = true)
	@Mapping(target = "vimId", ignore = true)
	SoftwareImage map(SwImage swi);

	default long map(final Size value) {
		if (null == value) {
			return 0;
		}
		return value.getValue().longValue();
	}

	@Mapping(target = "md5", ignore = true)
	@Mapping(target = "sha256", ignore = true)
	@Mapping(target = "sha512", ignore = true)
	Checksum mapToChecksum(ChecksumData o);
}
