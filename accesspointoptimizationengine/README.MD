pathlossHeatMap <- getPathlossHeatMap(Wall[] walls): float[][]
vacancyGrid <- generateVacanyGrid(): GridCell[][]

accessPointCount <- 0
MAX_STEPS = 10
AVERAGE_DECIBEL_THRESHOLD <- 50

bestSignalStrengthHeatmap: float[][] <- null
bestAccessPoints: AccessPoint[] <- null

do 
	accessPointCount++

	accessPoints: AccessPoint[] <- randomlyPlaceAccessPoints(vacanyGrid, accessPointCount)
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

Definitions:

Wall:
	start: Point
	end: Point
	material: Enum<Material>
	thickness: int

AccessPoint:
	position: Point
	antennaGain: float
	transmitPower: float

GridCell:
	vacant: boolean
