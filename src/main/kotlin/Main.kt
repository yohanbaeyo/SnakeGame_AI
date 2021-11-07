import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

suspend fun main() {
    val mainUI = MainUI()
    var finished = false
    var SecondPerFrame: Long = 0

    coroutineScope {
        launch {
            while(!finished) {
//                println("${mainUI.snakeBody[0].direction}")
                val tmpPos = mainUI.snakeBody[0].position + when(mainUI.tmpDirection) {
                    Direction.RIGHT -> Position(1, 0)
                    Direction.LEFT -> Position(-1, 0)
                    Direction.DOWN -> Position(0, 1)
                    Direction.UP -> Position(0, -1)
                }

                if(tmpPos.isInBoundary(Position(0, 0), Position(MainUI.CELL_HORIZONTAL_CNT, MainUI.CELL_VERTICAL_CNT))
                    && mainUI.isOccupied[tmpPos.x][tmpPos.y] == false) {
                    mainUI.snakeBody[0].type = CellType.BODY
                    /*mainUI.snakeBody[0].shape = when(mainUI.tmpDirection) {
                        Direction.LEFT ->
                    }*/
                    mainUI.snakeBody.addFirst(Cell(tmpPos, CellType.HEAD, mainUI.tmpDirection))
                    mainUI.isOccupied[tmpPos.x][tmpPos.y] = true
                    mainUI.remainPosition.remove(tmpPos)

                    if(mainUI.targetPosition != tmpPos) {
                        mainUI.isOccupied[mainUI.snakeBody.last().position.x][mainUI.snakeBody.last().position.y] = false
                        mainUI.remainPosition.add(mainUI.snakeBody.last().position)

                        mainUI.snakeBody.removeLast()
                        mainUI.snakeBody.last().type = CellType.END
                    } else {
                        mainUI.makeTarget()
                    }

//                    println(mainUI.snakeBody[0].position)
                    mainUI.mainPanel.repaint()

                } else {
                    println("Finished")
                    finished = true
                }
//                println("${mainUI.width} ${mainUI.height}")
                println(SecondPerFrame)
                SecondPerFrame = (450/Math.exp((mainUI.snakeBody.size-1).toDouble()/20)+ 50).toLong()
                mainUI.mainPanel.frameRateLabel.text = String.format("BlockPerSecond: %.3f", 1000/SecondPerFrame.toDouble())
                delay(SecondPerFrame)
            }
        }
    }
}
