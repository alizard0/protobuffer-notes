# protobuffer-notes
> protocol buffers are a language for serializing structure data.

```protobuffer
syntax = "proto2";
package notes;

option java_package = "com.alizardo.protobuffer";
option java_outer_classname = "BookProto";

message Book {
    required string isbn = 1;
    required string name = 2;
    required int32 pages = 3;
    
    Enum BookType {
        ROMANCE = 0;
        DRAMA = 1;
        CRIME = 2;
    }

    optional BookType type = 4;
    
    message BookAuthor {
        required string name = 1;
    }

    repeated BookAuthor author = 5;
}
```

That's an example of book structure using proto-buffers, `book.proto`. This file contains several keywords, namely:
1. `syntax`, for proto buffer version
1. `java_package`, for specifying the java package where the pojo will be generated
1. `java_outer_classname`, for specifying the wrap class name
1. `optional`, for optional fields
1. `repeated`, for repeated fields, proto buffer will preserve the order of repeat values.
1. `required`, for required fields. If field isn't set, it throws `RuntimeException`. Please note, **Required Is Forever**
> You should be very careful about marking fields as required. If at some point you wish to stop writing or sending a required field, it will be problematic to change the field to an optional field – old readers will consider messages without this field to be incomplete and may reject or drop them unintentionally

In terms of data types, it supports `bool`, `int32`, `float`, `double` and `string`. 
Moreover, you can create your own classes and assign them to fields.

Lastly, the *markers* (=1, =2) represent tags used in binary encoding.
> Tag numbers 1-15 require one less byte to encode than higher numbers, so as an optimization you can decide to use those tags for the commonly used or repeated elements, leaving tags 16 and higher for less-commonly used optional elements. Each element in a repeated field requires re-encoding the tag number, so repeated fields are particularly good candidates for this optimization.  


## Links
1. https://developers.google.com/protocol-buffers