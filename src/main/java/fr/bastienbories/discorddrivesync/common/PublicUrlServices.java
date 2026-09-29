package fr.bastienbories.discorddrivesync.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class PublicUrlServices {

    private final boolean secure;
    private final String hostname;
    private final int port;

    public PublicUrlServices(
            @Value("${server.secure-http}") boolean secure,
            @Value("${server.hostname}") String hostname,
            @Value("${server.port}") int port) {
        this.secure = secure;
        this.hostname = hostname;
        this.port = port;
    }

    //https://web.dev/articles/url-parts

    public UriComponentsBuilder buildOrigin(){
        String scheme = this.secure ? "https" : "http";
        int defaultPort = this.secure ? 443 : 80;

        UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(hostname);

        if (this.port > 0 && this.port != defaultPort) {
            builder.port(this.port);
        }

        return builder;
    }

    public String buildUrl(String pathname, String query, String token){
        UriComponentsBuilder builder = buildOrigin().path(pathname);

        if (token != null && !token.isBlank()) {
            builder.pathSegment(token);
        }
        if (query != null && !query.isBlank()) {
            builder.query(query);
        }

        return builder.encode().build().toUriString();
    }

    public String buildUrl(PathnameTypeEnum type, String query, String token){
        return buildUrl(type.pathname, query, token);
    }
}
