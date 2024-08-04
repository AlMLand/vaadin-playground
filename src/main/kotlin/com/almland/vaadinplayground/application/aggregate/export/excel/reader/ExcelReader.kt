package com.almland.vaadinplayground.application.aggregate.export.excel.reader

import com.almland.vaadinplayground.application.aggregate.export.excel.reader.rule.RuleCreator
import com.almland.vaadinplayground.domain.Todo
import com.almland.vaadinplayground.domain.TodoBuilder
import java.io.InputStream
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory

/**
 * incoming Excel file format:
 *      TITEL | BODY | AUTHOR
 *      ...   | ...  | ...
 */
internal object ExcelReader {

    private const val SHEET_INDEX = 0
    private const val CELL_NUMBER_ONE = 0
    private const val CELL_NUMBER_TWO = 1
    private const val CELL_NUMBER_THREE = 2

    fun readFromFile(inputStream: InputStream): Collection<Todo> =
        mutableListOf<Todo>().apply {
            WorkbookFactory
                .create(inputStream)
                .getSheetAt(SHEET_INDEX)
                .iterator()
                .also { skipHeaderRow(it) }
                .forEach {
                    add(
                        TodoBuilder.createTodo(
                            getCellValue(it.getCell(CELL_NUMBER_ONE)),
                            getCellValue(it.getCell(CELL_NUMBER_TWO)),
                            getCellValue(it.getCell(CELL_NUMBER_THREE))
                        )
                    )
                }
        }

    private fun <T> getCellValue(cell: Cell): T =
        RuleCreator().createRules(cell).let { rules ->
            rules.keys
                .filter { rules[it]!!.condition.get() }
                .map { rules[it]!!.process.get() }
                .first() as T
        }

    private fun skipHeaderRow(rowMutableIterator: MutableIterator<Row>) {
        rowMutableIterator.next()
    }
}
