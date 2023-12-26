
(ns tutorials)

; @note
; (?)Formatted versions(?) of the following tutorials are available in the Pretty UI documentation.



; @tutorial First steps
;
; Implement the [pretty-project/pretty-css](https://github.com/pretty-project/pretty-css) library in your project.
;
; Implement the [pretty-project/pretty-icons](https://github.com/pretty-project/pretty-icons) library in your project.
;
; Implement the [pretty-project/pretty-normalizer](https://github.com/pretty-project/pretty-normalizer) library in your project.
;
; Place the 'pretty-ui.min.css' file in the HTML header.
; You can find it in the [resources/public](https://github.com/pretty-project/pretty-ui/tree/release/resources/public) folder of this repository.



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



; @tutorial Parametering
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
;     (:require [pretty-presets.api :as pretty-presets]))
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
; Pretty UI items can display [metamorphic contents](https://github.com/bithandshake/cljc-metamorphic-content).
; Metamorphic contents can be various types such as strings, hiccups, [Reagent](https://github.com/reagent-project/reagent) components,
; [app dictionary](https://github.com/mt-app-kit/cljc-app-dictionary) terms, etc.



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



; @tutorial Customizing items
;
;
;
; @title :border-color
;
; @code Usage
; [pretty-elements/button {:border-color (keyword or string)}]
; [pretty-elements/button {:border-color :soft-blue}]
; [pretty-elements/button {:border-color "#888"}]
;
; @code Values
; :inherit, :transparent,
; :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning,
; :black, :grey, :white, :hard-blue, :soft-blue, :hard-green, :khaki-green, :soft-green, :hard-purple, :soft-purple, :hard-red, soft-red
;
;
;
; @title :border-position
;
; @code Usage
; [pretty-elements/button {:border-position (keyword)}]
; [pretty-elements/button {:border-position :top}]
;
; @code Values
; :all, :bottom, :top, :left, :right, :horizontal, :vertical
;
;
;
; @title :border-radius
;
; @code Usage
; [pretty-elements/button {:border-radius (map)}]
; [pretty-elements/button {:border-radius {:all :xs}}]
; [pretty-elements/button {:border-radius {:all :xs :tr :m}}]
;
; @code Values
; :tl, :tr, :br, :bl, :all (keyword)
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :fill-color
;
; @code Usage
; [pretty-elements/button {:fill-color (keyword or string)}]
; [pretty-elements/button {:fill-color :soft-blue}]
; [pretty-elements/button {:fill-color "#888"}]
;
; @code Values
; :inherit, :transparent,
; :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning,
; :black, :grey, :white, :hard-blue, :soft-blue, :hard-green, :khaki-green, :soft-green, :hard-purple, :soft-purple, :hard-red, soft-red
;
;
;
; @title :fill-pattern
;
; @code Usage
; [pretty-elements/button {:fill-pattern (keyword)}]
; [pretty-elements/button {:fill-pattern :striped}]
;
; @code Values
; :cover, :striped
;
;
;
; @title :font-size
;
; @code Usage
; [pretty-elements/button {:font-size (keyword)}]
; [pretty-elements/button {:font-size :xl}]
;
; @code Values
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
;
;
;
; @title :font-weight
;
; @code Usage
; [pretty-elements/button {:font-weight (keyword)}]
; [pretty-elements/button {:font-weight :medium}]
;
; @code Values
; :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
;
;
;
; @title :text-color
;
; @code Usage
; [pretty-elements/button {:text-color (keyword or string)}]
; [pretty-elements/button {:text-color :hard-purple}]
; [pretty-elements/button {:text-color "#555"}]
;
; @code Values
; :inherit,
; :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning,
; :black, :grey, :white, :hard-blue, :soft-blue, :hard-green, :khaki-green, :soft-green, :hard-purple, :soft-purple, :hard-red, soft-red
;
;
;
; @title :width
;
; @code Usage
; [pretty-elements/button {:width (keyword, px or string)}]
; [pretty-elements/button {:width :xxl}]
; [pretty-elements/button {:width 100}]
; [pretty-elements/button {:width "10%"}]
;
; @code Values
; :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
