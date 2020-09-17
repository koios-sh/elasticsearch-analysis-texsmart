package tencent.ai.texsmart;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.WString;

public interface CLib extends Library {
	CLib INSTANCE = Native.loadLibrary("tencent_ai_texsmart", CLib.class);

	public Pointer Nlu_CreateEngine(String data_dir, int worker_count);
	public void Nlu_DestroyEngine(Pointer engine);

	public Pointer Nlu_ParseTextExt(Pointer engine, WString text, int text_len, WString options);
	public Pointer Nlu_ParseUtf8TextExt(Pointer engine, String text, int text_len, String options);
	public void Nlu_DestroyOutput(Pointer result);
	
	public Pointer Nlu_GetNormText(Pointer result, Pointer len);
	
	public int Nlu_GetWordCount(Pointer result);
	public Pointer Nlu_GetWord(Pointer result, int idx);
	public int Nlu_GetPhraseCount(Pointer result);
	public Pointer Nlu_GetPhrase(Pointer result, int idx);
	public Pointer Nlu_TermStr(Pointer term);
	public int Nlu_TermOffset(Pointer term);
	public int Nlu_TermLen(Pointer term);
	public Pointer Nlu_TermTag(Pointer term);
	
	public int Nlu_GetEntityCount(Pointer result);
	public Pointer Nlu_GetEntity(Pointer result, int idx);
	public Pointer Nlu_EntityStr(Pointer entity);
	public int Nlu_EntityOffset(Pointer entity);
	public int Nlu_EntityLen(Pointer entity);
	public Pointer Nlu_EntityType(Pointer entity);
	public Pointer Nlu_EntityMeaning(Pointer entity);
	public Pointer Nlu_EntityTypeName(Pointer entityType);
	public Pointer Nlu_EntityTypeI18n(Pointer entityType);
	public int Nlu_EntityTypeFlag(Pointer entityType);
	public Pointer Nlu_EntityTypePath(Pointer entityType);
}

