package com.application.arenda.entities.utils.retrofit;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {
    private final RequestBody requestBody;
    private final ProgressListener listener;

    public ProgressRequestBody(RequestBody requestBody, ProgressListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        try {
            return requestBody.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        CountingSink countingSink = new CountingSink(bufferedSink);
        BufferedSink bufSink = Okio.buffer(countingSink);
        requestBody.writeTo(bufSink);
        bufSink.flush();
    }

    final class CountingSink extends ForwardingSink {
        private long bytesWritten = 0;
        CountingSink(Sink delegate) {
            super(delegate);
        }
        @Override
        public void write(@NonNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            bytesWritten += byteCount;
            listener.onProgress(bytesWritten, contentLength());
        }
    }

    public interface ProgressListener {
        void onProgress(long bytesWritten, long contentLength);
    }
}
