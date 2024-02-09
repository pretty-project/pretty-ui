
; FOR DOCUMENTATION READERS ONLY!
(ns tutorials)

; @note
; (?)Formatted versions(?) of the following tutorials are available in the Pretty UI documentation.









; @bug (#9912)
; If the keypress key-code is 13 (ENTER) the on-click-f function fires multiple times during the key is pressed!
; This phenomenon caused by:
; 1. The keydown event focuses the button via the 'button.side-effects/key-pressed' function.
; 2. One of the default actions of the 13 (ENTER) key is to fire the element's on-click
;    function on a focused button element. Therefore, the 'on-click-f' function
;    fires repeatedly during the 13 (ENTER) key is pressed.
; In case of using any other key than the 13 (ENTER) the 'on-click-f' function fires only by
; the 'button.side-effects/key-released' function.





; @tutorial Introduction
;
; This library includes:
;
; - UI elements (e.g., buttons, inputs, etc.).
; - UI components (more complex or preset UI elements).
; - UI layouts (popup modals, surfaces and sidebars).
; - Website components (e.g., side menu, contacts section, etc.).
;
; In the following tutorials they all referred as Pretty UI items.



; @tutorial Parameterizing
;
; Pretty UI items take two arguments:
;
; 1. A unique ID that is optional and must be a keyword.
;    (It can identify the item in control events and state subscriptions.)
;
; 2. A property map that describes the item's look and behaviour.
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]))
;
; (defn my-text-field
;   []
;   [:<> [pretty-elements/text-field           {:border-radius {:all :xs} :fill-color :muted}]
;        [pretty-elements/text-field :my-field {:border-radius {:all :xs} :fill-color :muted}])
; @---
;
; @note
; In case the ID is not provided the item generates one for itself.
;
; @note
; In case the provided ID changes the item doesn't take the new ID, it keeps the first one that was provided.
;
; @note
; In case of no ID is provided and the item gets rerendered it doesn't generate a new ID for itself, it keeps the first one that was generated.



; @tutorial Presets
;
; Pretty UI items optionally take a preset ID to implement preset property maps or preset functions.
;
; @code
; (ns my-namespace
;     (:require [pretty-presets.engine.api :as pretty-presets.engine]))
;
; (pretty-presets/reg-preset! :my-preset
;                             {:border-radius {:all :xs}})
;
; (pretty-presets/reg-preset! :my-preset-f
;                             (fn [%] (assoc % :border-radius {:all :xs})))
; @---
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]))
;
; (defn my-ui
;   []
;   [:<> [pretty-elements/text-field {:label "My text field"      :preset :my-preset}]
;        [pretty-elements/text-field {:label "Another text field" :preset :my-preset-f}])
; @---



; @tutorial Event handlers
;
; Pretty UI items can take functions and [Re-Frame](https://github.com/day8/re-frame) events as event handlers.
;
; @note
; Event handler functions and Re-Frame events take event related values as their last parameters.
; E.g., Text-field on-enter handlers take the field content as parameter.
;       Etc.
;
;
;
; @title Event handler functions
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]))
;
; (defn my-function [content-value] ...)
;
; [pretty-elements/button {:content "My button" :on-click my-function}]
; @---
;
;
;
; @title Re-Frame event handlers
;
; @code
; (ns my-namespace
;     (:require [re-frame.core :as r]))
;
; (r/reg-event-fx
;    :my-event
;    (fn [_ [_ content-value]] ...))
; @---
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]))
;
; (defn my-ui
;   []
;   [pretty-elements/button {:on-click [:my-event]}])
; @---
;
; @note
; The Pretty UI implements the [bithandshake/re-frame-extra](https://github.com/bithandshake/re-frame-extra) library
; that can handle Re-Frame metamorphic-events in addition to the default Re-Frame events.
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]))
;
; [pretty-elements/button {:on-click {:dispatch-n [[:my-event] {:dispatch-later [{:ms 50 :dispatch [:another-event]}]}]}}]
; @---



; @tutorial Content types
;
; Pretty UI items can display [metamorphic contents](https://github.com/mt-app-kit/cljc-metamorphic-content).
; Metamorphic contents can be various types such as strings, hiccups, [Reagent](https://github.com/reagent-project/reagent) components,
; [app dictionary](https://github.com/mt-app-kit/cljc-app-dictionary) terms, etc.



; @tutorial Values and value paths of inputs
;
; Pretty UI inputs can take their actual value as ...
; ... the provided ':value' parameter (as primary source),
; ... or the value in the Re-Frame state where the provided ':value-path' parameter points to (as secondary source).
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]))
;
; (defn my-text-field
;   []
;   [pretty-elements/text-field {:value-path [:my-value]}])
;
; (defn another-text-field
;   []
;   [pretty-elements/text-field {:value "My value"}])
; @---
;
; @note
; In case of no ':value-path' parameter is provided, the input will use a default Re-Frame value path.



; @tutorial Options and option paths of optionable inputs
;
; Pretty UI inputs can take their selectable options as ...
; ... the provided ':options' parameter (as primary source),
; ... or the value in the Re-Frame state where the provided ':options-path' parameter points to (as secondary source).



; @tutorial Output values of optionable inputs
;
; - If an optionable input ...
;   ... got a single option to select, its output is a single value.
;   ... got multiple options to select, its output is a vector of selected options.
;
; @code Example
; (ns my-namespace
;   (:require [pretty-inputs.api :as pretty-inputs]
;             [re-frame.core     :as r]))
;
; (defn my-checkbox
;   []
;   [pretty-inputs/checkbox {:options ["a" "b" "c"] :on-change println}])
; =>
; [], ["a"], ["a" "b"], ["a" "c"], ["a" "b" "c"], ["b"], ["b" "c"], ["c"]
;
; (defn another-checkbox
;   []
;   [pretty-inputs/checkbox {:options "abc" :on-change println}])
; =>
; nil, "abc"
; @---
