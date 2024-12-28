import { FunctionComponent, useMemo } from "react";

const SearchStudyProgramListComponent : FunctionComponent = () => {

    const [dataSP, ]

    const dataForCard = useMemo(()=>{
        return dataSP?.map((d)=>{
            return {
                id: d.id,
                name :d.name,
                description : d.description,
                prive : d.prive,
            };
        });
    }, [dataSP]);


    return(
        
    );
};

export default SearchStudyProgramListComponent;