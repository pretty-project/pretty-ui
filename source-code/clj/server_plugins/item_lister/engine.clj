
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.11.23
; Description:
; Version: v0.2.4
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns server-plugins.item-lister.engine
    (:require [mid-fruits.candy  :refer [param]]
              [x.server-core.api :as a :refer [r]]
              [mid-plugins.item-lister.engine :as engine]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (keywords in vector)
(def DEFAULT-ORDER-BY-OPTIONS [:by-date-descending :by-date-ascending :by-name-descending :by-name-ascending])

; @constant (keyword)
(def DEFAULT-ORDER-BY :by-date-descending)



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-plugins.item-lister.engine
(def DEFAULT-DOWNLOAD-LIMIT engine/DEFAULT-DOWNLOAD-LIMIT)
(def request-id             engine/request-id)
(def resolver-id            engine/resolver-id)
(def new-item-uri           engine/new-item-uri)
(def add-new-item-event     engine/add-new-item-event)
(def route-id               engine/route-id)
(def route-template         engine/route-template)
(def render-event           engine/render-event)



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- lister-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) lister-props
  ;
  ; @return (map)
  ;  {:download-limit (integer)
  ;   :initial-order-by (keyword)
  ;   :order-by-options (keywords in vector)}
  [lister-props]
  (merge {:download-limit   DEFAULT-DOWNLOAD-LIMIT
          :order-by         DEFAULT-ORDER-BY
          :order-by-options DEFAULT-ORDER-BY-OPTIONS}
         (param lister-props)))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/initialize!
  ; @param (keyword) extension-id
  ; @param (keyword) item-namespace
  ; @param (map)(opt) lister-props
  ;  {:download-limit (integer)(opt)
  ;    Default: DEFAULT-DOWNLOAD-LIMIT
  ;   :order-by (keyword)(opt)
  ;    Default: DEFAULT-ORDER-BY
  ;   :order-by-options (keywords in vector)(opt)
  ;    Default: DEFAULT-ORDER-BY-OPTIONS}
  ;
  ; @usage
  ;  [:item-lister/initialize! :my-extension :my-type]
  (fn [_ [_ extension-id item-namespace lister-props]]
      (let [lister-props (a/prot lister-props lister-props-prototype)]
           [:router/add-route! (route-id extension-id item-namespace)
                               {:route-template (route-template     extension-id item-namespace)
                                :client-event   [:item-lister/load! extension-id item-namespace lister-props]
                                :restricted?    true}])))
