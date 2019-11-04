package cn.sexycode.myjpa.boot;

import cn.sexycode.myjpa.AvailableSettings;
import cn.sexycode.util.core.cls.classloading.ClassLoaderService;
import cn.sexycode.util.core.cls.classloading.ClassLoaderServiceImpl;
import cn.sexycode.util.core.file.ArchiveHelper;
import cn.sexycode.util.core.properties.PropertiesUtil;
import cn.sexycode.util.core.str.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.persistence.PersistenceException;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Used by Hibernate to parse {@code persistence.xml} files in SE environments.
 *
 * @author Steve Ebersole
 */
public class PersistenceXmlParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceXmlParser.class);

    /**
     * Find all persistence-units from all accessible {@code META-INF/persistence.xml} resources
     *
     * @param integration The Map of integration settings
     * @return List of descriptors for all discovered persistence-units.
     */
    public static List<ParsedPersistenceXmlDescriptor> locatePersistenceUnits(Map integration) {
        final PersistenceXmlParser parser = new PersistenceXmlParser(new ClassLoaderServiceImpl(),
                PersistenceUnitTransactionType.RESOURCE_LOCAL);
        parser.doResolve(integration);
        return new ArrayList<>(parser.persistenceUnits.values());
    }

    /**
     * Parse a specific {@code persistence.xml} with the assumption that it defines a single
     * persistence-unit.
     *
     * @param persistenceXmlUrl The {@code persistence.xml} URL
     * @return The single persistence-unit descriptor
     */
    public static ParsedPersistenceXmlDescriptor locateIndividualPersistenceUnit(URL persistenceXmlUrl) {
        return locateIndividualPersistenceUnit(persistenceXmlUrl, Collections.emptyMap());
    }

    /**
     * Parse a specific {@code persistence.xml} with the assumption that it defines a single
     * persistence-unit.
     *
     * @param persistenceXmlUrl The {@code persistence.xml} URL
     * @param integration       The Map of integration settings
     * @return The single persistence-unit descriptor
     */
    @SuppressWarnings("WeakerAccess")
    public static ParsedPersistenceXmlDescriptor locateIndividualPersistenceUnit(URL persistenceXmlUrl,
            Map integration) {
        return locateIndividualPersistenceUnit(persistenceXmlUrl, PersistenceUnitTransactionType.RESOURCE_LOCAL,
                integration);
    }

    /**
     * Parse a specific {@code persistence.xml} with the assumption that it defines a single
     * persistence-unit.
     *
     * @param persistenceXmlUrl The {@code persistence.xml} URL
     * @param transactionType   The specific PersistenceUnitTransactionType to incorporate into the persistence-unit descriptor
     * @param integration       The Map of integration settings
     * @return The single persistence-unit descriptor
     */
    @SuppressWarnings("WeakerAccess")
    public static ParsedPersistenceXmlDescriptor locateIndividualPersistenceUnit(URL persistenceXmlUrl,
            PersistenceUnitTransactionType transactionType, Map integration) {
        final PersistenceXmlParser parser = new PersistenceXmlParser(new ClassLoaderServiceImpl(), transactionType);

        parser.parsePersistenceXml(persistenceXmlUrl, integration);

        assert parser.persistenceUnits.size() == 1;

        return parser.persistenceUnits.values().iterator().next();
    }

    /**
     * Parse a specific {@code persistence.xml} and return the descriptor for the persistence-unit with matching name
     *
     * @param persistenceXmlUrl The {@code persistence.xml} URL
     * @param name              The PU name to match
     * @return The matching persistence-unit descriptor
     */
    public static ParsedPersistenceXmlDescriptor locateNamedPersistenceUnit(URL persistenceXmlUrl, String name) {
        return locateNamedPersistenceUnit(persistenceXmlUrl, name, Collections.emptyMap());
    }

    /**
     * Parse a specific {@code persistence.xml} and return the descriptor for the persistence-unit with matching name
     *
     * @param persistenceXmlUrl The {@code persistence.xml} URL
     * @param name              The PU name to match
     * @param integration       The Map of integration settings
     * @return The matching persistence-unit descriptor
     */
    @SuppressWarnings("WeakerAccess")
    public static ParsedPersistenceXmlDescriptor locateNamedPersistenceUnit(URL persistenceXmlUrl, String name,
            Map integration) {
        return locateNamedPersistenceUnit(persistenceXmlUrl, name, PersistenceUnitTransactionType.RESOURCE_LOCAL,
                integration);
    }

    /**
     * Parse a specific {@code persistence.xml} and return the descriptor for the persistence-unit with matching name
     *
     * @param persistenceXmlUrl The {@code persistence.xml} URL
     * @param name              The PU name to match
     * @param transactionType   The specific PersistenceUnitTransactionType to incorporate into the persistence-unit descriptor
     * @param integration       The Map of integration settings
     * @return The matching persistence-unit descriptor
     */
    @SuppressWarnings("WeakerAccess")
    public static ParsedPersistenceXmlDescriptor locateNamedPersistenceUnit(URL persistenceXmlUrl, String name,
            PersistenceUnitTransactionType transactionType, Map integration) {
        assert StringUtils.isNotEmpty(name);

        final PersistenceXmlParser parser = new PersistenceXmlParser(new ClassLoaderServiceImpl(), transactionType);

        parser.parsePersistenceXml(persistenceXmlUrl, integration);
        assert parser.persistenceUnits.containsKey(name);

        return parser.persistenceUnits.get(name);
    }

    /**
     * Intended only for use by Hibernate tests!
     * <p/>
     * Parses a specific persistence.xml file...
     */
    public static Map<String, ParsedPersistenceXmlDescriptor> parse(URL persistenceXmlUrl,
            PersistenceUnitTransactionType transactionType) {
        return parse(persistenceXmlUrl, transactionType, Collections.emptyMap());
    }

    /**
     * Generic method to parse a specified {@code persistence.xml} and return a Map of descriptors
     * for all discovered persistence-units keyed by the PU name.
     *
     * @param persistenceXmlUrl The URL of the {@code persistence.xml} to parse
     * @param transactionType   The specific PersistenceUnitTransactionType to incorporate into the persistence-unit descriptor
     * @param integration       The Map of integration settings
     * @return Map of persistence-unit descriptors keyed by the PU name
     */
    public static Map<String, ParsedPersistenceXmlDescriptor> parse(URL persistenceXmlUrl,
            PersistenceUnitTransactionType transactionType, Map integration) {
        PersistenceXmlParser parser = new PersistenceXmlParser(new ClassLoaderServiceImpl(), transactionType);

        parser.doResolve(integration);
        return parser.persistenceUnits;
    }

    private final ClassLoaderService classLoaderService;

    private final PersistenceUnitTransactionType defaultTransactionType;

    private final Map<String, ParsedPersistenceXmlDescriptor> persistenceUnits;

    private PersistenceXmlParser(ClassLoaderService classLoaderService,
            PersistenceUnitTransactionType defaultTransactionType) {
        this.classLoaderService = classLoaderService;
        this.defaultTransactionType = defaultTransactionType;
        this.persistenceUnits = new ConcurrentHashMap<>();
    }

    private void doResolve(Map integration) {
        final List<URL> xmlUrls = classLoaderService.locateResources("META-INF/persistence.xml");
        if (xmlUrls.isEmpty()) {
            //			LOG.unableToFindPersistenceXmlInClasspath();
        } else {
            parsePersistenceXml(xmlUrls, integration);
        }
    }

    private void parsePersistenceXml(List<URL> xmlUrls, Map integration) {
        for (URL xmlUrl : xmlUrls) {
            parsePersistenceXml(xmlUrl, integration);
        }
    }

    private void parsePersistenceXml(URL xmlUrl, Map integration) {
        LOGGER.trace("Attempting to parse persistence.xml file : {}", xmlUrl.toExternalForm());

        final Document doc = loadUrl(xmlUrl);
        final Element top = doc.getDocumentElement();

        final NodeList children = top.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                final Element element = (Element) children.item(i);
                final String tag = element.getTagName();
                if (tag.equals("persistence-unit")) {
                    final URL puRootUrl = ArchiveHelper.getJarURLFromURLEntry(xmlUrl, "/META-INF/persistence.xml");
                    ParsedPersistenceXmlDescriptor persistenceUnit = new ParsedPersistenceXmlDescriptor(puRootUrl);
                    bindPersistenceUnit(persistenceUnit, element);

                    if (persistenceUnits.containsKey(persistenceUnit.getName())) {
                        LOGGER.warn(persistenceUnit.getName());
                        continue;
                    }

                    // per JPA spec, any settings passed in to PersistenceProvider bootstrap methods should override
                    // values found in persistence.xml
                    if (integration.containsKey(AvailableSettings.JPA_PERSISTENCE_PROVIDER)) {
                        persistenceUnit.setProviderClassName(
                                (String) integration.get(AvailableSettings.JPA_PERSISTENCE_PROVIDER));
                    }
                    if (integration.containsKey(AvailableSettings.JPA_TRANSACTION_TYPE)) {
                        String transactionType = (String) integration.get(AvailableSettings.JPA_TRANSACTION_TYPE);
                        persistenceUnit.setTransactionType(parseTransactionType(transactionType));
                    }
                    if (integration.containsKey(AvailableSettings.JPA_JTA_DATASOURCE)) {
                        persistenceUnit.setJtaDataSource(integration.get(AvailableSettings.JPA_JTA_DATASOURCE));
                    }
                    if (integration.containsKey(AvailableSettings.JPA_NON_JTA_DATASOURCE)) {
                        persistenceUnit.setNonJtaDataSource(integration.get(AvailableSettings.JPA_NON_JTA_DATASOURCE));
                    }

                    decodeTransactionType(persistenceUnit);

                    Properties properties = persistenceUnit.getProperties();
                    PropertiesUtil.overrideProperties(properties, integration);

                    persistenceUnits.put(persistenceUnit.getName(), persistenceUnit);
                }
            }
        }
    }

    private void decodeTransactionType(ParsedPersistenceXmlDescriptor persistenceUnit) {
        // if transaction type is set already
        // 		use that value
        // else
        //		if JTA DS
        //			use JTA
        //		else if NOT JTA DS
        //			use RESOURCE_LOCAL
        //		else
        //			use defaultTransactionType
        if (persistenceUnit.getTransactionType() != null) {
            return;
        }

        if (persistenceUnit.getJtaDataSource() != null) {
            persistenceUnit.setTransactionType(PersistenceUnitTransactionType.JTA);
        } else if (persistenceUnit.getNonJtaDataSource() != null) {
            persistenceUnit.setTransactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL);
        } else {
            persistenceUnit.setTransactionType(defaultTransactionType);
        }
    }

    private void bindPersistenceUnit(ParsedPersistenceXmlDescriptor persistenceUnit, Element persistenceUnitElement) {
        final String name = persistenceUnitElement.getAttribute("name");
        if (StringUtils.isNotEmpty(name)) {
            LOGGER.trace("Persistence unit name from persistence.xml : {}", name);
            persistenceUnit.setName(name);
        }

        final PersistenceUnitTransactionType transactionType = parseTransactionType(
                persistenceUnitElement.getAttribute("transaction-type"));
        if (transactionType != null) {
            persistenceUnit.setTransactionType(transactionType);
        }

        NodeList children = persistenceUnitElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) children.item(i);
                String tag = element.getTagName();
                if (tag.equals("non-jta-data-source")) {
                    persistenceUnit.setNonJtaDataSource(extractContent(element));
                } else if (tag.equals("jta-data-source")) {
                    persistenceUnit.setJtaDataSource(extractContent(element));
                } else if (tag.equals("provider")) {
                    persistenceUnit.setProviderClassName(extractContent(element));
                } else if (tag.equals("class")) {
                    persistenceUnit.addClasses(extractContent(element));
                } else if (tag.equals("mapping-file")) {
                    persistenceUnit.addMappingFiles(extractContent(element));
                } else if (tag.equals("jar-file")) {
                    persistenceUnit.addJarFileUrl(ArchiveHelper.getURLFromPath(extractContent(element)));
                } else if (tag.equals("exclude-unlisted-classes")) {
                    persistenceUnit.setExcludeUnlistedClasses(extractBooleanContent(element, true));
                } else if (tag.equals("delimited-identifiers")) {
                    persistenceUnit.setUseQuotedIdentifiers(true);
                } else if (tag.equals("validation-mode")) {
                    persistenceUnit.setValidationMode(extractContent(element));
                } else if (tag.equals("shared-cache-mode")) {
                    persistenceUnit.setSharedCacheMode(extractContent(element));
                } else if (tag.equals("properties")) {
                    NodeList props = element.getChildNodes();
                    for (int j = 0; j < props.getLength(); j++) {
                        if (props.item(j).getNodeType() == Node.ELEMENT_NODE) {
                            Element propElement = (Element) props.item(j);
                            if (!"property".equals(propElement.getTagName())) {
                                continue;
                            }
                            String propName = propElement.getAttribute("name").trim();
                            String propValue = propElement.getAttribute("value").trim();
                            if (propValue.isEmpty()) {
                                //fall back to the natural (Hibernate) way of description
                                propValue = extractContent(propElement, "");
                            }
                            persistenceUnit.getProperties().put(propName, propValue);
                        }
                    }
                }
            }
        }
    }

    private static String extractContent(Element element) {
        return extractContent(element, null);
    }

    private static String extractContent(Element element, String defaultStr) {
        if (element == null) {
            return defaultStr;
        }

        NodeList children = element.getChildNodes();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.TEXT_NODE
                    || children.item(i).getNodeType() == Node.CDATA_SECTION_NODE) {
                result.append(children.item(i).getNodeValue());
            }
        }
        return result.toString().trim();
    }

    private static boolean extractBooleanContent(Element element, boolean defaultBool) {
        String content = extractContent(element);
        if (content != null && content.length() > 0) {
            return Boolean.valueOf(content);
        }
        return defaultBool;
    }

    private static PersistenceUnitTransactionType parseTransactionType(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        } else if (value.equalsIgnoreCase("JTA")) {
            return PersistenceUnitTransactionType.JTA;
        } else if (value.equalsIgnoreCase("RESOURCE_LOCAL")) {
            return PersistenceUnitTransactionType.RESOURCE_LOCAL;
        } else {
            throw new PersistenceException("Unknown persistence unit transaction type : " + value);
        }
    }

    private Document loadUrl(URL xmlUrl) {
        final String resourceName = xmlUrl.toExternalForm();
        try {
            URLConnection conn = xmlUrl.openConnection();
            conn.setUseCaches(false); //avoid JAR locking on Windows and Tomcat
            try {
                try (InputStream inputStream = conn.getInputStream()) {
                    final InputSource inputSource = new InputSource(inputStream);
                    try {
                        DocumentBuilder documentBuilder = documentBuilderFactory().newDocumentBuilder();
                        try {
                            Document document = documentBuilder.parse(inputSource);
                            //							validate( document );
                            return document;
                        } catch (SAXException | IOException e) {
                            throw new PersistenceException("Unexpected error parsing [" + resourceName + "]", e);
                        }
                    } catch (ParserConfigurationException e) {
                        throw new PersistenceException("Unable to generate javax.xml.parsers.DocumentBuilder instance",
                                e);
                    }
                }
            } catch (IOException e) {
                throw new PersistenceException("Unable to obtain input stream from [" + resourceName + "]", e);
            }
        } catch (IOException e) {
            throw new PersistenceException("Unable to access [" + resourceName + "]", e);
        }
    }

    private DocumentBuilderFactory documentBuilderFactory;

    private DocumentBuilderFactory documentBuilderFactory() {
        if (documentBuilderFactory == null) {
            documentBuilderFactory = buildDocumentBuilderFactory();
        }
        return documentBuilderFactory;
    }

    private DocumentBuilderFactory buildDocumentBuilderFactory() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory;
    }

    public static class ErrorHandlerImpl implements ErrorHandler {
        private List<SAXException> errors;

        ErrorHandlerImpl(List<SAXException> errors) {
            this.errors = errors;
        }

        @Override
        public void error(SAXParseException error) {
            errors.add(error);
        }

        @Override
        public void fatalError(SAXParseException error) {
            errors.add(error);
        }

        @Override
        public void warning(SAXParseException warn) {
            LOGGER.trace(extractInfo(warn));
        }
    }

    private static String extractInfo(SAXException error) {
        if (error instanceof SAXParseException) {
            return "Error parsing XML [line : " + ((SAXParseException) error).getLineNumber() + ", column : "
                    + ((SAXParseException) error).getColumnNumber() + "] : " + error.getMessage();
        } else {
            return "Error parsing XML : " + error.getMessage();
        }
    }
}
