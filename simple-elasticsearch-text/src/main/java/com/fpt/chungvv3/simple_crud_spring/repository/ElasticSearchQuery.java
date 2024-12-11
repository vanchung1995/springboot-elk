package com.fpt.chungvv3.simple_crud_spring.repository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.fpt.chungvv3.simple_crud_spring.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class ElasticSearchQuery {
    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private final String indexName = "products";

    public String createOrUpdateDocument(Product product) throws IOException {
        IndexResponse indexResponse = elasticsearchClient.index(
                i -> i.index(indexName).id(product.getId()).document(product)
        );

        if (indexResponse.result().name().equalsIgnoreCase("Created")) {
            return "Document has been created successfully.";
        }

        if (indexResponse.result().name().equalsIgnoreCase("Updated")) {
            return "Document has been successfully updated.";
        }

        return "Error while performing the operation.";
    }

    public Product getDocumentById(String productId) throws IOException {
        GetResponse<Product> productGetResponse = elasticsearchClient.get(g -> g.index(indexName).id(productId), Product.class);

        if (productGetResponse.found()) {
            Product product = productGetResponse.source();
            System.out.println("Product: " + product.getName());
            return product;
        }

        System.out.println("Product not found");
        return null;
    }

    public String deleteDocumentById(String productId) throws IOException {
        DeleteResponse deleteResponse = elasticsearchClient.delete(d -> d.index(indexName).id(productId));

        if (Objects.isNull(deleteResponse.result()) || deleteResponse.result().name().equals("NotFound")) {
            return "Product not found";
        }

        return "Deleted product with id: " + deleteResponse.id();
    }

    public List<Product> searchAllDocuments() throws IOException {

        SearchRequest searchRequest = SearchRequest.of(s -> s.index(indexName));
        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, Product.class);
        List<Hit> hits = searchResponse.hits().hits();
        List<Product> products = new ArrayList<>();
        for (Hit object : hits) {
            System.out.print(((Product) object.source()));
            products.add((Product) object.source());
        }
        return products;
    }
}
