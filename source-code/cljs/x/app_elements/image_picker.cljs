

; WARNING! NOT TESTED! DO NOT USE!

;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.26
; Description:
; Version: v0.2.6
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.image-picker
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-core.api            :as a :refer [r]]
              [x.app-elements.engine.api :as engine]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- picker-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) picker-props
  ;
  ; @return (map)
  ;  {}
  [picker-props]
  (merge {}
         (param picker-props)))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-image-picker-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (map)
  [db [_ picker-id]]
  (r engine/get-element-props db picker-id))

(a/reg-sub :elements/get-image-picker-props get-image-picker-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) picker-props
  ;
  ; @return (hiccup)
  [picker-id picker-props]
  [:div.x-image-picker])

(defn element
  ; @param (keyword)(opt) picker-id
  ; @param (map) picker-props
  ;  {}
  ;
  ; @usage
  ;  [elements/image-picker {...}]
  ;
  ; @usage
  ;  [elements/image-picker :my-image-picker {...}]
  ;
  ; @return (component)
  ([picker-props]
   [element (a/id) picker-props])

  ([picker-id picker-props]
   (let [];picker-props (picker-props-prototype picker-props)
        [engine/stated-element picker-id
                               {:component     #'image-picker
                                :element-props picker-props
                                :subscriber    [:elements/get-image-picker-props picker-id]}])))
