
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-browser.body.events
    (:require [plugins.item-browser.body.subs     :as body.subs]
              [plugins.item-browser.core.events   :as core.events]
              [plugins.item-browser.core.subs     :as core.subs]
              [plugins.plugin-handler.body.events :as body.events]
              [x.app-core.api                     :refer [r]]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; plugins.plugin-handler.body.events
(def update-item-id! body.events/update-item-id!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ; @param (map) body-props
  ;  {:items-key (keyword)
  ;   :path-key (keyword)}
  ;
  ; @return (map)
  [db [_ browser-id {:keys [items-key path-key]}]]
  ; Az item-browser plugin minden Pathom lekérés küldésekor elküldi a szerver számára a body komponens
  ; {:items-key ...} és {:path-key ...} tulajdonságát.
  (cond-> db :update-item-id! (as-> %   (r update-item-id! % browser-id))
             :send-items-key! (assoc-in [:plugins :plugin-handler/meta-items browser-id :default-query-params :items-key] items-key)
             :send-path-key!  (assoc-in [:plugins :plugin-handler/meta-items browser-id :default-query-params :path-key]  path-key)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) browser-id
  ;
  ; @return (map)
  [db [_ browser-id]]
  (r core.events/reset-downloaded-item! db browser-id))
