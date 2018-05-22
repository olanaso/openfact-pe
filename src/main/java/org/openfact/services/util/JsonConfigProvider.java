package org.openfact.services.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.openfact.Config;
import org.openfact.common.util.StringPropertyReplacer;

import java.util.Properties;

public class JsonConfigProvider implements Config.ConfigProvider {

    private Properties properties;

    private JsonNode config;

    public JsonConfigProvider(JsonNode config, Properties properties) {
        this.config = config;
        this.properties = properties;
    }

    private static JsonNode getNode(JsonNode root, String... path) {
        if (root == null) {
            return null;
        }
        JsonNode n = root;
        for (String p : path) {
            n = n.get(p);
            if (n == null) {
                return null;
            }
        }
        return n;
    }

    @Override
    public String getProvider(String spi) {
        JsonNode n = getNode(config, spi, "provider");
        return n != null ? replaceProperties(n.textValue()) : null;
    }

    @Override
    public Config.Scope scope(String... path) {
        return new JsonScope(getNode(config, path));
    }

    private String replaceProperties(String value) {
        return StringPropertyReplacer.replaceProperties(value, properties);
    }

    public class JsonScope implements Config.Scope {

        private JsonNode config;

        public JsonScope(JsonNode config) {
            this.config = config;
        }

        @Override
        public String get(String key) {
            return get(key, null);
        }

        @Override
        public String get(String key, String defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            return replaceProperties(n.textValue());
        }

        @Override
        public String[] getArray(String key) {
            if (config == null) {
                return null;
            }

            JsonNode n = config.get(key);
            if (n == null) {
                return null;
            } else if (n.isArray()) {
                String[] a = new String[n.size()];
                for (int i = 0; i < a.length; i++) {
                    a[i] = replaceProperties(n.get(i).textValue());
                }
                return a;
            } else {
                return new String[]{replaceProperties(n.textValue())};
            }
        }

        @Override
        public Integer getInt(String key) {
            return getInt(key, null);
        }

        @Override
        public Integer getInt(String key, Integer defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            if (n.isTextual()) {
                return Integer.parseInt(replaceProperties(n.textValue()));
            } else {
                return n.intValue();
            }
        }

        @Override
        public Long getLong(String key) {
            return getLong(key, null);
        }

        @Override
        public Long getLong(String key, Long defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            if (n.isTextual()) {
                return Long.parseLong(replaceProperties(n.textValue()));
            } else {
                return n.longValue();
            }
        }

        @Override
        public Boolean getBoolean(String key) {
            return getBoolean(key, null);
        }

        @Override
        public Boolean getBoolean(String key, Boolean defaultValue) {
            if (config == null) {
                return defaultValue;
            }
            JsonNode n = config.get(key);
            if (n == null) {
                return defaultValue;
            }
            if (n.isTextual()) {
                return Boolean.parseBoolean(replaceProperties(n.textValue()));
            } else {
                return n.booleanValue();
            }
        }

        @Override
        public Config.Scope scope(String... path) {
            return new JsonScope(getNode(config, path));
        }

    }

}
