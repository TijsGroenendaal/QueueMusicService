import { FC, ReactNode } from "react";

export interface DefaultButtonProps {
  onClick: () => void;
  children: ReactNode;
  className?: string;
  disabled?: boolean;
}

export const DefaultButton: FC<DefaultButtonProps> = ({
  onClick,
  children,
  className,
  disabled = false,
}) => {
  return (
    <button
      className={`${disabled ? "text-gray-500" : "hover:border-gray-400 active:border-black"} border-gray-100 shadow-md border-2 rounded-md px-4 py-2 hover:cursor-pointer ${className ?? ""}`}
      onClick={onClick}
      disabled={disabled}
    >
      {children}
    </button>
  );
};
