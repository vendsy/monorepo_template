package com.tray.webpieces.server.filter.routes;

import java.nio.charset.StandardCharsets;

import javax.inject.Singleton;

import org.webpieces.httpparser.api.dto.KnownStatusCode;
import org.webpieces.router.api.controller.actions.Render;
import org.webpieces.router.api.controller.actions.RenderContent;
import org.webpieces.router.impl.compression.MimeTypes;

@Singleton
public class JsonErrorNotFoundController {

    private static final MimeTypes.MimeTypeResult MIME_TYPE = new MimeTypes.MimeTypeResult("application/json", StandardCharsets.UTF_8);

    public Render notFound() {
        return new RenderContent(new byte[0], KnownStatusCode.HTTP_404_NOTFOUND.getCode(), KnownStatusCode.HTTP_404_NOTFOUND.getReason(), MIME_TYPE);
    }

    public Render internalError() {
        return new RenderContent(new byte[0], KnownStatusCode.HTTP_500_INTERNAL_SVR_ERROR.getCode(), KnownStatusCode.HTTP_500_INTERNAL_SVR_ERROR.getReason(), MIME_TYPE);
    }

}
