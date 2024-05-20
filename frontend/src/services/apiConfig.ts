export interface ApiInterface {
  statusCode: number;
  message: string;
  data: any;
}

const API_URL = process.env.NEXT_PUBLIC_API_URL

export const apiHost = API_URL;

