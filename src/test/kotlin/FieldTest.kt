import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FieldTest {
    //constructor
    @Test
    fun `Should create Field obj if arguments are correct`() {
        val figures = setOf(Point(1, 1), Point(1, 2))
        val landscape = setOf(Point(3, 3), Point(4, 4))

        val field = assertDoesNotThrow() {
            Field(5, 6, figures, landscape)
        }

        assertEquals(5, field.getHeight())
        assertEquals(6, field.getWidth())
    }

    @Test
    fun `Should throw exception if height is negative`() {
        val exception = assertThrows<IllegalArgumentException> {
            Field(-1, 6, emptySet(), emptySet())
        }

        assertEquals("Height must be greater than 0", exception.message)
    }

    @Test
    fun `Should throw exception if width is negative`() {
        val exception = assertThrows<IllegalArgumentException> {
            Field(5, -1, emptySet(), emptySet())
        }

        assertEquals("Width must be greater than 0", exception.message)
    }

    //copy
    @Test
    fun `Should create a copy of the Field object`() {
        val originalFigure = setOf(Point(1, 1), Point(1, 2))
        val originalLandscape = setOf(Point(3, 3), Point(4, 4))
        val originalField = Field(5, 6, originalFigure, originalLandscape)

        val copiedField = originalField.copy()

        assertNotSame(originalField, copiedField)

        assertNotSame(originalField.getFigure(), copiedField.getFigure())
        assertNotSame(originalField.getLandscape(), copiedField.getLandscape())

        assertEquals(originalField.getFigure().first().x, copiedField.getFigure().first().x)
        assertEquals(originalField.getLandscape().first().y, copiedField.getLandscape().first().y)

        copiedField.getFigure().first().x = 10
        copiedField.getLandscape().first().y = 20

        assertNotEquals(originalField.getFigure().first().x, copiedField.getFigure().first().x)
        assertNotEquals(originalField.getLandscape().first().y, copiedField.getLandscape().first().y)
    }


    //move
    @Test
    fun `Should return field with figure moved by one step if there is space`() {
        val originalFigure = setOf(Point(1, 1), Point(1, 2))
        val originalLandscape = setOf(Point(4, 4), Point(4, 4))
        val field = Field(4, 6, originalFigure, originalLandscape)

        val newField = field.nextStep()

        val expectedFigure = originalFigure.map { Point(it.x, it.y + 1) }.toSet()
        assertEquals(expectedFigure.map { it.x }, newField.getFigure().map { it.x })
        assertEquals(expectedFigure.map { it.y }, newField.getFigure().map { it.y })
    }

    @Test
    fun `Should not move figure when it reaches the bottom`() {
        val originalFigure = setOf(Point(1, 4), Point(2, 4), Point(3, 4))
        val field = Field(5, 6, originalFigure, emptySet())

        val newField = field.nextStep()

        // Verify that the figure remained at the same position in the new field
        assertEquals(originalFigure.map { it.x }, newField.getFigure().map { it.x })
        assertEquals(originalFigure.map { it.y }, newField.getFigure().map { it.y })
    }

    @Test
    fun `Should return field with same figure if there is no space to move`() {
        val originalFigure = setOf(Point(1, 4), Point(2, 4))
        val originalLandscape = setOf(Point(1, 5), Point(1, 6), Point(2, 6))
        val field = Field(6, 6, originalFigure, originalLandscape)

        val newField = field.nextStep()

        assertEquals(originalFigure.map { it.x }, newField.getFigure().map { it.x })
        assertEquals(originalFigure.map { it.y }, newField.getFigure().map { it.y })
    }

    @Test
    fun `Should return same field if figure has no space to move`() {
        val originalFigure = setOf(Point(1, 4), Point(2, 4))
        val originalLandscape = setOf(Point(1, 5), Point(2, 5))
        val field = Field(5, 6, originalFigure, originalLandscape)

        val sameField = field.nextStep()
        assertSame(field, sameField)
    }

    //check if there is only one figure
    @Test
    fun `Should create fields if there is one figure`() {
        val figure = setOf(
            Point(2, 2),
            Point(1, 3), Point(2, 3), Point(3, 3),
            Point(2, 4)
        )
        val field = assertDoesNotThrow() {
            Field(
                7,
                8,
                figure,
                setOf(
                    Point(3, 4),
                    Point(3, 5), Point(7, 5),
                    Point(0, 6),
                    Point(3, 6), Point(4, 6), Point(5, 6), Point(6, 6), Point(7, 6)
                )
            )

        }

        assertEquals(figure, field.getFigure())
    }

    @Test
    fun `Should throw an error if there is more that one figure`() {
        val figure = setOf(
            Point(2, 2),
            Point(1, 3), Point(2, 3), Point(3, 3),
            Point(2, 4),
            Point(0,0)
        )

        val exception = assertThrows<IllegalArgumentException> {
            Field(
                7,
                8,
                figure,
                setOf(
                    Point(3, 4),
                    Point(3, 5), Point(7, 5),
                    Point(0, 6),
                    Point(3, 6), Point(4, 6), Point(5, 6), Point(6, 6), Point(7, 6)
                )
            )

        }

        assertEquals("In field must be only one figure", exception.message)
    }

    @Test
    fun `Should create field if there is one figure from one point`() {
        val field = assertDoesNotThrow() {
            Field(
                2,
                2,
                setOf(Point(0,0)),
                emptySet()
            )
        }

        assertEquals(setOf(Point(0,0)), field.getFigure())
    }

    @Test
    fun `Should create field if there are no figures`() {
        val field = assertDoesNotThrow() {
            Field(
                2,
                2,
                emptySet(),
                emptySet()
            )
        }

        assertEquals(setOf<Point>(), field.getFigure())
    }

    @Test
    fun `Should throw an exception if points are diagonal`() {
        val figure = setOf(
            Point(0, 0),
            Point(1, 1)
        )

        val exception = assertThrows<IllegalArgumentException> {
            Field(
                3,
                3,
                figure,
                emptySet()
            )

        }

        assertEquals("In field must be only one figure", exception.message)
    }
}