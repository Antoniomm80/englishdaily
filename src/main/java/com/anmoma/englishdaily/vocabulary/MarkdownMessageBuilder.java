package com.anmoma.englishdaily.vocabulary;

public class MarkdownMessageBuilder {
    private final StringBuilder sb = new StringBuilder();

    public MarkdownMessageBuilder append(String text) {
        sb.append(text);
        return this;
    }

    public MarkdownMessageBuilder appendBold(String text) {
        sb.append("\\*")
          .append(text.trim())
          .append("*");
        return this;
    }

    public MarkdownMessageBuilder appendBoldForLabel(String text) {
        sb.append("\\*")
          .append(text.trim())
          .append("*: ");
        return this;
    }

    public MarkdownMessageBuilder appendNewLine() {
        sb.append("\n");
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
