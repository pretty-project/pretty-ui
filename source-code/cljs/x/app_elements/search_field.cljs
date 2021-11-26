
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.25
; Description:
; Version: v0.8.8
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.search-field
    (:require [mid-fruits.candy          :as candy :refer [param]]
              [mid-fruits.io             :as io]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]
              [x.app-elements.text-field :as text-field :refer [text-field]]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- search-field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;  {:on-enter (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :emptiable? (boolean)
  ;   :start-adornments (maps in vector)}
  [_ {:keys [on-enter] :as field-props}]
  (merge {:color      :default
          :emptiable? true}
         (if (some? on-enter)   ; XXX#6054
             {:start-adornments [{:icon :search :on-click on-enter :tab-indexed? false}]}
             {:start-adornments [{:icon :search}]})
         (param field-props)))

(defn- field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) field-id
  ; @param (map) field-props
  ;
  ; @return (map)
  [field-id field-props]
  (let [search-field-props (search-field-props-prototype field-id field-props)]
       (text-field/field-props-prototype field-id search-field-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-sub ::get-view-props text-field/get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (keyword)(opt) field-id
  ; @param (map) field-props
  ;  {:auto-focus? (boolean)(constant)(opt)
  ;    Default: false
  ;   :class (string or vector)(opt)
  ;   :color (keyword)(opt)
  ;    :primary, :secondary, :default
  ;    Default: :default
  ;   :default-value (string)(constant)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :disabler (subscription vector)(opt)
  ;   :form-id (keyword)(opt)
  ;   :emptiable? (boolean)(constant)(opt)
  ;    Default: true
  ;   :helper (metamorphic-content)(opt)
  ;   :highlighted? (boolean)(opt)
  ;    Default: false
  ;   :info-tooltip (metamorphic-content)(opt)
  ;   :initial-value (string)(constant)(opt)
  ;   :label (metamorphic-content)(opt)
  ;    Only w/o {:placeholder ...}
  ;   :max-length (integer)(opt)
  ;   :modifier (function)(opt)
  ;   :on-blur (metamorphic-event)(constant)(opt)
  ;   :on-change (metamorphic-event)(constant)(opt)
  ;   :on-empty (metamorphic-event)(constant)(opt)
  ;    Only w/ {:emptiable? true}
  ;   :on-enter (metamorphic-event)(constant)(opt)
  ;   :on-focus (metamorphic-event)(constant)(opt)
  ;   :on-reset (metamorphic-event)(constant)(opt)
  ;    Only w/ {:resetable? true}
  ;   :on-type-ended (event-vector)(opt)
  ;    Az esemény-vektor utolsó paraméterként megkapja a mező aktuális értékét.
  ;   :placeholder (metamorphic-content)(opt)
  ;    Only w/o {:label ...}
  ;   :resetable? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :surface (map)(opt)
  ;    {:content (metamorphic-content)
  ;     :content-props (map)(opt)
  ;     :subscriber (subscription vector)(opt)}
  ;   :value-path (item-path vector)(constant)(opt)}
  ;
  ; @usage
  ;  [elements/search-field {...}]
  ;
  ; @usage
  ;  [elements/search-field :my-search-field {...}]
  ;
  ; @return (component)
  ([field-props]
   [view nil field-props])

  ([field-id field-props]
   (let [field-id    (a/id   field-id)
         field-props (a/prot field-id field-props field-props-prototype)]
        [engine/stated-element field-id
          {:component     #'text-field
           :element-props field-props
           :modifier      text-field/view-props-modifier
           :initializer   [:elements/init-field! field-id]
           :subscriber    [::get-view-props      field-id]}])))
