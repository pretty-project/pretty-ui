
(ns tutorials)

; @tutorial First steps
;
; Implement the [pretty-project/pretty-css](https://github.com/pretty-project/pretty-css) library in your project.
;
; Implement the [pretty-project/pretty-icons](https://github.com/pretty-project/pretty-icons) library in your project.
;
; Implement the [pretty-project/pretty-normalizer](https://github.com/pretty-project/pretty-normalizer) library in your project.
;
; Place the 'pretty-ui.min.css' file in your HTML header, you can find it in the [resources/public](https://github.com/pretty-project/pretty-ui/tree/release/resources/public) folder of this repository.

; @tutorial Introduction
;
; This library includes:
;
; - Elements such as buttons, inputs, and static UI elements.
; - Components that are composed and preset versions of elements (mostly for applications).
; - UI layouts such as popups (modals), surfaces and sidebars.
; - Website components.

; @tutorial Parametering

;All items (components / elements / layouts / website components) take two arguments.

;1. A unique ID that is optional and must be keyword type. If not provided the item
;   generates one for itself.)

;2. A property map that describes the item's look and behaviour.

;(ns my-namespace
;    (:require [elements.api :as elements]))

;(defn my-text-field
;  []
;  [:<> [elements/text-field           {:border-radius {:all :xs} :fill-color :muted}]
;       [elements/text-field :my-field {:border-radius {:all :xs} :fill-color :muted}]))

;> If the provided ID changes the item doesn't take the new ID, it keeps the first one that was provided.

;> If no ID is provided and the item rerenders it doesn't generate a new ID for itself, it keeps the first one that was generated.

; @tutorial Presets

;Every item (component / element / layout / website component) could use a provided preset ID
;(passed as the `:preset` property) to implement preset property maps or functions.

;(ns my-namespace
;    (:require [pretty-elements.api :as pretty-elements]
;              [pretty-presets.api  :as pretty-presets]))

;(pretty-presets/reg-preset! :my-preset
;                            {:border-radius {:all :xs}}]))

;(defn my-ui
;  []
;  [:<> [pretty-elements/text-field {:label "My text field"   :preset :my-preset}]
;       [pretty-elements/text-field {:label "Your text field" :preset :my-preset}]))

;(pretty-presets/reg-preset! :my-preset
;                            #(assoc % :border-radius {:all :xs})]))

;(defn my-ui
;  []
;  [:<> [pretty-elements/text-field {:label "My text field"   :preset :my-preset}]
;       [pretty-elements/text-field {:label "Your text field" :preset :my-preset}]))

; @tutorial Event handlers

;The components, elements and layouts take Re-Frame events as event handlers.

;(ns my-namespace
;    (:require [elements.api  :as elements]
;              [re-frame.core :as r]))

;(r/reg-event-fx :my-event (fn [_ _] {:dispatch [:do-something!]}))
;
;[elements/button {:on-click [:my-event]}]

;You can use Re-Frame metamorphic-events. Find more information about them at:
;[github.com/bithandshake/re-frame-api](https://github.com/bithandshake/re-frame-api)

;(ns my-namespace
;    (:require [elements.api :as elements]
;              [re-frame.api :as r]))

;(r/reg-event-fx :my-event (fn [_ _] [:do-something!]))
;
;[elements/button {:on-click {:dispatch-n [[:my-event]]}}]

;You can pass functions as handlers as well but the passed function will be evaluated
;and its return value will be dispatched as a metamorphic-event, so make sure your
;function returns a valid Re-Frame metamorphic-event or a `NIL` value.

;(ns my-namespace
;    (:require [elements.api :as elements]))

;(defn my-function [] (do-something! ...))
;
;[elements/button {:on-click my-function}]

; @tutorial Content types

; @tutorial Value paths of inputs

;Input elements and components can take `:value-path` property which points its
;value stored in the Re-Frame state.

;(ns my-namespace
;    (:require [elements.api :as elements]))

;[elements/text-field {:value-path [:my-value]}]

;Every element or component that takes `:value-path` property can generates a path
;in case of you don't want to specify it.

;In the following example the text-field element uses its default value path:
;`[:elements :element-handler/input-values *element-id*]`

;(ns my-namespace
;    (:require [elements.api :as elements]))

;[elements/text-field {}]

; @tutorial Options paths of optionable inputs
