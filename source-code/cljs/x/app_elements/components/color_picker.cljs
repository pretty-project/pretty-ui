
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.components.color-picker
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn picker-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {:get-value-f (function)
  ;   :layout (keyword)
  ;   :options-path (item-path vector)
  ;   :value-path (item-path vector)}
  [picker-id {:keys [] :as picker-props}]
  (merge {:layout       :fit
          :options-path (engine/default-options-path picker-id)
          :value-path   (engine/default-value-path   picker-id)}
         (param picker-props)
         ; A color-picker elem nem használja de a collectable engine működéséhez szükséges
         ; a {:get-value-f ...} tulajdonságot beállítani.
         {:get-value-f return}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-color-picker-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (map)
  [db [_ picker-id]]
  (merge (r engine/get-element-props     db picker-id)
         (r engine/get-collectable-props db picker-id)))

(a/reg-sub :elements/get-color-picker-props get-color-picker-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-picker-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ; @param (string) option
  ;
  ; @return (hiccup)
  [picker-id picker-props option]
  [:button.x-color-picker--option (engine/collectable-option-attributes picker-id picker-props option)
    [:div.x-color-picker--option--color {:style {:background-color option}}]])

(defn- color-picker-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:options (strings in vector)}
  ;
  ; @return (hiccup)
  [picker-id {:keys [options] :as picker-props}]
  (reduce (fn [color-list option]
              (conj color-list [color-picker-option picker-id picker-props option]))
          [:div.x-color-picker--options]
          (param options)))

(defn- color-picker-reset-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (hiccup)
  [picker-id picker-props]
  [:div.x-color-picker--reset-button])

(defn- color-picker-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;  {:label (metamorphic-content)(opt)
  ;   :required? (boolean)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label required?]}]
  (if label [:div.x-color-picker--label [components/content label]
                                        (if required? [:span.x-input--label-asterisk "*"])]))

(defn- color-picker-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (hiccup)
  [picker-id picker-props]
  [:div.x-color-picker--header
    [color-picker-reset-button picker-id picker-props]
    [color-picker-label        picker-id picker-props]])

(defn- color-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (hiccup)
  [picker-id picker-props]
  [:div.x-color-picker (engine/element-attributes picker-id picker-props)
                       [color-picker-header       picker-id picker-props]
                       [color-picker-options      picker-id picker-props]])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :form-id (keyword)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :initial-options (strings in vector)(constant)(opt)
  ;   :initial-value (strings in vector)(constant)(opt)
  ;   :label (metamorphic-content)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :fit
  ;    TODO ...
  ;   :on-select (metamorphic-event)(opt)
  ;   :options-path (item-path vector)(constant)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :resetable? (boolean)(opt)
  ;    Default: false
  ;    TODO ...
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [elements/color-picker {...}]
  ;
  ; @usage
  ;  [elements/color-picker :my-color-picker {...}]
  ;
  ; @usage
  ;  [elements/color-picker {:initial-options ["red" "green" "blue"]}]
  ;
  ; @return (component)
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   (let [picker-props (picker-props-prototype picker-id picker-props)]
        [engine/stated-element picker-id
                               {:render-f      #'color-picker
                                :element-props picker-props
                                :initializer   [:elements/init-collectable!      picker-id]
                                :subscriber    [:elements/get-color-picker-props picker-id]}])))
