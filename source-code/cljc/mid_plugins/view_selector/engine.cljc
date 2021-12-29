
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-plugins.view-selector.engine
    (:require [mid-fruits.keyword :as keyword]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keyword)
(def DEFAULT-VIEW-ID :main)



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/route-id :my-extension)
  ;  =>
  ;  :my-extension/route
  ;
  ; @return (keyword)
  [extension-id]
  (keyword (name extension-id) "route"))

(defn extended-route-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/extended-route-id :my-extension)
  ;  =>
  ;  :my-extension/extended-route
  ;
  ; @return (keyword)
  [extension-id]
  (keyword (name extension-id) "extended-route"))

(defn route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/route-template :my-extension)
  ;  =>
  ;  "/my-extension"
  ;
  ; @return (string)
  [extension-id]
  (str "/:app-home/" (name extension-id)))

(defn extended-route-template
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/extended-route-template :my-extension)
  ;  =>
  ;  "/my-extension/:view-id"
  ;
  ; @return (string)
  [extension-id]
  (str "/:app-home/" (name extension-id)
       "/:view-id"))

(defn route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/route-string :my-extension)
  ;  =>
  ;  "/:app-home/my-extension"
  ;
  ; @return (string)
  [extension-id]
  (str "/:app-home/" (name extension-id)))

(defn extended-route-string
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) view-id
  ;
  ; @example
  ;  (engine/extended-route-string :my-extension :my-view)
  ;  =>
  ;  "/:app-home/my-extension/my-view"
  ;
  ; @return (string)
  [extension-id view-id]
  (str "/:app-home/" (name extension-id)
       "/"           (name view-id)))

(defn load-event
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @example
  ;  (engine/load-event :my-extension)
  ;  =>
  ;  [:my-extension/load!]
  ;
  ; @return (event-vector)
  [extension-id]
  (let [event-id (keyword/add-namespace extension-id :load!)]
       [event-id]))