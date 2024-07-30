package com.almland.vaadinplayground.todo.service.export.excel

import com.almland.vaadinplayground.todo.domain.Todo
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service

@Service
internal class ExcelGenerator {

    companion object {
        private const val ROW_HEADER_INDEX = 0
        private const val ROW_BODY_MAGNIFICATION = 1
        private const val SHEET_NAME = "Todo items"
    }

    private enum class ExcelHeader { TITLE, BODY, AUTHOR }

    fun createExcelFile(todos: Set<Todo>): Workbook =
        XSSFWorkbook().apply {
            createSheet(SHEET_NAME).also { sheet ->
                getStyleHeader(this@apply).also { styleHeader ->
                    sheet.createRow(ROW_HEADER_INDEX).run {
                        ExcelHeader.entries.forEachIndexed { index, excelHeader ->
                            createCell(index).run { cellStyle = styleHeader; setCellValue(excelHeader.name) }
                        }
                    }
                }
                getStyleBody(this@apply).also { styleBody ->
                    todos.forEachIndexed { index, todo ->
                        sheet.createRow(index + ROW_BODY_MAGNIFICATION).run {
                            createCell(ExcelHeader.TITLE.ordinal).setCellValue(todo.title)
                            createCell(ExcelHeader.BODY.ordinal).run { cellStyle = styleBody; setCellValue(todo.body) }
                            createCell(ExcelHeader.AUTHOR.ordinal).setCellValue(todo.author)
                        }
                    }
                }
            }
        }

    private fun getStyleHeader(workbook: XSSFWorkbook): XSSFCellStyle =
        workbook.createCellStyle().apply {
            fillBackgroundColor = IndexedColors.LIGHT_GREEN.index
            borderBottom = BorderStyle.THIN
            workbook.createFont().apply {
                color = IndexedColors.BLUE.index
                bold = true
                setFont(this)
            }
        }

    private fun getStyleBody(workbook: XSSFWorkbook): XSSFCellStyle =
        workbook.createCellStyle().apply {
            workbook.createFont().run {
                bold = true
                setFont(this)
            }
        }
}
