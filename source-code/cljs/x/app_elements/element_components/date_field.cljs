
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.date-field
    (:require [mid-fruits.candy                             :refer [param]]
              [x.app-components.api                         :as components]
              [x.app-core.api                               :as a :refer [r]]
              [x.app-elements.element-components.text-field :as element-components.text-field]
              [x.app-elements.engine.api                    :as engine]
              [x.app-elements.adornment-handler.views       :as adornment-handler.views]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-elements.element-components.text-field
(def text-field-label element-components.text-field/text-field-label)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:name (keyword)
  ;   :type (keyword)
  ;   :value-path (vector)}
  [field-id field-props]
  (merge {:type       :date
          :value-path (engine/default-value-path field-id)}
         (param field-props)
          ; XXX#6782
         {:name (a/id)}))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-date-field-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-props db field-id)
         (r engine/get-input-props   db field-id)))

(a/reg-sub :elements/get-date-field-props get-date-field-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- date-field-input
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:input.x-date-field--input (engine/field-body-attributes field-id field-props)])

(defn- date-field-input-container
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field--input-container [adornment-handler.views/field-start-adornments field-id field-props]
                                      [date-field-input                               field-id field-props]
                                      [adornment-handler.views/field-end-adornments   field-id field-props]])

(defn- date-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  [field-id field-props]
  [:div.x-text-field (engine/element-attributes  field-id field-props)
                     [text-field-label           field-id field-props]
                     [date-field-input-container field-id field-props]])

(defn element
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :date-from (string)(opt)
  ;    TODO ...
  ;   :date-to (string)(opt)
  ;    TODO ...
  ;   :disabled? (boolean)(opt)
  ;    TODO ...
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :none
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [elements/date-field {...}]
  ;
  ; @usage
  ;  [elements/date-field :my-date-field {...}]
  ([field-props]
   [element (a/id) field-props])

  ([field-id field-props]
   (let [field-props (field-props-prototype field-id field-props)]
        [engine/stated-element field-id
                               {:element-props field-props
                                :render-f      #'date-field
                                :initializer   [:elements/init-field!          field-id]
                                :subscriber    [:elements/get-date-field-props field-id]}])))
