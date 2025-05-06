export class ApiError {
  constructor(
    public message: string,
    public status: number,
    public code: string,
  ) {}
}

export type ApiResponse<T> = T | ApiError;
