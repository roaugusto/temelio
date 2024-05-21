'use client'

import Image from "next/image";
import Link from 'next/link';
import { useState } from 'react';

import logoImage from '@/assets/images/temelio.png';

export function Header() {
  const [menuOpen, setMenuOpen] = useState(false);

  const toggleMenu = () => {
    setMenuOpen(!menuOpen);
  };

  const closeMenu = () => {
    setMenuOpen(false);
  };

  return (
    <header className="p-4 flex items-center justify-between relative z-10 bg-white">
      <div className='flex gap-3 items-center'>
        <Image src={logoImage} alt="logo" />
        <h1 className="text-sm lg:text-2xl font-semibold">Temelio Nonprofit Management</h1>
      </div>

      <div className="md:hidden">
        <button onClick={toggleMenu} className="block text-gray-800 focus:outline-none">
          {menuOpen ? (
            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          ) : (
            <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16m-7 6h7" />
            </svg>
          )}
        </button>
      </div>

      <div className={`md:flex flex-col md:flex-row gap-3 md:items-center md:justify-end ${menuOpen ? 'absolute top-16 right-0 w-80 bg-white p-4 rounded-md shadow-md z-20 transform translate-x-0' : 'hidden'}`}>
        <Link href="/" onClick={closeMenu} className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md block md:inline-block'>
          Organizations
        </Link>
        <Link href="/donation" onClick={closeMenu} className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md block md:inline-block'>
          Donations
        </Link>
        <Link href="/emails" onClick={closeMenu} className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md block md:inline-block'>
          Emails
        </Link>
        <Link href="/settings" onClick={closeMenu} className='hover:text-blue-800 hover:bg-blue-100 p-2 rounded-md block md:inline-block'>
          Settings
        </Link>
      </div>
    </header>
  )
};
