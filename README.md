# Protocol Definition for Socket Calculator

Date: 2023-11-09

Author: Lee minwoo

# 1. Introduction

This document defines the protocol for a socket-based calculator. The calculator supports four operations: addition, subtraction, multiplication, and division.

# 2. Protocol

The protocol consists of the following messages:

# 2.1. Request Message

The request message is sent from the client to the server. It contains the following fields:

Operation: The operation to be performed (e.g., ADD, SUB, MUL, DIV).
Operand1: The first operand.
Operand2: The second operand.
The request message is formatted as follows:

Operation Operand1 Operand2
For example, the following request message requests the server to add 5 and 3:

ADD 5 3
# 2.2. Response Message

The response message is sent from the server to the client. It contains the following fields:

HasError: Indicates whether an error occurred (e.g., yes, no).
ErrorCode: The error code if an error occurred (e.g., TOO_MANY_ARGUMENTS, DIVIDED_BY_ZERO).
Message: The error message or the answer if no error occurred.
The response message is formatted as follows:

JSON
{
  "hasError": "yes" or "no",
  "errorCode": "ErrorCode",
  "message": "ErrorMessage or Answer"
}

For example, the following response message indicates that an error occurred because the request contained too many arguments:

JSON
{
  "hasError": "yes",
  "errorCode": "TOO_MANY_ARGUMENTS",
  "message": "too_many_arguments"
}

The following response message indicates that the server successfully added 5 and 3:

JSON
{
  "hasError": "no",
  "errorCode": "none",
  "message": "8"
}

# 3. Error Codes

The following error codes may be returned by the server:

TOO_MANY_ARGUMENTS: The request contained too many arguments.
DIVIDED_BY_ZERO: The client attempted to divide by zero.
WRONG_EXPRESSION: The expression is invalid.

# 4. Example Usage

The following is an example of how to use the socket calculator:

The client sends the following request message to the server:

ADD 5 3
The server processes the request and sends the following response message to the client:

JSON
{
  "hasError": "no",
  "errorCode": "none",
  "message": "8"
}
코드를 사용할 때는 주의하시기 바랍니다. 자세히 알아보기
The client displays the answer to the user.

# 5. Conclusion

This protocol definition document provides a comprehensive overview of the protocol for the socket-based calculator. This protocol allows clients to perform basic arithmetic operations using a socket-based communication channel.

README for GitHub

This repository contains the source code for a socket-based calculator. The calculator supports four operations: addition, subtraction, multiplication, and division.

To use the calculator, you will need to create a client and a server. The client will send requests to the server, and the server will respond with the results.

The client code is located in the client directory. The server code is located in the server directory.

To build the client, run the following command:

make client
To build the server, run the following command:

make server
To start the client, run the following command:

./client
To start the server, run the following command:

./server
Once the client and server are running, you can enter a request. The request should be in the following format:

Operation Operand1 Operand2
For example, to add 5 and 3, you would enter the following request:

ADD 5 3
The server will respond with the result of the operation. In this case, the server would respond with the following:

8
