package org.carlosacademic.springai.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Service for getting the rules of a file a return into a String
 */
@Service
public class ContextService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextService.class);

    public String getRulesFor(String name){
        try {
            var filename = String.format("classpath:context/%s.txt", name.
                    toLowerCase().replace(" ", "-"));

            return new DefaultResourceLoader()
                    .getResource(filename)
                    .getContentAsString(Charset.defaultCharset());
        }catch (IOException e){
            LOGGER.info("Context not found using the default context");
        }
        return "";
    }
}
