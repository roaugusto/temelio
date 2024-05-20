import { toastMessage } from "@/util/toastMessage";
import { ApiInterface, apiHost } from "./apiConfig"

export interface ITemplateData {
  id?: string;
  name: string;
  subject: string;
  template: string;
}

export async function getTemplate(): Promise<ITemplateData> {
  try {
    const res = await fetch(`${apiHost}/template`);

    if (!res.ok) {
      toastMessage("Error when searching for template", "error");
    }

    const resApi = await res.json() as ApiInterface;

    return resApi.data as ITemplateData;
  } catch (error) {
    console.error("Error when searching for template:", error);
    throw error;
  }
}

export async function updateTemplate(data: ITemplateData): Promise<void> {
  try {
    const res = await fetch(`${apiHost}/template`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      toastMessage("Error when updating template", "error");
      return
    }

    toastMessage('Template updated successfully!', 'success')
    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      throw new Error("API response does not contain 'date' field");
    }
  } catch (error) {
    console.error("Error when updating template:", error);
    throw error;
  }
}

export async function createTemplate(data: ITemplateData): Promise<void> {
  try {
    const res = await fetch(`${apiHost}/template`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      toastMessage("Error when creating template", "error");
      return
    }

    toastMessage('Template created successfully!', 'success')
    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      throw new Error("API response does not contain 'date' field");
    }
  } catch (error) {
    console.error("Error when creating template:", error);
    throw error;
  }
}