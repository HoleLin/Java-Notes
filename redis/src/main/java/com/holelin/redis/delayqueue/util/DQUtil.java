package com.holelin.redis.delayqueue.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;


public class DQUtil {

    private static final String KEY_PREFIX = "card_im_delay_queue";

    private static final String DELIMITER = ":";

    public static String buildKey(String namespace, String key) {
        Preconditions.checkArgument(StringUtils.isNotBlank(namespace));
        Preconditions.checkArgument(StringUtils.isNotBlank(key));
        return KEY_PREFIX + DELIMITER + namespace + DELIMITER + key;
    }

    public static String buildKey(String key) {
        return KEY_PREFIX + DELIMITER + key;
    }
}
