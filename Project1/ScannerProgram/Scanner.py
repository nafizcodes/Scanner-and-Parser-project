
import sys

#Func Name: main()
#Func Input: (NO INPUT)
#Func Output: 0
#Func Objective: (To Drive the Program)
def main():
    #File Opener
    try:
        #Take the file Argument
        file = sys.argv[1]

        #Opening The File
        fileOpen = open(file, 'r')

        #Generate A Token List
        tokenList = scan(fileOpen)

        #Closing The File
        fileOpen.close()

        #Print the Token List
        lparen = '('
        rparen = ')'
        if tokenList == "error.":
            print(tokenList)
        else:
            print(str(lparen) + ', '.join(tokenList) + str(rparen))
    
    #Print Statement If Entered File Does Not Exist
    except FileNotFoundError:
        print("Error: ERROR READING FILE")

    #Print Statement If No File Entered
    except IndexError:
        print("Error: File Name Not Inserted : Please Insert File Name")

#Function name: scan()
#Func Input: fileOpen (The program utilized to create a token list from)
#Func Output: tokenList (an array of tokens)
#Func Objective: (To scan the entire file, locate the individual tokens, and print as a list)
def scan(fileOpen):
    #Intialize tokenList as an empty list
    tokenList = []

    #Variable to check that the "longest possible token" rule for tokens with multiple characters/number is followed
    tokenVerifier = False

    #While loop that runs while EOF is not reached
    while True:
        #While loop that runs while a character exists within the file
        while True:
            #Verify that there is not a duplicate read token,
            if tokenVerifier == False:
                pointer = fileOpen.read(1)

            #Return Read Verifier back to false
            tokenVerifier = False;
          
          #Case for when there is no character
            if pointer == "":
                break

            #Characters that are not added to the tokenList
            if pointer == " ":
                continue
            elif pointer == "\n":
                continue
            elif pointer == "\t":
                continue
            elif pointer == "/":
                pointer = fileOpen.read(1)

                #Instance for not adding comments to the tokenList
                if pointer == "/":
                    while pointer != "\n":
                        pointer = fileOpen.read(1)
                elif pointer == "*":
                    while True:
                        pointer = fileOpen.read(1)
                        if pointer == "*":
                            pointer = fileOpen.read(1)
                            if pointer == "/":
                                break
                            else:
                                continue

               #Instance for adding Division token to tokenList
                else:
                    tokenVerifier = True
                    tokenList.append("division")
                continue

            #Instance utilized to differentiate tokens into their respective types using the provided automata
            elif pointer == "*":
                tokenList.append("times")
                continue
            elif pointer == "(":
                tokenList.append("left parenthesis")
                continue
            elif pointer == ")":
                tokenList.append("right parethesis")
                continue
            elif pointer == "+":
                tokenList.append("addition")
                continue
            elif pointer == "-":
                tokenList.append("subtraction")
                continue
            elif pointer == ":":
                pointer = fileOpen.read(1)
                if pointer == "=":
                    tokenList.append("assign")
                else:
                    return "Error: Invalid Token"
                continue

             #Instance to Handle the "." (decimal), to ensure only 1 iteration of "." is allowed
            elif pointer == ".":
                pointer = fileOpen.read(1)
                if pointer.isdigit():
                    while pointer.isdigit():
                        pointer = fileOpen.read(1)
                        if pointer.isdigit():
                            continue
                        else:
                            readCheck = True
                            tokenList.append("number")
                else:
                    return "Error: Invalid Token"
                continue

            #Instance for the validation of the "number" token
            elif pointer.isdigit():
                while pointer.isdigit():
                    pointer = fileOpen.read(1)
                    if pointer.isdigit():
                        continue
                    else:
                        readCheck = True
                        tokenList.append("number")
                continue
           
           #Instance to validate the "read" and "write" tokens
            elif pointer.isalpha():
                #Check if the sequence of tokens are letters

                currentToken = []
                while pointer.isalpha():
                    currentToken.append(pointer)
                    pointer = fileOpen.read(1)

                    if pointer.isalpha():
                        continue
                    else:
                        readCheck = True
                        string = ''.join([str(item) for item in currentToken])
                        string = string.lower()

                        #Verification that the sequence of tokens is for sure "read" or "write"
                        if string == "read":
                            tokenList.append("read")
                        elif string == "write":
                            tokenList.append("write")
                        else:
                            tokenList.append("id")
                        currentToken = []
                continue
            else:
                tokenList = "error."
                break
        if pointer == "":
            break
    return tokenList
main()
