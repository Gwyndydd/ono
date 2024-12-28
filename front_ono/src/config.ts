import { isString } from "radash";

const toBoolean = ( boolStr: string | undefined, defaultValue: boolean = false): boolean => {
  if (!isString(boolStr)) {
    return defaultValue;
  }

  if (boolStr.toLowerCase() === "false") {
    return false;
  }
  if (boolStr.toLowerCase() === "true") {
    return true;
  }

  return defaultValue;
};

const config = {
  appName: import.meta.env.VITE_APP_NAME,
  backendBaseUrl: import.meta.env.VITE_BACKEND_BASE_URL,
  // Debug
  enableDevTools: toBoolean(import.meta.env.VITE_ENABLE_DEV_TOOLS, false),
};

export default config;