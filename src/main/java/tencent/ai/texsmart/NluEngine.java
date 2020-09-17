package tencent.ai.texsmart;

import com.sun.jna.Pointer;
import com.sun.jna.WString;

//import com.sun.jna.Platform;

public class NluEngine {

	public boolean init(String dataDir, int workerCount) {
		enginePointer = CLib.INSTANCE.Nlu_CreateEngine(dataDir, workerCount);
		return enginePointer != null;
	}

	/**
	 * Analyze text and get parsing results (word segmentation, POS tagging, NER, semantic expansion, etc.)
	 * @param text: The input natural language text
	 * @return Parsing results
	 */
	public NluOutput parseText(String text) {
		WString options = null;
		WString text_wstr = new WString(text);
		Pointer result = CLib.INSTANCE.Nlu_ParseTextExt(enginePointer, text_wstr, text_wstr.length(), options);
		NluOutput output = new NluOutput();
		output.dataPointer = result;
		return output;
	}

	/**
	 * Analyze text and get parsing results (word segmentation, POS tagging, NER, semantic expansion, etc.)
	 * @param text: The input natural language text
	 * @param options: Parsing options, in JSON format
	 * @return Parsing results
	 */
	public NluOutput parseText(String text, String options) {
		WString text_wstr = new WString(text);
		WString options_wstr = new WString(options);
		Pointer result = CLib.INSTANCE.Nlu_ParseTextExt(enginePointer, text_wstr, text_wstr.length(), options_wstr);
		NluOutput output = new NluOutput();
		output.dataPointer = result;
		return output;
	}
	
	protected void finalize() {
		if(enginePointer != null) {
			CLib.INSTANCE.Nlu_DestroyEngine(enginePointer);
			enginePointer = null;
		}
	}

	protected Pointer enginePointer = null;
}
