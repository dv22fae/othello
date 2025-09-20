#!/bin/bash

# Example script for the Othello assignment. Make sure to make it executable (chmod +x othello)
# Change the last line (java Othello ...) if necessary
#
# usage: bash othello <position> <time_limit> <do_compile>
# 
# Author: Ola Ringdahl
# Changed by: Fredrik Alexandre and Samuel Hagner

position=$1
time_limit=$2
do_compile=$3 

if [ "$#" -ne 3 ]; then
	# do_compile not set (not enough input arguments)
	do_compile=0
fi

# Changes directory to the location of your program 
# $(dirname "$0") is the path to where this script is located  
cd "$(dirname "$0")" # don't change this

if [ $do_compile -eq 1 ]; then
	# Compile the code:
	javac *.java \
	2> >(grep -v -E '^Picked up (JAVA_TOOL_OPTIONS|_JAVA_OPTIONS):' >&2)
else
	# Call your Java program with a position and time limit:
	java Othello $position $time_limit \
	2> >(grep -v -E '^Picked up (JAVA_TOOL_OPTIONS|_JAVA_OPTIONS):' >&2)
fi
