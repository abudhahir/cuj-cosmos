package com.cleveloper.utilities.jsonschema.comparison;

import com.cleveloper.utilities.jsonschema.JsonComparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Default JSON comparator using Jackson for structural comparison.
 *
 * <p>Returns an empty string when documents are equivalent. Differences are reported as lines in the
 * form of JSON Pointers with a short message. Intended as a lightweight comparator without extra
 * runtime dependencies.
 */
public class DefaultJsonComparator implements JsonComparator {

  private final ObjectMapper mapper = new ObjectMapper();

  @Override
  public String compare(String expected, String actual) {
    try {
      JsonNode exp = mapper.readTree(expected);
      JsonNode act = mapper.readTree(actual);
      List<String> diffs = new ArrayList<>();
      diff("", exp, act, diffs);
      return String.join("\n", diffs);
    } catch (JsonProcessingException e) {
      return "Failed to parse JSON: " + e.getOriginalMessage();
    }
  }

  private void diff(String path, JsonNode exp, JsonNode act, List<String> diffs) {
    if (exp == null && act == null) return;
    if (exp == null) {
      diffs.add(path + ": expected missing, actual present");
      return;
    }
    if (act == null) {
      diffs.add(path + ": expected present, actual missing");
      return;
    }

    if (exp.isObject() && act.isObject()) {
      // Check expected fields
      Iterator<Map.Entry<String, JsonNode>> fields = exp.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> f = fields.next();
        String child = path + "/" + escape(f.getKey());
        JsonNode right = act.get(f.getKey());
        if (right == null) {
          diffs.add(child + ": missing in actual");
        } else {
          diff(child, f.getValue(), right, diffs);
        }
      }
      // Check extra fields
      Iterator<String> actFields = act.fieldNames();
      while (actFields.hasNext()) {
        String name = actFields.next();
        if (!exp.has(name)) {
          diffs.add(path + "/" + escape(name) + ": unexpected field in actual");
        }
      }
    } else if (exp.isArray() && act.isArray()) {
      int min = Math.min(exp.size(), act.size());
      for (int i = 0; i < min; i++) {
        diff(path + "/" + i, exp.get(i), act.get(i), diffs);
      }
      if (exp.size() > act.size()) {
        diffs.add(path + ": expected has more items (" + exp.size() + ">" + act.size() + ")");
      } else if (act.size() > exp.size()) {
        diffs.add(path + ": actual has more items (" + act.size() + ">" + exp.size() + ")");
      }
    } else {
      if (!exp.equals(act)) {
        diffs.add(path + ": expected=" + safe(exp) + ", actual=" + safe(act));
      }
    }
  }

  private String escape(String s) {
    return s.replace("~", "~0").replace("/", "~1");
  }

  private String safe(JsonNode node) {
    if (node == null) return "null";
    if (node.isTextual()) return '"' + node.asText() + '"';
    return node.toString();
  }
}

