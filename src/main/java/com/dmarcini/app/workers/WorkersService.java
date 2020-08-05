package com.dmarcini.app.workers;

import com.dmarcini.app.domain.CrawledPage;

import java.util.Set;
import java.util.concurrent.*;

public class WorkersService {
    private final int nThreads;
    private final int maxDepth;
    private final ExecutorService executorService;
    private Worker worker;

    public WorkersService(int nThreads, int maxDepth) {
        this.nThreads = nThreads;
        this.maxDepth = maxDepth;
        this.executorService = Executors.newFixedThreadPool(nThreads);
    }

    public void execute(String initialTask) {
        worker = new Worker(initialTask, maxDepth);

        for (int i = 0; i < nThreads; ++i) {
            executorService.execute(worker);
        }
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

    public Set<CrawledPage> getCrawledPages() {
        return worker.getCrawledPages();
    }

    public int getCrawledPageNum() {
        return worker.getCrawledPages().size();
    }
}
