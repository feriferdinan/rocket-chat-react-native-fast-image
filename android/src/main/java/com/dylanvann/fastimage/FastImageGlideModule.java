package com.dylanvann.fastimage;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.react.modules.network.OkHttpClientProvider;

import java.io.InputStream;

import okhttp3.OkHttpClient;

// We need an AppGlideModule to be present for progress events to work.
@GlideModule
public final class FastImageGlideModule extends AppGlideModule {

  @Override
  public void registerComponents(Context context, Glide glide, Registry registry) {
      OkHttpClient client = OkHttpClientProvider
                .getOkHttpClient()
                .newBuilder()
                .build();

      FastImageOkHttpUrlLoader.Factory factory = new FastImageOkHttpUrlLoader.Factory(client);
      registry.replace(GlideUrl.class, InputStream.class, factory);
  }
}
