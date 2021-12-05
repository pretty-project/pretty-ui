
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.16
; Description:
; Version: v0.5.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.password-field
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.form           :as form]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.text-field :as text-field :refer [text-field]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- password-field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:validate? (boolean)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :type (keyword)(opt)
  ;   :validator (map)
  ;    {:f (function)
  ;     :invalid-message (keyword)}}
  [field-id {:keys [validate?] :as field-props}]
  (merge {:color :default
          :label :password
          :type  :password
          :value-path (engine/default-value-path field-id)}
         (param field-props)
         (if validate? {:tooltip   :valid-password-rules
                        :validator {:f form/password-valid?
                                    :invalid-message :password-is-too-weak}})))

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  (let [password-field-props (password-field-props-prototype field-id field-props)]
       (text-field/field-props-prototype field-id password-field-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ;
  ; @param (map)
  [db [_ field-id]]
  (merge (r text-field/get-view-props       db field-id)
         (r engine/get-passfield-view-props db field-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :default
  ;    Default: :default
  ;   :class (string or vector)(opt)
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :left
  ;   :initial-value (string)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :max-length (integer)(opt)
  ;   :on-blur (metamorphic-event)(constant)(opt)
  ;   :on-change (metamorphic-event)(constant)(opt)
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :required? (boolean)(constant)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :validate? (boolean)(opt)
  ;    Default: false
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/password-field {...}]
  ;
  ; @usage
  ;  [elements/password-field :my-password-field {...}]
  ;
  ; @return (component)
  ([field-props]
   [view (a/id) field-props])

  ([field-id field-props]
   (let [field-props (a/prot field-id field-props field-props-prototype)]
        [engine/stated-element field-id
                               {:component     #'text-field
                                :element-props field-props
                                :initializer   [:elements/init-field! field-id]
                                :subscriber    [::get-view-props      field-id]}])))
