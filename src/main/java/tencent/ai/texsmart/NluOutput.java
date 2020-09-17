package tencent.ai.texsmart;

import com.sun.jna.Pointer;

import java.util.ArrayList;

public class NluOutput {

	/** A word or a phrase */
	public class Term {
		public String str;
		public int offset = 0;
		public int len = 0;
		public String tag;

		public String toString() {
			return "str:" + str + " offset:" + offset + " len:" + len + " tag:" + tag;
		}

		public int length() { return this.str.length(); }

		public boolean equals(Object obj) {
			if (obj instanceof Term) {
				Term term = (Term)obj;
				if (this.tag == term.tag && this.tag.equals(term.tag)) {
					return true;
				}
			}

			return super.equals(obj);
		}
	}
	
	/** Entity type information */
	public class EntityType {
		public String name;
		public String i18n;
		public int flag = 0;
		public String path;
	}

	/** Entity information */
	public class Entity {
		public String str;
		public int offset = 0;
		public int len = 0;
		public EntityType type;
		public String meaning;
	}

	/**
	 * Get the normalized text
	 * @param result: The parsing result object
	 * @return The normalize text
	 */
	public String normText() {
		Pointer len = null;
		Pointer ptr = CLib.INSTANCE.Nlu_GetNormText(dataPointer, len);
		return getWideStr(ptr);
	}
	
	/**
	 * Get words from the parsing result.
	 * @param result: The parsing result object
	 * @return A list of words
	 */
	public ArrayList<Term> words() {
		ArrayList<Term> termList = new ArrayList<Term>();
		int count = CLib.INSTANCE.Nlu_GetWordCount(dataPointer);
		for(int idx = 0; idx < count; idx++) {
			Pointer termPtr = CLib.INSTANCE.Nlu_GetWord(dataPointer, idx);
			Term newTerm = new Term();
			newTerm.str = getWideStr(CLib.INSTANCE.Nlu_TermStr(termPtr));
			newTerm.offset = CLib.INSTANCE.Nlu_TermOffset(termPtr);
			newTerm.len = CLib.INSTANCE.Nlu_TermLen(termPtr);
			newTerm.tag = getWideStr(CLib.INSTANCE.Nlu_TermTag(termPtr));
			termList.add(newTerm);
		}
		return termList;
	}
	
	/**
	 * Get phrases from the parsing result.
	 * @param result: The parsing result object
	 * @return A list of phrases
	 */
	public ArrayList<Term> phrases() {
		ArrayList<Term> temrList = new ArrayList<Term>();
		int count = CLib.INSTANCE.Nlu_GetPhraseCount(dataPointer);
		for(int idx = 0; idx < count; idx++) {
			Pointer termPtr = CLib.INSTANCE.Nlu_GetPhrase(dataPointer, idx);
			Term newTerm = new Term();
			newTerm.str = getWideStr(CLib.INSTANCE.Nlu_TermStr(termPtr));
			newTerm.offset = CLib.INSTANCE.Nlu_TermOffset(termPtr);
			newTerm.len = CLib.INSTANCE.Nlu_TermLen(termPtr);
			newTerm.tag = getWideStr(CLib.INSTANCE.Nlu_TermTag(termPtr));
			temrList.add(newTerm);
		}
		return temrList;
	}

	/**
	 * Get entities from the parsing result.
	 * @param result: The parsing result object
	 * @return A list of entities
	 */
	public ArrayList<Entity> entities() {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		int count = CLib.INSTANCE.Nlu_GetEntityCount(dataPointer);
		for(int idx = 0; idx < count; idx++) {
			Pointer entityPtr = CLib.INSTANCE.Nlu_GetEntity(dataPointer, idx);
			Entity newEntity = new Entity();
			newEntity.str = getWideStr(CLib.INSTANCE.Nlu_EntityStr(entityPtr));
			newEntity.offset = CLib.INSTANCE.Nlu_EntityOffset(entityPtr);
			newEntity.len = CLib.INSTANCE.Nlu_EntityLen(entityPtr);
			newEntity.type = new EntityType();
			Pointer typePtr = CLib.INSTANCE.Nlu_EntityType(entityPtr);
			newEntity.type.name = getWideStr(CLib.INSTANCE.Nlu_EntityTypeName(typePtr));
			newEntity.type.i18n = getWideStr(CLib.INSTANCE.Nlu_EntityTypeI18n(typePtr));
			newEntity.type.flag = CLib.INSTANCE.Nlu_EntityTypeFlag(typePtr);
			newEntity.type.path = getWideStr(CLib.INSTANCE.Nlu_EntityTypePath(typePtr));
			newEntity.meaning = getWideStr(CLib.INSTANCE.Nlu_EntityMeaning(entityPtr));
			entityList.add(newEntity);
		}
		return entityList;
	}
	
	public void close() {
		if(dataPointer != null) {
			CLib.INSTANCE.Nlu_DestroyOutput(dataPointer);
			dataPointer = null;
		}
	}

	//An utility function
	protected String getWideStr(Pointer strPtr) {
		if(strPtr == null) {
			return null;
		}
		
		return strPtr.getWideString(0);
	}
	
	protected void finalize() {
		close();
	}

	protected Pointer dataPointer;
}
