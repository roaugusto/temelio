import { toastMessage } from "@/util/toastMessage";
import { ApiInterface, apiHost } from "./apiConfig"

export interface IOrganizationData {
  id?: string | null;
  name: string;
  address: string;
  email: string;
}

export async function getOrganizations(): Promise<IOrganizationData[]> {
  try {
    const res = await fetch(`${apiHost}/organizations`);

    if (!res.ok) {
      toastMessage("Error when searching for organizations", "error");
    }

    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      throw new Error("API response does not contain 'date' field");
    }

    return resApi.data as IOrganizationData[];
  } catch (error) {
    console.error("Error when searching for organizations:", error);
    throw error;
  }
}

export async function getOrganization(id: string): Promise<IOrganizationData | null> {
  try {
    const res = await fetch(`${apiHost}/organizations/${id}`);

    if (!res.ok) {
      toastMessage("Error when searching for organization", "error");
    }

    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      return null;
    }

    return resApi.data as IOrganizationData;
  } catch (error) {
    console.error("Error when searching for organization:", error);
    throw error;
  }
}

export async function createOrganization(data: IOrganizationData): Promise<void> {
  try {
    const res = await fetch(`${apiHost}/organizations`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      toastMessage("Error when creating organization", "error");
      return
    }

    toastMessage("Organization created successfully", "success");
  } catch (error) {
    console.error("Error when creating organization:", error);
    throw error;
  }
}

export async function updateOrganization(data: IOrganizationData): Promise<void> {

  if (!data.id) {
    toastMessage("Error when updating organization", "error");
    return
  }

  try {
    const res = await fetch(`${apiHost}/organizations/${data.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      toastMessage("Error when updating organization", "error");
      return
    }

    toastMessage("Organization updated successfully", "success");
  } catch (error) {
    console.error("Error when updating organization:", error);
    throw error;
  }
}

export async function deleteOrganization(id: string): Promise<void> {
  try {
    const res = await fetch(`${apiHost}/organizations/${id}`, {
      method: "DELETE",
    });

    if (!res.ok) {
      toastMessage("Error when deleting organization", "error");
      return
    }

    toastMessage("Organization deleted successfully", "success");
  } catch (error) {
    console.error("Error when deleting organization:", error);
    throw error;
  }
}