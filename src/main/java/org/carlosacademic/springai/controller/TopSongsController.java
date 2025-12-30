package org.carlosacademic.springai.controller;

import org.carlosacademic.springai.service.TopSongsOpenIAService;
import org.carlosacademic.springai.service.TopSongsOpenIAStreamingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class TopSongsController {

    private final TopSongsOpenIAService topSongsOpenIAService;
    private final TopSongsOpenIAStreamingService topSongsOpenIAStreamingService;

    public TopSongsController(TopSongsOpenIAService topSongsOpenIAService, TopSongsOpenIAStreamingService topSongsOpenIAStreamingService) {
        this.topSongsOpenIAService = topSongsOpenIAService;
        this.topSongsOpenIAStreamingService = topSongsOpenIAStreamingService;
    }

    @PostMapping("/{year}")
    public ResponseEntity<List<String>> getSongsByYear(@PathVariable String year) {
        return ResponseEntity.ok(topSongsOpenIAService.getSongsByYear(year));
    }

    @PostMapping(value = "/{year}/stream", produces = "application/ndjson")
    public Flux<String> getSongsByYearStream(@PathVariable String year) {
        return topSongsOpenIAStreamingService.getSongsByYear(year);
    }
}
