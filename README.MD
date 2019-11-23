pathlossHeatMap <- getPathlossHeatMap(Wall[] walls): Matrix of floats?

accessPointCount <- 0
MAX_STEPS = 10
AVERAGE_DECIBEL_THRESHOLD <- 50

bestSignalStrengthHeatmap: float[][] <- null
bestAccessPoints: AccessPoint[] <- null

do 
	accessPointCount++

	accessPoints: AccessPoint[] <- randomlyPlaceAccessPoints(accessPointCount)
	step = 0
	areaCoverageAchieved = false

	while(step < MAX_STEPS) 
		signalStrengthHeatmap <- getSignalStrengthHeatmap(pathlossHeatMap, accessPoints): float[][]

		GridCell cell <- getMostAttractiveGridCell(signalStrengthMap)
		accessPoint <- getBestAccessPointToMove(signalStrengthMap, cell, accessPoints)
		
		moveAccessPoint(accessPoint, cell)

		if(getAreaCoverage(signalStrengthHeatmap) >= AVERAGE_DECIBELS_THRESHOLD)
			areaCoverageAchieved = true
			bestSignalStrengthHeatmap = signalStrengthHeatmap
			bestAccessPoints = accessPoints

			break;
		step++

while(areaCoverageAchieved == false)

return bestSignalStrengthHeatMap, bestAccessPointLocations


outputImage(bestSignalStrengthHeatMap)


 	









start x = 20
start y = 10

end x = 25
end y = 

Coordinate space is in meters? yes x10m
A point is top left or middle? yes

Definitions:

Wall:
	x1,y1,x2,y2 (float?)
	thickness (float cm)
	material (enum)