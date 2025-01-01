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
package com.ubiqube.etsi.mano.service.pkg.tosca;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ubiqube.etsi.mano.Constants;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.service.pkg.PkgUtils;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.ArtefactReader;
import com.ubiqube.etsi.mano.service.pkg.tosca.vnf.mapping.ArtefactInformationsMapping;
import com.ubiqube.etsi.mano.sol004.CsarModeEnum;
import com.ubiqube.parser.tosca.Import;
import com.ubiqube.parser.tosca.Imports;
import com.ubiqube.parser.tosca.ParseException;
import com.ubiqube.parser.tosca.RepositoryDefinition;
import com.ubiqube.parser.tosca.ToscaContext;
import com.ubiqube.parser.tosca.ToscaParser;
import com.ubiqube.parser.tosca.api.ToscaApi;

import jakarta.annotation.Nonnull;

/**
 *
 * @author Olivier Vignaud {@literal <ovi@ubiqube.com>}
 *
 */
public abstract class AbstractPackageReader extends ArtefactReader implements Closeable {

	private static final String FOUND_NODE_IN_TOSCA_MODEL = "Found {} {} node in TOSCA model";

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPackageReader.class);
	@Nonnull
	private final ToscaContext root;
	@Nonnull
	private final ToscaParser toscaParser;
	@Nonnull
	private final File tempFile;
	@Nonnull
	private final BinaryRepository repo;

	private final ToscaApi toscaApi;

	protected AbstractPackageReader(final InputStream data, final BinaryRepository repo, final UUID id, final ArtefactInformationsMapping artefactInformationsMapping) {
		super(data, repo, id, artefactInformationsMapping);
		this.repo = repo;
		tempFile = PkgUtils.fetchData(data);
		toscaParser = getToscaParser();
		final CsarModeEnum mode = toscaParser.getMode();
		if (mode == CsarModeEnum.DOUBLE_ZIP) {
			unpackAndResend(id);
		}
		root = toscaParser.getContext();
		toscaApi = getToscaApi();
	}

	private void unpackAndResend(final UUID id) {
		try (final InputStream is = toscaParser.getCsarInputStream()) {
			repo.storeBinary(id, Constants.REPOSITORY_FILENAME_VNFD, is);
		} catch (final IOException e) {
			throw new ParseException(e);
		}
	}

	protected <T, U> Set<U> getSetOf(final Class<T> manoClass, final Function<T, U> mapper, final Map<String, String> parameters) {
		final List<T> list = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, list.size(), manoClass.getSimpleName());
		return list.stream()
				.map(mapper::apply)
				.collect(Collectors.toSet());
	}

	protected <T, U> List<U> getListOf(final Class<T> manoClass, final Function<T, U> mapper, final Map<String, String> parameters) {
		final List<T> obj = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, obj.size(), manoClass.getSimpleName());
		return obj.stream().map(mapper::apply).toList();
	}

	protected <T> List<T> getListOf(final Class<T> manoClass, final Map<String, String> parameters) {
		final List<T> obj = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, obj.size(), manoClass.getSimpleName());
		return obj.stream().toList();
	}

	protected <T> List<T> getObjects(final Class<T> manoClass, final Map<String, String> parameters) {
		final List<T> obj = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, obj.size(), manoClass.getSimpleName());
		return obj.stream().toList();
	}

	protected Map<String, RepositoryDefinition> getPkgRepositories() {
		return root.getRepositories();
	}

	public final List<String> getImports() {
		final Imports imps = this.root.getImports();
		final String entry = this.toscaParser.getEntryFileName();
		final List<String> imports = imps.entrySet().stream()
				.map(Entry::getValue)
				.map(Import::getResolved)
				.toList();
		final ArrayList<String> ret = new ArrayList<>(imports);
		ret.add(entry);
		return ret;
	}

	@Override
	public void close() throws IOException {
		Files.delete(tempFile.toPath());
	}
}
