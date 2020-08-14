package com.madhatter.intothefuture;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FetchingService {

    private List<String> ids;

    private RestTemplate restTemplate;

    public FetchingService(RestTemplate restTemplate) throws ExecutionException, InterruptedException {
        this.restTemplate = restTemplate;
        ids = new ArrayList<>();
        ids.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"));
        init();
    }

    private void init() throws ExecutionException, InterruptedException {
        Long start = System.currentTimeMillis();
        List<ApiResponse> responses = fetchAllUsingFuture(ids, 5);
        log.info(responses.toString());
        Long finish = System.currentTimeMillis();
        log.info(MessageFormat.format("Process duration: {0} in ms", finish - start));
    }

    // Future example
    private List<ApiResponse> fetchAllUsingFuture(List<String> ids, int threadCount) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<List<String>> chunks = Utils.splitToChunks(ids, threadCount);
        System.out.println(chunks);
        List<Callable<List<ApiResponse>>> tasks = new ArrayList<>();
        chunks.forEach(chunk -> tasks.add(wrapFetchInCallable(chunk)));
        List<Future<List<ApiResponse>>> resultFuture = executorService.invokeAll(tasks);
        List<ApiResponse> responses = new ArrayList<>();
        for (Future<List<ApiResponse>> future : resultFuture) {
            responses.addAll(future.get());
        }
        executorService.shutdown();
        return responses;
    }

    // Future example
    private Callable<List<ApiResponse>> wrapFetchInCallable(List<String> ids) {
        return () -> {
            List<ApiResponse> responses = new ArrayList<>();
            ids.forEach(id -> {
                responses.add(fetchData(id));
            });
            return responses;
        };
    }

    // CompletableFuture example
    private List<ApiResponse> fetchAllUsingCompletableFuture(List<String> ids, int threadCount) {
        List<List<String>> chunks = Utils.splitToChunks(ids, threadCount);
        List<CompletableFuture<List<ApiResponse>>> futures = new ArrayList<>();
        chunks.forEach(chunk -> futures.add(wrapFetchInCompletableFuture(chunk)));
        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList());
    }

    // CompletableFuture example
    private CompletableFuture<List<ApiResponse>> wrapFetchInCompletableFuture(List<String> ids) {
        return CompletableFuture.supplyAsync(() -> {
            List<ApiResponse> responses = new ArrayList<>();
            ids.forEach(id -> {
                responses.add(fetchData(id));
            });
            return responses;
        });
    }

    private ApiResponse fetchData(String id) {
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(id, ApiResponse.class);
        log.info(MessageFormat.format("Fetching from {0}", id));
        ApiResponse body = response.getBody();
        log.info(MessageFormat.format("Retrieved {0}", body));
        return body;
    }
}
