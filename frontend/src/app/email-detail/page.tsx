'use client'

import { Header } from "@/components/Header";
import { InputControl, InputRoot } from "@/components/Input";
import { ChevronLeft } from "lucide-react";
import { useRouter, useSearchParams } from "next/navigation";

import { IEmailData, getEmail } from "@/services/emails";
import { useEffect, useState } from "react";
import moment from "moment";

const codeExample = `
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
</head>
<body>
  <h1>Hello, World!</h1>
</body>
</html>
`;

export default function EmailDetail() {
  const router = useRouter()
  const searchParams = useSearchParams()
  const id = searchParams.get('id')
  const [email, setEmail] = useState<IEmailData | null>(null)

  useEffect(() => {
    loadEmail()
  }, [])


  async function loadEmail() {
    if (id === null) {
      return
    }
    const data = await getEmail(id);
    setEmail(data);
  }


  return (
    <div className="flex flex-col min-h-screen">
      <Header />

      <div className="flex mt-2 items-center">
        <ChevronLeft className="cursor-pointer" size={36} onClick={() => router.back()} />
        <h1 className="text-2xl ml-1">Email Detail</h1>
      </div>

      <div className="flex gap-3 border p-4 rounded-md flex-col bg-white mt-3">
        <label
          htmlFor="organizationName"
          className="flex items-center text-sm font-medium text-blue-900"
        >
          Organization Name
        </label>
        <div className="gap-6">
          <InputRoot>
            <InputControl
              name="organizationName"
              value={email?.organizationName}
              disabled
            ></InputControl>
          </InputRoot>
        </div>
        <label
          htmlFor="organizationEmail"
          className="flex items-center text-sm font-medium text-blue-900"
        >
          Organization Email
        </label>
        <div className="gap-6">
          <InputRoot>
            <InputControl
              name="organizationEmail"
              value={email?.organizationEmail}
              disabled
            ></InputControl>
          </InputRoot>
        </div>
        <div className="flex gap-6">
          <div className="w-96">
            <label
              htmlFor="subject"
              className="flex items-center text-sm font-medium text-blue-900"
            >
              Subject
            </label>
            <div className="gap-6">
              <InputRoot>
                <InputControl
                  name="subject"
                  value={email?.subject}
                  disabled
                ></InputControl>
              </InputRoot>
            </div>
          </div>
          <div>
            <label
              htmlFor="sendDate"
              className="flex items-center text-sm font-medium text-blue-900"
            >
              Date / Time
            </label>
            <div className="gap-6">
              <InputRoot>
                <InputControl
                  name="sendDate"
                  value={moment(email?.sendDate).format('YYYY-MM-DD HH:mm:ss')}
                  disabled
                ></InputControl>
              </InputRoot>
            </div>
          </div>
        </div>

        <label
          htmlFor="content"
          className="flex items-center text-sm font-medium text-blue-900"
        >
          Content
        </label>
        <div className="border p-4 border-blue-100 rounded-md">
          <div dangerouslySetInnerHTML={{ __html: email?.content || '' }} />
        </div>
      </div>

    </div>
  );
}