/* Ashley Curtis 695514
Assignment 1 COMP20007 2018 Semester 1
Task 1 and 2 

*/

#include <stdio.h>
#include <stdlib.h>
#include "query.h"
#include "list.h"
#include "heap.h"
#include <assert.h>

int most_terms(Index *index, int max_term, int max_term_number);
void replace_heapItem(Heap *h, Index *index, int i);
void order_and_print(Heap *h, int n_results);


/* ******* TASK 1 ************
***************************** */
void print_array_results(Index *index, int n_results, int n_documents) {	
	//guard for no results
	if (n_results == 0){
		printf("You asked for 0 results!\n");
		exit(0);
	}
	float *array = (float*)malloc(n_documents*sizeof(float));
	
	//fill up the array with all the scores of IDs
	for (int i=0; i<index->num_terms; i++){
		while(!list_is_empty(index->doclists[i])){
			Node *temp = index->doclists[i]->head;
			Document *temp_doc = temp->data;
			array[temp_doc->id] += temp_doc->score;
			temp=temp->next;
			list_remove_start(index->doclists[i]);
		}
	}
	
	//initialise heap with first n_results indexes
	Heap *h = new_heap(n_results);
	for (int i =0; i<n_results; i++){
		heap_insert(h, array[i], i, -1);
	}
	
	//iterate through array and insert into heap if its score is greater
	//than the current smallest value in the heap
	for (int i=n_results; i<n_documents; i++){
		if(array[i] > heap_peek_key(h)){
			heap_remove_min(h);
			heap_insert(h, array[i], i, -1);
		}
	}
	order_and_print(h, n_results);
}



/* ******* TASK 2 ************
***************************** */
void print_merge_results(Index *index, int n_results) {
	if (n_results == 0){
		printf("You asked for 0 results!\n");
		exit(0);
	}
	//declare both heaps
	Heap *h_final = new_heap(n_results);
	Heap *h_inter = new_heap(index->num_terms);
	HeapItem check_doc;

	//initialise final heap
	for (int i=0; i<n_results; i++){
		heap_insert(h_final, -1, -1, -1);
	}
	//initialise intermediate heap with first ID of each term
	for (int i=0; i<index->num_terms; i++){
		replace_heapItem(h_inter, index, i);
	}
	

	//find the search term with the most ID results (longest list)
	int max_term = 0;
	int max_term_number = 0;
	max_term_number = most_terms(index, max_term, max_term_number);
	
	check_doc = heap_remove(h_inter);
	int current_id = check_doc.key;
	float current_score = 0;
	
	//loop unitl term with longest list is empty
	while (!list_is_empty(index->doclists[max_term_number])){
		int i = check_doc.which_term;

		//check if current ID matches the one off the intermediate heap
		if (current_id == (int)check_doc.key){
				//if so, add its score to running total
				current_score += check_doc.data;
				//replace 
				if (!list_is_empty(index->doclists[i])){
					replace_heapItem(h_inter, index, i);
					check_doc = heap_remove(h_inter);
				}
				else{
					check_doc = heap_remove(h_inter);
				}
		}
		//if we are finished with an ID, add it to final heap if
		//it is greater than the current smallest value in h_final
		else{
			if (current_score > heap_peek_key(h_final)){
				heap_remove_min(h_final);
				heap_insert(h_final, current_score, current_id, -1);
			}
			current_score = 0;
			current_id = check_doc.key;
		}	
	}
	//order and print
	order_and_print(h_final, n_results);
}

//finds the search term with the longest list
int most_terms(Index *index, int max_term, int max_term_number){
	for(int i=0; i<index->num_terms; i++){
		if(index->doclists[i]->size>=max_term){
			max_term = index->doclists[i]->size;
			max_term_number = i;
		}			
	}
	return max_term_number;
}

//takes the next item off the top of the current list and inserts into a heap
void replace_heapItem(Heap *h, Index *index, int i){
	Node *temp = index->doclists[i]->head;
	Document *temp_doc = temp->data;
	heap_insert(h, temp_doc->id, temp_doc->score, i);
	list_remove_start(index->doclists[i]);
}

void order_and_print(Heap *h, int n_results){
	HeapItem *ordered = (HeapItem*)malloc(n_results*sizeof(HeapItem));
	for (int i = 0; i<n_results; i++){
		ordered[i] = heap_remove(h);
	}
	for(int i=n_results-1; i>=0; i--){
		printf("%6d %.6f\n", (int)ordered[i].data, ordered[i].key);
	}
}