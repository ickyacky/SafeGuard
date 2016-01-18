package com.helion3.safeguard.util;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.helion3.safeguard.SafeGuard;

public class DataUtil {
    private DataUtil() {}

    /**
     * Checks an object against known primitive object types.
     *
     * @param object
     * @return boolean If object is a primitive type
     */
    public static boolean isPrimitiveType(Object object) {
        return (object instanceof Boolean ||
                object instanceof Byte ||
                object instanceof Character ||
                object instanceof Double ||
                object instanceof Float ||
                object instanceof Integer ||
                object instanceof Long ||
                object instanceof Short ||
                object instanceof String);
    }

    /**
     * Converts a DataView object into a JsonObject.
     *
     * @param view DataView
     * @return JsonObject JsonObject representation of the DataView
     */
    public static JsonObject jsonFromDataView(DataView view) {
        JsonObject json = new JsonObject();

        Set<DataQuery> keys = view.getKeys(false);
        for (DataQuery query : keys) {
            Optional<Object> optional = view.get(query);
            if (optional.isPresent()) {
                String key = query.asString(".");

                if (optional.get() instanceof List) {
                    List<?> list = (List<?>) optional.get();
                    Iterator<?> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Object object = iterator.next();

                        if (object instanceof DataView) {
                            json.add(key, jsonFromDataView((DataView) object));
                        }
                        else if (DataUtil.isPrimitiveType(object)) {
                            Gson gson = new GsonBuilder().create();
                            JsonArray array = gson.toJsonTree(optional.get()).getAsJsonArray();
                            json.add(key, array);
                            break;
                        }
                        else {
                            SafeGuard.getLogger().error("Unsupported list data type: " + object.getClass().getName());
                        }
                    }
                }
                else if (optional.get() instanceof DataView) {
                    json.add(key, jsonFromDataView((DataView) optional.get()));
                }
                else {
                    Object obj = optional.get();

                    if (obj instanceof String) {
                        json.addProperty(key, (String) obj);
                    }
                    else if (obj instanceof Number) {
                        json.addProperty(key, (Number) obj);
                    }
                    else if (obj instanceof Boolean) {
                        json.addProperty(key, (Boolean) obj);
                    }
                    else if (obj instanceof Character) {
                        json.addProperty(key, (Character) obj);
                    }
                }
            }
        }

        return json;
    }
}
