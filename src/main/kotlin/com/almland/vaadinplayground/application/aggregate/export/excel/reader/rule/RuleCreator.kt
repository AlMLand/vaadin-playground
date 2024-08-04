package com.almland.vaadinplayground.application.aggregate.export.excel.reader.rule

import java.util.function.Supplier
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType

internal class RuleCreator {

    fun createRules(cell: Cell): Map<CellType, Rule> =
        mutableMapOf<CellType, Rule>().apply {
            this[CellType.NUMERIC] = createCellTypeNumeric(cell)
            this[CellType.STRING] = createCellTypeString(cell)
            this[CellType.BOOLEAN] = createCellTypeBoolean(cell)
            this[CellType.BLANK] = createCellTypeBlank(cell)
        }

    private fun createRule(
        condition: Supplier<Boolean>,
        process: Supplier<Any>
    ): Rule = Rule(condition, process)

    private fun createCellTypeNumeric(cell: Cell): Rule =
        createRule(
            { cell.cellType == CellType.NUMERIC },
            { cell.numericCellValue }
        )

    private fun createCellTypeString(cell: Cell): Rule =
        createRule(
            { cell.cellType == CellType.STRING },
            { cell.stringCellValue }
        )

    private fun createCellTypeBoolean(cell: Cell): Rule =
        createRule(
            { cell.cellType == CellType.BOOLEAN },
            { cell.booleanCellValue }
        )

    private fun createCellTypeBlank(cell: Cell): Rule =
        createRule(
            { cell.cellType == CellType.BLANK },
            { "" }
        )
}
