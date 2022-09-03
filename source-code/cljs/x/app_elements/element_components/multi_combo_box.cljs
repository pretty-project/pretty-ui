
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.multi-combo-box
    (:require [mid-fruits.candy                             :refer [param return]]
              [mid-fruits.keyword                           :as keyword]
              [mid-fruits.vector                            :as vector]
              [x.app-components.api                         :as components]
              [x.app-core.api                               :as a :refer [r]]
              [x.app-elements.element-components.chip-group :rename {element chip-group}]
              [x.app-elements.combo-box.views  :as combo-box.views]
              [x.app-elements.engine.api                    :as engine]
              [x.app-elements.input.helpers                    :as input.helpers]))



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
          :no-chips-label (param no-options-selected-label)}))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {:get-label-f (function)
  ;   :get-value-f (function)
  ;   :no-options-selected-label (metamorphic-content)
  ;   :options-path (vector)
  ;   :value-path (vector)}
  [group-id group-props]
  (merge {:get-label-f               return
          :get-value-f               return
          :no-options-selected-label :no-options-selected}
          ; ezek nem is kellekene:
          ;:options-path              (input.helpers/default-options-path group-id)}
          ;:value-path                (input.helpers/default-value-path   group-id)}
          ;:layout :row}
         (param group-props)))

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {:on-focus (metamorphic-event)
  ;   :group-id (keyword)
  ;   :select-option-event (event-vector)}
  [group-id group-props]
  (let [field-id (group-id->field-id group-id)]
       (merge (select-keys group-props INHERITED-FIELD-PROPS)
              {:group-id group-id
               :on-blur             [:elements/remove-multi-combo-box-controllers! field-id]
               :on-focus            [:elements/reg-multi-combo-box-controllers!    field-id]
               :select-option-event [:elements/stack-to-group-value! group-id]})))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-multi-combo-box-chip-group-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @param (map)
  [db [_ group-id]]
  (merge (r engine/get-element-props     db group-id)
         (r engine/get-input-group-props db group-id)))

(a/reg-sub :elements/get-multi-combo-box-chip-group-props get-multi-combo-box-chip-group-props)

(defn- get-multi-combo-box-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ;
  ; @param (map)
  [db [_ group-id]]
  (r engine/get-element-props db group-id))

(a/reg-sub :elements/get-multi-combo-box-props get-multi-combo-box-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- multi-combo-box-chip-group-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  (let [chip-group-props (group-props->chip-group-props group-id group-props)]
       [:div.x-multi-combo-box--chip-group [chip-group chip-group-props]]))

(defn- multi-combo-box-chip-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [components/subscriber group-id
                         {:base-props group-props
                          :render-f   multi-combo-box-chip-group-structure
                          :subscriber [:elements/get-multi-combo-box-chip-group-props group-id]}])

(defn- multi-combo-box-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  (let [field-id    (group-id->field-id    group-id)
        field-props (field-props-prototype group-id group-props)]
       [:div.x-multi-combo-box--field [combo-box.views/element field-id field-props]]))

(defn- multi-combo-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  [group-id group-props]
  [:div.x-multi-combo-box (engine/element-attributes  group-id group-props)
                          [multi-combo-box-chip-group group-id group-props]
                          [multi-combo-box-field      group-id group-props]
                          [engine/element-helper      group-id group-props]])

(defn element
  ; XXX#0711
  ; A multi-combo-box elem alapkomponense a combo-box elem.
  ; A multi-combo-box elem további paraméterezését a combo-box elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:get-label-f (function)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :initial-options (vector)(opt)
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: :no-options
  ;   :no-options-selected-label (metamorphic-content)(opt)
  ;    Default: :no-options-selected
  ;   :on-select (metamorphic-event)(opt)
  ;   :option-component (component)(opt)
  ;    Default: x.app-elements.combo-box/default-option-component
  ;   :options-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/multi-combo-box {...}]
  ;
  ; @usage
  ;  [elements/multi-combo-box :my-multi-combo-box {...}]
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [group-props (group-props-prototype group-id group-props)]
        [engine/stated-element group-id
                               {:element-props group-props
                                :render-f      #'multi-combo-box
                                :initializer   [:elements/init-selectable!          group-id]
                                :subscriber    [:elements/get-multi-combo-box-props group-id]}])))
