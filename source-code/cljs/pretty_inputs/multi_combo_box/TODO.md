
### ENTER pressed

 If the field expandable is visible ...
 ... if any option is highlighted ...
     ... add the LABEL of the highlighted option to the value group.
     ... hide the expandable.
     ... discard the option highlighter.
... if no option is highlighted ...
    ... if the field is empty ...
        ... hide the expandable.
    ... if the field is not empty ...
        ... add the field content to the value group.
        ... empty the field.
        ... hide the expandable.

If the field expandable is not visible ...
... if the field is not empty ...
    ... add the field content to the value group.
    ... empty the field.

### COMMA pressed (prevent default!)     

If the field is not empty ...
... add the field content to the value group.
... empty the field.

### Field value changed

- discard the highlighting

### Field blurred

If the field is not empty ...
... add the field content to the value group.
... empty the field.
