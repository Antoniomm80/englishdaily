package com.anmoma.englishdaily.vocabulary;

public class MarkdownMessageBuilder {
    private final StringBuilder sb = new StringBuilder();

    public MarkdownMessageBuilder append(String text) {
        sb.append(escapeReservedCharacters(text.trim()));
        return this;
    }

    public MarkdownMessageBuilder appendBold(String text) {
        sb.append("*")
          .append(escapeReservedCharacters(text.trim()))
          .append("*");
        return this;
    }

    public MarkdownMessageBuilder appendBoldForLabel(String text) {
        sb.append("*")
          .append(escapeReservedCharacters(text.trim()))
          .append("*: ");
        return this;
    }

    public MarkdownMessageBuilder appendNewLine() {
        sb.append("\n");
        return this;
    }

    public String escapeReservedCharacters(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        // Characters that need to be escaped in MarkdownV2 format
        char[] reservedChars = { '_', '*', '[', ']', '(', ')', '~', '`', '>', '#', '+', '-', '=', '|', '{', '}', '.', '!' };

        String escapedText = text;
        for (char reservedChar : reservedChars) {
            // Escape each reserved character
            escapedText = escapedText.replace(String.valueOf(reservedChar), "\\" + reservedChar);
        }

        return escapedText;
    }

    public String build() {
        return sb.toString();
    }
}
