import galaxyFold from "../../public/images/galaxyfold.png";
import babyCarseat from "../../public/images/baby-carseat.png";
import millenniumFalcon from "../../public/images/millennium-falcon.png";

export function getImgPath(category) {
  switch (category) {
    case "D":
      return galaxyFold;
    case "C":
      return babyCarseat;
    case "H":
      return millenniumFalcon;
  }
}
