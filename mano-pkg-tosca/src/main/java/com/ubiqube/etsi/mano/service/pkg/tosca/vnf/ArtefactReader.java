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
package com.ubiqube.etsi.mano.service.pkg.tosca.vnf;

import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ubiqube.etsi.mano.dao.mano.AdditionalArtifact;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.service.pkg.tosca.Sol004Loader;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.tosca.ArtefactInformations;
import com.ubiqube.parser.tosca.ToscaParser;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ArtefactReader extends Sol004Loader {
	@Nonnull
	private final ToscaParser toscaParser;
	private final ArtefactInformationsMapping artefactInformationsMapping;

	public ArtefactReader(final InputStream data, final BinaryRepository repo, final UUID id, final ArtefactInformationsMapping artefactInformationsMapping) {
		super(data, repo, id);
		this.toscaParser = getToscaParser();
		this.artefactInformationsMapping = artefactInformationsMapping;
	}

	protected List<ArtefactInformations> getCsarFiles() {
		return toscaParser.getFiles();
	}

	protected <U> Set<AdditionalArtifact> getCsarFiles(final Class<U> dest) {
		return toscaParser.getFiles().stream()
				.map(x -> artefactInformationsMapping.map(x))
				.collect(Collectors.toSet());
	}

	@Nullable
	public final String getManifestContent() {
		return this.toscaParser.getManifestContent();
	}

	public byte[] getFileContent(final String fileName) {
		return toscaParser.getFileContent(fileName);
	}

	public InputStream getFileInputStream(final String path) {
		return toscaParser.getFileInputStream(path);
	}

	public List<String> getVnfdFiles(final List<String> imports, final boolean includeSignature) {
		final Set<String> ret = new HashSet<>(imports);
		if (imports.size() > 1) {
			ret.add("TOSCA-Metadata/TOSCA.meta");
		}
		if (includeSignature) {
			final List<ArtefactInformations> files = toscaParser.getFiles();
			final Set<String> cert = files.stream()
					.filter(x -> ret.contains(x.getCertificate()))
					.flatMap(x -> Stream.of(x.getSignature(), x.getCertificate()))
					.collect(Collectors.toSet());
			ret.addAll(cert);
		}
		return ret.stream().toList();
	}
}
