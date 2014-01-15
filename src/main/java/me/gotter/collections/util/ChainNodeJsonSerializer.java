package me.gotter.collections.util;

import me.gotter.collections.ChainNode;

public class ChainNodeJsonSerializer
{
    public static String serialize(ChainNode node)
    {
        // Scalars
        if (node.isNull()) {
            return "null";
        }
        if (node.isString()) {
            return "\"" + wrap(node.toString()) + "\"";
        }
        if (node.isBool()) {
            return node.isTrue() ? "true" : "false";
        }
        if (node.isFloat() || node.isInt() || node.isLong() || node.isDouble()) {
            return "" + node.get();
        }

        // Map
        if (node.isMap()) {
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            int i=0;
            for (String key :node.keySet()) {
                if (i++ > 0) {
                    sb.append(',');
                }
                sb.append("\"" + wrap(key) + "\":");
                sb.append(serialize(node.get(key)));
            }
            sb.append('}');
            return sb.toString();
        }

        // Array
        if (node.isList()) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            int i=0;
            for (ChainNode inner : node) {
                if (i++ > 0) {
                    sb.append(',');
                }
                sb.append(serialize(inner));
            }
            sb.append("]");
            return sb.toString();
        }

        // Unknown type
        throw new RuntimeException("Unsupported node type");
    }

    public static String wrap(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
