# Lamports_Assignment

## Project contributors


- **Name:** Urmil Vora
- **Roll No.:**  23CS06018
- **Name:** Rajnish Maurya
- **Roll No.:**  23CS06009
- **Name:** Bhushan Shrirame
- **Roll No.:**  23CS06002


## Instructions for Running the Program

This program requires input from the user to set up processes and their priorities, as well as information about their peers. Follow the instructions below to input the required data for three users:

1. **Compiling the Code:**

   Before running the program, you need to compile the Java code. Follow these steps:

   - Open a terminal or command prompt.
   - Navigate to the directory containing the Java file `Lamports_Mutual_Exclusion.java`.
   - Compile the code using the following command:
   
     ```
     javac Lamports_Mutual_Exclusion.java
     ```

2. **Running the Program:**

   Once the code is compiled, you can run the program. Follow these steps:

   - In the same terminal or command prompt, execute the following command:
   
     ```
     java Lamports_Mutual_Exclusion
     ```

3. **Input Parameters:**

   For each user, follow these steps to input the required data:

   - **Process ID:** Enter a unique identifier for the process.
   - **Priority:** Assign a priority level to the process (numeric value).
   - **Peer 1 IP:** Enter the IP address of the first peer.
   - **Peer 1 ID:** Assign a unique identifier to the first peer.
   - **Peer 2 IP:** Enter the IP address of the second peer.
   - **Peer 2 ID:** Assign a unique identifier to the second peer.

## Example Input

**User 1:**<br>
Enter process id: 2<br>
Enter priority: 1<br>
Enter peer1 IP: 10.10.127.128<br>
Enter peer1 ID: 1<br>
Enter peer2 IP: 10.10.127.156<br>
Enter peer2 ID: 3<br>

**User 2:**<br>
Enter process id: 3<br>
Enter priority: 2<br>
Enter peer1 IP: 10.10.127.129<br>
Enter peer1 ID: 2<br>
Enter peer2 IP: 10.10.127.157<br>
Enter peer2 ID: 4<br>

**User 3:**<br>
Enter process id: 4<br>
Enter priority: 3<br>
Enter peer1 IP: 10.10.127.130<br>
Enter peer1 ID: 3<br>
Enter peer2 IP: 10.10.127.158<br>
Enter peer2 ID: 5<br>


## Execution Timing

After entering the details for all users, every user has to execute the program at the same time if not their is a 3 second delay allowed for user to execute (The delay can be increase).
