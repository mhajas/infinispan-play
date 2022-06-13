package fax.play.service;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheContainer;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.marshall.ProtoStreamMarshaller;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.query.remote.client.ProtobufMetadataManagerConstants;

public class CacheProvider {

   public static final String CACHE1_NAME = "keyword1";
   public static final String CACHE2_NAME = "keyword2";

   private RemoteCacheManager cacheManager;
   private static ProtoStreamMarshaller marshaller = new ProtoStreamMarshaller();

   public RemoteCacheManager init(CacheDefinition cacheDefinition, GeneratedSchema ... schemas) {
      cacheManager = CacheFactory.create();
      cacheManager.administration().removeCache(CACHE1_NAME);
      cacheManager.administration().removeCache(CACHE2_NAME);
      cacheManager = CacheFactory.create(cacheDefinition, marshaller, schemas);
      return cacheManager;
   }

   public RemoteCacheManager getCacheManager() {
      return cacheManager;
   }

   public RemoteCacheManager updateSchema(CacheDefinition cacheDefinition, GeneratedSchema ... schemas) {
      stop();
      cacheManager = CacheFactory.create(cacheDefinition, marshaller, schemas);
      return cacheManager;
   }

   public void updateSchema(RemoteCache<?,?> cache, GeneratedSchema ... schemas) {
      RemoteCacheContainer remoteCacheContainer = cache.getRemoteCacheContainer();

      for (GeneratedSchema schema : schemas) {
         // Register proto schema && entity marshaller on client side
         schema.registerSchema(marshaller.getSerializationContext());
         schema.registerMarshallers(marshaller.getSerializationContext());

         // Register proto schema && entity marshaller on server side
         RemoteCache<String, String> metadataCache = remoteCacheContainer.getCache(ProtobufMetadataManagerConstants.PROTOBUF_METADATA_CACHE_NAME);
         metadataCache.put(schema.getProtoFileName(), schema.getProtoFile());
      }
   }

   public void updateIndexSchema(String ... cacheNames) {
      for (String cacheName : cacheNames) {
         cacheManager.administration().updateIndexSchema(cacheName);
      }
   }

   public void reindexCaches(String ... cacheNames) {
      for (String cacheName : cacheNames) {
         cacheManager.administration().reindexCache(cacheName);
      }
   }

   public void stop() {
      if (cacheManager != null) {
         cacheManager.stop();
      }
   }
}
