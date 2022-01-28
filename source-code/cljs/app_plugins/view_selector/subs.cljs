
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.21
; Description:
; Version: v0.6.4
; Compatibility: x4.5.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.view-selector.subs
    (:require [mid-fruits.candy  :refer [param return]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-router.api  :as router]
              [app-plugins.view-selector.engine :as engine]
              [mid-plugins.view-selector.subs   :as subs]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.view-selector.subs
(def get-selector-props subs/get-selector-props)
(def get-meta-item      subs/get-meta-item)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn route-handled?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (let [route-id (r router/get-current-route-id db)]
       (or (= route-id (engine/route-id          extension-id))
           (= route-id (engine/extended-route-id extension-id)))))

(defn route-exists?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (or (r router/route-exists? db (engine/route-id          extension-id))
      (r router/route-exists? db (engine/extended-route-id extension-id))))

(defn set-title?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (boolean)
  [db [_ extension-id]]
  (r route-handled? db extension-id))

(defn get-derived-view-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ;
  ; @return (keyword)
  ;  A view-id forrásából (route-path param) származó adat.
  ;  A forrás hiánya esetén a default-view-id paraméter.
  ;  A default-view-id paraméter hiánya esetén a DEFAULT-VIEW-ID konstans.
  [db [_ extension-id]]
  (let [default-view-id (r get-meta-item db extension-id :default-view-id)]
       (if-let [derived-view-id (r router/get-current-route-path-param db :view-id)]
               (let [derived-view-id (keyword derived-view-id)
                     allowed-view-ids (r get-meta-item db extension-id :allowed-view-ids)]
                    (if (or (not (vector?          allowed-view-ids))
                            (vector/contains-item? allowed-view-ids derived-view-id))
                        ; If allowed-view-ids is NOT in use,
                        ; or allowed-view-ids is in use & derived-view-id is allowed ...
                        (return derived-view-id)
                        ; If allowed-view-ids is in use & derived-view-id is NOT allowed ...
                        (or default-view-id engine/DEFAULT-VIEW-ID)))
               (or default-view-id engine/DEFAULT-VIEW-ID))))

(defn get-selected-view-id
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-selected-view-id db :my-extension)
  ;
  ; @return (keyword)
  [db [_ extension-id]]
  (r get-meta-item db extension-id :view-id))

; @usage
;  [:view-selector/get-selected-view-id :my-extension]
(a/reg-sub :view-selector/get-selected-view-id get-selected-view-id)

(defn get-view-props
  ; @param (keyword) extension-id
  ;
  ; @usage
  ;  (r view-selector/get-view-props db :my-extension)
  ;
  ; @return (map)
  ;  {:view-id (keyword)}
  [db [_ extension-id]]
  {:view-id (r get-selected-view-id db extension-id)})

; @usage
;  [:view-selector/get-view-props :my-extension]
(a/reg-sub :view-selector/get-view-props get-view-props)
