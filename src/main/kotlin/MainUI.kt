import MainUI.Companion.CELL_SIZE
import java.awt.Color
import java.awt.Graphics
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.random.Random


enum class CellType {
    HEAD, BODY, END, TARGET
}

enum class Direction {
    LEFT, RIGHT, UP, DOWN
}

enum class BodyShape {
    L, R, U, D, LD, LU, UL, UR, RU, RD, DL, DR
}

data class Position(var x: Int, var y: Int) {
    operator fun plus(b: Position): Position {
        return Position(x+b.x, y+b.y)
    }

    fun isInBoundary(p1:Position, p2:Position): Boolean = (p1.x<=x && x<p2.x && p1.y<=y && y<p2.y)
}

data class Cell(var position: Position, var type: CellType, var direction: Direction) {
    fun drawCell(g: Graphics) {
        g.fillRect(position.x * CELL_SIZE, position.y * CELL_SIZE, CELL_SIZE, CELL_SIZE)
    }
}

class MainUI : JFrame("Test"){
    val snakeBody: ArrayDeque<Cell> = ArrayDeque()
    val mainPanel = MainPanel(this)
    var tmpDirection: Direction = Direction.UP
    val isOccupied = Array(CELL_HORIZONTAL_CNT){
        x -> Array<Boolean>(CELL_VERTICAL_CNT) {
            y -> (x==CELL_HORIZONTAL_CNT/2 && y==CELL_VERTICAL_CNT/2)
        }
    }
    var remainPosition = ArrayList<Position>()
    lateinit var targetPosition: Position

    companion object {
        const val CELL_SIZE = 20
        const val CELL_HORIZONTAL_CNT = 40
        const val CELL_VERTICAL_CNT = 30
        const val width = CELL_SIZE * CELL_HORIZONTAL_CNT
        const val height = CELL_SIZE * CELL_VERTICAL_CNT
    }

    fun makeTarget() {
        targetPosition = remainPosition.random()
        remainPosition.remove(targetPosition)
    }

    init {
        snakeBody.addFirst(Cell(Position(CELL_HORIZONTAL_CNT/2, CELL_VERTICAL_CNT/2), CellType.HEAD, Direction.UP))
        isOccupied[CELL_HORIZONTAL_CNT/2][CELL_VERTICAL_CNT/2] = true

        for(i in 0.. CELL_HORIZONTAL_CNT-1) {
            for(j in 0..CELL_VERTICAL_CNT-1) {
                if (!isOccupied[i][j]) {
                    remainPosition.add(Position(i, j))
                }
            }
        }

        makeTarget()

        addKeyListener(object : KeyAdapter() {
            override fun keyPressed(e: KeyEvent?) {
                 tmpDirection = when(e!!.keyCode) {
                    KeyEvent.VK_LEFT, KeyEvent.VK_A ->  Direction.LEFT
                    KeyEvent.VK_RIGHT, KeyEvent.VK_D -> Direction.RIGHT
                    KeyEvent.VK_UP, KeyEvent.VK_W -> Direction.UP
                    KeyEvent.VK_DOWN, KeyEvent.VK_S -> Direction.DOWN
                    else -> tmpDirection
                }
            }
        })

        setSize(814, 636)

        add(mainPanel)
        isVisible = true

        defaultCloseOperation = EXIT_ON_CLOSE

    }

    class MainPanel(val mainUI: MainUI) : JPanel() {
        val frameRateLabel = JLabel("")

        init {
            add(frameRateLabel)
        }

        override fun paintComponent(g: Graphics?) {
            super.paintComponent(g)
            if (g == null) return

            g.color = Color.BLACK
//            g.drawRect(1, 1, i*10, j*10)
            mainUI.snakeBody.forEachIndexed { index, cell ->
                cell.drawCell(g)
            }
            g.color = Color.RED
            g.fillRect(mainUI.targetPosition.x * CELL_SIZE, mainUI.targetPosition.y * CELL_SIZE, CELL_SIZE, CELL_SIZE)
//            g.drawRect(0, 0, cellHorizontalCnt * cellSize, cellVerticalCnt * cellSize)
        }
    }
}