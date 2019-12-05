package mt.com.go.apoe.controller;

import mt.com.go.apoe.OptimizationEngine;
import mt.com.go.apoe.model.plan.UiWall;
import mt.com.go.apoe.model.recommendation.Recommendation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OptimizationController {

    @PostMapping("/plan/optimize")
    public Recommendation optimizePlan(@RequestBody UiWall[] uiWalls) {
        return new OptimizationEngine(uiWalls).optimize();
    }

}
