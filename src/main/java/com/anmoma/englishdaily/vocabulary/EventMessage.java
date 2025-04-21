package com.anmoma.englishdaily.vocabulary;

import java.time.Instant;

public record EventMessage<T>(Instant createDate, T message, boolean isMarkdown) {
}
