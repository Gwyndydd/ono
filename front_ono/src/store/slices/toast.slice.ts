import { createSlice, PayloadAction } from "@reduxjs/toolkit";

type Toast = {
    id: string;
    message: string;
    type?: 'success' | 'error' | 'info';
  };

  
type ToastState = {
    toasts: Toast[];
};

const initialState: ToastState = {
    toasts: [],
};

const toastSlice = createSlice({
    name: 'toast',
    initialState: initialState,
    reducers: {
        addToast: (state, action: PayloadAction<Toast>) => {
            state.toasts.push(action.payload);
        },
        removeToast: (state, action: PayloadAction<string>) => {
            state.toasts = state.toasts.filter((toast) => toast.id !== action.payload);
        },
        clearToasts: (state) => {
            state.toasts = [];
        },
    },
});

export const { addToast, removeToast } = toastSlice.actions;

export default toastSlice;