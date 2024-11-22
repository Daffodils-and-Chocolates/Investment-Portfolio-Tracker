package com.example.demo.exception.notFound;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("ERROR :\nUser with ID " + id + " not found.");
    }
}
/*  

\033[0;31m is the ANSI escape code to start red text.
\033[0m resets the color back to default after the message.

 */