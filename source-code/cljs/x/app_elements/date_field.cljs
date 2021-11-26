
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.07.24
; Description:
; Version: v0.1.6
; Compatibility: x4.3.4



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.date-field
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  ;  {:min-width (keyword)
  ;   :type (keyword)
  ;   :value-path (item-path vector)}
  [field-id field-props]
  (merge {:min-width  :s
          :type       :date
          :value-path (engine/default-value-path field-id)}
         (param field-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @return (map)
  [db [_ field-id]]
  (merge (r engine/get-element-view-props db field-id)
         (r engine/get-input-view-props   db field-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- date-field-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;  {:label (metamorphic-content)(opt)}
  ;
  ; @return (hiccup)
  [_ {:keys [label]}]
  (if (some? label)
      [:div.x-date-field--label (components/content {:content label})]))

(defn- date-field-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:input.x-date-field--input
    (engine/field-body-attributes field-id view-props)])

(defn- date-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [field-id view-props]
  [:label.x-date-field
    (engine/element-attributes field-id view-props)
    [date-field-label field-id view-props]
    [date-field-body  field-id view-props]])

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:class (string or vector)(opt)
  ;   :date-from (string)(opt)
  ;    TODO ...
  ;   :date-to (string)(opt)
  ;    TODO ...
  ;   :disabled? (boolean)(opt)
  ;    TODO ...
  ;   :disabler (subscription vector)(opt)
  ;    TODO ...
  ;   :form-id (keyword)(opt)
  ;   :highlighted? (boolean)(opt)
  ;    Default: false
  ;   :label (metamorphic-content)(opt)
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :min-width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :none
  ;    Default: :s
  ;   :style (map)(opt)
  ;   :value-path (item-path vector)(opt)}
  ;
  ; @usage
  ;  [elements/date-field {...}]
  ;
  ; @usage
  ;  [elements/date-field :my-date-field {...}]
  ;
  ; @return (component)
  ([field-props]
   [view nil field-props])

  ([field-id field-props]
   (let [field-id    (a/id   field-id)
         field-props (a/prot field-id field-props field-props-prototype)]
        [engine/stated-element field-id
          {:component     #'date-field
           :element-props field-props
           :initializer   [:elements/init-field! field-id]
           :subscriber    [::get-view-props      field-id]}])))
