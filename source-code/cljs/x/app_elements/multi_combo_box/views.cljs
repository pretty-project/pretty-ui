
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box.views
    (:require [mid-fruits.keyword                           :as keyword]
              [mid-fruits.vector                            :as vector]
              [x.app-components.api                         :as components]
              [x.app-core.api                               :as a :refer [r]]
              [x.app-elements.chip-group.views :as chip-group.views]
              [x.app-elements.combo-box.views  :as combo-box.views]
              [x.app-elements.engine.api                    :as engine]
              [x.app-elements.input.helpers                    :as input.helpers]
              [x.app-elements.multi-combo-box.helpers :as multi-combo-box.helpers]
              [x.app-elements.multi-combo-box.prototypes :as multi-combo-box.prototypes]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
;  A multi-combo-box elem mely paramétereit örökölje a combo-box elem
(def INHERITED-FIELD-PROPS
     [:autofocus? :border-color :min-width :get-label-f
      :get-value-f :group-value :max-length :no-options-label :on-blur
      :on-empty :on-focus :on-reset :on-select
      :option-component :options-path :placeholder
      :debug])




;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- group-id->field-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @example
  ;  (group-id->field-id :my-multi-combo-box)
  ;  =>
  ;  :my-multi-combo-box--field
  ;
  ; @return (keyword)
  [group-id]
  (keyword/append group-id :field "--"))

(defn- group-props->chips
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) group-props
  ;  {:get-label-f (function)
  ;   :group-value (vector)}
  ;
  ; @return (maps in vector)
  ;  [{:label (metamorphic-context)
  ;    :variant (keyword)}]
  [{:keys [get-label-f group-value]}]
  (letfn [(f [result value] (conj result {:label (get-label-f value)}))]
         (reduce f [] group-value)))

(defn- group-props->chip-group-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) group-id
  ; @param (map) group-props
  ;  {:group-value (vector)
  ;   :no-options-selected-label (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ;  {:chips (vector)
  ;   :delete-chip-event (metamorphic-event)
  ;   :no-chips-label (metamorphic-content)}
  [group-id {:keys [no-options-selected-label] :as group-props}]
  (merge (select-keys group-props [:label])
         {:chips     (group-props->chips group-props)
          :on-delete [:elements/unstack-from-group-value! group-id]
          ; Mivel a multi-combo-box elem a chip-group elem feliratát használja, ezért
          ; ha nincs kiválasztva opció, akkor a chip-group elem felirata és a text-field
          ; közötti távolság nagyobb, mint az alap text-field elem és annak a felirata
          ; közötti távolság.
          ; Ezért szükséges a chip-group elem {:no-chips-label ...} tulajdonságának
          ; használatával megjelenített szöveggel megtörni ezt a távolságot.
          :no-chips-label no-options-selected-label}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-chip-group-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props])
  ;(let [chip-group-props (group-props->chip-group-props box-id box-props)]))
       ;[:div.x-multi-combo-box--chip-group [chip-group chip-group-props]]))

(defn- multi-combo-box-chip-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [group-id    (multi-combo-box.helpers/box-id->group-id         box-id)
        group-props (multi-combo-box.prototypes/group-props-prototype box-id box-props)]
       [chip-group.views/element group-id group-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  (let [field-id    (multi-combo-box.helpers/box-id->field-id          box-id)
        field-props (multi-combo-box.prototypes/field-props-prototype  box-id box-props)
        field-props (multi-combo-box.prototypes/field-events-prototype box-id box-props field-id field-props)]

       ; He?
       ;[:div.x-multi-combo-box--field [combo-box.views/element field-id field-props]]

       [combo-box.views/element field-id field-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  [box-id box-props]
  [:div.x-multi-combo-box (multi-combo-box.helpers/box-attributes box-id box-props)
                          [engine/element-header                  box-id box-props]
                          [multi-combo-box-chip-group             box-id box-props]
                          [multi-combo-box-field                  box-id box-props]])

(defn element
  ; XXX#0711
  ; A multi-combo-box elem alapkomponense a combo-box elem.
  ; A multi-combo-box elem további paraméterezését a combo-box elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) box-id
  ; @param (map) box-props
  ;  {:field-content-f (function)(opt)
  ;    Default: return
  ;   :field-value-f (function)(opt)
  ;    Default: return
  ;   :initial-options (vector)(opt)
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: :no-options
  ;   :no-options-selected-label (metamorphic-content)(opt)
  ;    Default: :no-options-selected
  ;   :on-select (metamorphic-event)(opt)
  ;   :option-label-f (function)(opt)
  ;    Default: return
  ;   :option-value-f (function)(opt)
  ;    Default: return
  ;   :option-component (component)(opt)
  ;    Default: x.app-elements.combo-box/default-option-component
  ;   :options (vector)(opt)
  ;   :options-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/multi-combo-box {...}]
  ;
  ; @usage
  ;  [elements/multi-combo-box :my-multi-combo-box {...}]
  ([box-props]
   [element (a/id) box-props])

  ([box-id box-props]
   (let [box-props (multi-combo-box.prototypes/box-props-prototype box-id box-props)]
        [multi-combo-box box-id box-props])))
