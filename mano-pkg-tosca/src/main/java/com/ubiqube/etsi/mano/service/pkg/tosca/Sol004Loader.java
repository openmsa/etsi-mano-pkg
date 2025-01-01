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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ubiqube.etsi.mano.Constants;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.service.pkg.PkgUtils;
import com.ubiqube.etsi.mano.sol004.CsarModeEnum;
import com.ubiqube.etsi.mano.utils.Version;
import com.ubiqube.parser.tosca.ParseException;
import com.ubiqube.parser.tosca.ToscaContext;
import com.ubiqube.parser.tosca.ToscaParser;
import com.ubiqube.parser.tosca.api.ToscaApi;
import com.ubiqube.parser.tosca.api.ToscaMapper;

import jakarta.annotation.Nonnull;

public class Sol004Loader {

	private static final String FOUND_NODE_IN_TOSCA_MODEL = "Found {} {} node in TOSCA model";

	private static final String JAR_PATH = "/tosca-class-%s-2.0.0-SNAPSHOT.jar";

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPackageReader.class);
	@Nonnull
	private final ToscaContext root;
	@Nonnull
	private final ToscaParser toscaParser;
	@Nonnull
	private final File tempFile;
	@Nonnull
	private final BinaryRepository repo;

	private ToscaApi toscaApi;

	public Sol004Loader(final InputStream data, final BinaryRepository repo, final UUID id) {
		this.repo = repo;
		tempFile = PkgUtils.fetchData(data);
		toscaParser = new ToscaParser(tempFile);
		final CsarModeEnum mode = toscaParser.getMode();
		if (mode == CsarModeEnum.DOUBLE_ZIP) {
			unpackAndResend(id);
		}
		root = toscaParser.getContext();
		prepareVersionSettings();
	}

	private void prepareVersionSettings() {
		final Version version = getVersion(root.getMetadata());
		final String jarPath = String.format(JAR_PATH, toJarVersions(version));
		final URL cls = this.getClass().getResource(jarPath);
		if (null == cls) {
			throw new ParseException("Unable to find " + jarPath);
		}
		final URLClassLoader urlLoader = URLClassLoader.newInstance(new URL[] { cls }, this.getClass().getClassLoader());
		Thread.currentThread().setContextClassLoader(urlLoader);
		final ToscaMapper toscaMapper = getVersionedMapperMethod(urlLoader);
		toscaApi = new ToscaApi(urlLoader, toscaMapper);
	}

	private static ToscaMapper getVersionedMapperMethod(final URLClassLoader urlLoader) {
		try (InputStream stream = urlLoader.getResourceAsStream("META-INF/tosca-resources.properties")) {
			final Properties props = new Properties();
			props.load(stream);
			final Class<?> clz = urlLoader.loadClass(props.getProperty("mapper"));
			return (ToscaMapper) clz.getDeclaredConstructor().newInstance();
		} catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
			throw new GenericException(e);
		}
	}

	private static String toJarVersions(final Version v) {
		return v.toString().replace(".", "");
	}

	private static Version getVersion(final Map<String, String> metadata) {
		final String author = metadata.get("template_author");
		if (null == author) {
			return new Version("2.5.1");
		}
		return Optional.ofNullable(metadata.get("template_version"))
				.map(Version::new)
				.orElseGet(() -> new Version("2.5.1"));
	}

	private void unpackAndResend(final UUID id) {
		try (final InputStream is = toscaParser.getCsarInputStream()) {
			repo.storeBinary(id, Constants.REPOSITORY_FILENAME_VNFD, is);
		} catch (final IOException e) {
			throw new ParseException(e);
		}
	}

	public ToscaParser getToscaParser() {
		return toscaParser;
	}

	public ToscaApi getToscaApi() {
		return toscaApi;
	}

}
