
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.5.6
; Compatibility: x4.4.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.subs
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]
              [app-plugins.view-selector.engine :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-derived-view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (map) selector-props
  ;  {:allowed-view-ids (keywords in vector)(opt)
  ;   :default-view-id (keyword)(opt)}
  ;
  ; @return (keyword)
  ;  A view-id forrásából (route-path param) származó adat.
  ;  A forrás hiánya esetén a default-view-id paraméter.
  ;  A default-view-id paraméter hiánya esetén a DEFAULT-VIEW-ID konstans.
  [db [_ extension-id {:keys [allowed-view-ids default-view-id]}]]
  (if-let [derived-view-id (r router/get-current-route-path-param db :view-id)]
          (let [derived-view-id (keyword derived-view-id)]
               (if (or (not (vector?          allowed-view-ids))
                       (vector/contains-item? allowed-view-ids derived-view-id))
                   ; If allowed-view-ids is NOT in use,
                   ; or allowed-view-ids is in use & derived-view-id is allowed ...
                   (return derived-view-id)
                   ; If allowed-view-ids is in use & derived-view-id is NOT allowed ...
                   (or default-view-id engine/DEFAULT-VIEW-ID)))
          (or default-view-id engine/DEFAULT-VIEW-ID)))

(defn get-selected-view
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view db :my-extension)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (get-in db [extension-id :view-selector/meta-items :view-id]))

; @usage
;  [:view-selector/get-selected-view :my-extension]
(a/reg-sub :view-selector/get-selected-view get-selected-view)

(defn get-view-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-view-props db :my-extension)
  ;
  ; @return (map)
  ;  {:view-id (keyword)}
  [db [_ extension-id]]
  {:view-id (r get-selected-view db extension-id)})

; @usage
;  [:view-selector/get-view-props :my-extension]
(a/reg-sub :view-selector/get-view-props get-view-props)
