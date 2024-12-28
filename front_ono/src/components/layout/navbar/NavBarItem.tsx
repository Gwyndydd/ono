import { FunctionComponent, ReactNode, useMemo } from 'react';
import { Link, useMatch } from 'react-router-dom';
import { arrayToClass } from '../../../utils/utils';

interface NavbarItemProps {
  label: string;
  icon: ReactNode;
  to: string;
  state?: Record<string, unknown>;
  replace?: boolean;
}

const NavbarItem: FunctionComponent<NavbarItemProps> = ({ label, icon, to, replace, state }) => {
  const match = useMatch(`${to}/*`);

  const customClass = useMemo(() => arrayToClass(['navbar-item', match ? 'navbar-item--active' : '']), [match]);

  return (
    <Link className={customClass} to={to} replace={replace} state={state}>
      {icon && <div className='navbar-item__icon'>{icon}</div>}
      <div className='navbar-item__label'>{label}</div>
    </Link>
  );
};

export default NavbarItem;