import { FC, ReactNode } from "react";

export interface DefaultButtonProps {
  onClick: () => void;
  children: ReactNode;
}

export const DefaultButton: FC<DefaultButtonProps> = ({
  onClick,
  children,
}) => {
  return (
    <button className="bg-indigo rounded-md px-4 py-2" onClick={onClick}>
      {children}
    </button>
  );
};
