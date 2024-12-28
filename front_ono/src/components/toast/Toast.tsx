import { FunctionComponent, useEffect, useMemo } from "react";
import { Button } from "react-aria-components";
import { arrayToClass } from "../../utils/utils";

type ToastProps = {
    id: string;
    message: string;
    type?: 'success' | 'error' | 'info';
    onDismiss: () => void;
  };
  
const Toast: FunctionComponent<ToastProps> = ({ message, type, onDismiss }) => {

    useEffect(() => {
        const timer = setTimeout(onDismiss, 5000); // Auto-dismiss après 5 secondes
        return () => clearTimeout(timer);
    }, [onDismiss]);

    const customClass = useMemo(() => arrayToClass(['toast', type ?? '']), [type]);

    return (
        <div className={customClass}>
            <span>{message}</span>
            <Button onPress={onDismiss}>✖</Button>
        </div>
    );
};

export default Toast;