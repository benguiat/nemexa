package de.dfki.lt.nemex;

import de.dfki.lt.nemex.f.NemexFController;
import de.dfki.lt.nemex.f.data.NemexFBean;
import de.dfki.lt.nemex.f.similarity.SimilarityMeasure;

public class Test_NemexF {
	
	//TODO Make this a property file

	public static void main(String[] args) {
		long time1;
		long time2;
		
		NemexFBean nemexFBean = new NemexFBean();
		
		// BEGIN - Setting parameters
		
		// test Faerie Paper

//				nemexFBean.setnGramSize(1);
//				nemexFBean.setGazetteerFilePath("src/main/webapp/resources/faerie.txt");
//		
//				nemexFBean.setSimilarityMeasure(SimilarityMeasure.ED_SIMILARITY_MEASURE);
//				nemexFBean.setSimilarityThreshold(0.0);
//		
//				nemexFBean.setQueryString("an efficient filter for approximate membership checking. venkaee "
//						+ "shga kamunshik kabarati, dong xin, surauijt chadhurisigmod.");
		
				//nemexFBean.setQueryString("chakrabarti");
		
				//nemexFBean.setQueryString("venkaee shga kamunshi");

		// test with examples from:
		nemexFBean.setnGramSize(5);
		

//		nemexFBean.setSimilarityMeasure(SimilarityMeasure.COSINE_SIMILARITY_MEASURE);
//		nemexFBean.setSimilarityThreshold(0.8);
//
//		nemexFBean.setGazetteerFilePath("resources/MedicalTerms-mwl-plain.txt");
//		nemexFBean.setQueryString(
//				"Cytochemical myeloperoxidase (MPO) positivity represents the gold standard for discrimination "
//						+ "between lymphatic and myeloid blasts. Rarely, cytochemical MPO reaction may be positive in >or=3% of "
//						+ "blasts with clear lymphoblastep morphology. We present 5 patients with cytochemically MPO-positive acute "
//						+ "leukemia classified as lymphoblastic by cytomorphology and anlymphophlastic (n=3) or biphenotypic (n=2) "
//						+ "by immunophenotyping, who entered first-line treatment for lymphoblastic leukemia. "
//						+ "The former 3 are in first remission and both with biphenotypic leukemia relapsed with acute myeloid leukemia. "
//						+ "The study primarily shows that cytochemical MPO expression in childhood acute leukemia revealing typical "
//						+ "lymfoblastic morphology and phenotype does rarely exist. Although a small number of patients studied, "
//						+ "cytochemical MPO expression in acute leukemia does not seem to require myeloid leukemia treatment in case of "
//						+ "otherwise lymphoblastic cytomorphology and phenotype."
//						);
				
//				nemexFBean.setQueryString("lymfoblastic");

//		// NE-List:
		nemexFBean.setnGramSize(3);
		nemexFBean.setSimilarityMeasure(SimilarityMeasure.COSINE_SIMILARITY_MEASURE);
		nemexFBean.setSimilarityThreshold(0.7);
		
//		// "/Users/gune00/data/NE-Lists/pantelWikiListSeeds.txt"
		nemexFBean.setGazetteerFilePath("src/main/webapp/resources/MedicalTerms-mwl-plain.txt");
		nemexFBean.setQueryString(
				"It is known that insulin can form aggregates, fibrils and gel-like structures when it is subjected to chemical "
				+ "and/or physical stress, e.g. increased temperatures and shaking. This can lead to obstruction of the implantable "
				+ "pump and under-delivery of insulin. Hyperglycaemia, ketoacidosis or coma may develop within hours in case of malfunction "
				+ "of the pump system. As soon as patients notice a rapid increase in blood glucose, which does not respond to a bolus dose of insulin, "
				+ "the possibility of pump obstruction should be investigated by a physician trained to perform pump investigations. "
				+ "From experience gained in a 6-month comparative phase III study (HUBIN_L_05335) with Insuman Implantable administered via the "
				+ "Medtronic MiniMed Implantable Pump in 84 patients aged 26 to 80 years (see section 5.1) "
				+ "and from clinical experience with insulin human 100 IU/ml and 40 IU/ml, "
				+ "the following adverse reactions were observed."
			);
//		
		// END of parameter setting
		
		// set aligner and selector method
		nemexFBean.setSelector(new de.dfki.lt.nemex.f.selector.ScoreSelector(nemexFBean));
		
		// initialize controller
		NemexFController controller = new NemexFController(nemexFBean);
		// create ngram list of input string
		controller.setCharacterNgramFromQueryString(nemexFBean.getQueryString());
		
		// define aligner
		nemexFBean.setAligner(new de.dfki.lt.nemex.f.aligner.BucketCountPruneAligner());
		
		System.out.println(nemexFBean.toString());
		
		time1 = System.currentTimeMillis();
		controller.reset();
		controller.process();
		controller.selectCandidates();
		time2 = System.currentTimeMillis();
		controller.printSelectedCandidates();
		System.out.println("System time (msec): " + (time2-time1));
	}

}
