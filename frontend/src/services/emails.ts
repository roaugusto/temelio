import { toastMessage } from "@/util/toastMessage";
import { ApiInterface, apiHost } from "./apiConfig";

export interface IEmailData {
  id?: string | null;
  organizationId: string;
  organizationEmail: string;
  organizationName: string;
  subject: string;
  content: string;
  sendDate: string;
}


export async function getEmails(): Promise<IEmailData[]> {
  try {
    const res = await fetch(`${apiHost}/emails`);

    if (!res.ok) {
      toastMessage("Error when searching for emails", "error");
    }

    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      throw new Error("API response does not contain 'date' field");
    }

    return resApi.data as IEmailData[];
  } catch (error) {
    console.error("Error when searching for emails:", error);
    throw error;
  }
}

export async function getEmail(id: string): Promise<IEmailData | null> {
  try {
    const res = await fetch(`${apiHost}/emails/${id}`);

    if (!res.ok) {
      toastMessage("Error when searching for email", "error");
    }

    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      return null;
    }

    return resApi.data as IEmailData;
  } catch (error) {
    console.error("Error when searching for email:", error);
    throw error;
  }
}
