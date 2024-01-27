
; FOR DOCUMENTATION READERS ONLY!
(ns tutorials)

; @note
; (?)Formatted versions(?) of the following tutorials are available in the Pretty UI documentation.



; material symbols outlined is the default icon family



; @bug (#9912)
; If the keypress key-code is 13 (ENTER) the on-click-f function fires multiple times during the key is pressed!
; This phenomenon caused by:
; 1. The keydown event focuses the button via the 'button.side-effects/key-pressed' function.
; 2. One of the default actions of the 13 (ENTER) key is to fire the element's on-click
;    function on a focused button element. Therefore, the 'on-click-f' function
;    fires repeatedly during the 13 (ENTER) key is pressed.
; In case of using any other key than the 13 (ENTER) the 'on-click-f' function fires only by
; the 'button.side-effects/key-released' function.







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



; @tutorial Customizing items
;
;
;

;
; @title :class
;
; @code Usage
; [pretty-elements/button {:class (keyword or keywords in vector)]
; [pretty-elements/button {:class :my-class]
; [pretty-elements/button {:class [:my-class :another-class]]
;
;
;
; @title :click-effect
;
; @code Usage
; [pretty-elements/button {:click-effect (keyword)}]
; [pretty-elements/button {:click-effect :opacity}]
;
; @code Predefined values
; :none, :opacity
;
;
;
; @title :content
;
; @note
; Check out the [cljc-metamorphic-content](https://github.com/mt-app-kit/cljc-metamorphic-content) library.
;
; @code Usage
; [pretty-elements/button {:content (metamorphic-content)}]
; [pretty-elements/button {:content "My string"}]
; [pretty-elements/button {:content :my-dictionary-term}]
; [pretty-elements/button {:content 123}]
; [pretty-elements/button {:content [:div "My hiccup"]}]
; [pretty-elements/button {:content #'my-reagent-component}]
;
;
;
; @title :cursor
;
; @code Usage
; [pretty-elements/button {:cursor (keyword or string)}]
; [pretty-elements/button {:cursor :pointer}]
; [pretty-elements/button {:cursor "pointer"}]
;
; @code Predefined values
; :default, :disabled, :grab, :grabbing, :move, :pointer, :progress
;
;
;
; @title :disabled?
;
; @code Usage
; [pretty-elements/button {:disabled? (boolean)}]
; [pretty-elements/button {:disabled? true}]
;
;
;

;
;
;
; @title :font-size
;
; @code Usage
; [pretty-elements/button {:font-size (keyword, px or string)}]
; [pretty-elements/button {:font-size :xs}]
; [pretty-elements/button {:font-size 10}]
; [pretty-elements/button {:font-size "10px"}]
; [pretty-elements/button {:font-size "5em"}]
;
; @code Predefined values
; :inherit,
; :micro, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :font-weight
;
; @code Usage
; [pretty-elements/button {:font-weight (keyword or integer)}]
; [pretty-elements/button {:font-weight :medium}]
; [pretty-elements/button {:font-weight 500}]
;
; @code Predefined values
; :inherit,
; :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black, :extra-black
;
;
;
; @title :gap
;
; @code Usage
; [pretty-elements/button {:gap (keyword, px or string)}]
; [pretty-elements/button {:gap :xs}]
; [pretty-elements/button {:gap 10}]
; [pretty-elements/button {:gap "10px"}]
; [pretty-elements/button {:gap "5%"}]
;
; @code Predefined values
; :auto,
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :helper
;
; @note
; Check out the [cljc-metamorphic-content](https://github.com/mt-app-kit/cljc-metamorphic-content) library.
;
; @code Usage
; [pretty-elements/button {:content (metamorphic-content)}]
; [pretty-elements/button {:content "My string"}]
; [pretty-elements/button {:content :my-dictionary-term}]
; [pretty-elements/button {:content 123}]
;
;
;
; @title :height
;
; @code Usage
; [pretty-elements/button {:height (keyword, px or string)}]
; [pretty-elements/button {:height :xs}]
; [pretty-elements/button {:height 10}]
; [pretty-elements/button {:height "10px"}]
; [pretty-elements/button {:height "5%"}]
;
; @code Predefined values
; :auto, :content, :parent,
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :horizontal-align
;
; @code Usage
; [pretty-elements/button {:horizontal-align (keyword)}]
; [pretty-elements/button {:horizontal-align :right}]
;
; @code Predefined values
; :center, :left, :right,
; :space-around, :space-between, :space-evenly
;
;
;

;
;
;
; @title :hover-effect
;
; @code Usage
; [pretty-elements/button {:hover-effect (keyword)}]
; [pretty-elements/button {:hover-effect :opacity}]
;
; @code Predefined values
; :none, :opacity
;
;
;
; @title :href-target
;
; @code Usage
; [pretty-elements/text {:href-target (keyword)}]
; [pretty-elements/text {:href-target :blank :href-uri "https://..."}]
;
; @code Predefined values
; :blank, :self
;
;
;
; @title :href-uri
;
; @code Usage
; [pretty-elements/button {:href-uri (string)}]
; [pretty-elements/button {:href-uri "https://..."}]
;
;
;
; @title :icon
;
; @code Usage
; [pretty-elements/button {:icon (keyword)}]
; [pretty-elements/button {:icon :any_material_icons_icon_name}]
; [pretty-elements/button {:icon :any_material_symbols_icon_name}]
;
;
;
; @title :icon-color
;
; @code Usage
; [pretty-elements/button {:icon-color (keyword or string)}]
; [pretty-elements/button {:icon-color :soft-blue}]
; [pretty-elements/button {:icon-color "#888"}]
;
; @code Predefined values
; :inherit,
; :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning,
; :black, :grey, :white, :hard-blue, :soft-blue, :hard-green, :khaki-green, :soft-green, :hard-purple, :soft-purple, :hard-red, soft-red
;
;
;
; @title :icon-family
;
; @code Usage
; [pretty-elements/button {:icon-family (keyword)}]
; [pretty-elements/button {:icon-family :material-symbols-outlined}]
;
; @code Predefined values
; :material-symbols-filled, :material-symbols-outlined
;
;
;
; @title :icon-position
;
; @code Usage
; [pretty-elements/button {:icon-position (keyword)}]
; [pretty-elements/button {:icon-position :left}]
;
; @code Predefined values
; :left, :right
;
;
;
; @title :icon-size
;
; @code Usage
; [pretty-elements/button {:icon-size (keyword, px or string)}]
; [pretty-elements/button {:icon-size :xs}]
; [pretty-elements/button {:icon-size 10}]
; [pretty-elements/button {:icon-size "10px"}]
; [pretty-elements/button {:icon-size "5em"}]
;
; @code Predefined values
; :inherit,
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :indent
;
; @code Usage
; [pretty-elements/button {:indent (map) {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)}}]
; [pretty-elements/button {:indent {:all :xs}}]
; [pretty-elements/button {:indent {:all :xs :tr :xxl}}]
; [pretty-elements/button {:indent {:all 10}}]
; [pretty-elements/button {:indent {:all "10px"}}]
; [pretty-elements/button {:indent {:all "5%"}}]
;
; @code Predefined values
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :keypress
;
; @note
; Check out the [cljs-keypress-handler](https://github.com/mt-app-kit/cljs-keypress-handler) library.
;
; @code Usage
; [pretty-elements/button {:keypress (map)}]
; [pretty-elements/button {:keypress {:key-code 13 ...} :on-click-f (fn [] ...)}]
;
;
;
; @title :line-height
;
; @code Usage
; [pretty-elements/button {:line-height (keyword, px or string)}]
; [pretty-elements/button {:line-height :xs}]
; [pretty-elements/button {:line-height 10}]
; [pretty-elements/button {:line-height "10px"}]
; [pretty-elements/button {:line-height "5em"}]
;
; @code Predefined values
; :auto, :inherit, :text-block,
; :micro, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
;
;
;
; @title :max-height
;
; @code Usage
; [pretty-elements/button {:max-height (keyword, px or string)}]
; [pretty-elements/button {:max-height :xs}]
; [pretty-elements/button {:max-height 10}]
; [pretty-elements/button {:max-height "10px"}]
; [pretty-elements/button {:max-height "5%"}]
;
; @code Predefined values
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :max-width
;
; @code Usage
; [pretty-elements/button {:max-width (keyword, px or string)}]
; [pretty-elements/button {:max-width :xs}]
; [pretty-elements/button {:max-width 10}]
; [pretty-elements/button {:max-width "10px"}]
; [pretty-elements/button {:max-width "5%"}]
;
; @code Predefined values
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :min-height
;
; @code Usage
; [pretty-elements/button {:min-height (keyword, px or string)}]
; [pretty-elements/button {:min-height :xs}]
; [pretty-elements/button {:min-height 10}]
; [pretty-elements/button {:min-height "10px"}]
; [pretty-elements/button {:min-height "5%"}]
;
; @code Predefined values
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :min-width
;
; @code Usage
; [pretty-elements/button {:min-width (keyword, px or string)}]
; [pretty-elements/button {:min-width :xs}]
; [pretty-elements/button {:min-width 10}]
; [pretty-elements/button {:min-width "10px"}]
; [pretty-elements/button {:min-width "5%"}]
;
; @code Predefined values
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :on-click
;
; @code Usage
; [pretty-elements/button {:on-click (function or Re-Frame metamorphic-event)}]
; [pretty-elements/button {:on-click (fn [] (println '...'))}]
; [pretty-elements/button {:on-click my-function}]
; [pretty-elements/button {:on-click [:my-event]}]
;
;
;
; @title :on-mouse-over
;
; @code Usage
; [pretty-elements/button {:on-mouse-over (function or Re-Frame metamorphic-event)}]
; [pretty-elements/button {:on-mouse-over (fn [] (println '...'))}]
; [pretty-elements/button {:on-mouse-over my-function}]
; [pretty-elements/button {:on-mouse-over [:my-event]}]
;
;
;
; @title :option-color-f
;
; @code Usage
; [pretty-inputs/select {:option-color-f (function)}]
; [pretty-inputs/select {:option-color-f :color :get-options-f (fn [] [{:color "red" :label "Option #1"}])}]
;
;
;
; @title :option-helper-f
;
; @code Usage
; [pretty-inputs/select {:option-helper-f (function)}]
; [pretty-inputs/select {:option-helper-f :helper :get-options-f (fn [] [{:helper "Lorem Ipsum" :label "Option #1"}])}]
;
;
;
; @title :option-label-f
;
; @code Usage
; [pretty-inputs/select {:option-label-f (function)}]
; [pretty-inputs/select {:option-label-f :label :get-options-f (fn [] [{:label "Option #1"}])}]
;
;
;
; @title :option-value-f
;
; @code Usage
; [pretty-inputs/select {:option-value-f (function)}]
; [pretty-inputs/select {:option-value-f :value :get-options-f (fn [] [{:label "Option #1" :value "#1"}])}]
;
;
;
; @title :orientation
;
; @code Usage
; [pretty-elements/menu-bar {:orientation (keyword)}]
; [pretty-elements/menu-bar {:orientation :horizontal}]
;
; @code Predefined values
; :horizontal, :vertical
;
;
;
; @title :outdent
;
; @code Usage
; [pretty-elements/button {:outdent (map) {:all, :bottom, :left, :right, :top, :horizontal, :vertical (keyword, px or string)}}]
; [pretty-elements/button {:outdent {:all :xs}}]
; [pretty-elements/button {:outdent {:all :xs :tr :xxl}}]
; [pretty-elements/button {:outdent {:all 10}}]
; [pretty-elements/button {:outdent {:all "10px"}}]
; [pretty-elements/button {:outdent {:all "5%"}}]
;
; @code Predefined values
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
;
;
;
; @title :overflow
;
; @code Usage
; [pretty-elements/row {:overflow (keyword)}]
; [pretty-elements/row {:overflow :scroll}]
;
; @code Predefined values
; :inherit, (is the :overflow really inheritable?)
; :hidden, :scroll, :visible, :wrap
;
;
;
; @title :placeholder
;
; @note
; Check out the [cljc-metamorphic-content](https://github.com/mt-app-kit/cljc-metamorphic-content) library.
;
; Replaces the ':content' or ':label' values if not provided.
;
; @code Usage
; [pretty-elements/button {:placeholder (metamorphic-content)}]
; [pretty-elements/button {:placeholder "My string"            :content nil}]
; [pretty-elements/button {:placeholder :my-dictionary-term    :content nil}]
; [pretty-elements/button {:placeholder 123                    :content nil}]
; [pretty-elements/button {:placeholder [:div "My hiccup"]     :content nil}]
; [pretty-elements/button {:placeholder #'my-reagent-component :content nil}]
;
;
;
; @title :preset
;
; @code Usage
; (pretty-presets/reg-preset! :my-preset   {:fill-color :soft-blue :width :xs})
; (pretty-presets/reg-preset! :my-preset-f #(merge % {:fill-color :soft-blue :width :xs}))
; [pretty-elements/button {:preset (keyword)}]
; [pretty-elements/button {:preset :my-preset}]
; [pretty-elements/button {:preset :my-preset-f}]
;
;
;
; @title :progress
;
; @code Usage
; [pretty-elements/button {:progress (percent)}]
; [pretty-elements/button {:progress 42}]
;
;
;
; @title :progress-color
;
; @code Usage
; [pretty-elements/button {:progress-color (keyword or string)}]
; [pretty-elements/button {:progress-color :soft-blue}]
; [pretty-elements/button {:progress-color "#888"}]
;
; @code Predefined values
; :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning,
; :black, :grey, :white, :hard-blue, :soft-blue, :hard-green, :khaki-green, :soft-green, :hard-purple, :soft-purple, :hard-red, soft-red
;
;
;
; @title :progress-direction
;
; @code Usage
; [pretty-elements/button {:progress-direction (keyword)}]
; [pretty-elements/button {:progress-direction :ltr}]
;
; @code Predefined values
; :ltr, :rtl,
; :btt, :ttb
;
;
;
; @title :progress-duration
;
; @code Usage
; [pretty-elements/button {:progress-duration (ms)}]
; [pretty-elements/button {:progress-duration 250}]
;
;
;
; @title :reveal-effect
;
; @code Usage
; [pretty-elements/button {:reveal-effect (keyword)}]
; [pretty-elements/button {:reveal-effect :opacity}]
;
; @code Predefined values
; :none, :delayed, :opacity
;
;
;
; @title :text-align
;
; @code Usage
; [pretty-elements/text {:text-align (keyword)}]
; [pretty-elements/text {:text-align :left}]
;
; @code Predefined values
; :center, :left, :right
;
;
;
; @title :text-color
;
; @code Usage
; [pretty-elements/button {:text-color (keyword or string)}]
; [pretty-elements/button {:text-color :soft-blue}]
; [pretty-elements/button {:text-color "#888"}]
;
; @code Predefined values
; :inherit,
; :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning,
; :black, :grey, :white, :hard-blue, :soft-blue, :hard-green, :khaki-green, :soft-green, :hard-purple, :soft-purple, :hard-red, soft-red
;
;
;
; @title :text-overflow
;
; @code Usage
; [pretty-elements/button {:text-overflow (keyword)}]
; [pretty-elements/button {:text-overflow :ellipsis}]
;
; @code Predefined values
; :inherit, (is the :text-overflow really inheritable?)
; :ellipsis, :hidden, :visible, :wrap
;
;
;
; @title :text-selectable?
;
; @code Usage
; [pretty-elements/text {:text-selectable? (boolean)}]
; [pretty-elements/text {:text-selectable? false}]
;
;
;
; @title :text-transform
;
; @code Usage
; [pretty-elements/button {:text-transform (keyword)}]
; [pretty-elements/button {:text-transform :lowercase}]
;
; @code Predefined values
; :capitalize, :lowercase, :uppercase
;
;
;
; @title :timeout
;
; @code Usage
; [pretty-elements/button {:timeout (ms)}]
; [pretty-elements/button {:on-click (fn [] (println '...')) :timeout 5000}]
;
;
;
; @title :vertical-align
;
; @code Usage
; [pretty-elements/text {:vertical-align (keyword)}]
; [pretty-elements/text {:vertical-align :bottom}]
;
; @code Predefined values
; :bottom, :center, :top,
; :space-around, :space-between, :space-evenly
;
;
;
; @title :width
;
; @code Usage
; [pretty-elements/button {:width (keyword, px or string)}]
; [pretty-elements/button {:width :xs}]
; [pretty-elements/button {:width 10}]
; [pretty-elements/button {:width "10px"}]
; [pretty-elements/button {:width "5%"}]
;
; @code Predefined values
; :auto, :content, :parent,
; :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
