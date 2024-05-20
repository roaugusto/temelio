'use client'

import { ColumnData, GenericRowData, TableData } from '@/components/TableData';
import { Button } from '@mui/material';
import Link from 'next/link';
import { useEffect, useState } from 'react';

import { DialogMessage } from '@/components/DialogMessage';
import { Header } from '@/components/Header';
import { IDonationData, deleteDonation, getDonations, notifyDonation } from '@/services/donations';
import { useRouter } from 'next/navigation';

const columns: ColumnData[] = [
  { id: 'id', label: 'Id', minWidth: 0 },
  { id: 'organizationName', label: 'Organization Name', minWidth: 1 },
  { id: 'amount', label: 'Donation', minWidth: 1 },
  { id: 'isDonationNotified', label: 'Notified', minWidth: 1, align: 'center' },
]

interface IDonationTableData {
  id: string;
  organizationName: string;
  amount: string;
  isDonationNotified: string;
}

export default function Donation() {
  const router = useRouter()

  const [donations, setDonations] = useState<IDonationTableData[]>([]);
  const [isDialogOpened, setDialogOpened] = useState(false)
  const [rowSelected, setRowSelected] = useState<GenericRowData>()

  useEffect(() => {
    loadDonations();
  }, []);


  async function loadDonations() {
    const data = await getDonations() as IDonationData[];

    const donations = data.map((donation) => {
      return {
        id: donation.id,
        organizationName: donation.organization.name,
        amount: `$ ${donation.amount}`,
        isDonationNotified: donation.isDonationNotified ? 'Yes' : 'No',
      }
    }) as IDonationTableData[]

    setDonations(donations);
  }

  async function handleDelete(row: GenericRowData) {
    await deleteDonation(row.id)
    loadDonations()
  }

  async function handleNotifyDonation() {
    await notifyDonation()
    loadDonations()
  }

  return (
    <div className="flex flex-col min-h-screen">
      <Header />

      <div className="flex mt-2 gap-3 items-center justify-between">
        <h1 className="text-2xl ml-1">Donations</h1>
        <div className='flex gap-3'>
          <Button variant="contained" component={Link} href="/donation-add">
            + Add
          </Button>
          <Button variant="contained" onClick={handleNotifyDonation}>
            Notify Donations
          </Button>
        </div>
      </div>

      <div className="mt-4">
        <TableData
          columns={columns}
          rows={donations}
          rowsPerPage={10}
          onDelete={(row) => {
            setRowSelected(row)
            setDialogOpened(true)
          }}
          onEdit={(row) => {
            router.push(`/donation-add?id=${row.id}`)
          }}
        />
      </div>


      <DialogMessage
        title="Delete Donation"
        description="Are you sure you want to delete the donation?"
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
