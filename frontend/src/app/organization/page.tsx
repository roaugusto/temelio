'use client'

import { Header } from "@/components/Header";
import { InputControl, InputRoot } from "@/components/Input";
import { ChevronLeft } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";
import { FormProvider, useForm } from "react-hook-form";

import { createOrganization, getOrganization, updateOrganization } from "@/services/organization";
import { zodResolver } from '@hookform/resolvers/zod';
import { useEffect, useState } from "react";
import { z } from 'zod';

const organizationFormSchema = z.object({
  name: z.string().min(1, { message: 'Please provide the organization name!' }),
  address: z.string().min(1, { message: 'Please provide the address!' }),
  email: z.string().email({ message: 'Invalid email!' }),
})

type organizationFormData = z.infer<typeof organizationFormSchema>
type FormKeys = keyof organizationFormData


export default function Organization() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const id = searchParams.get('id')

  const [isNewOrganization, setIsNewOrganization] = useState<boolean>(false)

  const organizationForm = useForm<organizationFormData>({
    resolver: zodResolver(organizationFormSchema),
  })

  const {
    handleSubmit,
    formState: { errors },
    setValue,
  } = organizationForm

  useEffect(() => {
    loadOrganization()
  }, [])

  async function loadOrganization() {
    if (id === null) {
      setIsNewOrganization(true)
      return
    }

    const data = await getOrganization(id);
    if (data) {
      fillFormWithExistingData(data)
    }
  }

  async function fillFormWithExistingData(
    organizationData: organizationFormData,
  ) {
    const objectKeys = Object.keys(
      organizationFormSchema.shape,
    ) as FormKeys[]

    for (const key of objectKeys) {
      if (key in organizationData) {
        setValue(key, organizationData[key])
      }
    }
  }

  async function onSubmit(data: organizationFormData) {
    if (isNewOrganization) {
      await createOrganization(data)
    } else {
      const updateData = { ...data, id }
      await updateOrganization(updateData)
    }
    router.back()
  }

  return (
    <div>
      <div className="flex mt-2 items-center">
        <ChevronLeft className="cursor-pointer" size={36} onClick={() => router.back()} />
        <h1 className="text-lg lg:text-2xl ml-1">Organization</h1>
      </div>

      <FormProvider {...organizationForm}>
        <form
          id="organization"
          className="al mb-3 mt-3 flex w-full flex-col gap-2"
          onSubmit={handleSubmit(onSubmit)}
        >
          <div className="flex gap-3 border p-4 rounded-md flex-col bg-white">
            <label
              htmlFor="name"
              className="flex items-center text-sm font-medium text-blue-900"
            >
              Name
            </label>
            <div className="gap-6">
              <InputRoot>
                <InputControl
                  name="name"
                  hasError={!!errors.name}
                  errorMessage={errors.name?.message}
                ></InputControl>
              </InputRoot>
            </div>
            <label
              htmlFor="address"
              className="flex items-center text-sm font-medium text-blue-900"
            >
              Address
            </label>
            <div className="gap-6">
              <InputRoot>
                <InputControl
                  name="address"
                  hasError={!!errors.address}
                  errorMessage={errors.address?.message}
                ></InputControl>
              </InputRoot>
            </div>
            <label
              htmlFor="email"
              className="flex items-center text-sm font-medium text-blue-900"
            >
              E-mail
            </label>
            <div className="gap-6">
              <InputRoot>
                <InputControl
                  name="email"
                  hasError={!!errors.email}
                  errorMessage={errors.email?.message}
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