package com.example.approval.service;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import jakarta.annotation.PostConstruct;

// import javax.annotation.PostConstruct;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
@Data
public class ConfigurationService {
    private final List<FieldConfig> fields = new ArrayList<>();
    private final Map<String, String> processTransitions = new HashMap<>();

    @Data
    @AllArgsConstructor
    public static class FieldConfig {
        private String id;
        private String label;
        private String type;
        private boolean required;
        private Map<String, List<Option>> categoryOptions = new HashMap<>();
    }

    @Data
    @AllArgsConstructor
    public static class Option {
        private String value;
        private String label;
    }

    @PostConstruct
    public void init() {
        loadFields();
        loadProcess();
    }

    private void loadFields() {
        try (InputStream is = getClass().getResourceAsStream("/config/fields.xml")) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            NodeList fieldNodes = doc.getElementsByTagName("field");

            for (int i = 0; i < fieldNodes.getLength(); i++) {
                Element element = (Element) fieldNodes.item(i);
                String id = element.getAttribute("id");
                String label = element.getAttribute("label");
                String type = element.getAttribute("type");
                boolean required = Boolean.parseBoolean(element.getAttribute("required"));

                FieldConfig config = new FieldConfig(id, label, type, required, new HashMap<>());

                if ("dropdown".equals(type)) {
                    NodeList options = element.getElementsByTagName("option");
                    if (options.getLength() > 0) {
                        // Simple dropdown
                        List<Option> simpleOptions = new ArrayList<>();
                        for (int j = 0; j < options.getLength(); j++) {
                            Element opt = (Element) options.item(j);
                            simpleOptions.add(new Option(opt.getAttribute("value"), opt.getAttribute("value")));
                        }
                        config.getCategoryOptions().put("DEFAULT", simpleOptions);
                    }

                    NodeList mainCats = element.getElementsByTagName("main-category");
                    for (int j = 0; j < mainCats.getLength(); j++) {
                        Element mainCat = (Element) mainCats.item(j);
                        String mainVal = mainCat.getAttribute("value");
                        List<Option> subOptions = new ArrayList<>();
                        NodeList subOpts = mainCat.getElementsByTagName("option");
                        for (int k = 0; k < subOpts.getLength(); k++) {
                            Element opt = (Element) subOpts.item(k);
                            subOptions.add(new Option(opt.getAttribute("value"), opt.getAttribute("value")));
                        }
                        config.getCategoryOptions().put(mainVal, subOptions);
                    }
                }
                fields.add(config);
            }
        } catch (Exception e) {
            logger.error("Failed to load fields.xml", e);
        }
    }

    private void loadProcess() {
        try (InputStream is = getClass().getResourceAsStream("/config/process.xml")) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);
            NodeList stepNodes = doc.getElementsByTagName("step");

            for (int i = 0; i < stepNodes.getLength(); i++) {
                Element step = (Element) stepNodes.item(i);
                String stepId = step.getAttribute("id");
                NodeList actionNodes = step.getElementsByTagName("action");
                for (int j = 0; j < actionNodes.getLength(); j++) {
                    Element action = (Element) actionNodes.item(j);
                    String actionId = action.getAttribute("id");
                    String target = action.getAttribute("target");
                    processTransitions.put(stepId + ":" + actionId, target);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load process.xml", e);
        }
    }

    public String getNextStep(String currentStep, String action) {
        return processTransitions.get(currentStep + ":" + action);
    }

    public List<FieldConfig> getFields() {
        return fields;
    }
}
