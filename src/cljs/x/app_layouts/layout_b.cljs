
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.16
; Description:
; Version: v0.2.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.layout-b
    (:require [mid-fruits.candy     :refer [param return]]
              [mid-fruits.keyword   :as keyword]
              [x.app-components.api :as components]
              [x.app-core.api       :as a]
              [x.app-elements.api   :as elements]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) layout-props
  ;
  ; @return (map)
  ;  {:min-width (keyword)}
  [layout-props]
  (merge {}
         (param layout-props)))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {}
  ;
  ; @return (component)
  [layout-id {:keys []}])

(defn- layout-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {}
  ;
  ; @return (component)
  [_ {:keys []}])

(defn- layout-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ;  {}
  ;
  ; @return (component)
  [layout-id layout-props])

(defn layout
  ; @param (keyword)(opt) layout-id
  ; @param (map) layout-props
  ;  {}
  ;
  ; @usage
  ;  [layouts/layout-b {...}]
  ;
  ; @usage
  ;  [layouts/layout-b :my-layout {...}]
  ;
  ; @return (component)
  ([layout-props]
   (let [layout-id (a/id)]
        [layout layout-id layout-props]))

  ([layout-id layout-props]
   (let [layout-props (a/prot layout-props layout-props-prototype)]
        [layout-b layout-id layout-props])))
