package griffon.domain;

import java.util.Map;

/**
 *
 */
public final class GriffonPersistenceUtil {
    public static final String ARGUMENT_MAX = "max";
    public static final String ARGUMENT_OFFSET = "offset";
    public static final String ARGUMENT_ORDER = "order";
    public static final String ARGUMENT_SORT = "sort";
    public static final String ORDER_DESC = "desc";
    public static final String ORDER_ASC = "asc";
    public static final String ARGUMENT_IGNORE_CASE = "ignoreCase";
    public static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
    public static final Object[] EMPTY_ARRAY = new Object[0];

    public static int retrieveMaxValue(Object[] arguments) {
        int result = -1;
        if(arguments.length > 1) { 
            result = retrieveInt(arguments[1], ARGUMENT_MAX);
            if(arguments.length > 2 && result == -1) {
                result = retrieveInt(arguments[2], ARGUMENT_MAX);
            }
        }
        return result;
    }

    public static int retrieveOffsetValue(Object[] arguments) {
        int result = -1;
        if(arguments.length > 1) {
            if(isMapWithValue(arguments[1], ARGUMENT_OFFSET)) {
                result = ((Number)((Map)arguments[1]).get(ARGUMENT_OFFSET)).intValue();
            }
            if(arguments.length > 2 && result == -1) {
                if(isMapWithValue(arguments[2], ARGUMENT_OFFSET) ) {
                    result = retrieveInt(arguments[2], ARGUMENT_OFFSET);
                } else if(isIntegerOrLong(arguments[1]) && isIntegerOrLong(arguments[2])) {
                    result = ((Number)arguments[2]).intValue();
                }
            }
            if(arguments.length > 3 && result == -1 ) {
                if(isIntegerOrLong(arguments[3])) {
                     result = ((Number)arguments[3]).intValue();
                }
            }
        }
        return result;
    }
    
    public static int retrieveInt(Object param, String key) {
        if(isMapWithValue(param, key)) {
             Integer convertedParam = parseInt(((Map) param).get(key));
             return convertedParam.intValue();
        } else if(isIntegerOrLong(param)) {
             return ((Number)param).intValue();
        }
        return -1;
    }

    public static boolean isIntegerOrLong(Object param) {
        return (param instanceof Integer) || (param instanceof Long);
    }

    public static boolean isMapWithValue(Object param, String key) {
        return (param instanceof Map) && ((Map)param).containsKey(key);
    }

    private static Integer parseInt(Object obj) {
        if(obj == null) {
            return new Integer(0);
        } else if(Number.class.isAssignableFrom(obj.getClass())) {
            return new Integer(((Number) obj).intValue());
        } else if(obj instanceof CharSequence) {
            return Integer.valueOf(((CharSequence) obj).toString());
        }
        return new Integer(0); 
    }
}
