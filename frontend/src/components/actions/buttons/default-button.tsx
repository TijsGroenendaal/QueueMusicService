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
    <button className="hover:border-gray-300 active:border-black hover:border-2 rounded-md px-4 py-2 hover:cursor-pointer" onClick={onClick}>
      {children}
    </button>
  );
};
