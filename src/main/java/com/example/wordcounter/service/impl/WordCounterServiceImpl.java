package com.example.wordcounter.service.impl;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.wordcounter.service.TranslatorService;
import com.example.wordcounter.service.WordCounterService;
import com.example.wordcounter.util.EnglishDictionary;


@Service
public class WordCounterServiceImpl implements WordCounterService{

    @Autowired
    private TranslatorService translatorService;

    private Map<String,AtomicInteger> wordMap = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void addWords(List<String> words) { 
        
        for (String word : words) {
            word = word.toLowerCase();

            //Check passed word in english dictonary else translate it into english
            if (!EnglishDictionary.isPresent(word)) {
                word = translatorService.translateToEnglish(word);
                /*
                 * Here either we will get exact translated word or similar word. Or else we will get message 
                 * that service was unable to translate the word.
                 * Need to handle accordingly then
                 */               

           }  

        //Put the word in map and increament count if already present   
        wordMap.computeIfAbsent(word, w -> new AtomicInteger(0)).incrementAndGet(); 

        }      
        
    }

    @Override
    public int getWordCount(String word) {  
        AtomicInteger wordCount = wordMap.get(word);        
        return wordCount != null ? wordCount.get() : 0; 
    }    
    
}