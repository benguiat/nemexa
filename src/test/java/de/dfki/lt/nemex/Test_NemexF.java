package de.dfki.lt.nemex;

import de.dfki.lt.nemex.f.NemexFController;
import de.dfki.lt.nemex.f.data.Candidate;
import de.dfki.lt.nemex.f.data.NemexFBean;
import de.dfki.lt.nemex.f.data.NemexFIndex;
import de.dfki.lt.nemex.f.similarity.SimilarityMeasure;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Test_NemexF {

    public Test_NemexF() {
        super();

    }

    public static NemexFBean setBean(int ngram, double threshold, String gazeeter) {
        NemexFBean nemexFBean = new NemexFBean();
        nemexFBean.setnGramSize(ngram);
        nemexFBean.setGazetteerFilePath(gazeeter);

        nemexFBean.setSimilarityMeasure(SimilarityMeasure.COSINE_SIMILARITY_MEASURE);
        nemexFBean.setSimilarityThreshold(threshold);

        return nemexFBean;
    }
    


    public static String run_basic(String input, int ngram, double threshold, String gazetteer) throws IOException {
        System.out.println("Starting Nemex...");

        NemexFBean nemexFBean = setBean(ngram, threshold, gazetteer);
        nemexFBean.setAligner(new de.dfki.lt.nemex.f.aligner.BucketCountPruneAligner());
        nemexFBean.setSelector(new de.dfki.lt.nemex.f.selector.ScoreSelector(nemexFBean));

        nemexFBean.setQueryString(input);

        String output = "";

        // initialize controller
        NemexFController controller = new NemexFController(nemexFBean);
        // create ngram list of input string
        controller.setCharacterNgramFromQueryString(nemexFBean.getQueryString());

        controller.reset();
        controller.process();
        controller.selectCandidates();

        if (controller.countAllFoundCandidates() != 0) {
            output = output + "<" + input + ">\n";

            //controller.printSelectedCandidates();
            Map<Long, List<Candidate>> candidates = controller.getCandidates().getCandidates();

            for (Map.Entry<Long, List<Candidate>> entry : candidates.entrySet()) {

                int oldLeft = -1;
                int oldRight = -1;

                long entityId = entry.getValue().get(0).getEntityIndex();
                List<String> wholeItem = NemexFIndex.getEntry(controller.getCandidates().getGazetteerFilePath(), entityId);

                String strWhole = "[" + wholeItem.get(0) + ", " + wholeItem.get(1) + ", " + wholeItem.get(2) + "]\n";
                output = output + strWhole;

                for (int l = 0; l < entry.getValue().size(); l++) {

                    int itemLeft = entry.getValue().get(l).getLeftSpan();
                    int itemRight = entry.getValue().get(l).getRightSpan();
                    int moveLeft = (itemLeft > 14) ? 15 : itemLeft;
                    int moveRight = (input.length() - itemRight > 14) ? 15 : input.length() - itemRight;

                    String checkString = input.substring(itemLeft, itemRight);

                    String itemString = input.substring(itemLeft - moveLeft, itemRight + moveRight);

                    //System.out.println("FOUND: L1- " + itemLeft + " L2- " + oldLeft + " R1- " + itemRight + " R2- " + oldRight);
                    if (itemLeft >= oldRight && itemRight >= oldLeft) {

                        output = output + "    >>>[" + entry.getValue().get(l) + "]" + "\n       '" + itemString + "'\n";

                    } else {
                        continue;
                    }

                    oldLeft = itemLeft;
                    oldRight = itemRight;

                }
            }
        }

        return output;

    }

    public static void main(String[] args) throws IOException {
        //args0 = ngram, args1 = threshold, args2 = gazeeter
        setBean(5, 0.8, "src/main/webapp/resources/MedicalTerms-mwl-plain.txt");

        String input
                = "It is known that insulin can form aggregates, fibrils and gel-like structures when it is subjected to chemical "
                + "and/or physical stress, e.g. increased temperatures and shaking. This can lead to obstruction of the implantable "
                + "pump and under-delivery of insulin. Hyperglycaemia, ketoacidosis or coma may develop within hours in case of malfunction "
                + "of the pump system. As soon as patients notice a rapid increase in blood glucose, which does not respond to a bolus dose of insulin, "
                + "the possibility of pump obstruction should be investigated by a physician trained to perform pump investigations. "
                + "From experience gained in a 6-month comparative phase III study (HUBIN_L_05335) with Insuman Implantable administered via the "
                + "Medtronic MiniMed Implantable Pump in 84 patients aged 26 to 80 years (see section 5.1) "
                + "and from clinical experience with insulin human 100 IU/ml and 40 IU/ml, "
                + "the following adverse reactions were observed.";

        //args3 = input, args0 = ngram, args1 = threshold, args2 = gazetteer
        System.out.println(run_basic(input, 5, 0.8, "src/main/webapp/resources/MedicalTerms-mwl-plain.txt"));
        System.exit(0);
    }

//    public static void main(String[] args) throws IOException {
//        //args0 = ngram, args1 = threshold, args2 = gazeeter
//        setBean(Integer.parseInt(args[0]), Double.parseDouble(args[1]), args[2]);
//        
//        //args3 = input, args0 = ngram, args1 = threshold, args2 = gazetteer
//        System.out.println(run_basic(args[3], Integer.parseInt(args[0]), Double.parseDouble(args[1]), args[2]));
//        System.exit(0);
//    }
}
