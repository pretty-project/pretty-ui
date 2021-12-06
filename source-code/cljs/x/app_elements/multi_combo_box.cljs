
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.07
; Description:
; Version: v0.9.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-combo-box
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.keyword        :as keyword]
              [mid-fruits.map            :as map]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.chip-group :rename {element chip-group}]
              [x.app-elements.combo-box  :as combo-box :rename {element combo-box}]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
;  A multi-combo-box elem mely paramétereit örökölje a combo-box elem
(def INHERITED-FIELD-PROPS
     [:auto-focus? :color :extendable? :min-width :get-label-f :get-value-f
      :group-value :no-options-label :max-length :on-blur :on-empty
      :on-focus :on-reset :on-select :on-type-ended :option-component
      :options-path :placeholder
      :debug])

; @constant (metamorphic-content)
(def DEFAULT-NO-OPTIONS-SELECTED-LABEL :no-options-selected)



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
  (reduce (fn [result value]
              (vector/conj-item result {:label (get-label-f value)}))
          (param [])
          (param group-value)))

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
  (merge group-props {:chips     (group-props->chips group-props)
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
  ;   :indent (keyword)
  ;   :no-options-selected-label (metamorphic-content)
  ;   :options-path (item-path vector)
  ;   :value-path (item-path vector)}
  [group-id group-props]
  (merge {:get-label-f               return
          :get-value-f               return
          :indent                    :left
          :no-options-selected-label DEFAULT-NO-OPTIONS-SELECTED-LABEL
          :options-path              (engine/default-options-path group-id)
          :value-path                (engine/default-value-path   group-id)}
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
       (merge (map/inherit group-props INHERITED-FIELD-PROPS)
              {:on-focus            [:elements/reg-multi-combo-box-controllers! field-id]
               :group-id            group-id
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

(defn- xi8071
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (hiccup)
  [group-id group-props]
  (let [chip-group-props (group-props->chip-group-props group-id group-props)]
       [:div.x-multi-combo-box--chip-group [chip-group chip-group-props]]))

(defn- multi-combo-box-chip-group
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (component)
  [group-id group-props]
  [components/subscriber group-id
                         {:base-props group-props
                          :component  #'xi8071
                          :subscriber [:elements/get-multi-combo-box-chip-group-props group-id]}])

(defn- multi-combo-box-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (hiccup)
  [group-id group-props]
  (let [field-id    (group-id->field-id group-id)
        field-props (a/prot             group-id group-props field-props-prototype)]
       [:div.x-multi-combo-box--field [combo-box field-id field-props]]))

(defn- multi-combo-box
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (component)
  [group-id group-props]
  [:div.x-multi-combo-box [multi-combo-box-chip-group  group-id group-props]
                          [multi-combo-box-field       group-id group-props]
                          [engine/element-helper       group-id group-props]
                          [engine/element-info-tooltip group-id group-props]])

(defn element
  ; @param (keyword)(opt) group-id
  ; @param (map) group-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;    Default: false
  ;   :class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :default
  ;    Default: :default
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :emptiable? (boolean)(opt)
  ;    Default: true
  ;   :extendable? (boolean)(opt)
  ;    Default: false
  ;   :form-id (keyword)(opt)
  ;   :get-label-f (function)(constant)(opt)
  ;    Default: return
  ;   :get-value-f (function)(opt)
  ;    Default: return
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :left
  ;   :initial-options (vector)(constant)(opt)
  ;   :initial-value (*)(constant)(opt)
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :s
  ;   :no-options-label (metamorphic-content)(opt)
  ;    Default: x.app-elements.combo-box/DEFAULT-NO-OPTIONS-LABEL
  ;   :no-options-selected-label (metamorphic-content)(opt)
  ;    Default: DEFAULT-NO-OPTIONS-SELECTED-LABEL
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :max-length (integer)(opt)
  ;   :on-empty (metamorphic-event)(constant)(opt)
  ;    Only w/ {:emptiable? true}
  ;   :on-extend (metamorphic-event)(constant)(opt)
  ;    Default: [:elements/add-option! field-id value]
  ;    Only w/ {:extendable? true}
  ;   :on-reset (metamorphic-event)(constant)(opt)
  ;    Only w/ {:resetable? true}
  ;   :on-select (metamorphic-event)(constant)(opt)
  ;   :on-type-ended (event-vector)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :option-component (component)(opt)
  ;    Default: x.app-elements.combo-box/default-option-component
  ;   :options-path (item-path vector)(constant)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/multi-combo-box {...}]
  ;
  ; @usage
  ;  [elements/multi-combo-box :my-multi-combo-box {...}]
  ;
  ; @return (component)
  ([group-props]
   [element (a/id) group-props])

  ([group-id group-props]
   (let [group-props (a/prot group-id group-props group-props-prototype)]
        [engine/stated-element group-id
                               {:component     #'multi-combo-box
                                :element-props group-props
                                :initializer   [:elements/init-selectable!          group-id]
                                :subscriber    [:elements/get-multi-combo-box-props group-id]}])))
