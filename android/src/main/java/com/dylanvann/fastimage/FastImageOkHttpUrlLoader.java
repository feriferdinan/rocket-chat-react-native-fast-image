package com.dylanvann.fastimage;

import androidx.annotation.NonNull;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/** A simple model loader for fetching media over http/https using OkHttp. */
public class FastImageOkHttpUrlLoader implements ModelLoader<GlideUrl, InputStream> {

  private static Call.Factory client;

  // Public API.
  @SuppressWarnings("WeakerAccess")
  public FastImageOkHttpUrlLoader(@NonNull Call.Factory client) {
    this.client = client;
  }

  public static void setOkHttpClient(Call.Factory okHttpClient) {
    client = okHttpClient;
  }

  @Override
  public boolean handles(@NonNull GlideUrl url) {
    return true;
  }

  @Override
  public LoadData<InputStream> buildLoadData(
      @NonNull GlideUrl model, int width, int height, @NonNull Options options) {
    return new LoadData<>(model, new OkHttpStreamFetcher(client, model));
  }

  /** The default factory for {@link FastImageOkHttpUrlLoader}s. */
  // Public API.
  @SuppressWarnings("WeakerAccess")
  public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
    private static volatile Call.Factory internalClient;
    private final Call.Factory client;

    private static Call.Factory getInternalClient() {
      if (internalClient == null) {
        synchronized (Factory.class) {
          if (internalClient == null) {
            internalClient = new OkHttpClient();
          }
        }
      }
      return internalClient;
    }

    /** Constructor for a new Factory that runs requests using a static singleton client. */
    public Factory() {
      this(getInternalClient());
    }

    /**
     * Constructor for a new Factory that runs requests using given client.
     *
     * @param client this is typically an instance of {@code OkHttpClient}.
     */
    public Factory(@NonNull Call.Factory client) {
      this.client = client;
    }

    @NonNull
    @Override
    public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
      return new FastImageOkHttpUrlLoader(client);
    }

    @Override
    public void teardown() {
      // Do nothing, this instance doesn't own the client.
    }
  }
}
