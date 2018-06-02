/* COMP20007 Assignment 2
   Ashley Curtis 695514
*/
#include <time.h>
#include <stdio.h>
#include <string.h>
#include "spell.h"
#include <stdlib.h>
#include <limits.h>
#include "hashtbl.h"

int min(int a, int b);
void freeArray(char **array, int n);
void fill_hash(HashTable *table, List *dictionary);
void dist_1_array(char *word, int n, char **dist_1, int *counter);
void get_dist1(char *word, char **closest, HashTable *table, char **dist_1);
void fill_array(char **dic_array, List *dictionary);
int edit_distance(char *word1, char *word2);


// see Assignment Task 1: Computing edit distance
void print_edit_distance(char *word1, char *word2) {
	printf("%d\n", edit_distance(word1, word2));
}

// see Assignment Task 2: Enumerating all possible edits
void print_all_edits(char *word) {
	int counter = 0;
	int n = strlen(word);
	//53*n + 26 is the number of 1 edit distance words for word of length n
	char *dist_1[53*n + 26];
	//fill the array with all possible distance 1 words
	dist_1_array(word, n, dist_1, &counter);
	for(int i =0; i<counter; i++){
		printf("%s\n", dist_1[i]);
	}
	freeArray(dist_1, (53*n + 26));
}

// see Assignment Task 3: Spell checking
void print_checked(List *dictionary, List *document) {
	//create a hash table 3 times larger than
	//the number of words in dictionary
	HashTable *table = new_hash_table(3 * dictionary->size);
	fill_hash(table, dictionary);
	Node *temp = document->head;
	//for every word in document, if the word is present
	//in the dictionary by hash lookup, print
	for (int i=0; i<document->size; i++){
		if (hash_table_has(table, temp->data)){
			printf("%s\n", (char*)temp->data);
		}
		//if it is not, print with '?'
		else {
			printf("%s?\n", (char*)temp->data);
		}
		temp = temp->next;
	}
	free_hash_table(table);	
}

// see Assignment Task 4: Spelling correction
void print_corrected(List *dictionary, List *document) {
	//create a hash table 3 times larger than
	//the number of words in dictionary
	HashTable *table = new_hash_table(3 * dictionary->size);
	fill_hash(table, dictionary);
	char **dic_array = malloc(256*sizeof(char)*dictionary->size);
	fill_array(dic_array, dictionary);
	
	Node *temp = document->head;
	//loop through each word in document
	for (int j=0; j<document->size; j++){
		int cur_len = strlen(temp->data);
		//check for no spelling error ie. exists in dictionary
		if (hash_table_has(table, temp->data))
			printf("%s\n", (char*)temp->data);
		/*only allow strings less than 35 characters to pass
		into the optimal algorithm*/
		else if (cur_len<=15){
			//current closest word to document word
			char *closest = NULL;
			/*array with size to hold all words of distance 1
			depending on legth of string*/
			char *dist_1[53*cur_len + 26];
			//fill the array and get the closest and most probable word
			get_dist1(temp->data, &closest, table, dist_1);
			//if there is a word of distance 1, print it
			if(closest != NULL)
				printf("%s\n", closest);
			else{
				//else look for 2nd closest
				int i=0;
				char *closest2 = NULL;
				int current_best = INT_MAX;
				//loop through entire distance 1 array
				while(i!=53*cur_len + 26){
					closest = NULL;
					/*for every distance 1 word, fill an array of ITS
					dist 1 word (ie. dist 2 from original word*/
					char *dist_2 [53*strlen(dist_1[i]) + 26];
					get_dist1(dist_1[i], &closest, table, dist_2);
					/*if there is a distance 2 word and it's more
					probable in dictionary, make it the current dist 2 word*/
					if(closest != NULL){
						if(hash_table_get(table, closest)<current_best){
							current_best = hash_table_get(table, closest);
							closest2 = closest;
						}
					}
					i++;
				}
				//if there is a distance 2 word, print it
				if(closest2 != NULL)	
					printf("%s\n", closest2);
				else{
					//if not, naively iterate through dictionary
					//searching for dist 3 words
					for(int c=0; c<dictionary->size; c++){
						if(edit_distance((char*)dic_array[c],
							temp->data)==3){
							printf("%s\n", dic_array[c]);
							break;
						}
						//if its not there print word with '?'
						else if(c==(dictionary->size)-1){
							printf("%s?\n", (char*)temp->data);
						}
					}
				}
			}
		}
		//if string too long, naively iterate through dictionary
		else{
			for(int c=0; c<dictionary->size; c++){
				if(edit_distance((char*)dic_array[c],
					temp->data)==3){
					printf("%s\n", dic_array[c]);
					break;
				}
				else if(c==(dictionary->size)-1){
					printf("%s?\n", (char*)temp->data);
				}
			}
		}
		temp = temp->next;
	}
	free_hash_table(table);
	free(dic_array);
}



/****Helper Functions ******/
//function to iterate through distance 1 strings
//and find if there is a dictionary match
void get_dist1(char *word, char **closest, HashTable *table, char **dist_1){
	int counter = 0;
	int current_best = INT_MAX;
	int n = strlen(word);
	dist_1_array(word, n, dist_1, &counter);
	for(int i=0; i<counter; i++){
		if (hash_table_has(table, dist_1[i])){
			if(hash_table_get(table, dist_1[i])<current_best){
				current_best = hash_table_get(table, dist_1[i]);
				*closest = dist_1[i];
			}
		}
	}
}
//functon used in edit distance to return the smaller of 2 ints
int min(int a, int b){
	if (a<=b)
		return a;
	return b;
}

void freeArray(char **array, int n){
	for(int i=0; i<n; i++){
		free(array[i]);
	}
}

//function passes through dictionary and fills hash table with it
void fill_hash(HashTable *table, List *dictionary){
	int counter =1;
	Node *temp = dictionary->head;
	while(temp!=NULL){
		hash_table_put(table, temp->data, counter);
		temp = temp->next;
		counter++;
	}
}

//core of code for task 2 and used in task 4 to fill up
//arrays with all the distance 1 words of a given word
void dist_1_array(char *word, int n, char **dist_1, int *counter){
	//for loops to delete a single character n times for each char in string
	for(int i = 0; i<n; i++){
		char *temp;
		//malloc space for size of string
		temp = malloc((n) * sizeof(char));
		for(int j = 0; j<n-1; j++){
				if(j<i)
					temp[j]=word[j];
				else {
					temp[j] = word[j+1];
				}
			}
			temp[n-1] = '\0';
			dist_1[*counter] = temp;
			(*counter)++;
	}
	//iterates through each spot of sting (before and after and between chars)
	//and places every letter of alphabet one at a time
	char *alphabet = "abcdefghijklmnopqrstuvwxyz";
	for(int i=0; i<n+1; i++){
		for(int j=0; j<strlen(alphabet); j++){
			char *temp_add;
			//malloc n+2 because of adding a char and space for '\0'
			temp_add = malloc((n+2)* sizeof(char));
			for(int k=0; k<i; k++){
				temp_add[k] = word[k];
			}
			temp_add[i] = alphabet[j];
			for(int m=i; m<n; m++){
				temp_add[m+1] = word[m];
			}
			temp_add[n+1] = '\0';
			dist_1[*counter]=temp_add;
			(*counter)++;
		}
	}
	//iterates through each char and replaces it with every letter of alphabet
	for(int i=0; i<n; i++){
		for(int j=0; j<strlen(alphabet); j++){
			char *temp_replace;
			//malloc n+1 since no new chars except '\0' at end
			temp_replace = malloc((n+1)* sizeof(char));
			for(int k=0; k<i; k++){
				temp_replace[k] = word[k];
			}
			temp_replace[i] = alphabet[j];
			for(int m=i+1; m<n; m++){
				temp_replace[m] = word[m];
			}
			temp_replace[n] = '\0';
			dist_1[*counter]=temp_replace;
			(*counter)++;
		}
	}
}

//fills an array with every word in dictionary
void fill_array(char **dic_array, List *dictionary){
	Node *temp = dictionary->head;
	for(int i=0; i<dictionary->size; i++){
		dic_array[i] = temp->data;
		temp = temp->next;
	}
	dic_array[dictionary->size] = '\0';
}

//returns Levenshtein distance between 2 words
//core of task 1 and part of task 4
int edit_distance(char *word1, char *word2) {
	int n = strlen(word1);
	int m = strlen(word2);
	int E[n+1][m+1];
	//fill grid axis
	for(int i = 0; i<n+1; i++){
		E[i][0] = i;
	}
	//still filling axis
	for(int j = 1; j<m+1; j++){
		E[0][j] = j;
	}
	E[0][0] = 0;
	for(int i = 1; i<n+1; i++){
		for(int j = 1; j<m+1; j++){
			//if words have same char at given spot, cost is 0, otherwise 1
			int sub_cost = (word1[i-1]==word2[j-1]) ? 0 : 1;
			E[i][j] = min(E[i-1][j-1] + sub_cost, min(E[i-1][j]+1,
				E[i][j-1]+1));
		}
	}
	return E[n][m];
	
}