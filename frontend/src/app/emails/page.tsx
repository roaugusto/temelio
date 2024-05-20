'use client'

import { ColumnData, TableData } from '@/components/TableData';
import { Button } from '@mui/material';
import Link from 'next/link';
import { useEffect, useState } from 'react';

import { Header } from '@/components/Header';
import { IEmailData, getEmails } from '@/services/emails';
import { useRouter } from 'next/navigation';
import moment from 'moment';

const columns: ColumnData[] = [
  { id: 'id', label: 'Id', minWidth: 0 },
  { id: 'organizationName', label: 'Organization Name', minWidth: 1 },
  { id: 'organizationEmail', label: 'Email', minWidth: 1 },
  { id: 'subject', label: 'Subject', minWidth: 1, align: 'center' },
  { id: 'sendDate', label: 'Send Date', minWidth: 1, align: 'center' },
]

interface IEmailTableData {
  id: string;
  organizationName: string;
  organizationEmail: string;
  subject: string;
  sendDate: string;
}

export default function Emails() {
  const router = useRouter()

  const [emails, setEmails] = useState<IEmailTableData[]>([]);

  useEffect(() => {
    loadEmails();
  }, []);


  async function loadEmails() {
    const data = await getEmails() as IEmailData[];

    const emails = data.map((email) => {
      return {
        id: email.id,
        organizationName: email.organizationName,
        organizationEmail: email.organizationEmail,
        subject: email.subject,
        sendDate: moment(email.sendDate).format('YYYY-MM-DD HH:mm:ss'),
      }
    }) as IEmailTableData[]

    console.log('emails', emails)
    setEmails(emails);
  }

  return (
    <div className="flex flex-col min-h-screen">
      <Header />

      <div className="flex mt-2 gap-3 items-center justify-between">
        <h1 className="text-2xl ml-1">Emails</h1>
        <Button variant="contained" component={Link} href="/email-add">
          + Add
        </Button>
      </div>

      <div className="mt-4">
        <TableData
          columns={columns}
          rows={emails}
          rowsPerPage={10}
          showDeleteButton={false}
          showEditButton={false}
          enableClickRow={true}
          onClick={(row) => {
            router.push(`/email-detail?id=${row.id}`)
          }}
        />
      </div>

    </div>
  );
}
