

; WARNING! NOT TESTED! DO NOT USE!



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.26
; Description:
; Version: v0.2.6
; Compatibility: x4.4.3



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.image-picker
    (:require [mid-fruits.candy          :refer [param]]
              [x.app-components.api      :as components]
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

(defn- get-view-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ;
  ; @return (map)
  [db [_ picker-id]]
  (merge (r engine/get-element-view-props db picker-id)))

(a/reg-sub ::get-view-props get-view-props)



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- image-picker
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) picker-id
  ; @param (map) view-props
  ;
  ; @return (hiccup)
  [picker-id view-props]
  [:div.x-image-picker])

(defn view
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
   [view (a/id) picker-props])

  ([picker-id picker-props]
   (let [];picker-props (a/prot picker-props picker-props-prototype)
        [engine/stated-element picker-id
                               {:component     #'image-picker
                                :element-props picker-props
                                :subscriber    [::get-view-props picker-id]}])))
