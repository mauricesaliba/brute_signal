package mt.com.go.apoe.model.recommendation;

import mt.com.go.apoe.model.AccessPoint;

public class EmptyRecommendation extends Recommendation {

    public EmptyRecommendation() {
        super(new AccessPoint[0], new float[0][0]);
    }

}
