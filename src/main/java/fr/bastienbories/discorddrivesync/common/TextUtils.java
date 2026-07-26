package fr.bastienbories.discorddrivesync.common;

import org.nibor.autolink.LinkExtractor;
import org.nibor.autolink.LinkSpan;
import org.nibor.autolink.LinkType;
import org.nibor.autolink.Span;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class TextUtils {

    public static List<String> getLinkFromContent(String content){
        LinkExtractor linkExtractor = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL)).build();
        List<String> links = new ArrayList<>();

        for (LinkSpan span : linkExtractor.extractLinks(content)) {
            String link = content.substring(span.getBeginIndex(), span.getEndIndex());
            links.add(link);
        }

        return links;
    }

    public static StringBuilder removeLinkFromContent(String content){
        LinkExtractor extractor = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL)).build();
        Iterable<Span> spans = extractor.extractSpans(content);

        StringBuilder result = new StringBuilder();
        for (Span span : spans) {
            if (!(span instanceof LinkSpan)) {
                result.append(content, span.getBeginIndex(), span.getEndIndex());
            }
        }

        return result;
    }

    public static StringBuilder removeNewLines(String content) {
        return new StringBuilder(content.replace("\n", ""));
    }

    public static String removeChannelTagFromContent(String contentRaw){
        return contentRaw.replaceAll("<#\\d+>\\s*", "").trim();
    }
}
