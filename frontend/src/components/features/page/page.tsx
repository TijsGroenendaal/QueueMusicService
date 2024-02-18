import { Header } from "@/components/features/header/header";

const Page = ({ children }: { children: React.ReactNode }) => {
  return (
    <main className="sm:w-[640px] w-full m-auto pt-4 px-4 md:px-0 h-full flex flex-col">
      <Header />
      {children}
    </main>
  );
};

export default Page;
