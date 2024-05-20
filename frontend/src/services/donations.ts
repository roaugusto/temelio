import { toastMessage } from "@/util/toastMessage";
import { ApiInterface, apiHost } from "./apiConfig"
import { IOrganizationData } from "./organization";

export interface IDonationData {
  id?: string | null;
  amount: number;
  isDonationNotified?: boolean;
  organization: IOrganizationData
}

export interface IDonationChange {
  id?: string | null;
  organizationId: string
  amount: number;
  isDonationNotified?: boolean;
}


export async function getDonations(): Promise<IDonationData[]> {
  try {
    const res = await fetch(`${apiHost}/donations`);

    if (!res.ok) {
      toastMessage("Error when searching for donations", "error");
    }

    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      throw new Error("API response does not contain 'date' field");
    }

    return resApi.data as IDonationData[];
  } catch (error) {
    console.error("Error when searching for donations:", error);
    throw error;
  }
}

export async function getDonation(id: string): Promise<IDonationData | null> {
  try {
    const res = await fetch(`${apiHost}/donations/${id}`);

    if (!res.ok) {
      toastMessage("Error when searching for donation", "error");
    }

    const resApi = await res.json() as ApiInterface;

    if (!resApi.data) {
      return null;
    }

    return resApi.data as IDonationData;
  } catch (error) {
    console.error("Error when searching for donation:", error);
    throw error;
  }
}

export async function createDonation(data: IDonationChange): Promise<void> {

  const newDonation = {
    organizationId: data.organizationId,
    amount: data.amount,
  } as IDonationChange

  try {
    const res = await fetch(`${apiHost}/donations`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(newDonation),
    });

    if (!res.ok) {
      toastMessage("Error when creating donation", "error");
      return
    }

    toastMessage("Donation created successfully", "success");
  } catch (error) {
    console.error("Error when creating donation:", error);
    throw error;
  }
}

export async function updateDonation(data: IDonationChange): Promise<void> {

  if (!data.id) {
    toastMessage("Error when updating donation", "error");
    return
  }

  try {
    const res = await fetch(`${apiHost}/donations/${data.id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!res.ok) {
      toastMessage("Error when updating donation", "error");
      return
    }

    toastMessage("Donation updated successfully", "success");
  } catch (error) {
    console.error("Error when updating donation:", error);
    throw error;
  }
}

export async function deleteDonation(id: string): Promise<void> {
  try {
    const res = await fetch(`${apiHost}/donations/${id}`, {
      method: "DELETE",
    });

    if (!res.ok) {
      toastMessage("Error when deleting donation", "error");
      return
    }

    toastMessage("Donation deleted successfully", "success");
  } catch (error) {
    console.error("Error when deleting donation:", error);
    throw error;
  }
}

export async function notifyDonation(): Promise<void> {
  try {
    const res = await fetch(`${apiHost}/donations/send-notification`, {
      method: "POST",
    });

    if (!res.ok) {
      toastMessage("Error when notifying donation", "error");
      return
    }

    toastMessage("Donations notified successfully", "success");
  } catch (error) {
    console.error("Error when notifying donation:", error);
    throw error;
  }
}