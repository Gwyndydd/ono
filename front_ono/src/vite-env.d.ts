/// <reference types="vite/client" />

interface ImportMetaEnv {
    readonly VITE_APP_NAME: string;
    readonly VITE_DEFAULT_PORT: string;
    readonly VITE_BACKEND_BASE_URL: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}
