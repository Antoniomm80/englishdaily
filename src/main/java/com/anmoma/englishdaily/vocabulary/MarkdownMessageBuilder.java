package com.anmoma.englishdaily.vocabulary;

public class MarkdownMessageBuilder {
    private final StringBuilder sb = new StringBuilder();

    public MarkdownMessageBuilder append(String text) {
        sb.append(text);
        return this;
    }

    public MarkdownMessageBuilder appendBold(String text) {
        sb.append("**")
          .append(text.trim())
          .append("** ");
        return this;
    }

    public MarkdownMessageBuilder appendBoldForLabel(String text) {
        sb.append("**")
          .append(text.trim())
          .append("**: ");
        return this;
    }

    public MarkdownMessageBuilder appendItalic(String text) {
        sb.append("_")
          .append(text.trim())
          .append("_");
        return this;
    }

    public MarkdownMessageBuilder appendCode(String text) {
        sb.append("`")
          .append(text.trim())
          .append("`");
        return this;
    }

    public MarkdownMessageBuilder appendLink(String text, String url) {
        sb.append("[")
          .append(text)
          .append("](")
          .append(url)
          .append(")");
        return this;
    }

    public MarkdownMessageBuilder appendListItem(String text) {
        sb.append("- ")
          .append(text)
          .append("\n");
        return this;
    }

    public MarkdownMessageBuilder appendBlockQuote(String text) {
        sb.append("> ")
          .append(text)
          .append("\n");
        return this;
    }

    public MarkdownMessageBuilder appendNewLine() {
        sb.append("\n");
        return this;
    }

    public MarkdownMessageBuilder appendHorizontalRule() {
        sb.append("---\n");
        return this;
    }

    public MarkdownMessageBuilder appendHeader1(String text) {
        return appendHeader(1, text);
    }

    public MarkdownMessageBuilder appendHeader2(String text) {
        return appendHeader(2, text);
    }

    public MarkdownMessageBuilder appendHeader3(String text) {
        return appendHeader(3, text);
    }

    private MarkdownMessageBuilder appendHeader(int level, String text) {
        for (int i = 0; i < level; i++) {
            sb.append("#");
        }
        sb.append(" ")
          .append(text.trim())
          .append("\n");
        return this;
    }

    public String build() {
        return sb.toString();
    }
}
