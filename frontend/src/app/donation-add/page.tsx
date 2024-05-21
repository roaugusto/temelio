'use client'

import { Header } from "@/components/Header";
import { InputControl, InputRoot } from "@/components/Input";
import { ChevronLeft } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";
import { FormProvider, useForm } from "react-hook-form";

import { IDonationChange, createDonation, getDonation, updateDonation } from "@/services/donations";
import { IOrganizationData, getOrganizations } from "@/services/organization";
import { zodResolver } from '@hookform/resolvers/zod';
import { FormControl, InputLabel, MenuItem, Select } from "@mui/material";
import { useEffect, useState } from "react";
import { z } from 'zod';

const donationFormSchema = z.object({
  amount: z.preprocess((val) => parseFloat(val as string), z.number().min(1, { message: 'Please provide the donation amount!' })),
})

type donationFormData = z.infer<typeof donationFormSchema>
type FormKeys = keyof donationFormData


export default function DonationAdd() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const id = searchParams.get('id')
  const [organizations, setOrganizations] = useState<IOrganizationData[]>([]);
  const [selectedOrganization, setSelectedOrganization] = useState<IOrganizationData | null>(null)

  const [isNewDonation, setIsNewDonation] = useState<boolean>(false)

  const donationForm = useForm<donationFormData>({
    resolver: zodResolver(donationFormSchema),
  })

  const {
    handleSubmit,
    formState: { errors },
    setValue,
  } = donationForm

  useEffect(() => {
    loadOrganizations()
    loadDonation()
  }, [])


  async function loadOrganizations() {
    const data = await getOrganizations();
    setOrganizations(data);
  }

  async function loadDonation() {
    if (id === null) {
      setIsNewDonation(true)
      return
    }
    const data = await getDonation(id);

    if (data) {
      setSelectedOrganization(data.organization)
      fillFormWithExistingData(data)
    }
  }

  async function fillFormWithExistingData(
    donationData: donationFormData,
  ) {
    const objectKeys = Object.keys(
      donationFormSchema.shape,
    ) as FormKeys[]

    for (const key of objectKeys) {
      if (key in donationData) {
        setValue(key, donationData[key])
      }
    }
  }

  async function onSubmit(data: donationFormData) {
    if (isNewDonation) {
      const donation = {
        organizationId: selectedOrganization?.id,
        amount: data.amount,
      } as IDonationChange
      await createDonation(donation)
    } else {
      const donation = {
        id: id,
        organizationId: selectedOrganization?.id,
        amount: data.amount,
        isDonationNotified: false,
      } as IDonationChange
      await updateDonation(donation)
    }
    router.back()
  }

  return (
    <div>
      <div className="flex mt-2 items-center">
        <ChevronLeft className="cursor-pointer" size={36} onClick={() => router.back()} />
        <h1 className="text-lg lg:text-2xl ml-1">Donation</h1>
      </div>

      <FormProvider {...donationForm}>
        <form
          id="donation"
          className="al mb-3 mt-3 flex w-full flex-col gap-2"
          onSubmit={handleSubmit(onSubmit)}
        >
          <div className="flex gap-3 border p-4 rounded-md flex-col bg-white">
            <FormControl sx={{ m: 1, minWidth: 120 }} size="small">
              <InputLabel id="organization">Select Organization</InputLabel>
              <Select
                labelId="organization"
                id="organization"
                value={selectedOrganization?.id || ''}
                label="Select Organization"
                onChange={(e) => {
                  const organization = organizations.find((organization) => organization.id === e.target.value)
                  setSelectedOrganization(organization || null)
                }}
              >
                {organizations.map((organization) =>
                  <MenuItem
                    key={organization.id || ''}
                    value={organization.id || ''}>{organization.name}</MenuItem>
                )
                }
              </Select>
            </FormControl>
            <label
              htmlFor="amount"
              className="flex items-center text-sm font-medium text-blue-900"
            >
              Donation
            </label>
            <div className="gap-6">
              <InputRoot>
                <InputControl
                  name="amount"
                  hasError={!!errors.amount}
                  errorMessage={errors.amount?.message}
                ></InputControl>
              </InputRoot>
            </div>
          </div>

          <div className="flex justify-end">
            <button
              className="rounded-lg border bg-blue-700 px-2 py-1 font-semibold text-white shadow-sm hover:bg-blue-800"
              type="submit"
            >
              Save
            </button>
          </div>
        </form>
      </FormProvider>


    </div>
  );
}