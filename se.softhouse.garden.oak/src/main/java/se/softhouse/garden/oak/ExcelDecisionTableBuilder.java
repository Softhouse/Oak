/**
 * Copyright (c) 2011, Mikael Svahn, Softhouse Consulting AB
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package se.softhouse.garden.oak;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;

import se.softhouse.garden.oak.model.ABasicRegisterPtr;
import se.softhouse.garden.oak.model.ARegisterPtr;
import se.softhouse.garden.oak.statement.AddStatement;
import se.softhouse.garden.oak.statement.AndStatement;
import se.softhouse.garden.oak.statement.AssignStatement;
import se.softhouse.garden.oak.statement.CompareStatement;
import se.softhouse.garden.oak.statement.CompareStatement.OP;
import se.softhouse.garden.oak.statement.EmptyStatement;
import se.softhouse.garden.oak.statement.EqualsStatement;
import se.softhouse.garden.oak.statement.InvokeTableStatement;
import se.softhouse.garden.oak.statement.Statement;
import se.softhouse.garden.oak.statement.SubStatement;
import se.softhouse.garden.oak.table.ActionTable;
import se.softhouse.garden.oak.table.DecisionTable;
import se.softhouse.garden.oak.table.StatementAddTable;
import se.softhouse.garden.oak.table.StatementTable;

/**
 * @author Mikael Svahn
 * 
 */
public class ExcelDecisionTableBuilder
{

  protected Map<String, StatementBuilder> statementBuilders = new HashMap<String, ExcelDecisionTableBuilder.StatementBuilder>();
  protected Map<String, StatementBuilder> conditionBuilders = new HashMap<String, ExcelDecisionTableBuilder.StatementBuilder>();
  protected Map<String, ActionTableBuilder> tableBuilders = new HashMap<String, ExcelDecisionTableBuilder.ActionTableBuilder>();

  public ExcelDecisionTableBuilder()
  {
    this.statementBuilders.put("assign", createAssignStatement());
    this.statementBuilders.put("add", createAddStatement());
    this.statementBuilders.put("sub", createSubStatement());
    this.statementBuilders.put("table", createTableStatement());

    this.conditionBuilders.put("equals", createEqualsStatement());
    this.conditionBuilders.put("lt", createCompareStatement(OP.LT));
    this.conditionBuilders.put("le", createCompareStatement(OP.LE));
    this.conditionBuilders.put("eq", createCompareStatement(OP.EQ));
    this.conditionBuilders.put("ge", createCompareStatement(OP.GE));
    this.conditionBuilders.put("gt", createCompareStatement(OP.GT));
    this.conditionBuilders.put("assign", createAssignStatement());
    this.conditionBuilders.put("add", createAddStatement());
    this.conditionBuilders.put("sub", createSubStatement());

    this.tableBuilders.put("condition", createConditionTable(false));
    this.tableBuilders.put("multicondition", createConditionTable(true));
    this.tableBuilders.put("statement", createStatementTable());
    this.tableBuilders.put("addstatement", createAddStatementTable());
  }

  public void load(InputStream i, DecisionEngine engine) throws InvalidFormatException, IOException
  {
    loadFromStream(i, engine);
  }

  public void load(String filename, DecisionEngine engine) throws IOException, InvalidFormatException
  {
    InputStream inp = new FileInputStream(filename);
    loadFromStream(inp, engine);
  }

  private void loadFromStream(InputStream is, DecisionEngine engine) throws IOException, InvalidFormatException
  {
    if (is == null)
    {
      throw new IOException("Unable to find resource.");
    }
    Workbook wb = WorkbookFactory.create(is);
    for (int i = 0; i < wb.getNumberOfSheets(); i++)
    {
      Sheet sheet = wb.getSheetAt(i);

      DecisionTable table = new DecisionTable();
      List<Integer> tableIndexes = locateActionTables(sheet);
      for (int k = 0; k < tableIndexes.size() - 1; k++)
      {
        table.addTable(create(table, sheet, tableIndexes.get(k), tableIndexes.get(k + 1)));
      }

      engine.addDecisionTable(sheet.getSheetName(), table);

    }
  }

  private ActionTable create(DecisionTable decissionTable, Sheet sheet, int start, int stop) throws IOException, InvalidFormatException
  {
    Iterator<Row> iterator = sheet.iterator();
    iterator.next();
    Row tableTypeRow = iterator.next();
    Row argsRow = iterator.next();
    Row paramRow = iterator.next();
    Row opRow = iterator.next();
    iterator.next();

    Cell cell = tableTypeRow.getCell(start);
    String tableType = cell.getStringCellValue().toLowerCase();
    ActionTableBuilder tableBuilder = this.tableBuilders.get(tableType);
    if (tableBuilder != null)
    {
      return tableBuilder.build(argsRow, paramRow, opRow, iterator, start, stop);
    }
    return null;
  }

  private List<Integer> locateActionTables(Sheet sheet)
  {
    List<Integer> indexes = new ArrayList<Integer>();
    Row tableTypeRow = sheet.getRow(1);
    Row opRow = sheet.getRow(5);

    for (int i = tableTypeRow.getFirstCellNum(); i < tableTypeRow.getLastCellNum(); i++)
    {
      Cell typeRowCell = tableTypeRow.getCell(i);
      if (typeRowCell == null)
      {
        throw new NullPointerException("Sheet[" + sheet.getSheetName() + "] - Row:Col[1:" + i + "] - Cell object is null. (#columns = " + tableTypeRow.getLastCellNum() + ")");
      }
      String cellValue = typeRowCell.getStringCellValue();
      if (cellValue == null)
      {
        throw new NullPointerException("Sheet[" + sheet.getSheetName() + "] - Row:Col[1:" + i + "] - Cell value is null.");
      }
      if (!cellValue.isEmpty())
      {
        indexes.add(i);
      }
    }
    indexes.add((int) opRow.getLastCellNum());
    return indexes;
  }

  private List<Statement> createStatements(Map<String, StatementBuilder> builder, Row argsRow, Row paramRow, Row opRow, Iterator<Row> iterator, int start, int stop)
  {

    int size = stop - start;
    ARegisterPtr[] params = new ARegisterPtr[size];
    String[] ops = new String[size];

    for (int i = 0; i < size; i++)
    {
      ops[i] = opRow.getCell(i + start).getStringCellValue().toLowerCase();
      Cell cell = paramRow.getCell(i + start);
      if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
      {
        params[i] = new ABasicRegisterPtr(cell.getStringCellValue());
      }
    }

    List<Statement> statements = new ArrayList<Statement>();
    while (iterator.hasNext())
    {
      Row row = iterator.next();
      List<Statement> subStatements = new ArrayList<Statement>();
      for (int i = 0; i < size; i++)
      {
        Cell cell = row.getCell(i + start);
        if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)
        {
          StatementBuilder statementCreator = builder.get(ops[i]);
          if (statementCreator != null)
          {
            subStatements.add(statementCreator.build(cell, params[i]));
          }
        }
      }
      switch (subStatements.size())
      {
        case 0:
          statements.add(new EmptyStatement());
          break;
        case 1:
          statements.addAll(subStatements);
          break;
        default:
          statements.add(new AndStatement(subStatements));
          break;

      }
    }
    return statements;
  }

  private Object getCellValue(int type, Cell cell)
  {
    switch (type)
    {
      case Cell.CELL_TYPE_BOOLEAN:
        return cell.getBooleanCellValue();
      case Cell.CELL_TYPE_NUMERIC:
        return getNumericValue(type, cell);
      case Cell.CELL_TYPE_STRING:
        return cell.getStringCellValue();
      case Cell.CELL_TYPE_FORMULA:
        return getCellValue(cell.getCachedFormulaResultType(), cell);
    }
    return null;
  }

  private Number getNumericValue(int type, Cell cell)
  {
    switch (type)
    {
      case Cell.CELL_TYPE_NUMERIC:
        if (cell instanceof XSSFCell)
        {
          String raw = ((XSSFCell) cell).getRawValue();

          return new BigDecimal(raw);
        }
        return cell.getNumericCellValue();
      case Cell.CELL_TYPE_FORMULA:
        return getNumericValue(cell.getCachedFormulaResultType(), cell);
      case Cell.CELL_TYPE_STRING:
      {
        String raw = cell.getStringCellValue();
        if (raw == null || raw.isEmpty() || !raw.matches("0-9"))
        {
          // We throw this instead of the plain NumberFormatException that would've been
          // thrown otherwise.
          throw new IllegalArgumentException("Invalid Numeric String Cell value [" + raw + "] in Sheet(" + cell.getSheet().getSheetName() + "). Row:Column[" + cell.getRowIndex() + 1 + ":"
              + (cell.getColumnIndex() + 1) + "]");
        }
        return new BigDecimal(raw);
      }

    }
    return BigDecimal.ZERO;
  }

  private StatementBuilder createAssignStatement()
  {
    return new StatementBuilder()
    {

      @Override
      public Statement build(Cell cell, ARegisterPtr name)
      {
        return new AssignStatement(name, getCellValue(cell.getCellType(), cell));
      }
    };
  }

  private StatementBuilder createAddStatement()
  {
    return new StatementBuilder()
    {

      @Override
      public Statement build(Cell cell, ARegisterPtr name)
      {
        return new AddStatement(name, getCellValue(cell.getCellType(), cell));
      }
    };
  }

  private StatementBuilder createSubStatement()
  {
    return new StatementBuilder()
    {

      @Override
      public Statement build(Cell cell, ARegisterPtr name)
      {
        return new SubStatement(name, getCellValue(cell.getCellType(), cell));
      }
    };
  }

  private StatementBuilder createEqualsStatement()
  {
    return new StatementBuilder()
    {

      @Override
      public Statement build(Cell cell, ARegisterPtr name)
      {
        return new EqualsStatement(name, getCellValue(cell.getCellType(), cell));
      }
    };
  }

  private StatementBuilder createCompareStatement(final OP op)
  {
    return new StatementBuilder()
    {

      @Override
      public Statement build(Cell cell, ARegisterPtr name)
      {
        return new CompareStatement(name, getNumericValue(cell.getCellType(), cell), op);
      }
    };
  }

  private StatementBuilder createTableStatement()
  {
    return new StatementBuilder()
    {

      @Override
      public Statement build(Cell cell, ARegisterPtr name)
      {
        return new InvokeTableStatement(name, cell.getStringCellValue());
      }
    };
  }

  private ActionTableBuilder createConditionTable(final boolean multi)
  {
    return new ActionTableBuilder()
    {

      @Override
      public ActionTable build(Row argsRow, Row paramRow, Row opRow, Iterator<Row> iterator, int start, int stop)
      {
        StatementTable table = new StatementTable();
        table.setMulti(multi);
        table.setStatements(createStatements(ExcelDecisionTableBuilder.this.conditionBuilders, argsRow, paramRow, opRow, iterator, start, stop));
        return table;
      }
    };
  }

  private ActionTableBuilder createStatementTable()
  {
    return new ActionTableBuilder()
    {

      @Override
      public ActionTable build(Row argsRow, Row paramRow, Row opRow, Iterator<Row> iterator, int start, int stop)
      {
        StatementTable table = new StatementTable();
        table.setMulti(true);
        table.setStatements(createStatements(ExcelDecisionTableBuilder.this.statementBuilders, argsRow, paramRow, opRow, iterator, start, stop));
        return table;
      }
    };
  }

  private ActionTableBuilder createAddStatementTable()
  {
    return new ActionTableBuilder()
    {

      @Override
      public ActionTable build(Row argsRow, Row paramRow, Row opRow, Iterator<Row> iterator, int start, int stop)
      {
        Cell cell = argsRow.getCell(start);
        StatementAddTable table = new StatementAddTable(cell == null ? null : new ABasicRegisterPtr(cell.getStringCellValue()));
        table.setMulti(true);
        table.setStatements(createStatements(ExcelDecisionTableBuilder.this.statementBuilders, argsRow, paramRow, opRow, iterator, start, stop));
        return table;
      }
    };
  }

  public interface StatementBuilder
  {
    Statement build(Cell cell, ARegisterPtr name);
  }

  public interface ActionTableBuilder
  {
    ActionTable build(Row argsRow, Row paramRow, Row opRow, Iterator<Row> iterator, int start, int stop);
  }
}
