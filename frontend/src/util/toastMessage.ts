import { toast } from "react-toastify";

export function toastMessage(message: string, type: 'success' | 'error' | 'info' | 'warning') {
  toast(message, {
    hideProgressBar: true,
    autoClose: 3000,
    type: type,
    theme: 'colored',
  })
}
