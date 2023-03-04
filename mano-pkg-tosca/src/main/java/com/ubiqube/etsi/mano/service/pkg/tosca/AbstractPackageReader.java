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
package com.ubiqube.etsi.mano.service.pkg.tosca;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ubiqube.etsi.mano.Constants;
import com.ubiqube.etsi.mano.exception.GenericException;
import com.ubiqube.etsi.mano.repository.BinaryRepository;
import com.ubiqube.etsi.mano.service.pkg.PkgUtils;
import com.ubiqube.etsi.mano.sol004.CsarModeEnum;
import com.ubiqube.etsi.mano.tosca.ArtefactInformations;
import com.ubiqube.parser.tosca.Import;
import com.ubiqube.parser.tosca.Imports;
import com.ubiqube.parser.tosca.ParseException;
import com.ubiqube.parser.tosca.ToscaContext;
import com.ubiqube.parser.tosca.ToscaParser;
import com.ubiqube.parser.tosca.api.OrikaMapper;
import com.ubiqube.parser.tosca.api.ToscaApi;
import com.ubiqube.parser.tosca.scalar.Version;

import jakarta.annotation.Nonnull;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 *
 * @author Olivier Vignaud <ovi@ubiqube.com>
 *
 */
public abstract class AbstractPackageReader implements Closeable {

	private static final String FOUND_NODE_IN_TOSCA_MODEL = "Found {} {} node in TOSCA model";

	private static final String JAR_PATH = "/tosca-class-%s-2.0.0-SNAPSHOT.jar";

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPackageReader.class);
	@Nonnull
	private final ToscaContext root;
	@Nonnull
	private final MapperFacade mapper;
	@Nonnull
	private final ToscaParser toscaParser;
	@Nonnull
	private final File tempFile;
	@Nonnull
	private final BinaryRepository repo;

	private URLClassLoader urlLoader;

	private MapperFacade toscaMapper;

	private ToscaApi toscaApi;

	protected AbstractPackageReader(final InputStream data, final BinaryRepository repo, final UUID id) {
		this.repo = repo;
		tempFile = PkgUtils.fetchData(data);
		toscaParser = new ToscaParser(tempFile);
		final CsarModeEnum mode = toscaParser.getMode();
		if (mode == CsarModeEnum.DOUBLE_ZIP) {
			unpackAndResend(id);
		}
		root = toscaParser.getContext();
		final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		additionalMapping(mapperFactory);
		final ConverterFactory converterFactory = mapperFactory.getConverterFactory();
		converterFactory.registerConverter(new SizeConverter());
		converterFactory.registerConverter(new TimeConverter());
		converterFactory.registerConverter(new FrequencyConverter());
		mapper = mapperFactory.getMapperFacade();
		prepareVersionSettings();
	}

	private void prepareVersionSettings() {
		final Version version = getVersion(root.getMetadata());
		final String jarPath = String.format(JAR_PATH, toJarVersions(version));
		final URL cls = this.getClass().getResource(jarPath);
		if (null == cls) {
			throw new ParseException("Unable to find " + jarPath);
		}
		this.urlLoader = URLClassLoader.newInstance(new URL[] { cls }, this.getClass().getClassLoader());
		Thread.currentThread().setContextClassLoader(urlLoader);
		toscaMapper = createToscaMapper().getMapperFacade();
		toscaApi = new ToscaApi(urlLoader, toscaMapper);
	}

	private MapperFactory createToscaMapper() {
		final MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
		final ConverterFactory converterFactory = mapperFactory.getConverterFactory();
		converterFactory.registerConverter(new SizeConverter());
		converterFactory.registerConverter(new Size2Converter());
		converterFactory.registerConverter(new TimeConverter());
		converterFactory.registerConverter(new Time2Converter());
		converterFactory.registerConverter(new FrequencyConverter());
		converterFactory.registerConverter(new Frequency2Converter());
		converterFactory.registerConverter(new Range2Converter());
		final OrikaMapper meh = getVersionedMapperMethod();
		meh.configureMapper(mapperFactory);
		return mapperFactory;
	}

	private OrikaMapper getVersionedMapperMethod() {
		try (InputStream stream = urlLoader.getResourceAsStream("META-INF/tosca-resources.properties")) {
			final Properties props = new Properties();
			props.load(stream);
			final Class<?> clz = urlLoader.loadClass(props.getProperty("mapper"));
			return (OrikaMapper) clz.getDeclaredConstructor().newInstance();
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

	protected abstract void additionalMapping(MapperFactory mapperFactory);

	protected <T, U> Set<U> getSetOf(final Class<T> manoClass, final Class<U> to, final Map<String, String> parameters) {
		final List<T> list = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, list.size(), manoClass.getSimpleName());
		return list.stream()
				.map(x -> mapper.map(x, to))
				.collect(Collectors.toSet());
	}

	protected <T> Set<T> getSetOf(final Class<T> manoClass, final Map<String, String> parameters) {
		final List<T> list = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, list.size(), manoClass.getSimpleName());
		return list.stream()
				.collect(Collectors.toSet());
	}

	protected <T, U> List<U> getListOf(final Class<T> manoClass, final Class<U> to, final Map<String, String> parameters) {
		final List<T> obj = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, obj.size(), manoClass.getSimpleName());
		return mapper.mapAsList(obj, to);
	}

	protected <U> List<U> getObjects(final Class<U> manoClass, final Map<String, String> parameters) {
		final List<U> obj = toscaApi.getObjects(root, parameters, manoClass);
		LOG.debug(FOUND_NODE_IN_TOSCA_MODEL, obj.size(), manoClass.getSimpleName());
		return toscaMapper.mapAsList(obj, manoClass);
	}

	protected List<ArtefactInformations> getCsarFiles() {
		return toscaParser.getFiles();
	}

	protected <U> Set<U> getCsarFiles(final Class<U> dest) {
		return toscaParser.getFiles().stream()
				.map(x -> mapper.map(x, dest))
				.collect(Collectors.toSet());
	}

	protected <U> Set<U> getSetOf(final Class<U> to, final Map<String, String> parameters, final Class<?>... toscaClass) {
		final Set<U> ret = new LinkedHashSet<>();
		for (final Class<?> class1 : toscaClass) {
			final Set<U> set = getSetOf(class1, to, parameters);
			ret.addAll(set);
		}
		return ret;
	}

	protected MapperFacade getMapper() {
		return mapper;
	}

	@Override
	public void close() throws IOException {
		Files.delete(tempFile.toPath());
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

	public final String getManifestContent() {
		return this.toscaParser.getManifestContent();
	}

	public byte[] getFileContent(final String fileName) {
		return toscaParser.getFileContent(fileName);
	}

	public InputStream getFileInputStream(final String path) {
		return toscaParser.getFileInputStream(path);
	}

	public List<String> getVnfdFiles(final boolean includeSignature) {
		final List<String> imports = getImports();
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
