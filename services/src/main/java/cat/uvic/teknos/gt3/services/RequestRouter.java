package cat.uvic.teknos.gt3.services;

import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;

public interface RequestRouter {
    RawHttpResponse<?> execRequest(RawHttpRequest request);
}
