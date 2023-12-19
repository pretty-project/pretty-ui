
(ns tutorials)

; @tutorial First steps
;
; Implement the [pretty-project/pretty-css](https://github.com/pretty-project/pretty-css) library in your project.
;
; Implement the [pretty-project/pretty-icons](https://github.com/pretty-project/pretty-icons) library in your project.
;
; Implement the [pretty-project/pretty-normalizer](https://github.com/pretty-project/pretty-normalizer) library in your project.
;
; Place the 'pretty-ui.min.css' file in the HTML header. You can find it in the [resources/public](https://github.com/pretty-project/pretty-ui/tree/release/resources/public) folder of this repository.

; @tutorial Introduction
;
; This library includes:
;
; - Elements such as buttons, inputs, and static UI elements.
; - Components that are composed and preset versions of elements (mostly for applications).
; - UI layouts such as popups (modals), surfaces and sidebars.
; - Website components.

; @tutorial Parametering
;
; Pretty UI items (components / elements / layouts / website components) take two arguments:
;
; 1. A unique ID that is optional and must be keyword type and if isn't provided the item
;    generates one for itself.
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
; If the provided ID changes the item doesn't take the new ID, it keeps the first one that was provided.
;
; @note
; If no ID is provided and the item rerenders it doesn't generate a new ID for itself, it keeps the first one that was generated.

; @tutorial Presets
;
; Pretty UI items (components / elements / layouts / website components) could use a provided preset ID
; (passed as the `:preset` property) to implement preset property maps or preset functions.
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]
;               [pretty-presets.api  :as pretty-presets]))
;
; (pretty-presets/reg-preset! :my-preset
;                             {:border-radius {:all :xs}})
;
; (defn my-ui
;   []
;   [:<> [pretty-elements/text-field {:label "My text field"      :preset :my-preset}]
;        [pretty-elements/text-field {:label "Another text field" :preset :my-preset}])
; @---
;
; @code
; (pretty-presets/reg-preset! :my-preset
;                             #(assoc % :border-radius {:all :xs}))
;
; (defn my-ui
;   []
;   [:<> [pretty-elements/text-field {:label "My text field"      :preset :my-preset}]
;        [pretty-elements/text-field {:label "Another text field" :preset :my-preset}])
; @---

; @tutorial Event handlers
;
; Pretty UI items (components, elements, layouts and website components) take Re-Frame events as event handlers.
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]
;               [re-frame.core :as r]))
;
; (r/reg-event-fx :my-event (fn [_ _] {:dispatch [:do-something!]}))
;
; (defn my-ui
;   []
;   [pretty-elements/button {:on-click [:my-event]}])
; @---
;
; The Pretty UI implements the [bithandshake/re-frame-extra](https://github.com/bithandshake/re-frame-extra) library
; that can handle Re-Frame metamorphic-events in addition to the default Re-Frame events.
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]
;               [re-frame.extra.api :as r]))
;
; (r/reg-event-fx :my-event (fn [_ _] [:do-something!]))
;
; [pretty-elements/button {:on-click {:dispatch-n [[:my-event]]}}]
; @---
;
; You can pass functions as event handlers as well. The provided function will be evaluated
; and its return value will be dispatched as a metamorphic-event, so make sure your
; function returns a valid Re-Frame metamorphic-event or NIL.
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]
;               [re-frame.extra.api :as r]))
;
; (defn my-function [] (do-something! ...))
;
; [pretty-elements/button {:on-click my-function}]
; @---

; @tutorial Content types
; ...

; @tutorial Value paths of inputs
;
; Pretty UI input elements and input components take the `:value-path` property which points
; to the input value in the Re-Frame state.
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]))
;
; [pretty-elements/text-field {:value-path [:my-value]}]
; @---
;
; Input elements and components that take the `:value-path` property can generate
; a value path in case of it isn't provided.
;
; In the following example the 'text-field' element uses a default value path:
;
; @code
; (ns my-namespace
;     (:require [pretty-elements.api :as pretty-elements]
;               [re-frame.core :as r]))
;
; (defn my-ui
;   []
;   [:<> [pretty-elements/text-field :my-text-field {}]
;        [:div "The field value in the Re-Frame state:"
;              (r/subscribe [:get-item [:pretty-elements :element-handler/input-values :my-text-field]])]])
; @---

; @tutorial Option paths of optionable inputs
; ...
