
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.3.4
; Compatibility: x4.5.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-browser.events
    (:require [mid-fruits.candy  :refer [param return]]
              [x.server-core.api :as a :refer [r]]
              [server-plugins.item-browser.engine :as engine]
              [server-plugins.item-lister.events  :as events]))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn browser-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) browser-props
  ;
  ; @return (map)
  [extension-id item-namespace browser-props]
  (merge {}
         (events/lister-props-prototype extension-id item-namespace browser-props)))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  [_ [_ extension-id item-namespace browser-props]]
  [:router/add-route! (engine/route-id extension-id item-namespace)
                      {:client-event   [:item-browser/load-browser! extension-id item-namespace browser-props]
                       :route-template (engine/route-template extension-id)
                       :restricted?    true}])

(defn add-extended-route!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  [_ [_ extension-id item-namespace browser-props]]
  [:router/add-route! (engine/extended-route-id extension-id item-namespace)
                      {:client-event   [:item-browser/load-browser! extension-id item-namespace browser-props]
                       :route-template (engine/extended-route-template extension-id)
                       :restricted?    true}])

(a/reg-event-fx
  :item-browser/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map) browser-props
  ;  {:default-item-id (string)(opt)
  ;   :search-keys (keywords in vector)(opt)
  ;    Default: item-lister/DEFAULT-SEARCH-KEYS}
  ;
  ; @usage
  ;  [:item-browser/initialize! :my-extension :my-type {:default-item-id "my-item"
  ;                                                     :search-keys     [:name :email-address]}]
  (fn [cofx [_ extension-id item-namespace browser-props]]
      (let [browser-props (browser-props-prototype extension-id item-namespace browser-props)]
           {:dispatch-n [(r add-route!          cofx extension-id item-namespace browser-props)
                         (r add-extended-route! cofx extension-id item-namespace browser-props)]})))
