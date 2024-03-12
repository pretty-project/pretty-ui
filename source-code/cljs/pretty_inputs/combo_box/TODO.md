
### DOWN pressed (prevent default!)

- show expandable
- highlight next option

### UP pressed (prevent default!)

- show expandable
- highlight prev option

### ESC pressed

If the surface of the combo-box ...
... displays options, pressing the ESC button:
    - hides the expandable.
    - discards the highlight on the highlighted option.
... doesn't display any options, pressing the ESC button:
    - fires the original ESC event of the text-field.

### ENTER pressed

If the expandable of the combo-box is visible ...
... and any option is highlighted, pressing the ENTER button ...
    ... hides the expandable,
    ... discards the highlight on the highlighted option,
    ... stores the highlighted option into the application state,
    ... uses the highlighted option's LABEL as the field content,
    ... fires the 'on-type-ended' event.
... and no option is highlighted, pressing the ENTER button ...
    ... hides the expandable.

### Field value changed

- discard the highlighting
