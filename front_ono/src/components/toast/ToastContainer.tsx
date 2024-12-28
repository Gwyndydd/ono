
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../store/redux.store';
import { removeToast } from '../../store/slices/toast.slice';
import Toast from './Toast';
import { FunctionComponent } from 'react';

import './Toast.css';

const ToastContainer: FunctionComponent = () => {
  const dispatch = useDispatch();
  const toasts = useSelector((state: RootState) => state.toast.toasts);

  return (
    <div className='toast-container'>
      {toasts.map((toast) => (
        <Toast
          key={toast.id}
          {...toast}
          onDismiss={() => dispatch(removeToast(toast.id))}
        />
      ))}
    </div>
  );
};

export default ToastContainer;