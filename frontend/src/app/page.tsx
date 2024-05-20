'use client'

import { ColumnData, GenericRowData, TableData } from '@/components/TableData';
import { IOrganizationData, deleteOrganization, getOrganizations } from '@/services/organization';
import { Button } from '@mui/material';
import Link from 'next/link';
import { useEffect, useState } from 'react';

import { DialogMessage } from '@/components/DialogMessage';
import { Header } from '@/components/Header';
import { useRouter } from 'next/navigation';

const columns: ColumnData[] = [
  { id: 'id', label: 'Id', minWidth: 0 },
  { id: 'name', label: 'Name', minWidth: 1 },
  { id: 'address', label: 'Address', minWidth: 1 },
  { id: 'email', label: 'E-mail', minWidth: 1 }
]

export default function Home() {
  const router = useRouter()

  const [organizations, setOrganizations] = useState<IOrganizationData[]>([]);
  const [isDialogOpened, setDialogOpened] = useState(false)
  const [rowSelected, setRowSelected] = useState<GenericRowData>()

  useEffect(() => {
    loadOrganizations();
  }, []);


  async function loadOrganizations() {
    const data = await getOrganizations();
    setOrganizations(data);
  }

  async function handleDelete(row: GenericRowData) {
    await deleteOrganization(row.id)
    loadOrganizations()
  }

  return (
    <div>
      <div className="flex mt-2 gap-3 items-center justify-between">
        <h1 className="text-2xl ml-1">Organizations</h1>
        <Button variant="contained" component={Link} href="/organization">
          + Add
        </Button>
      </div>

      <div className="mt-4">
        <TableData
          columns={columns}
          rows={organizations}
          rowsPerPage={10}
          onDelete={(row) => {
            setRowSelected(row)
            setDialogOpened(true)
          }}
          onEdit={(row) => {
            router.push(`/organization?id=${row.id}`)
          }}
        />
      </div>


      <DialogMessage
        title="Delete Organization"
        description="Are you sure you want to delete the organization?"
        description2="After deletion, data recovery will not be possible."
        open={isDialogOpened}
        btnCancel="Cancel"
        btnOk="Delete"
        onClose={async (value) => {
          if (value === true && rowSelected) {
            handleDelete(rowSelected)
          }
          setDialogOpened(false)
        }}
      />
    </div>
  );
}
