package com.ChatBot;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RagConfiguration {

	private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);
	
	@Value("classpath:/docs/FAQ.txt")
	private Resource faq;
	
	@Value("vectorstore.json")
	private String vectorStoreName;
	
	@Bean
	SimpleVectorStore simpleVectorStore(EmbeddingClient embeddingClient) {
		SimpleVectorStore simpleVectorStore = new SimpleVectorStore(embeddingClient);
		File vectorStoreFile = getVectorStore();
		if(vectorStoreFile.exists()) {
			log.info("vector Store file Exists");
			simpleVectorStore.load(vectorStoreFile);
			
		}else {
			log.info("Vector store file doesnot exist,loading documents");
			TextReader textReader = new TextReader(faq);
			textReader.getCustomMetadata().put("filename", "FAQ.txt");
			List<Document> documents = textReader.get();
			TokenTextSplitter textSplitter = new TokenTextSplitter();
			List<Document> splitdocuments = textSplitter.apply(documents);
			
			simpleVectorStore.add(splitdocuments);
			simpleVectorStore.save(vectorStoreFile);
		}
		return simpleVectorStore;
		 
		 
	}
	
	private File getVectorStore() {
		Path path = Paths.get("src","main","resources","data");
		String absolutePath = path.toFile().getAbsoluteFile()+"/"+vectorStoreName;
		return new File(absolutePath);
	}
	
}
