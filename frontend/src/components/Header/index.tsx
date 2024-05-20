import { Button } from '@mui/material';
import Image from "next/image";
import Link from 'next/link';

import logoImage from '@/assets/images/temelio.png';

export function Header() {
  return (
    <div className="p-4 flex items-center justify-between">
      <div className='flex gap-3'>
        <Image src={logoImage} alt="logo" />
        <h1 className="text-2xl font-semibold">Temelio Nonprofit Management</h1>
      </div>


      <div className='flex gap-3'>
        <Link href="/" className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md'>
          Organizations
        </Link>
        <Link href="/donation" className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md'>
          Donations
        </Link>
        <Link href="/emails" className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md'>
          Emails
        </Link>
        <Link href="/settings" className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md'>
          Settings
        </Link>
      </div>
    </div>
  )
};