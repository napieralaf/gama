package msi.gama.doc.websiteGen;

import java.util.List;

class Topic {
	public String m_id;
	public String m_name;
	public List<String> m_associatedLearningConceptList;
	public float m_xPos;
	public float m_yPos;
	public float m_xPosBigHallow;
	public float m_yPosBigHallow;
	public float m_sizeBigHallow;
	
	public Topic(String id, String name, float xPos, float yPos, 
			float xPosBigHallow, float yPosBigHallow, float sizeBigHallow,
			List<String> associatedLearningConceptList) {
		m_id = id;
		m_name = name;
		m_xPos = xPos;
		m_yPos = yPos;
		m_xPosBigHallow = xPosBigHallow;
		m_yPosBigHallow = yPosBigHallow;
		m_sizeBigHallow = sizeBigHallow;
		m_associatedLearningConceptList = associatedLearningConceptList;
	}
}