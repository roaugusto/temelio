'use client'

import Paper from '@mui/material/Paper'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TablePagination from '@mui/material/TablePagination'
import TableRow from '@mui/material/TableRow'
import { Edit, Trash2 } from 'lucide-react'
import { useEffect, useState } from 'react'

export type ColumnData = {
  id: string
  label: string
  minWidth?: number
  align?: 'right' | 'center' | 'left'
  format?: (value: number) => string
}

export type GenericRowData = {
  [key: string]: any
}

type TableCustomProps = {
  columns: ColumnData[]
  rows: GenericRowData[]
  rowsPerPage: number
  onDelete?: (row: GenericRowData) => void
  onEdit?: (row: GenericRowData) => void
  onClick?: (row: GenericRowData) => void
  height?: number
  showEditButton?: boolean
  showDeleteButton?: boolean
  enableClickRow?: boolean
}

export function TableData({
  columns,
  rows,
  rowsPerPage,
  onDelete = () => {
    console.log('delete')
  },
  onEdit = () => {
    console.log('edit')
  },
  onClick = () => {
    console.log('click')
  },
  height = 400,
  showEditButton = true,
  showDeleteButton = true,
  enableClickRow = false,
}: TableCustomProps) {
  const [page, setPage] = useState(0)
  const [tableRows, setTableRows] = useState<GenericRowData[]>([])

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage)
  }

  useEffect(() => {
    setTableRows(rows)
  }, [rows])

  const styledClick = enableClickRow ? 'pointer' : ''

  return (
    <Paper sx={{ width: '100%', overflow: 'hidden', borderRadius: 3 }}>
      <TableContainer sx={{ height }}>
        <Table aria-label="table" size="small">
          <TableHead className={'bg-blue-25'}
          >
            <TableRow sx={{ minHeight: 40 }}>
              {columns.map((column) => {
                if (column.id === 'id' || column.id === 'uid') {
                  return null
                }
                return (
                  <TableCell
                    key={column.id}
                    align={column.align}
                    style={{ minWidth: column.minWidth }}
                  >
                    <div className={'text-blue-900'}
                    >
                      {column.label}
                    </div>
                  </TableCell>
                )
              })}
              <TableCell align="center" style={{ width: 10 }} />
            </TableRow>
          </TableHead>
          <TableBody>
            {tableRows
              .slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage)
              .map((row, rowIndex) => (
                <TableRow
                  hover
                  role="checkbox"
                  tabIndex={-1}
                  key={rowIndex}
                  sx={{ minHeight: 50, cursor: styledClick }}
                  onClick={() => {
                    if (enableClickRow) {
                      onClick(row)
                    }
                  }}
                >
                  {columns.map((column) => {
                    if (column.id === 'id' || column.id === 'uid') {
                      return null
                    }
                    const value = row[column.id]

                    return (
                      <TableCell key={column.id} align={column.align}>
                        {column.format && typeof value === 'number'
                          ? column.format(value)
                          : value}
                      </TableCell>
                    )
                  })}
                  <TableCell>
                    <div className="flex items-center gap-3">
                      {showEditButton && (
                        <Edit
                          className="cursor-pointer"
                          size={18}
                          onClick={() => onEdit(row)}
                        />
                      )}
                      {showDeleteButton && (
                        <Trash2
                          className="cursor-pointer"
                          size={18}
                          color="rgb(248 113 113)"
                          onClick={() => onDelete(row)}
                        />
                      )}
                    </div>
                  </TableCell>
                </TableRow>
              ))}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[]}
        component="div"
        count={tableRows.length}
        rowsPerPage={rowsPerPage}
        page={page}
        onPageChange={handleChangePage}
      />
    </Paper>
  )
}
